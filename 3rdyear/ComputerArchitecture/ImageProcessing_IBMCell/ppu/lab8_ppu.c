/*
 * DUMITRESCU EVELINA 331CA
 * Tema 3 ASC
 *
 */
#include <stdlib.h>
#include <stdio.h>
#include <errno.h>
#include <libspe2.h>
#include <pthread.h>
#include "utils.h"
#include <pthread.h>
extern spe_program_handle_t lab8_spu;
#define MAX_SPU_THREADS   16
#define spu_mfc_ceil16(value)   ((value +  15) &  ~15)
	
struct image bigimage;
char input_path[MAX_PATH_LEN]__attribute__ ((aligned(16)));
char output_path[MAX_PATH_LEN]__attribute__ ((aligned(16)));
int  num_frames;
char buf[MAX_PATH_LEN];
struct image input[NUM_STREAMS]__attribute__ ((aligned(16)));;
struct ctx_transf ctxtr[NUM_STREAMS]__attribute__ ((aligned(16)));;
struct my_barrier_t bar;
/* data layout for an image:
 * if RED_i, GREEN_i, BLUE_i are the red, green and blue values for 
 * the i-th pixel in the image than the data array inside struct image
 * looks like this:
 * RED_0 GREEN_0 BLUE_0 RED_1 GREEN_1 BLUE_1 RED_2 ...
*/

/*read a character from a file specified by a descriptor*/
char read_char(int fd, char* path){
	char c;
	int bytes_read;
	
	bytes_read = read(fd, &c, 1);
	if (bytes_read != 1){
		PRINT_ERR_MSG_AND_EXIT("Error reading from %s\n", path);
	}
	
	return c;
}

//allocate image data
void alloc_image(struct image* img){		
	img->data = malloc_align(NUM_CHANNELS * WIDTH * HEIGHT*sizeof(char),4);
        img->final = malloc_align(NUM_CHANNELS * WIDTH *4*sizeof(char),4);
	img->final2 = malloc_align(NUM_CHANNELS * WIDTH * HEIGHT*sizeof(char),4);
	if (!img->data){
		PRINT_ERR_MSG_AND_EXIT("Calloc failed\n");
	}
}

//free image data
void free_image(struct image* img){
	free(img->data);
		free(img->final2);
			free(img->final);
}

/* read from fd until character c is found
 * result will be atoi(str) where str is what was read before c was
 * found 
 */
unsigned int read_until(int fd, char c, char* path){

	char buf[SMALL_BUF_SIZE];
	int i;
	unsigned int res;
	
	i = 0;
	memset(buf, 0, SMALL_BUF_SIZE);
	buf[i] = read_char(fd, path);
	while (buf[i] != c){
		i++;
		if (i >= SMALL_BUF_SIZE){
			PRINT_ERR_MSG_AND_EXIT("Unexpected file format for %s\n", path);
		}
		buf[i] = read_char(fd, path);
	}
	res = atoi(buf);
	if (res <= 0) {
		PRINT_ERR_MSG_AND_EXIT("Result is %d when reading from %s\n", 
			res, path);
	}
	
	return res;
}

//read a pnm image
void read_pnm(char* path, struct image* img){
	int fd, bytes_read, bytes_left;
	char image_type[IMAGE_TYPE_LEN];
	unsigned char *ptr;
	unsigned int max_color;
	
	fd = open(path, O_RDONLY);
	
	if (fd < 0){
		PRINT_ERR_MSG_AND_EXIT("Error opening %s\n", path);
		exit(1);
	}
	
	//read image type; should be P6
	bytes_read = read(fd, image_type, IMAGE_TYPE_LEN);
	if (bytes_read != IMAGE_TYPE_LEN){
		PRINT_ERR_MSG_AND_EXIT("Couldn't read image type for %s\n", path);
	}
	if (strncmp(image_type, "P6", IMAGE_TYPE_LEN)){
		PRINT_ERR_MSG_AND_EXIT("Expecting P6 image type for %s. Got %s\n", 
			path, image_type);
	}
	
	//read \n
	read_char(fd, path);
	int w,h;	
	//read width, height and max color value
	w = read_until(fd, ' ', path);
	h = read_until(fd, '\n', path);
	max_color = read_until(fd, '\n', path);
	if (max_color != MAX_COLOR){
		PRINT_ERR_MSG_AND_EXIT("Unsupported max color value %d for %s\n", 
			max_color, path);
	}
		
	
	//read the actual data 
	bytes_left = w * h * NUM_CHANNELS;
	ptr = img->data;
	while (bytes_left > 0){
		bytes_read = read(fd, ptr, bytes_left);
		if (bytes_read <= 0){
			PRINT_ERR_MSG_AND_EXIT("Error reading from %s\n", path);
		}
		ptr += bytes_read;
		bytes_left -= bytes_read;
	}
			
	close(fd);
}

//write a pnm image
void write_pnm(char* path, struct image* img, int w, int h){
	int fd, bytes_written, bytes_left;
	char buf[32];
	unsigned char* ptr;
		
	fd = open(path, O_WRONLY | O_CREAT | O_TRUNC, 0644);
	if (fd < 0){
		PRINT_ERR_MSG_AND_EXIT("Error opening %s\n", path);
	}
		
	//write image type, image width, height and max color
	sprintf(buf, "P6\n%d %d\n%d\n", w, h, MAX_COLOR);
	ptr = (unsigned char*)buf;
	bytes_left = strlen(buf);
	while (bytes_left > 0){
		bytes_written = write(fd, ptr, bytes_left);
		if (bytes_written <= 0){
			PRINT_ERR_MSG_AND_EXIT("Error writing to %s\n", path);
		}
		bytes_left -= bytes_written;
		ptr += bytes_written;
	}
	
	//write the actual data
	ptr = img->final2;
	bytes_left = w * h * NUM_CHANNELS;
	while (bytes_left > 0){
		bytes_written = write(fd, ptr, bytes_left);
		if (bytes_written <= 0){
			PRINT_ERR_MSG_AND_EXIT("Error writing to %s\n", path);
		}
		bytes_left -= bytes_written;
		ptr += bytes_written;
	}
	
	close(fd);
}
//create final result image from downscaled images
void create_big_image(struct image* scaled, struct image* big_image){
	unsigned int i, j, k;
	unsigned char* ptr = big_image->final2;
	struct image* img_ptr;
	unsigned int height = HEIGHT/SCALE_FACTOR;
	unsigned int width = WIDTH/SCALE_FACTOR;
	
	for (i = 0; i < NUM_IMAGES_HEIGHT; i++){
		for (k = 0; k < height; k++) {
			//line by line copy
			for (j = 0; j < NUM_IMAGES_WIDTH; j++){
				img_ptr = &scaled[i * NUM_IMAGES_WIDTH + j];
				memcpy(ptr, &img_ptr->final2[k * width * NUM_CHANNELS], width * NUM_CHANNELS);
				ptr += width * NUM_CHANNELS;
			}
		}
		
	}
}
/*
 * initialize PPU threads barrier
 */
void my_barrier_init(struct my_barrier_t *bar) {
    pthread_mutex_init(&bar->lock, NULL);
    pthread_cond_init(&bar->cond, NULL);
 
    /*how many threads have already passed*/
    bar->nr_arrived=0;
}
 
 
/*
 * destroy thread barrier
 */ 
void my_barrier_destroy(struct my_barrier_t *bar) {
    pthread_cond_destroy(&bar->cond);
    pthread_mutex_destroy(&bar->lock);  
}


/*
 * thread barrier
 */ 
void barier( int frame)
{
 
    /*before processing internal operations, get the mutex*/
    pthread_mutex_lock(&bar.lock);
    bar.nr_arrived ++;
    /*am I the last thread?*/
    int is_last_to_arrive = (bar.nr_arrived == NUM_STREAMS);
    /*incremenet number of threads that wait at the barrier*/

 
    // cat timp mai sunt fire de execuție care nu au ajuns la bariera, asteptam.

        // lockul se elibereaza automat inainte de a incepe asteptarea
        
 
    /*last execution thread wakes up the other threads*/
    if (is_last_to_arrive) {
        /*write the image*/
        alloc_image(&bigimage);
        create_big_image(input, &bigimage);
        sprintf(buf, "%s/frame%d.pnm",output_path,	 frame);
        write_pnm(buf,&bigimage, WIDTH, HEIGHT);
        /*reset number of threads*/
        bar.nr_arrived=0;
        pthread_cond_broadcast(&bar.cond);
    }
    else
     /* wait until other threads have reached the barrier*/
     pthread_cond_wait(&bar.cond, &bar.lock);
 
    /*after finishing operations, realease the mutex*/
    pthread_mutex_unlock(&bar.lock);
 
    return ;
 
} 

/*
 * function executed by PPu threads for mailbox event management
 */
void *mailbox_pthread_function(void *thread_arg) {
    struct ctx_transf *ct =  (struct ctx_transf *) thread_arg;
	spe_context_ptr_t ctx = ct->ctx;
    int id=ct->id; 
	int i=0;
	struct timeval t1, t2;
	double scaled_time=0;	
	unsigned int ceva=1;	
	int count=HEIGHT/SCALE_FACTOR;
	int size=WIDTH*NUM_CHANNELS/SCALE_FACTOR;
    int j;
	int addr;
	int nevents;
    uint32_t recv __attribute__((aligned(128)));
    /*create event handler*/
   	spe_event_handler_ptr_t event_handler;
	event_handler = spe_event_handler_create();
	spe_event_unit_t pevent,event_received;;
	pevent.events = SPE_EVENT_OUT_INTR_MBOX; 
	pevent.spe = ctx;
	spe_event_handler_register(event_handler, &pevent);   
	sprintf(buf, "%s/stream%02d/image%d.pnm", input_path, 
				1, ct->id+ 1);	

	for(j=0;j<num_frames;j++)
	{
       	gettimeofday(&t1, NULL);
        char buf[MAX_PATH_LEN];
        sprintf(buf, "%s/stream%02d/image%d.pnm", input_path, 
				 ct->id + 1,j + 1);
    	addr=&input[ct->id];
    	/*read  image input */
	    gettimeofday(&t2, NULL);
    	scaled_time += GET_TIME_DELTA(t1, t2);	    
    	read_pnm(buf, &input[ct->id]);
	    gettimeofday(&t1, NULL);    	
    	spe_in_mbox_write(ctx,&addr,1,SPE_MBOX_ANY_NONBLOCKING);
        for (i=0;i<count;i++) 
        {	
            /*wait for an event*/		
        	nevents = spe_event_wait(event_handler,&event_received,1,-1);
        	if(nevents>=0)
			{
				if (event_received.events & SPE_EVENT_OUT_INTR_MBOX)
				{
					while (spe_out_intr_mbox_status(event_received.spe) < 1);
                    /*receive a processed chunk*/
					spe_out_intr_mbox_read(event_received.spe, &recv, 1, SPE_MBOX_ANY_NONBLOCKING);
    				memcpy(input[id].final2+i*size,input[id].final,size);
					spe_in_mbox_write(ctx,&ceva,1,SPE_MBOX_ANY_NONBLOCKING);
				} 
			
			}	
				

        }
gettimeofday(&t2, NULL);
scaled_time += GET_TIME_DELTA(t1, t2);	    
barier(j+1);

}
printf("[SPU %d] Scale time: %lf\n", ct->id,scaled_time);
/*send finish image*/
addr=0;
spe_in_mbox_write(ctx,&addr,1,SPE_MBOX_ANY_NONBLOCKING);
return NULL;

}

/*
 * function executed by PPU threads for SPU context management
 */
void *ppu_pthread_function(void *thread_arg) {

	struct image *arg = (struct image *) thread_arg;
    spe_context_ptr_t ctx=ctxtr[arg->id].ctx;

	/* Load SPE program into context */
	if (spe_program_load (ctx, &lab8_spu)) {
            perror ("Failed loading program");
            exit (1);
        }
     struct ctx_transf *ct=malloc_align(sizeof(struct ctx_transf),4);
   ct->id=arg->id;
    ct->ctx=ctx;
	/* create mbox thread*/
        pthread_t mbox_thread;
	if (pthread_create (&mbox_thread, NULL, &mailbox_pthread_function, ct))  {
            perror ("Failed creating thread");
            exit (1);
        }
 
	/* Run SPE context */
	unsigned int entry = SPE_DEFAULT_ENTRY;
		spe_stop_info_t stop_info;

	/* TODO: transferati prin argument adresa si dimensiunea transferului initial */
	if (spe_context_run(ctx, &entry, 0, thread_arg, sizeof(struct image), &stop_info) < 0) {  
		perror ("Failed running context");
		exit (1);
	}

	if (spe_context_destroy (ctx) != 0) {
            perror("Failed destroying context");
            exit (1);
        }
if(pthread_join (mbox_thread, NULL)) {
            perror("Failed pthread_join");
            exit (1);
        }
	pthread_exit(NULL);
	return NULL;
}

/*
 * MAIN
 */
int main(int argc, char **argv)
{
	int i, j;
    /*initialize barrier*/
    my_barrier_init(&bar);
    int spu_threads;
	struct timeval tinit, tfin;   
	double total_time=0;
    pthread_t threads[MAX_SPU_THREADS];
    if (argc != 4){
		printf("Usage: ./serial input_path output_path num_frames\n");
		exit(1);
	}
	gettimeofday(&tinit, NULL);	
    /*initialize input and output image frames path */	
	strncpy(input_path, argv[1], MAX_PATH_LEN - 1);
	strncpy(output_path, argv[2], MAX_PATH_LEN - 1);
	num_frames = atoi(argv[3]);

	if (num_frames > MAX_FRAMES)
		num_frames = MAX_FRAMES;

    /*for each SPU create a new context */
	for (j = 0; j < NUM_STREAMS; j++)
	{
		spe_context_ptr_t ctx;
        if ((ctx = spe_context_create (SPE_EVENTS_ENABLE, NULL)) == NULL) {
            perror ("Failed creating context");
            exit (1);
        }
       	ctxtr[j].ctx=ctx;
       	ctxtr[j].id=j;			
	   	alloc_image(&input[j]);
		input[j].id=j;    
	}
		

    /* 
     * Determine the number of SPE threads to create.
     */

    spu_threads = spe_cpu_info_get(SPE_COUNT_USABLE_SPES, -1);
    if (spu_threads > MAX_SPU_THREADS) spu_threads = MAX_SPU_THREADS;

    /* 
     * Create several SPE-threads to execute 'simple_spu'.
     */
    for(i = 0; i < spu_threads; i++) {

        /* Create thread for each SPE context */
        if (pthread_create (&threads[i], NULL, &ppu_pthread_function, &input[i]))  {
            perror ("Failed creating thread");
            exit (1);
        }
    }
    /* Wait for SPU-thread to complete execution.  */
    for (i = 0; i < spu_threads; i++) {
    /* Destroy context */


        if (pthread_join (threads[i], NULL)) {
            perror("Failed pthread_join");
            exit (1);
        }
    }
 	gettimeofday(&tfin, NULL);
	total_time += GET_TIME_DELTA(tinit, tfin); 		
    /*destroy barrier*/
    my_barrier_destroy(&bar);
	printf("Total time: %lf\n", total_time);    
    return 0;
}

