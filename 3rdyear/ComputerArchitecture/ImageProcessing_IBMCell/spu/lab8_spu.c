/*
 * DUMITRESCU EVELINA 331CA
 * Tema 3 ASC
 *
 */
#include <stdio.h>
#include <spu_intrinsics.h>
#include <spu_mfcio.h>
#define WIDTH  624
#define HEIGHT  352
#define SCALE_FACTOR 4
#define NUM_CHANNELS 3
#define GET_COLOR_VALUE(img, i, j, k) \
	((img)->data[((i) * (WIDTH) + (j)) * NUM_CHANNELS + (k)])
#define RED(img, i, j)		GET_COLOR_VALUE(img, i, j, 0)
#define GREEN(img, i, j)	GET_COLOR_VALUE(img, i, j, 1)
#define BLUE(img, i, j)		GET_COLOR_VALUE(img, i, j, 2)
#define waitag(t) mfc_write_tag_mask(1<<t); mfc_read_tag_status_all();
/*
 * image structure
 * @param data : stores initial image
 * @param final: stores 4 lines from the initial image that have been scaled
 * @param final2: stores the final scaled image
 */
struct image{
	unsigned char *data;
	unsigned char *final;
	unsigned char * final2;
	int id;
};

int main(unsigned long long speid, unsigned long long argp, unsigned long long envp)
{

	int i=0, j=0, dim, elem_no;
	struct image p __attribute__ ((aligned(16)));
	uint32_t tag_id = mfc_tag_reserve();
	if (tag_id==MFC_TAG_INVALID){
		printf("SPU: ERROR can't allocate tag ID\n"); return -1;
	}
	/*obtain via DMA the structure and the size of the transfer*/
	mfc_get((void*)&p, (uint32_t)argp, (uint32_t) envp, tag_id, 0, 0);
	waitag(tag_id);
	/*line dimension*/
	dim = WIDTH * 3;	
	elem_no = dim*4;
	unsigned char A[elem_no] __attribute__ ((aligned(16)));
    unsigned char B[elem_no] __attribute__ ((aligned(16)));
	vector unsigned char  *vA=(vector unsigned char *)A;
	int k;
	int addr;
    while(1)
    {
    /*wait for next message */
    while (spu_stat_in_mbox()==0);
	addr=spu_read_in_mbox();
    /*if end message break*/
	if(addr==0) break;
    i=0;
	do {
	    /*get 4 lines with size 3 *WIDTH*/
		mfc_get((void*)A, (uint32_t)(p.data)+i*dim, 4*dim, tag_id, 0, 0);
		waitag(tag_id);
        /*scale the image*/
		for(j=0;j<3 * WIDTH/16;j++)
		{
    		vA[j]=spu_avg(vA[j],vA[j+dim/16]);
        	vA[2*dim/16+j]=spu_avg(vA[j+2*dim/16], vA[j+3*dim/16]);
        	vA[j]=spu_avg(vA[j],vA[2*dim/16+j]);
		}	
		k=0;
		for(j=0;j<WIDTH*3;j+=12)
		{

            int n,rvalue, gvalue,bvalue;
            rvalue=bvalue=gvalue=0;
            for(n=0;n<SCALE_FACTOR;n++)
			{
			    rvalue+=A[j+n*NUM_CHANNELS];
			    gvalue+=A[j+n*NUM_CHANNELS+1];
			    bvalue+=A[j+n*NUM_CHANNELS+2];			    			    
            }
			B[k]=rvalue/SCALE_FACTOR;
           	k++;		
	        B[k]=gvalue/SCALE_FACTOR;
            k++;
			B[k]=bvalue/SCALE_FACTOR;
			k++;	
		}
		/*transfer the 4 scaled lines*/
		mfc_put((void*)B, (uint32_t)(p.final), WIDTH*3/4-4, tag_id, 0, 0);
		waitag(tag_id);
        mfc_put((void*)(B+WIDTH*3/4-4), (uint32_t)(p.final+WIDTH*3/4-4), 4, tag_id, 0, 0);
        waitag(tag_id);
		i+=4; 
        /*confirm next step*/
		spu_write_out_intr_mbox((uint32_t) i);
		spu_read_in_mbox();
	  } while (i<HEIGHT); // + do la linia 36
    }
    /*finished tasks*/
	mfc_tag_release(tag_id);
	return 0;
}



