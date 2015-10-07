/*
 * Evelina Dumitrescu 341C3
 * Tema 0 SO2
 */

#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/module.h>
#include <linux/slab.h>
#include <linux/list.h>
#include <linux/proc_fs.h>
#include <linux/seq_file.h>
#include <asm/uaccess.h>
#include<linux/string.h>

MODULE_DESCRIPTION("List Data Structure in procfs");
MODULE_AUTHOR("Laura Vasilescu");
MODULE_LICENSE("GPL");

#define procfs_dir_name		"list"
#define procfs_file_read	"preview"
#define procfs_file_write	"management"

/* define commands */
#define DELA "dela"
#define DELF "delf"
#define ADDF "addf"
#define ADDE "adde"

/* proc entries for read and write content */
static struct proc_dir_entry *proc_list = NULL;
static struct proc_dir_entry *proc_list_read = NULL;
static struct proc_dir_entry *proc_list_write = NULL;

#define PROCFS_MAX_SIZE		1024
#define LOG_LEVEL	KERN_ALERT

/* list definition */
struct awsome_list {
    char *content;
    struct list_head list;
};

static struct list_head head;

/* list node allocation */
static struct awsome_list * awsome_list_alloc(const char *content)
{
	struct awsome_list *al;
	al = kmalloc(sizeof(*al), GFP_KERNEL);
	if(!al)
		return NULL;
	al->content=(char *)kmalloc(sizeof(char)*PROCFS_MAX_SIZE,GFP_KERNEL);
	memcpy(al->content, content,PROCFS_MAX_SIZE);
	return al;
}

/* remove all nodes with the specified content */
static void awsome_list_remove_all(const char *content )
{
	struct list_head *p, *q;
	struct awsome_list *al;
	list_for_each_safe(p, q, &head) {
		al = (struct awsome_list *)list_entry(p, struct awsome_list, list);
		if (strcmp(al->content,content)==0) {
			list_del(p);
			kfree(al->content);
			kfree(al);
		}
	}
}

/* remove first node with the specified content */
static void awsome_list_remove_first(char *content )
{
	struct list_head *p, *q;
	struct awsome_list *al;
	list_for_each_safe(p, q, &head) {
		al = (struct awsome_list *)list_entry(p, struct awsome_list, list);
		if (strcmp(al->content,content) == 0){
			list_del(p);
			kfree(al->content);
			kfree(al);
			return;
		}
	}
}

/* print each list node, each node on a separate line */
static int list_proc_show(struct seq_file *m, void *v)
{
	struct list_head *p;
	struct awsome_list *al;
	list_for_each(p, &head) {
		al = list_entry(p, struct awsome_list, list);
        seq_printf(m, al->content);
	}
	return 0;
}

static int list_read_open(struct inode *inode, struct  file *file) {
	return single_open(file, list_proc_show, NULL);
}

static int list_write_open(struct inode *inode, struct  file *file) {
	return single_open(file, list_proc_show, NULL);
}

/* execute specific action */
static ssize_t list_write(struct file *file, const char __user *buffer, size_t count,
		   loff_t *offs)
{
	char *local_buffer;
	unsigned long local_buffer_size = 0;
	char *action;
	char *content;
    	struct awsome_list *al;
	local_buffer = (char *)kmalloc(PROCFS_MAX_SIZE*sizeof(char),GFP_KERNEL);
	local_buffer_size = count;
	if (local_buffer_size > PROCFS_MAX_SIZE ) {
		local_buffer_size = PROCFS_MAX_SIZE;
	}
	memset(local_buffer, 0, PROCFS_MAX_SIZE);
	if (copy_from_user(local_buffer, buffer, local_buffer_size) ) {
		return -EFAULT;
	}

	action = strsep(&local_buffer," ");
	content = strsep(&local_buffer," ");
	if(strcmp(action, ADDF)==0) {
		al = awsome_list_alloc(content);
		list_add(&al->list, &head);
	} else  if(strcmp(action, ADDE)==0) {
		al = awsome_list_alloc(content);
		list_add_tail(&al->list, &head);
	} else if(strcmp(action, DELF)==0) {
		awsome_list_remove_first(content);
	} else if(strcmp(action, DELA)==0) {
		awsome_list_remove_all(content);
	}
	kfree(local_buffer);
	return local_buffer_size;
}

static const struct file_operations r_fops = {
	.owner		= THIS_MODULE,
	.open		= list_read_open,
	.read		= seq_read,
	.release	= single_release,
};

static const struct file_operations w_fops = {
	.owner		= THIS_MODULE,
	.open		= list_write_open,
	.write		= list_write,
	.release	= single_release,
};


/* initialize module */
static int list_init(void)
{
	proc_list = proc_mkdir(procfs_dir_name, NULL);
	if (!proc_list)
		return -ENOMEM;

	proc_list_read = proc_create(procfs_file_read, 0, proc_list, &r_fops);
	if (!proc_list_read)
		goto proc_list_cleanup;

	proc_list_write = proc_create(procfs_file_write, 0, proc_list, &w_fops);
	if (!proc_list_write)
		goto proc_list_cleanup;

    /* initialize list */
	INIT_LIST_HEAD(&head);
	return 0;

proc_list_cleanup:
	proc_remove(proc_list);
	return -ENOMEM;
}

/* remove module */
static void list_exit(void)
{
	proc_remove(proc_list);
}

module_init(list_init);
module_exit(list_exit);
