#include <stdio.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/time.h>
#include <libspe2.h>
#include <libmisc.h>
#define HEIGHT 352
#define WIDTH 624
#define S_WIDTH 624/4
#define S_HEIGHT 352/4
#define PRINT_ERR_MSG_AND_EXIT(format, ...) \
	{ \
	fprintf(stderr, "%s:%d: " format, __func__, __LINE__, ##__VA_ARGS__); \
	fflush(stderr); \
	exit(1); \
	}

#define NUM_STREAMS 		16	
#define MAX_FRAMES			100	//there are at most 100 frames available
#define MAX_PATH_LEN		256
#define IMAGE_TYPE_LEN 		2
#define SMALL_BUF_SIZE 		16
#define SCALE_FACTOR		4
#define NUM_CHANNELS		3 //red, green and blue
#define MAX_COLOR			255
#define NUM_IMAGES_WIDTH	4 // the final big image has 4 small images
#define NUM_IMAGES_HEIGHT	4 // on the width and 4 on the height

//macros for easily accessing data
#define GET_COLOR_VALUE(img, i, j, k) \
	((img)->data[((i) * (img->width) + (j)) * NUM_CHANNELS + (k)])
#define RED(img, i, j)		GET_COLOR_VALUE(img, i, j, 0)
#define GREEN(img, i, j)	GET_COLOR_VALUE(img, i, j, 1)
#define BLUE(img, i, j)		GET_COLOR_VALUE(img, i, j, 2)

//macro for easily getting how much time has passed between two events
#define GET_TIME_DELTA(t1, t2) ((t2).tv_sec - (t1).tv_sec + \
				((t2).tv_usec - (t1).tv_usec) / 1000000.0)

/*
 * image structure
 * @param data : stores initial image
 * @param final: stores 4 lines from the initial image that have been scaled
 * @param final2: stores the final scaled image
 */
typedef struct image{
    unsigned char* data;    
	unsigned char  *final;
	unsigned char *final2;
	int id;
}image;

/*
 * context transfer structure    
 * @param id: stores SPU id
 * @param ctx: stores context
 *
 */
typedef struct ctx_transf
{
        int id;
        spe_context_ptr_t ctx;

}ctx_transf;
struct my_barrier_t {
    // mutex used for seralising data access
    pthread_mutex_t lock;
 
    // condition variable that waits for all threads to reach the barrier
    pthread_cond_t  cond;
 
    // number of threads that already came
    int nr_arrived;
};

