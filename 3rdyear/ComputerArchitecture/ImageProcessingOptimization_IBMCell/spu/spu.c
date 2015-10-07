/*
 * Dumitrescu Evelina 331CA
 * Tema 4 ASC   
 */
#include <spu_mfcio.h>
#include <stdio.h>
#include <libmisc.h>
#include <string.h>

#include "../common.h"

#define MY_TAG 5
#define DONE 1
#define waitag(t) mfc_write_tag_mask(1<<t); mfc_read_tag_status_all();

void process_image_dmalist(struct image* img){
}

/*
 * process image using double buffering
 */
void process_image_double(struct image* img){
	unsigned char *input[2], *output, *temp;
	unsigned int addr1, addr2;
	unsigned int width=img->width;
	uint32_t tag_id[2];
	unsigned int  i, j, k;
	unsigned int  red, green, blue;
    int buf, next_buf; 
   	buf = 0;
	vector unsigned char *v1, *v2, *v3, *v4, *v5 ;
	input[0] = malloc_align(NUM_CHANNELS * SCALE_FACTOR * width, 4);
	input[1] = malloc_align(NUM_CHANNELS * SCALE_FACTOR * width, 4);
	output = malloc_align(NUM_CHANNELS * width / SCALE_FACTOR, 4);
	temp = malloc_align(NUM_CHANNELS * width, 4);

	
	v5 = (vector unsigned char *) temp;
	
	tag_id[0] = mfc_tag_reserve();
	if (tag_id[0]==MFC_TAG_INVALID){
		printf("SPU: ERROR can't allocate tag ID\n"); return -1;
	}
	tag_id[1] = mfc_tag_reserve();
	if (tag_id[1]==MFC_TAG_INVALID){
		printf("SPU: ERROR can't allocate tag ID\n"); return -1;
	}
	addr2 = (unsigned int)img->dst; //start of image
	addr2 += (img->block_nr / NUM_IMAGES_HEIGHT) * width * NUM_CHANNELS * 
		img->height / NUM_IMAGES_HEIGHT; //start line of spu block
	addr2 += (img->block_nr % NUM_IMAGES_WIDTH) * NUM_CHANNELS *
		width / NUM_IMAGES_WIDTH;
    
    /*the first transfer, outside the loop*/
	mfc_getb(input[buf], ((unsigned int)img->src), SCALE_FACTOR * width * NUM_CHANNELS, tag_id[buf], 0, 0);
    for (i=1; i<img->height / SCALE_FACTOR; i++)
    {
        if(buf == 1) next_buf = 0;
        else next_buf = 1;
		/*request the next buffer from the PPU*/
		addr1 = ((unsigned int)img->src) + i * width * NUM_CHANNELS * SCALE_FACTOR;
		mfc_getb(input[next_buf], addr1, SCALE_FACTOR * width * NUM_CHANNELS, tag_id[next_buf], 0, 0);
		/*wait for the previous buffer*/
		waitag(tag_id[buf]);
		v1 = (vector unsigned char *) &(input[buf][0]);
	    
	    v2 = (vector unsigned char *) &(input[buf][1 * width * NUM_CHANNELS]);
        	
	    v3 = (vector unsigned char *) &(input[buf][2 * width * NUM_CHANNELS]);
       	
	    v4 = (vector unsigned char *) &(input[buf][3 * width * NUM_CHANNELS]);
	   
	    /*process previous buffer*/
		for (j = 0; j < width * NUM_CHANNELS / 16; j++)
		{
			v5[j] = spu_avg(spu_avg(v1[j], v2[j]), spu_avg(v3[j], v4[j]));
		}
		for (j=0; j < width; j+=SCALE_FACTOR)
		{
			red = 0;
			green = 0;
			blue = 0;
			for (k = j; k < j + SCALE_FACTOR; k++) 
			{
				red += temp[k * NUM_CHANNELS + 0];
				green += temp[k * NUM_CHANNELS + 1];
				blue += temp[k * NUM_CHANNELS + 2];
			}
			output[j / SCALE_FACTOR * NUM_CHANNELS + 0] = (unsigned char) (red/SCALE_FACTOR);
			output[j / SCALE_FACTOR * NUM_CHANNELS + 1] = (unsigned char) (green/SCALE_FACTOR);
			output[j / SCALE_FACTOR * NUM_CHANNELS + 2] = (unsigned char) (blue/SCALE_FACTOR);
		}

        /*send the buffer to the PPU*/
		mfc_put(output, addr2, width / SCALE_FACTOR * NUM_CHANNELS, MY_TAG, 0, 0);
		addr2 += width * NUM_CHANNELS; 
		mfc_write_tag_mask(1 << MY_TAG);
		mfc_read_tag_status_all();
		/*get ready for the next iteration*/
		buf = next_buf;
	}	
    /*process the last buffer*/
	for (j = 0; j < width * NUM_CHANNELS / 16; j++)
	{
			v5[j] = spu_avg(spu_avg(v1[j], v2[j]), spu_avg(v3[j], v4[j]));
	}
		
	for (j=0; j < width; j+=SCALE_FACTOR)
	{
			red = 0;
			green = 0;
			blue = 0;
			for (k = j; k < j + SCALE_FACTOR; k++) 
			{
				red += temp[k * NUM_CHANNELS + 0];
				green += temp[k * NUM_CHANNELS + 1];
				blue += temp[k * NUM_CHANNELS + 2];
			}
            output[j / SCALE_FACTOR * NUM_CHANNELS + 0] = (unsigned char) (red/SCALE_FACTOR);
			output[j / SCALE_FACTOR * NUM_CHANNELS + 1] = (unsigned char) (green/SCALE_FACTOR);
			output[j / SCALE_FACTOR * NUM_CHANNELS + 2] = (unsigned char) (blue/SCALE_FACTOR);
	}
    /*send the last buffer to the PPU*/
	mfc_put(output, addr2, width / SCALE_FACTOR * NUM_CHANNELS, MY_TAG, 0, 0);
	mfc_write_tag_mask(1 << MY_TAG);
	mfc_read_tag_status_all();
	waitag(tag_id[buf]);
	free_align(input[0]);
	free_align(input[1]);
	free_align(temp);
	free_align(output);
	mfc_tag_release(tag_id[0]);
	mfc_tag_release(tag_id[1]);
}

/*
 * process once 2 lines of a image
 */
void process_image_2lines(struct image* img){

    unsigned char *input;
    unsigned int addr1, addr2;
    unsigned int i,j,k;
    unsigned int width = img->width;
    unsigned int red[2], green[2], blue[2];
    vector unsigned char *v1[2], *v2[2], *v3[2], *v4[2],*v5[2];
    unsigned char *output[2];
    unsigned char *temp[2];
	input = malloc_align(2 * NUM_CHANNELS * SCALE_FACTOR * width, 4);
	output[0] = malloc_align(NUM_CHANNELS * width / SCALE_FACTOR, 4);
	output[1] = malloc_align(NUM_CHANNELS * width / SCALE_FACTOR, 4);
	temp[1] = malloc_align(NUM_CHANNELS * width, 4);
	temp[0] = malloc_align(NUM_CHANNELS * width, 4);
	v1[0] = (vector unsigned char *) &input[0];
	v2[0] = (vector unsigned char *) &input[1 * width * NUM_CHANNELS];
	v3[0] = (vector unsigned char *) &input[2 * width * NUM_CHANNELS];
	v4[0] = (vector unsigned char *) &input[3 * width * NUM_CHANNELS];
	v1[1] = (vector unsigned char *) &input[4 * width * NUM_CHANNELS];
	v2[1] = (vector unsigned char *) &input[5 * width * NUM_CHANNELS];
	v3[1] = (vector unsigned char *) &input[6 * width * NUM_CHANNELS];
	v4[1] = (vector unsigned char *) &input[7 * width * NUM_CHANNELS];
	v5[0] = (vector unsigned char *) temp[0];
	v5[1] = (vector unsigned char *) temp[1];
	/*start of image*/
	addr2 = (unsigned int)img->dst; 
	/*start line of SPU block*/
	addr2 += (img->block_nr / NUM_IMAGES_HEIGHT) * width * NUM_CHANNELS * 
		img->height / NUM_IMAGES_HEIGHT; 
	addr2 += (img->block_nr % NUM_IMAGES_WIDTH) * NUM_CHANNELS *
		width / NUM_IMAGES_WIDTH;
	for (i=0; i<img->height / (SCALE_FACTOR); i+=2){
	    /*request the buffer from the SPU*/
		addr1 = ((unsigned int)img->src) + i * width * NUM_CHANNELS * SCALE_FACTOR;
		mfc_get(input, addr1, SCALE_FACTOR * width * NUM_CHANNELS*2 , MY_TAG, 0, 0);
		mfc_write_tag_mask(1 << MY_TAG);
		mfc_read_tag_status_all();
		/*compute the 2 lines*/		
		for (j = 0; j < width * NUM_CHANNELS / 16; j++){
			v5[0][j] = spu_avg(spu_avg(v1[0][j], v2[0][j]), spu_avg(v3[0][j], v4[0][j]));
			v5[1][j] = spu_avg(spu_avg(v1[1][j], v2[1][j]), spu_avg(v3[1][j], v4[1][j]));
		}
		for (j=0; j < width; j+=SCALE_FACTOR){
			red[0] = 0;
			green[0] = 0;
			blue[0] = 0;
			red[1] = 0;
			green[1] = 0;
			blue[1] = 0;
			for (k = j; k < j + SCALE_FACTOR; k++) {
				red[0] += temp[0][k * NUM_CHANNELS + 0];
				green[0] += temp[0][k * NUM_CHANNELS + 1];
				blue[0] += temp[0][k * NUM_CHANNELS + 2];
				red[1] += temp[1][k * NUM_CHANNELS + 0];
				green[1] += temp[1][k * NUM_CHANNELS + 1];
				blue[1] += temp[1][k * NUM_CHANNELS + 2];
			}

			output[0][j / SCALE_FACTOR * NUM_CHANNELS + 0] = (unsigned char) (red[0]/SCALE_FACTOR);
			output[0][j / SCALE_FACTOR * NUM_CHANNELS + 1] = (unsigned char) (green[0]/SCALE_FACTOR);
			output[0][j / SCALE_FACTOR * NUM_CHANNELS + 2] = (unsigned char) (blue[0]/SCALE_FACTOR);
			output[1][j / SCALE_FACTOR * NUM_CHANNELS + 0] = (unsigned char) (red[1]/SCALE_FACTOR);
			output[1][j / SCALE_FACTOR * NUM_CHANNELS + 1] = (unsigned char) (green[1]/SCALE_FACTOR);
			output[1][j / SCALE_FACTOR * NUM_CHANNELS + 2] = (unsigned char) (blue[1]/SCALE_FACTOR);
		}
        /*send the processed buffer to the PPU*/
    	mfc_put(output[0], addr2, width / SCALE_FACTOR * NUM_CHANNELS, MY_TAG, 0, 0);
		addr2 += width * NUM_CHANNELS; 
		mfc_write_tag_mask(1 << MY_TAG);		
		mfc_put(output[1], addr2, width / SCALE_FACTOR * NUM_CHANNELS, MY_TAG, 0, 0);
		addr2 += width * NUM_CHANNELS; 
		mfc_write_tag_mask(1 << MY_TAG);
		mfc_read_tag_status_all();
	}

	free_align(temp[1]);
	free_align(output[1]);
	free_align(temp[0]);
	free_align(input);
	free_align(output[0]);


}


void process_image_simple(struct image* img){
	unsigned char *input, *output, *temp;
	unsigned int addr1, addr2, i, j, k, r, g, b;
	int block_nr = img->block_nr;
	vector unsigned char *v1, *v2, *v3, *v4, *v5 ;

	input = malloc_align(NUM_CHANNELS * SCALE_FACTOR * img->width, 4);
	output = malloc_align(NUM_CHANNELS * img->width / SCALE_FACTOR, 4);
	temp = malloc_align(NUM_CHANNELS * img->width, 4);

	v1 = (vector unsigned char *) &input[0];
	v2 = (vector unsigned char *) &input[1 * img->width * NUM_CHANNELS];
	v3 = (vector unsigned char *) &input[2 * img->width * NUM_CHANNELS];
	v4 = (vector unsigned char *) &input[3 * img->width * NUM_CHANNELS];
	v5 = (vector unsigned char *) temp;

	addr2 = (unsigned int)img->dst; //start of image
	addr2 += (block_nr / NUM_IMAGES_HEIGHT) * img->width * NUM_CHANNELS * 
		img->height / NUM_IMAGES_HEIGHT; //start line of spu block
	addr2 += (block_nr % NUM_IMAGES_WIDTH) * NUM_CHANNELS *
		img->width / NUM_IMAGES_WIDTH;

	for (i=0; i<img->height / SCALE_FACTOR; i++){
		//get 4 lines
		addr1 = ((unsigned int)img->src) + i * img->width * NUM_CHANNELS * SCALE_FACTOR;
		mfc_get(input, addr1, SCALE_FACTOR * img->width * NUM_CHANNELS, MY_TAG, 0, 0);
		mfc_write_tag_mask(1 << MY_TAG);
		mfc_read_tag_status_all();

		//compute the scaled line
		for (j = 0; j < img->width * NUM_CHANNELS / 16; j++){
			v5[j] = spu_avg(spu_avg(v1[j], v2[j]), spu_avg(v3[j], v4[j]));
		}
		for (j=0; j < img->width; j+=SCALE_FACTOR){
			r = g = b = 0;
			for (k = j; k < j + SCALE_FACTOR; k++) {
				r += temp[k * NUM_CHANNELS + 0];
				g += temp[k * NUM_CHANNELS + 1];
				b += temp[k * NUM_CHANNELS + 2];
			}
			r /= SCALE_FACTOR;
			b /= SCALE_FACTOR;
			g /= SCALE_FACTOR;

			output[j / SCALE_FACTOR * NUM_CHANNELS + 0] = (unsigned char) r;
			output[j / SCALE_FACTOR * NUM_CHANNELS + 1] = (unsigned char) g;
			output[j / SCALE_FACTOR * NUM_CHANNELS + 2] = (unsigned char) b;
		}

		//put the scaled line back
		mfc_put(output, addr2, img->width / SCALE_FACTOR * NUM_CHANNELS, MY_TAG, 0, 0);
		addr2 += img->width * NUM_CHANNELS; //line inside spu block
		mfc_write_tag_mask(1 << MY_TAG);
		mfc_read_tag_status_all();
	}

	free_align(temp);
	free_align(input);
	free_align(output);
}

int main(uint64_t speid, uint64_t argp, uint64_t envp){
	unsigned int data[NUM_STREAMS];
	unsigned int num_spus = (unsigned int)argp, i, num_images;
	struct image my_image __attribute__ ((aligned(16)));
	int mode = (int)envp;

	speid = speid; //get rid of warning

	while(1){
		num_images = 0;
		for (i = 0; i < NUM_STREAMS / num_spus; i++){
			//assume NUM_STREAMS is a multiple of num_spus
			while(spu_stat_in_mbox() == 0);
			data[i] = spu_read_in_mbox();
			if (!data[i])
				return 0;
			num_images++;
		}

		for (i = 0; i < num_images; i++){
			mfc_get(&my_image, data[i], sizeof(struct image), MY_TAG, 0, 0);
			mfc_write_tag_mask(1 << MY_TAG);
			mfc_read_tag_status_all();
			switch(mode){
				default:
				case MODE_SIMPLE:
					process_image_simple(&my_image);
					break;
				case MODE_2LINES:
					process_image_2lines(&my_image);
					break;
				case MODE_DOUBLE:
					process_image_double(&my_image);
					break;
				case MODE_DMALIST:
					process_image_dmalist(&my_image);
					break;
			}
		}	
		data[0] = DONE;
		spu_write_out_intr_mbox(data[0]);	
	}

	return 0;
}
