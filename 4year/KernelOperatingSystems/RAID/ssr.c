/*
 * DUMITRESCU EVELINA 341C3
 * Homework 3 Software RAID
 */

#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/fs.h>
#include <linux/wait.h>
#include <linux/crc32.h>
#include <linux/sched.h>
#include <linux/genhd.h>
#include <linux/blkdev.h>
#include "ssr.h"

MODULE_AUTHOR("Eu");
MODULE_DESCRIPTION("Software RAID");
MODULE_LICENSE("GPL");

/* pointer to physical device structure */
static struct ssr_dev_struct ssr_dev;

/* open device */
static int my_block_open(struct block_device *bdev, fmode_t mode)
{
	return 0;
}

/* release device */
static void my_block_release(struct gendisk *gd, fmode_t mode)
{
	return;
}

/* tells if bio event processing finished */
static void bi_complete(struct bio *bio, int error)
{
	complete((struct completion *)bio->bi_private);
}


/* block device operations struct */
static const struct block_device_operations my_block_ops = {
	.owner = THIS_MODULE,
	.open = my_block_open,
	.release = my_block_release
};


/* work processing handler */
static void work_handler(struct work_struct *work)
{
	struct ssr_kthreadjob *w;
	struct bio *bio;
	struct completion event;
	w = container_of(work, struct ssr_kthreadjob, work);
	bio = w->bio;
	init_completion(&event);
	bio->bi_end_io = bi_complete;
	bio->bi_private = &event;
	submit_bio(bio->bi_rw, bio);
	wait_for_completion(&event);
	bio_put(bio);

}

/* schedules a kthread for bio work processing */
static int schedule_bio_work(struct bio *bio)
{
	struct ssr_kthreadjob *w;
	w = kmalloc(sizeof(struct ssr_kthreadjob), GFP_KERNEL);
	if (unlikely(!w)) {
		printk(LOG_LEVEL "[ERROR schedule_bio_work] kmalloc:\
					    not enough memory for ssr_kthreadjob\n");
		return -ENOMEM;
	}
	w->bio = bio_clone(bio, GFP_NOIO);
	if (unlikely(!w->bio))
		goto out;

	INIT_WORK(&w->work, work_handler);
	schedule_work(&w->work);

	flush_scheduled_work();

 out:
	kfree(w);
	return 0;
}

/* used to compute sector/offset in a sector for crc*/
static inline size_t ssr_get_crc_offset(size_t data_sector)
{
	return LOGICAL_DISK_SIZE + data_sector * CRC32_SIZE;
}

static inline size_t ssr_get_crc_sector(size_t data_sector)
{
	return ssr_get_crc_offset(data_sector) / KERNEL_SECTOR_SIZE;
}

static inline size_t ssr_get_crc_offset_in_sector(size_t data_sector)
{
	return (data_sector * CRC32_SIZE) % KERNEL_SECTOR_SIZE;
}

/* computes crc for a specific disk data sector */
static void compute_crc(size_t bio_nrsector, size_t bio_startsector, int index,
			struct bio *bio, struct bio *crcbio)
{
	unsigned char *disk_databuffer;
	unsigned char *crc_databuffer;
	size_t crc_sectoroffset;
	size_t disk_sectoroffset;
	disk_databuffer = __bio_kmap_atomic(bio, index);
	crc_databuffer = __bio_kmap_atomic(crcbio, 0);
	crc_sectoroffset =
	    ssr_get_crc_offset_in_sector(bio_nrsector + bio_startsector);
	disk_sectoroffset = bio_nrsector * KERNEL_SECTOR_SIZE;
	*(unsigned int *)(crc_databuffer + crc_sectoroffset) =
	    crc32(0, disk_databuffer + disk_sectoroffset, KERNEL_SECTOR_SIZE);
	__bio_kunmap_atomic(crc_databuffer);
	__bio_kunmap_atomic(disk_databuffer);

}

/* release bio pages */
static void bio_release_pages(struct bio *bio)
{
	struct bio_vec *bvec;
	int i;
	bio_for_each_segment_all(bvec, bio, i) {
		struct page *page = bvec->bv_page;
		if (page)
			put_page(page);
	}
}

/* frees bio memory */
static void release_crcbio(struct bio *bio)
{
	bio_release_pages(bio);
	bio_put(bio);
}

/* initialize bio for crc */
static struct bio *create_crcbio(size_t bio_nrsector, size_t bio_startsector,
				 struct bio *bio, int index)
{
	int err;
	struct bio *crcbio = bio_alloc(GFP_NOIO, 1);
	struct page *page;
	size_t crc_nrsector;
	crcbio->bi_bdev = bio->bi_bdev;
	crc_nrsector = ssr_get_crc_sector(bio_nrsector + bio_startsector);
	crcbio->bi_sector = crc_nrsector;
	page = alloc_page(GFP_NOIO);
	if (unlikely(!page)) {
		printk(LOG_LEVEL
		       "[ERROR create_crcbio] alloc_page: failed to alloc page\n");
	}
	err = bio_add_page(crcbio, page, KERNEL_SECTOR_SIZE, 0);
	if (unlikely(err < 0)) {
		printk(LOG_LEVEL
		       "[ERROR create_crcbio] bio_add_page: failed to add page\n");
	}
	crcbio->bi_vcnt = 1;
	crcbio->bi_idx = 0;
	return crcbio;

}

/* checks data integrity */
static int verify_crc(size_t bio_nrsector, size_t bio_startsector,
		      struct bio *bio, int index)
{
	int err;
	unsigned char *disk_databuffer;
	unsigned char *crc_databuffer;
	unsigned int actual_crc;
	unsigned int read_crc;
	struct bio *crcbio;
	size_t crc_nrsector;
	crc_nrsector =
	    ssr_get_crc_offset_in_sector(bio_nrsector + bio_startsector);
	bio->bi_rw = BIO_DIR_READ;
	crcbio = create_crcbio(bio_nrsector, bio_startsector, bio, index);
	err = schedule_bio_work(bio);
	if (unlikely(err < 0)) {
		printk(LOG_LEVEL "[ERROR verify_crc] schedule_bio_work:\
						 failed to schedule bio work\n");
		goto out;
	}
	err = schedule_bio_work(crcbio);
	if (unlikely(err < 0)) {
		printk(LOG_LEVEL "[ERROR verify_crc] schedule_bio_work:\
					 failed to schedule bio work\n");

		goto out;
	}
	disk_databuffer = __bio_kmap_atomic(bio, index);
	crc_databuffer = __bio_kmap_atomic(crcbio, 0);
	actual_crc = *(unsigned int *)(crc_databuffer + crc_nrsector);
	read_crc = crc32(0,
			 disk_databuffer + bio_nrsector * KERNEL_SECTOR_SIZE,
			 KERNEL_SECTOR_SIZE);
	__bio_kunmap_atomic(crc_databuffer);
	__bio_kunmap_atomic(disk_databuffer);
	release_crcbio(crcbio);
	return actual_crc == read_crc;

 out:
	release_crcbio(crcbio);
	return err;

}

/* recovers corrupted disk data */
static int data_recovery(size_t bio_nrsector, size_t bio_startsector,
			 struct bio *bio, int index)
{
	int err = 0;

	bio->bi_bdev = ssr_dev.ssr_bdev1;
	if (verify_crc(bio_nrsector, bio_startsector, bio, index) == 0) {
		bio->bi_bdev = ssr_dev.ssr_bdev2;
		if (verify_crc(bio_nrsector, bio_startsector, bio, index)) {
			bio->bi_rw = BIO_DIR_WRITE;
			bio->bi_bdev = ssr_dev.ssr_bdev1;
			err = schedule_bio_work(bio);
			if (unlikely(err != 0)) {
				printk(LOG_LEVEL
				       "[ERROR data_recovery] schedule_bio_work\n");

				goto out;
			}
		}

	}
	bio->bi_bdev = ssr_dev.ssr_bdev2;
	if (verify_crc(bio_nrsector, bio_startsector, bio, index) == 0) {
		bio->bi_bdev = ssr_dev.ssr_bdev1;
		if (verify_crc(bio_nrsector, bio_startsector, bio, index)) {
			bio->bi_rw = BIO_DIR_WRITE;
			bio->bi_bdev = ssr_dev.ssr_bdev2;
			err = schedule_bio_work(bio);
			if (unlikely(err != 0)) {
				printk(LOG_LEVEL
				       "[ERROR data_recovery] schedule_bio_work:\
										 failed to schedule bio work\n");

				goto out;
			}
		}
	}
 out:
	return err;
}

/* sends a crc bio job for writting */
static int write_crcdiskdata(size_t bio_nrsector, size_t bio_startsector,
			     struct bio *bio, int index)
{
	int err = 0;
	struct bio *crcbio;
	bio->bi_bdev = ssr_dev.ssr_bdev1;
	crcbio = create_crcbio(bio_nrsector, bio_startsector, bio, index);
	crcbio->bi_rw = BIO_DIR_READ;
	err = schedule_bio_work(crcbio);
	if (unlikely(err != 0)) {
		printk(LOG_LEVEL "[ERROR write_crcdiskdata] schedule_bio_work:\
					    failed to schedule bio work\n");

		goto out;
	}
	crcbio->bi_rw = BIO_DIR_WRITE;
	compute_crc(bio_nrsector, bio_startsector, index, bio, crcbio);
	err = schedule_bio_work(crcbio);
	if (unlikely(err != 0)) {
		printk(LOG_LEVEL "[ERROR write_crcdiskdata] schedule_bio_work:\
					    failed to schedule bio work\n");

		goto out;
	}
	crcbio->bi_bdev = ssr_dev.ssr_bdev2;
	err = schedule_bio_work(crcbio);
	if (unlikely(err != 0)) {
		printk(LOG_LEVEL "[ERROR write_crcdiskdata] schedule_bio_work:\
					    failed to schedule bio work\n");

		goto out;
	}
	release_crcbio(crcbio);
 out:
	return err;

}

/* process each bio request */
static int ssr_xfer_bio(struct bio *bio)
{
	int i;
	int err = 0;
	struct bio_vec *bvec;
	sector_t bio_startsector = bio->bi_sector;
	int nr_sect;

	int bio_nrsector;
	int direction;
	bio_for_each_segment(bvec, bio, i) {
		nr_sect = bio_sectors(bio);
		if ((bio_end_sector(bio) * KERNEL_SECTOR_SIZE) > ssr_dev.size) {
			err = -EIO;
			goto out;
		}
		direction = bio_data_dir(bio);
		if (direction == BIO_DIR_WRITE) {
			bio->bi_bdev = ssr_dev.ssr_bdev1;
			err = schedule_bio_work(bio);
			if (unlikely(err != 0)) {
				printk(LOG_LEVEL
				       "[ERROR ssr_xfer_bio] schedule_bio_work:\
									    failed to schedule bio work\n");

				goto out;
			}
			bio->bi_bdev = ssr_dev.ssr_bdev2;
			err = schedule_bio_work(bio);
			if (unlikely(err != 0)) {
				printk(LOG_LEVEL
				       "[ERROR ssr_xfer_bio] schedule_bio_work:\
									    failed to schedule bio work\n");

				goto out;
			}

			for (bio_nrsector = 0; bio_nrsector < nr_sect;
			     bio_nrsector++) {
				err =
				    write_crcdiskdata(bio_nrsector,
						      bio_startsector, bio, i);
				if (unlikely(err != 0)) {
					printk(LOG_LEVEL
					       "[ERROR ssr_xfer_bio] write_crcdiskdata:\
											    failed to write crc disk data\n");

					goto out;
				}
			}

		} else if (direction == BIO_DIR_READ) {
			for (bio_nrsector = 0; bio_nrsector < nr_sect;
			     bio_nrsector++) {
				err =
				    data_recovery(bio_nrsector, bio_startsector,
						  bio, i);
				if (unlikely(err != 0)) {
					printk(LOG_LEVEL
					       "[ERROR ssr_xfer_bio] write_crcdiskdata:\
											    failed to recover data\n");

					goto out;
				}

			}
		}
	}

 out:
	return err;
}

/* sends a bio processing request */
static void ssr_make_request(struct request_queue *q, struct bio *bio)
{
	int status = 0;
	status = ssr_xfer_bio(bio);
	bio_endio(bio, status);
}

/* opens block device disk */
static struct block_device *open_disk(char *name)
{
	struct block_device *bdev;

	bdev =
	    blkdev_get_by_path(name, FMODE_READ | FMODE_WRITE | FMODE_EXCL,
			       THIS_MODULE);
	if (IS_ERR(bdev)) {
		printk(LOG_LEVEL "[ERROR open_disk] blkdev_get_by_path:\
					    failed to get block device\n");
		return NULL;
	}

	return bdev;
}



/* initializes request queue for the ssr device */
static int init_request_queue(struct ssr_dev_struct *dev)
{
	dev->queue = blk_alloc_queue(GFP_KERNEL);
	if (unlikely(dev->queue == NULL)) {
		printk(LOG_LEVEL
		       "[ERROR create_block_device] blk_init_queue: out of memory\n");
		return -ENOMEM;
	}
	blk_queue_make_request(dev->queue, ssr_make_request);
	return 0;
}

/* closes a block device disk */
static void close_disk(struct block_device *bdev)
{
	blkdev_put(bdev, FMODE_READ | FMODE_WRITE | FMODE_EXCL);
}

/* initializes gendisk for ssr device */
static int init_gendisk(struct ssr_dev_struct *dev)
{
	int err = 0;
	dev->gd = alloc_disk(SSR_NUM_MINORS);
	if (unlikely(!dev->gd)) {
		printk(LOG_LEVEL
		       "[ERROR create_block_device] alloc_disk: failure\n");
		err = -ENOMEM;
		goto out_alloc_disk;
	}

	dev->ssr_bdev1 = open_disk(PHYSICAL_DISK1_NAME);
	if (unlikely(dev->ssr_bdev1 == NULL)) {
		printk(LOG_LEVEL
		       "[ERROR create_block_device] open_disk: no such device\n");
		err = -EINVAL;
		goto out_alloc_disk;
	}

	dev->ssr_bdev2 = open_disk(PHYSICAL_DISK2_NAME);
	if (unlikely(dev->ssr_bdev2 == NULL)) {
		printk(LOG_LEVEL
		       "[ERROR create_block_device] open_disk: no such device\n");
		err = -EINVAL;
		goto out_bdev2;
	}

	snprintf(dev->gd->disk_name, DISK_NAME_LEN, LOGICAL_DISK_NAME);
	dev->gd->queue = dev->queue;
	dev->gd->private_data = dev;
	set_capacity(dev->gd, LOGICAL_DISK_SECTORS);
	dev->gd->major = SSR_MAJOR;
	dev->gd->first_minor = SSR_FIRST_MINOR;
	dev->gd->fops = &my_block_ops;
	add_disk(dev->gd);

	return err;

 out_bdev2:
	close_disk(dev->ssr_bdev1);
 out_alloc_disk:
	blk_cleanup_queue(dev->queue);
	return err;

}

/* creates and initializes all  ssr device fields*/
static int create_block_device(struct ssr_dev_struct *dev)
{
	int err = 0;
	dev->size = LOGICAL_DISK_SIZE;

	err = init_request_queue(dev);
	if (unlikely(err < 0))
		goto out_blk_init;
	err = init_gendisk(dev);
	if (unlikely(err < 0))
		goto out_blk_init;

 out_blk_init:
	return err;

}

/* release all block device resources */
static void delete_block_device(struct ssr_dev_struct *dev)
{
	if (dev->gd) {
		del_gendisk(dev->gd);
		put_disk(dev->gd);
	}
	if (dev->queue)
		blk_cleanup_queue(dev->queue);

	close_disk(dev->ssr_bdev1);
	close_disk(dev->ssr_bdev2);
}

/* initialize ssr module */
static int __init ssr_init(void)
{
	int err = 0;
	err = register_blkdev(SSR_MAJOR, LOGICAL_DISK_NAME);
	if (unlikely(err < 0)) {
		printk(LOG_LEVEL
		       "[ERROR ssr_init] register_blkdev: unable to register\n");
		return err;
	}

	err = create_block_device(&ssr_dev);
	if (unlikely(err < 0))
		goto out;

	return 0;

 out:
	unregister_blkdev(SSR_MAJOR, LOGICAL_DISK_NAME);
	return err;

}

/* removes ssr module resources */
static void __exit ssr_exit(void)
{
	delete_block_device(&ssr_dev);
	unregister_blkdev(SSR_MAJOR, LOGICAL_DISK_NAME);
}

module_init(ssr_init);
module_exit(ssr_exit);
