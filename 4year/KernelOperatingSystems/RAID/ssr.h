/*
 * Simple Software Raid - Linux header file
 */

#ifndef SSR_H_
#define SSR_H_		1

#define SSR_MAJOR			240
#define SSR_FIRST_MINOR			0
#define SSR_NUM_MINORS			1

#define PHYSICAL_DISK1_NAME		"/dev/sda"
#define PHYSICAL_DISK2_NAME		"/dev/sdb"

/* sector size */
#define KERNEL_SECTOR_SIZE		512

/* physical partition size - 95 MB (more than this results in error) */
#define LOGICAL_DISK_NAME		"/dev/ssr"
#define LOGICAL_DISK_SIZE		(95 * 1024 * 1024)
#define LOGICAL_DISK_SECTORS		((LOGICAL_DISK_SIZE) / (KERNEL_SECTOR_SIZE))

/* sync data */
#define SSR_IOCTL_SYNC			1

#define BIO_DIR_READ		0
#define BIO_DIR_WRITE		1

#define CRC32_SIZE 4

#define LOG_LEVEL KERN_ERR

/* structure for a block device */
struct ssr_dev_struct {
	struct request_queue *queue;
	spinlock_t lock;
	struct gendisk *gd;
  struct block_device *ssr_bdev1, *ssr_bdev2;
	size_t size;
};

/* structure used when submitting a bio */
struct ssr_kthreadjob {
	struct bio *bio;
	struct work_struct work;
};

#endif
