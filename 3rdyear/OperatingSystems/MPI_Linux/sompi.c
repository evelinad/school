/**
  * SO Assignment 2 2013
  *
  * MPI
  *
  * Dumitrescu Evelina 331CA
  */

#include <string.h>
#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include "generic_queue.h"
#include "mpi_err.h"
#include "mpi.h"
/*define constants for mpi enviroment*/
#define INIT 0
#define FINALIZED 1
#define NOTHING 3

/*mpi_comm_world struct*/
struct mpi_comm
{
	int numprocs;
};

/*set initial/default values*/
int init=NOTHING;
int rank=0;
int mypid=0;
int finalized=NOTHING;
/*receive queue*/
msgq_t queue;
struct mpi_comm *mpi_comm_world=NULL;

/*
 * initialize mpi environment
 * this function should be called only one
 */
int DECLSPEC MPI_Init(int *argc, char ***argv)
{
	/*check if mpi_comm_world is initialized only once*/
	if(mpi_comm_world == NULL)
	{
	   /*update init flag*/
	   init = INIT;
	   /*update rank, pid and size inner variables*/
	   mpi_comm_world=(struct mpi_comm *)calloc(1, sizeof(struct mpi_comm));
	   mpi_comm_world->numprocs = atoi((*argv)[1]);
	   rank = atoi((*argv)[2]);
	   mypid=atoi((*argv)[3]);
	   /*check if mpi_comm_world memory allocation failed*/
	   if(mpi_comm_world == NULL) return MPI_ERR_IO;
	   /*create queue for receiving messages*/
	   char *qname=calloc(100, sizeof(char));
	   if(qname == NULL) return MPI_ERR_IO;
	   sprintf(qname, "%s_%d",BASE_QUEUE_NAME,rank);
	   queue = msgq_create(qname);
	   free(qname);
	   qname=NULL;
	   return MPI_SUCCESS;
	}
	else return MPI_ERR_OTHER;
}

/*
 * sets flag on 1 if mpi env was previosuly initialized
 */
int DECLSPEC MPI_Initialized(int *flag)
{
	if(init == INIT)
	{
		*flag = 1;
		fprintf(stderr,"initialized dupa init.\n");
	}
	else {*flag = 0;fprintf(stderr,"initialized inainte de init.\n");}
	return MPI_SUCCESS;
}
/*
 * returns the number of process from the mpi environment
 */
int DECLSPEC MPI_Comm_size(MPI_Comm comm, int *size)
{
  if(init == NOTHING || finalized == FINALIZED)
 	return MPI_ERR_OTHER;
  else
  if(comm!=mpi_comm_world)
  {
 	return MPI_ERR_COMM;
  }
 else
 {
	*size=mpi_comm_world->numprocs;
 	return MPI_SUCCESS;
}
}
/*
 * returns the rank of the current process
 */
int DECLSPEC MPI_Comm_rank(MPI_Comm comm, int *rank2)
{
 if(init == NOTHING || finalized == FINALIZED)
 	return MPI_ERR_OTHER;
 else
 if(comm!=mpi_comm_world)
 {
 	return MPI_ERR_COMM;
 }
 else
 { *rank2 = rank;
 	return MPI_SUCCESS;
 }
}
/*
 * free the resources used for the mpi environment
 */
int DECLSPEC MPI_Finalize()
{
 if(mpi_comm_world !=NULL)
 	{
 		finalized = FINALIZED;
 		msgq_detach(queue);
 		free(mpi_comm_world);
 		mpi_comm_world = NULL;
 		return MPI_SUCCESS;
 	}
 return MPI_ERR_OTHER;
}
/*
 * sets flag on 1 if MPI_Finalize was previosuly called
 */
int DECLSPEC MPI_Finalized(int *flag)
{
	if(finalized == FINALIZED)
{		*flag = 1;	fprintf(stderr,"finalized dupa finish.\n");	}
	else {*flag = 0;fprintf(stderr,"finalized inainte finish.\n");	}
 	return MPI_SUCCESS;
}

/*
 * sends a message to the process with rank dest
 */
int DECLSPEC MPI_Send(void *buf, int count, MPI_Datatype datatype, int dest,
		      int tag, MPI_Comm comm)
{
	/*check if this method was properly called*/
	if(finalized == FINALIZED || init == NOTHING)
		return MPI_ERR_OTHER;
	else
	if(comm!=MPI_COMM_WORLD)
		return MPI_ERR_COMM;
	else
	if(dest<0 || dest>=mpi_comm_world->numprocs)
		return MPI_ERR_RANK;
	else
	if(datatype != MPI_INT && datatype!=MPI_CHAR && datatype!=MPI_DOUBLE)
		return MPI_ERR_TYPE;
	else
	{
		/*initialize message struct*/
	    message_t msg;
	    int msg_size;
	    switch(datatype)
	    {
	    	case MPI_INT:
		    	msg_size=sizeof(int)*count;
	    		break;
	    	case MPI_CHAR:
		    	msg_size=sizeof(char)*count;
	    		break;
	    	case MPI_DOUBLE:
		    	msg_size=sizeof(double)*count;
	    		break;
		}
		memset(&msg, 0, sizeof(msg));
		memcpy(msg.val, buf,msg_size);
	    msg.source =rank;
	    msg.dest=dest;
	    msg.size=count;
	    msg.tag=tag;
		char *qname=calloc(100, sizeof(char));
		if(qname == NULL) return MPI_ERR_IO;
		msgq_t queue1;
     	        sprintf(qname, "%s_%d",BASE_QUEUE_NAME,dest);
		/*get a reference to the receiving queue of process dest */
		queue1 = msgq_create(qname);
		free(qname);
		msgq_send(queue1, &msg);
		msgq_detach(queue1);
	}
 return 0;
}

/*
 * waits for a message from the process with rank source to be received
 */
int DECLSPEC MPI_Recv(void *buf, int count, MPI_Datatype datatype,
		      int source, int tag, MPI_Comm comm, MPI_Status *status)
{
	/*check if this method was properly called*/
	if(finalized == FINALIZED || init == NOTHING)
		return MPI_ERR_OTHER;
	else
	if(comm!=MPI_COMM_WORLD)
		return MPI_ERR_COMM;
	else
	if(source!=MPI_ANY_SOURCE)
		return MPI_ERR_RANK;
	else
	if(tag!=MPI_ANY_TAG)
		return MPI_ERR_TAG;
	else
	if(datatype != MPI_INT && datatype!=MPI_CHAR && datatype!=MPI_DOUBLE)
		return MPI_ERR_TYPE;
	else
	{
	 /*initialize message struct*/
	 message_t msg;
   	 memset(&msg, 0, sizeof(msg));
	 msgq_recv(queue, &msg);
	 int msg_size;
	 switch(datatype)
	 {
	    	case MPI_INT:
		    	msg_size=sizeof(int)*count;
	    		break;
	    	case MPI_CHAR:
		    	msg_size=sizeof(char)*count;
	    		break;
	    	case MPI_DOUBLE:
		    	msg_size=sizeof(double)*count;
	    		break;
	 }
  	 memset((char *)buf,0,msg_size);
	 memcpy((char *)buf,msg.val,msg_size);
	 /*check if status should not be updated*/
	 if(status!=MPI_STATUS_IGNORE)
	 {
	 status->MPI_TAG=msg.tag;
	 status->MPI_SOURCE=msg.source;
	 status->_size=msg.size;

	}
  return MPI_SUCCESS;
}

}

/*
 * sets in count variable the number of MPI_Datatype last received
 */
int DECLSPEC MPI_Get_count(MPI_Status *status, MPI_Datatype datatype, int *count)
{
	if(init == NOTHING || finalized == FINALIZED)
		return MPI_ERR_OTHER;
	else
	if(datatype != MPI_INT  && datatype != MPI_CHAR && datatype!=MPI_DOUBLE)
		return MPI_ERR_TYPE;
	else
	{
		*count = status->_size;
		return MPI_SUCCESS;
 }

}

