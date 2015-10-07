#include <linux/module.h>
#include <linux/init.h>
#include <linux/kernel.h>
#include <linux/fs.h>
#include <linux/cdev.h>
#include <asm/uaccess.h>
#include <linux/sched.h>
#include <linux/kprobes.h>
#include <linux/limits.h>
#include <linux/wait.h>
#include <linux/miscdevice.h>
#include <linux/list.h>
#include <linux/slab.h>
#include <linux/seq_file.h>
#include <linux/proc_fs.h>
#include "include/tracer.h"

MODULE_DESCRIPTION("SO2 character device");
MODULE_AUTHOR("SO2");
MODULE_LICENSE("GPL");

#define LOG_LEVEL	KERN_DEBUG
#define MY_MINOR		42
#define MODULE_NAME		"tracer"
#define procfs_tracer_entry "tracer"
#define MCOUNT	 128
#define KMALLOC_FUNC "__kmalloc"
#define KFREE_FUNC "kfree"
#define SCHEDULE_FUNC "schedule"
#define MUTEX_LOCK_FUNC "mutex_lock_nested"
#define MUTEX_UNLOCK_FUNC "mutex_unlock"
#define UP_INTERRUPT_FUNC "up_interruptible"
#define DOWN_INTERRUPT_FUNC "down_interruptible"

static struct list_head process_list;
struct proc_dir_entry *tracer_proc_entry = NULL;
//DEFINE_RWLOCK(process_list_lock);
/*
 * definitions for
 		* memory kmalloc status
 		* data passing between kretprobe handlers
 		* process status
 */
struct memalloc_stats
{
	unsigned long int addr;
	unsigned long int size;
	struct list_head mslist;	
};

struct kmalloc_data
{
	unsigned long int addr;
	unsigned long int size;
};

struct process_stats {
	unsigned long pid;
	unsigned long mem_kmalloc;
	unsigned long mem_kfree;
	unsigned long count_kmalloc;
	unsigned long count_kfree;
	unsigned long count_mutex_lock;
	unsigned long count_mutex_unlock;
	unsigned long count_up_interruptible;
	unsigned long count_down_interruptible;
	unsigned long count_schedule;
	struct list_head memalloc_stats_list;
	struct list_head plist;
	
};

/*
 * methods for manipulating process_list
 		* alloc new entry
 		* insert new entry
 		* delete new entry
 *
 */
static struct process_stats *process_stats_find_pid(unsigned long pid)
{
	struct list_head *p;
	struct process_stats *ps;
//	read_lock(&process_list_lock);
	list_for_each(p, &process_list) {
		ps = list_entry(p, struct process_stats, plist);
		if (ps->pid == pid) {
		//	read_unlock(&process_list_lock);
			return ps;
		}
	}
//	read_unlock(&process_list_lock);
	return NULL;
}

static void process_stats_print_list(void)
{
	struct list_head *p;
	struct process_stats *ps;
//	read_lock(&process_list_lock);
	printk(LOG_LEVEL "process list: {\n");
	list_for_each(p, &process_list) {
		ps = list_entry(p, struct process_stats, plist);
		printk("pid %lu ", ps->pid);
	}
///	read_unlock(&process_list_lock);
	printk(LOG_LEVEL " }\n");
}


static struct process_stats *process_stats_alloc(unsigned long pid)
{
	struct process_stats *ps;

	ps = kmalloc(sizeof(*ps), GFP_KERNEL);
	if (ps == NULL)
		return NULL;
		
	INIT_LIST_HEAD(&(ps->memalloc_stats_list));		
	ps->pid = pid;
	ps->mem_kmalloc = 0;
	ps->mem_kfree = 0;
	ps->count_kmalloc = 0;
	ps->count_kfree = 0;
	ps->count_mutex_lock = 0;
	ps->count_mutex_unlock = 0;
	ps->count_up_interruptible = 0;
	ps->count_down_interruptible = 0;
	ps->count_schedule = 0;
	return ps;
}


static void process_stats_remove_from_list(unsigned long pid)
{
	struct list_head *p, *q;
	struct process_stats *ps;
//	write_lock(&process_list_lock);	
	list_for_each_safe(p, q, &process_list) {
		ps = (struct process_stats *)list_entry(p, struct process_stats, plist);
		if (ps->pid == pid){
			list_del(p);
			kfree(ps);
//			write_unlock(&process_list_lock);				
			return;
		}
	}
//	write_unlock(&process_list_lock);	
}


static void process_stats_add_to_list(unsigned long pid)
{
	struct process_stats *ps;
	ps = process_stats_alloc(pid);
//	write_lock(&process_list_lock);
	list_add(&ps->plist, &process_list);
//	write_unlock(&process_list_lock);
}



static void process_stats_purge_list(void)
{
	struct list_head *p,*q;
	struct process_stats *pd;
//	write_lock(&process_list_lock);
	list_for_each_safe(p,q,&process_list) {
		pd = list_entry(p,struct process_stats, plist);
		list_del(p);
		kfree(pd);
	}
//	write_unlock(&process_list_lock);
}

/*for each kmalloc call, allocate a new entry with kmalloc return value & memory allocation size */
static struct memalloc_stats *memalloc_stats_alloc(unsigned long int addr, unsigned long int size)
{
	struct memalloc_stats *ms;

	ms = kmalloc(sizeof(*ms), GFP_KERNEL);
	if (ms == NULL)
		return NULL;
	ms->addr= addr;
	ms->size = size;	
	return ms;
}



static void memalloc_stats_add_to_list(unsigned long addr, unsigned long size, struct process_stats * ps)
{
	struct memalloc_stats * memst = memalloc_stats_alloc(addr, size);
	list_add(&memst->mslist, &(ps->memalloc_stats_list));

}


static struct memalloc_stats * memalloc_stats_find_addr(unsigned long addr, struct list_head memalloc_stats_list)
{
	struct list_head *p;
	struct memalloc_stats *ms;

	list_for_each(p, &memalloc_stats_list) {
		ms = list_entry(p, struct memalloc_stats, mslist);
		if (ms->addr == addr) {
			return ms;
		}
	}

	return NULL;

}
/*entry & ret handler for kretprobe*/
static int kmalloc_entry_handler(struct kretprobe_instance *ri, struct pt_regs *regs)
{
	struct kmalloc_data *kd;
	struct process_stats *ps = process_stats_find_pid(current->pid);	
	if (!current->mm)
		return 1;
	//printk(LOG_LEVEL "ENTRY Handler:\n");		
	printk(LOG_LEVEL "ENTRY KMALLOC process pid = %lu\n",ri->task->pid);
	//process_stats_print_list();
	kd = (struct kmalloc_data*)ri->data;
	kd->size = regs->ax;
	if(ps)
		ps->count_kmalloc+=kd->size;
	return 0;
}

static int kmalloc_ret_handler(struct kretprobe_instance *ri, struct pt_regs *regs)
{
	struct process_stats *ps = process_stats_find_pid(current->pid);
	struct kmalloc_data *kd = (struct kmalloc_data *)ri->data;
	kd->addr = regs_return_value(regs);
	if(ps)
		memalloc_stats_add_to_list(kd->addr, kd->size,ps);

//	printk(LOG_LEVEL "RET Handler:\n");
	printk(LOG_LEVEL "RET KMALLOC current process pid = %lu\n",ri->task->pid);
//	process_stats_print_list();

//	printk(LOG_LEVEL "memst addr = %lu size = %lu %d", memst->addr, memst->size,ps);
	return 0;
}


static struct kretprobe kmalloc_kretprobe = {
	.handler		= kmalloc_ret_handler,
	.entry_handler		= kmalloc_entry_handler,
	.data_size		= sizeof(struct kmalloc_data),
	.maxactive		= 64,
	
};


static long schedule_handler(unsigned long clone_flags, unsigned long stack_start,
	      struct pt_regs *regs, unsigned long stack_size,
	      int __user *parent_tidptr, int __user *child_tidptr)
{
	struct process_stats *ps = process_stats_find_pid(current->pid);
	printk(LOG_LEVEL "SCHEDULE process pid = %lu\n",current->pid);	
	jprobe_return();
	return 0;
}


static long up_interrupt_handler(unsigned long clone_flags, unsigned long stack_start,
	      struct pt_regs *regs, unsigned long stack_size,
	      int __user *parent_tidptr, int __user *child_tidptr)
{
//	struct process_stats *ps = process_stats_find_pid(current->pid);
	printk(LOG_LEVEL "UP INTERRUPT process pid = %lu\n",current->pid);	
	jprobe_return();
	return 0;
}

static long down_interrupt_handler(unsigned long clone_flags, unsigned long stack_start,
	      struct pt_regs *regs, unsigned long stack_size,
	      int __user *parent_tidptr, int __user *child_tidptr)
{
//	struct process_stats *ps = process_stats_find_pid(current->pid);
	printk(LOG_LEVEL "DOWN INTERRRUPt process pid = %lu\n",current->pid);	
	jprobe_return();
	return 0;
}

static long mutex_lock_handler(unsigned long clone_flags, unsigned long stack_start,
	      struct pt_regs *regs, unsigned long stack_size,
	      int __user *parent_tidptr, int __user *child_tidptr)
{
	struct process_stats *ps = process_stats_find_pid(current->pid);
	printk(LOG_LEVEL "MUTEX LOCK process pid = %lu\n",current->pid);	
	jprobe_return();
	return 0;
}

static long mutex_unlock_handler(unsigned long clone_flags, unsigned long stack_start,
	      struct pt_regs *regs, unsigned long stack_size,
	      int __user *parent_tidptr, int __user *child_tidptr)
{
	struct process_stats *ps = process_stats_find_pid(current->pid);
	printk(LOG_LEVEL "MUTEX UNLOCK process pid = %lu\n",current->pid);	
	jprobe_return();
	return 0;
}

static struct jprobe schedule_jprobe = {
	.entry			= schedule_handler,
	.kp = {
		.symbol_name	= SCHEDULE_FUNC,
	},
};


static struct jprobe up_interrupt_jprobe = {
	.entry			= up_interrupt_handler,
	.kp = {
		.symbol_name	= UP_INTERRUPT_FUNC,
	},
};


static struct jprobe down_interrupt_jprobe = {
	.entry			= down_interrupt_handler,
	.kp = {
		.symbol_name	= DOWN_INTERRUPT_FUNC,
	},
};


static struct jprobe mutex_lock_jprobe = {
	.entry			= mutex_lock_handler,
	.kp = {
		.symbol_name	= MUTEX_LOCK_FUNC,
	},
};


static struct jprobe mutex_unlock_jprobe = {
	.entry			= mutex_unlock_handler,
	.kp = {
		.symbol_name	= MUTEX_UNLOCK_FUNC,
	},
};


/*
 * ioctl file operation function for /dev/tracer
 */
static long
dev_ioctl(struct file *file, unsigned int cmd, unsigned long arg)
{
	switch(cmd)
	{
		case TRACER_ADD_PROCESS:
		{
			printk(LOG_LEVEL "add proc %lu\n", arg);
			process_stats_add_to_list(arg);
//			process_stats_print_list();	

		}
		case TRACER_REMOVE_PROCESS:
		{
//			printk(LOG_LEVEL "remove proc %lu\n", arg);
//			process_stats_remove_from_list(arg);		
//			process_stats_print_list();	

		}
	}
	return 0;
}

static int kfree_entry_handler(struct kretprobe_instance *ri, struct pt_regs *regs)
{
	struct memalloc_stats *ms;
	struct process_stats *ps = process_stats_find_pid(current->pid);	
	if (!current->mm)
		return 1;
//	printk(LOG_LEVEL "ENTRY Handler:\n");		
	printk(LOG_LEVEL "ENTRY KFREE current process pid = %lu\n",ri->task->pid);
	if(ps)
	{
		ms = memalloc_stats_find_addr(regs->ax,ps->memalloc_stats_list);

		ps->count_kfree+=ms->size;
	}
	return 0;
}



static const struct file_operations dev_fops = {
	.owner = THIS_MODULE,
	.unlocked_ioctl = dev_ioctl,
};


static struct miscdevice tracer_dev = {
	.minor = MY_MINOR,
	.name = MODULE_NAME,
	.fops = &dev_fops,
};



static struct kretprobe kfree_kretprobe = {
	.entry_handler		= kfree_entry_handler,
	.data_size		= sizeof(struct kmalloc_data),
	.maxactive		= 64,
	
};
/* print each list node, each node on a separate line */
static int list_proc_show(struct seq_file *m, void *v)
{
	struct list_head *p;
	struct process_stats *ps;
	list_for_each(p, &process_list) {
		ps = list_entry(p, struct process_stats, plist);
        seq_printf(m, "%lu %lu %lu %lu %lu %lu %lu %lu %lu %lu\n",ps->pid, ps->mem_kmalloc,ps->mem_kfree, ps->count_kmalloc, ps->count_kfree, ps->count_up_interruptible, ps->count_down_interruptible, ps->count_schedule, ps->count_mutex_lock, ps->count_mutex_unlock);
	}
	
	return 0;
}

static int list_read_open(struct inode *inode, struct  file *file) {
	return single_open(file, list_proc_show, NULL);
}

static const struct file_operations r_fops = {
	.owner = THIS_MODULE,
	.open = list_read_open,
	.release = single_release,
	.read = seq_read,
};
	
static int so2_cdev_init(void)
{
	int retval;
	INIT_LIST_HEAD(&process_list);
	retval = misc_register(&tracer_dev);	
	
	if (retval < 0)
		return retval;
	
	kmalloc_kretprobe.kp.symbol_name = KMALLOC_FUNC;
	retval = register_kretprobe(&kmalloc_kretprobe);

	if(retval<0)
		return retval;

	kfree_kretprobe.kp.symbol_name = KFREE_FUNC;
	retval = register_kretprobe(&kfree_kretprobe);

	if(retval<0)
		return retval;

	retval = register_jprobe(&schedule_jprobe);

	if(retval<0)
		return retval;


	retval = register_jprobe(&down_interrupt_jprobe);

	if(retval<0)
		return retval;

	retval = register_jprobe(&up_interrupt_jprobe);

	if(retval<0)
		return retval;

	retval = register_jprobe(&mutex_lock_jprobe);

	if(retval<0)
	{
		printk(LOG_LEVEL "sunteti de cacacat");
		return retval;
	}

	retval = register_jprobe(&mutex_unlock_jprobe);
	if(retval<0)
	{
		printk(LOG_LEVEL "sunteti de cacacat");
		return retval;
	}

	tracer_proc_entry = proc_create(procfs_tracer_entry,0,NULL, &r_fops);
	if (!tracer_proc_entry)
		goto proc_tracer_cleanup;

	return 0;

proc_tracer_cleanup:
	proc_remove(tracer_proc_entry);
	return -ENOMEM;	
}

static void so2_cdev_exit(void)
{
	misc_deregister(&tracer_dev);
	proc_remove(tracer_proc_entry);
	unregister_kretprobe(&kmalloc_kretprobe);	
	unregister_kretprobe(&kfree_kretprobe);		
	unregister_jprobe(&schedule_jprobe);			
			
	unregister_jprobe(&down_interrupt_jprobe);			
	unregister_jprobe(&up_interrupt_jprobe);
	unregister_jprobe(&mutex_lock_jprobe);					
	unregister_jprobe(&mutex_unlock_jprobe);								
	
	printk(LOG_LEVEL "Missed probing %d instances of %s\n",
		kmalloc_kretprobe.nmissed, kmalloc_kretprobe.kp.symbol_name);	
	
}

module_init(so2_cdev_init);
module_exit(so2_cdev_exit);