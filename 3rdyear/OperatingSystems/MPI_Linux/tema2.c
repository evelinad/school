/**
  * SO Assignment 2 2013
  *
  * MPI
  *
  * Dumitrescu Evelina 331CA
  */

#include <string.h>
#include<stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include<stdlib.h>
#include "generic_queue.h"
#include "mpi_err.h"
#include "mpi.h"

/*
 * main
 */

int main(int argc , char **argv)
{
int num_procs;
int i;
int status, code;
status=0;
static msgq_t *queue;
char *rankstr=calloc(100, sizeof(char));
char *pidstr=calloc(100, sizeof(char));
/*number of processes*/
num_procs=atoi(argv[2]);
/*vector of pid process*/
pid_t *pid=(pid_t *)calloc(num_procs,sizeof(pid_t));
DIE(pid == NULL, "error in memory allocation");
/*vector of queues*/
queue=calloc(num_procs, sizeof(msgq_t));
DIE(queue == NULL, "error in memory allocation");
for(i=0;i<num_procs;i++)
{
	/*create a new process and a new queue*/
	pid[i]=fork();
	char *qname=calloc(100, sizeof(char));
	DIE(qname == NULL, "error in memory allocation");
	sprintf(qname, "%s_%d",BASE_QUEUE_NAME,i);
	queue[i] = msgq_create(qname);
	free(qname);
	switch(pid[i])
	{
		case -1:
			exit(EXIT_FAILURE);
		case 0:
		{
			/*launch new process*/
		   strcpy(rankstr, "\0");
		   sprintf(rankstr,"%d",i);
		   strcpy(pidstr, "\0");
		   sprintf(pidstr,"%d",(int)getpid());
		   const char *cmdargv[]={argv[3],argv[2],rankstr,pidstr, NULL};
	           code = execvp(argv[3],(char *const *)cmdargv);
	           exit(EXIT_FAILURE);
		}
		default:
			break;
	}
}
/*wait for child processes*/
for(i=0;i<num_procs;i++)
{
	waitpid(pid[i],&status,0);
	code|=WEXITSTATUS(status);
}

/*destroy queues*/
for(i=0;i<num_procs;i++)
{
msgq_destroy(queue[i]);
}
free(queue);
free(pid);
free(rankstr);
free(pidstr);
return code;

}
