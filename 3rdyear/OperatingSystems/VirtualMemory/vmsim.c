/*
 * DUMITRESCU EVELINA 331CA
 * Tema 3 SO Linux
 */
#include <sys/mman.h>
#include <string.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "vmsim.h"
#include "util.h"

#define SWAPTEMP "swaptempXXXXXX"
#define RAMTEMP "ramtempXXXXXX"
vm_map_alloc* table;
int size ;
/*
 * initialize the library
 */
w_boolean_t vmsim_init(void)
{
	w_set_exception_handler(vmsim_exception_handler);
	size = 0;
	srand (time(NULL));
	return TRUE;
}
/*
 * close the library
 */
w_boolean_t vmsim_cleanup(void)
{
	w_exception_handler_t old_handler;
	w_get_previous_exception_handler(&old_handler);
	w_set_exception_handler(old_handler);
	free(table);
	table = NULL;
	return TRUE;
}

/*
 * initialize an entry in the page table for the new allocation
 */
void init_entry(w_size_t num_pages, w_size_t num_frames,vm_map_t *map)
{
	int i;
	table[size].num_pages = num_pages;
	table[size].num_frames = num_frames;
	table[size].map=map;
	table[size].frame_table = calloc(num_frames, sizeof(frame));
	table[size].page_table = calloc(num_pages, sizeof(page_table_entry));
	DIE(table[size].page_table == NULL ||
	    table[size].frame_table == NULL, "memory allocation");
	for(i=0;i<num_pages;i++)
	{
		table[size].page_table[i].state=
		table[size].page_table[i].prev_state=STATE_NOT_ALLOC;
		table[size].page_table[i].frame = NULL;
		table[size].page_table[i].dirty=FALSE;
		table[size].page_table[i].start=map->start + i*w_get_page_size();
		table[size].page_table[i].protection=PROTECTION_NONE;
	}
	for(i=0;i<num_frames;i++)
		table[size].frame_table[i].pte=NULL;

}
/*
 * allocate a new virtual memory zone entry in the table
 *
 */
w_boolean_t vm_alloc(w_size_t num_pages, w_size_t num_frames, vm_map_t *map)
{
	w_boolean_t ret;
	int i;
	char *vmem;
	w_handle_t ram_handle, swap_handle;
	if(num_frames > num_pages)
		return FALSE;
	char swapfile[]= SWAPTEMP;
	char ramfile[]=RAMTEMP;
	table = realloc(table,(size+1)*sizeof(vm_map_alloc));
	table[size].num_pages = num_pages;
	table[size].num_frames = num_frames;
	ram_handle=mkstemp(ramfile);
	swap_handle=mkstemp(swapfile);
	DIE((ram_handle  == -1|| swap_handle == -1), "mkstemp");
	map->ram_handle=ram_handle;
	map->swap_handle=swap_handle;
	ret=ftruncate(map->ram_handle, num_frames * w_get_page_size());
	DIE(ret == -1, "ftruncate");
	ret=ftruncate(map->swap_handle, num_pages * w_get_page_size());
	DIE(ret == -1, "ftruncate");
	vmem=mmap(NULL, num_pages*w_get_page_size(), PROT_NONE, MAP_SHARED|MAP_ANONYMOUS, 0, 0); 
	DIE(vmem == (char *)-1, "map failed");
	map->start = vmem;
	init_entry(num_pages,num_frames,map);
	size+=1;
	return TRUE;
}
/*
 * free the mapped memory and close the handlers
 */
w_boolean_t vm_free(w_ptr_t start)
{
	int i;
	int j;
	int rc;
	for(i=0;i<size;i++)
	{
		if(table[i].map->start == start) break;
	}
	if(start== NULL) return FALSE;
	if(i == size) return FALSE;
	rc =munmap(start, w_get_page_size() * table[i].num_pages);
	if(rc<0)
	{
 		ERR("munmap");
 		return FALSE;
	}

	for(j = i; j < size-1; j++)
	{
		table[j].map = table[j+1].map;
		table[j].num_pages = table[j+1].num_pages;
		table[j].num_frames = table[j+1].num_frames;
	}
	free(table[i].page_table);
	free(table[i].frame_table);
	rc = w_close_file(table[i].map->ram_handle);
   	DIE(rc<0,"close file");
   	rc = w_close_file(table[i].map->swap_handle);
   	DIE(rc<0, "close file");
	size--;
	table = realloc(table,(size)*sizeof(vm_map_alloc));
	return TRUE;
}

/*
 * moves the page with identifier page from SWAP to RAM
 */
static void swap_in(int ind, int page, int frame_poz)
{

	int rc = mprotect(table[ind].page_table[page].start,
	        w_get_page_size(), PROT_READ);
	DIE(rc == FALSE, "mprotect");
	char * buf = calloc(w_get_page_size(), sizeof(char));
	void * ret;
	memcpy(buf,table[ind].page_table[page].start, w_get_page_size());
	rc = munmap(table[ind].page_table[page].start, w_get_page_size());
	DIE(rc<0, "munmap");
	ret=mmap(table[ind].page_table[page].start, w_get_page_size(),
	    PROT_READ|PROT_WRITE,MAP_SHARED|MAP_FIXED, table[ind].map->ram_handle,
	    frame_poz*w_get_page_size());
	DIE(ret == MAP_FAILED,"mmap");
    	memcpy(table[ind].page_table[page].start, buf, w_get_page_size());
    	rc = mprotect(table[ind].page_table[page].start, 1, PROT_READ);
    	DIE(rc == FALSE,"mprotect");
    free(buf);
    buf = NULL;
    table[ind].frame_table[frame_poz].pte->protection = PROTECTION_NONE;
	table[ind].frame_table[frame_poz].pte->dirty=FALSE;
	table[ind].frame_table[frame_poz].pte->state = STATE_IN_SWAP;
	table[ind].page_table[page].frame=&table[ind].frame_table[frame_poz];
	table[ind].page_table[page].prev_state=STATE_IN_SWAP;
	table[ind].page_table[page].dirty=FALSE;
	table[ind].page_table[page].state=STATE_IN_RAM;
	table[ind].page_table[page].protection = PROTECTION_READ;
	table[ind].frame_table[frame_poz].pte=&table[ind].page_table[page];
}
/*
 * moves the frame_poz RAM frame in SWAP
 */
static void swap_out(int ind, int frame_poz)
{
int rc;
if(table[ind].frame_table[frame_poz].pte->dirty == TRUE ||
	(table[ind].frame_table[frame_poz].pte->dirty == FALSE &&
	 table[ind].frame_table[frame_poz].pte->prev_state==STATE_NOT_ALLOC))
	{
		char * buf = calloc(w_get_page_size(), sizeof(char));
		void * ret;
		memcpy(buf,table[ind].frame_table[frame_poz].pte->start, w_get_page_size());
		rc = munmap(table[ind].frame_table[frame_poz].pte->start, w_get_page_size());
		DIE(rc<0, "munmap");
		ret=mmap(table[ind].frame_table[frame_poz].pte->start,
		w_get_page_size(), PROT_READ|PROT_WRITE,MAP_SHARED|MAP_FIXED,
		table[ind].map->swap_handle, frame_poz*w_get_page_size());
    		if(ret == MAP_FAILED )
		        DIE(rc<0,"mmap");
		memcpy(table[ind].frame_table[frame_poz].pte->start, buf, w_get_page_size());
	    	rc = mprotect(table[ind].frame_table[frame_poz].pte->start,
			1, PROT_NONE);
		DIE(rc == FALSE,"mprotect");
	    	free(buf);
	    	buf = NULL;
	}
}
/*
 * on page fault, try to map the page in a RAM frame
 *
 */
void demand_paging(int ind, int page)
{
	int rc;
	int i;
	int poz=-1;
	void *aux;
	/*search for an empty frame*/
	for(i=0;i<table[ind].num_frames;i++)
	{
		if(table[ind].frame_table[i].pte == NULL)
		{
			poz=i;
			break;
		}
	}
	/*if no empty frames are availbale, pick a random one*/
	if(poz==-1)
		poz=rand()%table[ind].num_frames;
	/*if no empty frames were available, swap out one*/
    if(table[ind].frame_table[poz].pte != NULL)
    {
    	swap_out(ind,poz);
	    rc=munmap(table[ind].frame_table[poz].pte->start,w_get_page_size());
    	DIE(rc<0, "munmap");
    	aux=mmap(table[ind].frame_table[poz].pte->start, w_get_page_size(),
    	    PROT_NONE, MAP_SHARED|MAP_ANONYMOUS,0,0);
    	DIE(aux==MAP_FAILED,"mmap");
    	table[ind].frame_table[poz].pte->prev_state=STATE_IN_RAM;
	    table[ind].frame_table[poz].pte->state=STATE_IN_SWAP;
	    table[ind].frame_table[poz].pte->frame=NULL;
	    table[ind].frame_table[poz].pte->dirty=FALSE;
	    table[ind].frame_table[poz].pte->protection=PROTECTION_NONE;
	}
	/*map the new page*/
	table[ind].frame_table[poz].pte=&table[ind].page_table[page];
	table[ind].page_table[page].frame=&table[ind].frame_table[poz];
	table[ind].page_table[page].start=table[ind].map->start +
	    page * w_get_page_size();
    aux= mmap(table[ind].page_table[page].start, w_get_page_size(),
        PROT_READ|PROT_WRITE, MAP_SHARED|MAP_FIXED, table[ind].map->ram_handle,
            poz*w_get_page_size());
	DIE(aux == (char *)-1, "mmap");
	memset(aux, 0, w_get_page_size());
	rc=mprotect(aux,1, PROT_READ);
	table[ind].page_table[page].protection = PROTECTION_READ;
	table[ind].page_table[page].prev_state=	table[ind].page_table[page].state;
	table[ind].page_table[page].state=STATE_IN_RAM;
	table[ind].page_table[page].dirty=FALSE;

}
/*
 * handles deamand paging, swap in/out
 */
void vmsim_exception_handler(int sig, siginfo_t *siginfo, void *aux)
{
	w_ptr_t addr;
	int rc,  i;
	/*if signal is not SIGSEGV, use the old handler*/
	if(sig!=SIGSEGV)
	{
		w_exception_handler_t old_h;
		w_get_previous_exception_handler(&old_h);
		old_h(sig, siginfo, aux);
		return;
	}
	addr = (char*)siginfo->si_addr;
	/*find the entry in the table the address belongs*/
	for(i=0;i<size;i++)
	{
	    w_ptr_t end;
        end = table[i].map->start + table[i].num_pages*w_get_page_size();
        if(table[i].map->start <= addr && addr < end)
        {
            /*compute the page number*/
	        int page = (addr - table[i].map->start) / w_get_page_size();
            /*check for current state*/
		    switch(table[i].page_table[page].state)
	    	{
  	            /*if the page is not mapped*/
		    	case  STATE_NOT_ALLOC:
		    		{
		    			demand_paging(i,page);
	    				break;
	    			}
	    		/*if page is in ram*/
	    		case STATE_IN_RAM:
	    		{
    	    		if(table[i].page_table[page].protection == PROTECTION_READ)
	        		{
	        		    /*mark the page as dirty and change
	        		     protection to PROTECTION_WRITE*/
	       			    table[i].page_table[page].protection =
						PROTECTION_WRITE;
			            table[i].page_table[page].dirty = TRUE;
    				    rc =  mprotect(table[i].page_table[page].start,
 						1, PROT_READ|PROT_WRITE);
			            DIE(rc<0,"mprotect");
			            break;
	    		    }
	    		 	
	    		 }
	    		/*page is in swap*/
	    		case STATE_IN_SWAP:
	    		{
	        		int poz = rand() % table[i].num_frames;
	        		/*swap out a random page and move
	        		 the new page from SWAP to RAM*/
	        		swap_out(i,poz);
	        		swap_in(i, page, poz);
	    			break;
	    		}

		}

	}

}
}



