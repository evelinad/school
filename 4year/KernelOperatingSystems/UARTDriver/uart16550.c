/*
 * DUMITRESCU EVELINA 341C3
 * UART Driver
 * Homework 2 SO2
 */

#include <linux/module.h>
#include <linux/fs.h>
#include <linux/init.h>
#include <linux/kernel.h>
#include <linux/sched.h>
#include <linux/wait.h>
#include <linux/kfifo.h>
#include <linux/cdev.h>
#include <linux/uaccess.h>
#include <linux/interrupt.h>
#include "uart16550.h"

MODULE_DESCRIPTION("Tema2SO2");
MODULE_AUTHOR("Evelina Dumitrescu");
MODULE_LICENSE("GPL");

#define LOG_LEVEL	KERN_DEBUG
#define MODULE_NAME		"uart16550"
#define DEBUG 0
#define MY_MAJOR 42
#define COM1_MINOR	0
#define COM2_MINOR	1
#define BUFFER_SIZE 1024

/*
 * module parameters
 */

int option = OPTION_BOTH;
module_param(option, int, 0);
int major = MY_MAJOR;
module_param(major, int, 0);

/*
 * struct uart_device_data keeps all the usefull info for de serial uart device
 */
struct uart_device_data {
	struct cdev cdev;
	spinlock_t read_lock;
	spinlock_t write_lock;
	wait_queue_head_t write_wq;
	wait_queue_head_t read_wq;
	DECLARE_KFIFO(read_buffer, char, BUFFER_SIZE);
	DECLARE_KFIFO(write_buffer, char, BUFFER_SIZE);
	atomic_t access;
	int base_addr;
};

struct uart_device_data devs[MAX_NUMBER_DEVICES];

void do_write(int int_id_reg, struct uart_device_data *dev)
{
	/*
	 * the transmitter holding register is empty
	 * && the write buffer contains dev_data,
	 * another byte can be sent to the dev_data port
	 */
	size_t wb_len;
	unsigned char byte;
	byte = inb(dev->base_addr + LSR) & (0x20);
	wb_len = kfifo_len(&dev->write_buffer);
	while (byte && (!wb_len == 0)) {
		char c;
		int nr;
		spin_lock(&dev->write_lock);
		/*
		 * get bytes from buffer and write them to the port
		 */
		nr = kfifo_out(&dev->write_buffer, &c, 1);
		wb_len = kfifo_len(&dev->write_buffer);
		spin_unlock(&dev->write_lock);
		outb(c, dev->base_addr);
	}
	/*
	 * disable the transmitter holding register interrupt
	 */
	wb_len = kfifo_len(&dev->write_buffer);
	byte = inb(dev->base_addr + IER) & ~(0x2);
	if (wb_len == 0)
		outb(byte, dev->base_addr + IER);
	/*
	 * the write buffer has more space left, notify the waiting process
	 */
	if (!(wb_len == BUFFER_SIZE))
		wake_up(&dev->write_wq);

}

void do_read(int int_id_reg, struct uart_device_data *dev)
{
	/*
	 * wait until a byte is ready to be read
	 * and the read buffer still has space left
	 */
	unsigned char byte;
	size_t rb_len;
	byte = inb(dev->base_addr + LSR) & 1;
	rb_len = kfifo_len(&dev->read_buffer);
	while (byte && !(rb_len == BUFFER_SIZE)) {
		char from = inb(dev->base_addr);
		spin_lock(&dev->read_lock);
		kfifo_in(&dev->read_buffer, &from, 1);
		byte = inb(dev->base_addr + LSR) & 1;
		rb_len = kfifo_len(&dev->read_buffer);
		spin_unlock(&dev->read_lock);
	}

	/*
	 * if read buffer has no more space left,
	 * disable Received dev_data Available Interrupt
	 */
	rb_len = kfifo_len(&dev->read_buffer);
	byte = inb(dev->base_addr + IER) & 0;
	if (rb_len == BUFFER_SIZE)
		outb(byte, dev->base_addr + IER);
	/*
	 * if there are bytes in read buffer, notify the process
	 */
	rb_len = kfifo_len(&dev->read_buffer);
	if (rb_len > 0)
		wake_up(&dev->read_wq);

}

/*
 * uart_device_write() - used for writting to the device buffer
 * from the userspace buffer
 */
static ssize_t
uart_device_write(struct file *file, const char __user *user_buffer,
		  size_t size, loff_t *offset)
{
	int bytes;
	unsigned char byte;
	size_t wb_free;
	size_t to_write;
	size_t wb_len;
	unsigned long flags;
	struct uart_device_data *dev_data =
	    (struct uart_device_data *)file->private_data;
	/*
	 * no dev_data available right now, try again later
	 */
	wb_len = kfifo_len(&dev_data->write_buffer);
	if (file->f_flags & O_NONBLOCK) {
		if (wb_len == BUFFER_SIZE)
			return -EAGAIN;
	} else {
		/*
		 * wait until there is available space in write buffer
		 */
		if (wait_event_interruptible(dev_data->write_wq,
					     !kfifo_is_full
					     (&dev_data->write_buffer))) {

			return -ERESTARTSYS;
		}
	}

	spin_lock_irqsave(&dev_data->write_lock, flags);
	/*
	 * don't write more than the write_buffer available space
	 */
	wb_len = kfifo_len(&dev_data->write_buffer);
	wb_free = BUFFER_SIZE - wb_len;
	if (size > wb_free)
		to_write = wb_free;
	else
		to_write = size;
	/*
	 * copy to device buffer
	 */
	bytes = 0;
	while (bytes < to_write) {
		if (get_user(byte, &user_buffer[bytes])) {
			spin_unlock_irqrestore(&dev_data->write_lock, flags);
			return -EFAULT;
		}
		kfifo_put(&dev_data->write_buffer, byte);
		bytes++;
	}

	spin_unlock_irqrestore(&dev_data->write_lock, flags);

	/*
	 * enable Transmitter Holding Register Empty Interrupt
	 */
	byte = inb(dev_data->base_addr + IER) | (0x2);
	outb(byte, dev_data->base_addr + IER);
	return size;
}

/*
 * uart_device_read() - used for reading from the device buffer
 * to the userspace buffer
 */
static ssize_t
uart_device_read(struct file *file, char __user *user_buffer,
		 size_t size, loff_t *offset)
{
	int bytes;
	unsigned char byte;
	unsigned long flags;
	size_t to_read;
	struct uart_device_data *dev_data =
	    (struct uart_device_data *)file->private_data;
	size_t rb_len = kfifo_len(&dev_data->read_buffer);
	if (rb_len == 0) {
		/*
		 * no dev_data available right now, try again later
		 */
		if (file->f_flags & O_NONBLOCK) {

			return -EAGAIN;
		} else {
			/*
			 * enable Received dev_data Available Interrupt
			 */
			byte = inb(dev_data->base_addr + IER) | 1;
			outb(byte, dev_data->base_addr + IER);
			/*
			 * wait until read buffer is not empty
			 */
			if (wait_event_interruptible(dev_data->read_wq,
					!kfifo_is_empty(&dev_data->read_buffer))) {
				return -ERESTARTSYS;
			}
		}
	}
	/*
	 * save interrupts
	 */
	spin_lock_irqsave(&dev_data->read_lock, flags);
	/*
	 * don't read more than read_buffer length
	 */
	rb_len = kfifo_len(&dev_data->read_buffer);
	if (size > rb_len)
		to_read = rb_len;
	else
		to_read = size;
	/*
	 * copy dev_data in userspace buffer
	 */
	bytes = 0;
	while (bytes < to_read) {
		if (kfifo_get(&dev_data->read_buffer, &byte)) {
			if (put_user(byte, &user_buffer[bytes])) {
				spin_unlock_irqrestore(&dev_data->read_lock, flags);
				return -EFAULT;
			}
			bytes++;
		}
	}

	/*
	 * restore interrupts
	 */
	spin_unlock_irqrestore(&dev_data->read_lock, flags);
	return to_read;

}

/*
 * uart_device_open() - called when opening the device
 */
static int uart_device_open(struct inode *inode, struct file *file)
{
	struct uart_device_data *dev_data =
	    container_of(inode->i_cdev, struct uart_device_data, cdev);
	if (atomic_cmpxchg(&dev_data->access, 1, 0) != 1)
		return -EBUSY;
	file->private_data = dev_data;
	set_current_state(TASK_INTERRUPTIBLE);
	schedule_timeout(10);
	return 0;
}

/*
 * uart_device_release() - called when releasing the device
 */
static int uart_device_release(struct inode *inode, struct file *file)
{

	struct uart_device_data *dev_data =
	    (struct uart_device_data *)file->private_data;
	atomic_inc(&dev_data->access);
	return 0;
}

/*
 * uart_device_ioctl() - handles control operations on the device
 */
static long
uart_device_ioctl(struct file *file, unsigned int cmd, unsigned long arg)
{
	unsigned char byte;
	struct uart_device_data *dev_data =
	    (struct uart_device_data *)file->private_data;
	struct uart16550_line_info info;
	if (cmd == UART16550_IOCTL_SET_LINE) {
		if (copy_from_user
		    (&info, (void *)arg, sizeof(struct uart16550_line_info)))
			return -EFAULT;
		/*
		 * deactivate interrupts
		 */
		outb(0, dev_data->base_addr + IER);
		/*
		 * set dlab bit on
		 */
		outb(0x80, dev_data->base_addr + LCR);
		/*
		 * set baud, length, parity and stop bits
		 */
		outb(info.baud, dev_data->base_addr);
		outb(0, dev_data->base_addr + 1);
		outb(info.par | info.len | info.stop,
		     dev_data->base_addr + LCR);
		/*
		 * set dlab bit off
		 */
		byte = inb(dev_data->base_addr + LCR) & ~(0x80);
		outb(byte, dev_data->base_addr + LCR);
		outb(1, dev_data->base_addr + IER);
		return 0;
	}
	return -ENOTTY;
}

static const struct file_operations uart_fops = {
	.owner = THIS_MODULE,
	.read = uart_device_read,
	.write = uart_device_write,
	.open = uart_device_open,
	.release = uart_device_release,
	.unlocked_ioctl = uart_device_ioctl,

};

/*
 * uart_interrupt_handler()
 */
irqreturn_t uart_interrupt_handler(int irq_no, void *dev_id)
{
	struct uart_device_data *dev = (struct uart_device_data *)dev_id;
	int int_id_reg = inb(dev->base_addr + IIR);
	if (irq_no == IRQ_COM2) {
		do_read(int_id_reg, dev);
		return IRQ_HANDLED;
	} else if (irq_no == IRQ_COM1) {
		do_write(int_id_reg, dev);
		return IRQ_HANDLED;
	} else {
		return IRQ_NONE;
	}
}

/*
 * reg_dev() - registers a device & initializes the structure components
 */
static int reg_dev(int base_addr, int minor, int irq)
{
	int err;
	err = register_chrdev_region(MKDEV(major, minor), 1, MODULE_NAME);
	if (err != 0) {
		printk(LOG_LEVEL "ERROR registering chrdev region: error %d\n",
		       err);
		goto out_reg_chrdev;
	}

	if ((request_irq(irq, uart_interrupt_handler, IRQF_SHARED, MODULE_NAME,
			 &devs[minor])) != 0) {
		printk(LOG_LEVEL "ERROR requesting irq: error %d\n", err);
		goto out_req_irq;
	}

	if (!request_region(base_addr, COM_NR_PORTS, MODULE_NAME)) {
		printk(LOG_LEVEL "ERROR registering region\n");
		err = -ENODEV;
		goto out_req_reg;
	}

	atomic_set(&devs[minor].access, 1);
	devs[minor].base_addr = base_addr;

	INIT_KFIFO(devs[minor].read_buffer);
	spin_lock_init(&devs[minor].read_lock);
	init_waitqueue_head(&devs[minor].read_wq);

	INIT_KFIFO(devs[minor].write_buffer);
	spin_lock_init(&devs[minor].write_lock);
	init_waitqueue_head(&devs[minor].write_wq);

	outb(0x0, base_addr + IER);
	outb(0x1, base_addr + IER);
	outb(0x0, base_addr + FCR);
	outb(0x7, base_addr + FCR);
	outb(0xb, base_addr + MCR);

	cdev_init(&devs[minor].cdev, &uart_fops);
	cdev_add(&devs[minor].cdev, MKDEV(major, minor), 1);
	return 0;

 out_reg_chrdev:
	return err;
 out_req_reg:
	free_irq(irq, &devs[minor]);
 out_req_irq:
	unregister_chrdev_region(MKDEV(major, minor), 1);
	return err;

}

/*
 * unreg_dev() - unregisters a device & frees resources
 */
void unreg_dev(int minor, int irq)
{
	int base_addr = devs[minor].base_addr;
	release_region(base_addr, COM_NR_PORTS);
	free_irq(irq, &devs[minor]);
	cdev_del(&devs[minor].cdev);
	kfifo_free(&devs[minor].read_buffer);
	kfifo_free(&devs[minor].write_buffer);
	unregister_chrdev_region(MKDEV(major, minor), 1);
}

/*
 * serial_init() called when module is inserted
 */
static int serial_init(void)
{

	int err;
	if (option == OPTION_BOTH) {
		err = reg_dev(PORT_COM1, COM1_MINOR, IRQ_COM1);
		if (err != 0) {
			printk(LOG_LEVEL "ERROR: registering COM1 device: %d\n",
			       err);
			return err;
		}
		err = reg_dev(PORT_COM2, COM2_MINOR, IRQ_COM2);
		if (err != 0) {
			printk(LOG_LEVEL "ERROR: registering COM2 device: %d\n",
			       err);
			return err;
		}
	}
	if (option == OPTION_COM1) {
		err = reg_dev(PORT_COM1, COM1_MINOR, IRQ_COM1);
		if (err != 0) {
			printk(LOG_LEVEL "ERROR: registering COM1 device: %d\n",
			       err);
			return err;
		}
	}

	if (option == OPTION_COM2) {
		err = reg_dev(PORT_COM2, COM2_MINOR, IRQ_COM2);
		if (err != 0) {
			printk(LOG_LEVEL "ERROR: registering COM2 device: %d\n",
			       err);
			return err;
		}
	}
	return 0;
}

/*
 * serial_exit() called when module is removed
 */
static void serial_exit(void)
{
	if (option == OPTION_BOTH) {
		unreg_dev(COM1_MINOR, IRQ_COM1);
		unreg_dev(COM2_MINOR, IRQ_COM2);
	}
	if (option == OPTION_COM1)
		unreg_dev(COM1_MINOR, IRQ_COM1);
	if (option == OPTION_COM2)
		unreg_dev(COM2_MINOR, IRQ_COM2);

}

module_init(serial_init);
module_exit(serial_exit);
