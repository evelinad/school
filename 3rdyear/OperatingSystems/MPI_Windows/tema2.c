/**
  * SO Assignment 2 2013
  *
  * MPI
  *
  * Dumitrescu Evelina 331CA
  */
#include "generic_queue.h"
#include <windows.h>
#include <string.h>
#include <stdlib.h>
#include<assert.h>
#define NAMESIZE 100
/*
 * close process function
 */
static VOID CloseProcess(LPPROCESS_INFORMATION lppi)
{
	CloseHandle(lppi->hThread);
	CloseHandle(lppi->hProcess);
}
/*
 * main
 */
int main(int argc, char **argv)
{
	int i ;
	BOOL *bRes, dwRes, bRes2;
	static msgq_t *queue;
	char *qname;
	char * cmd;
	/*number of processes*/
	int num_procs = atoi(argv[2]);
	STARTUPINFO *si;
	PROCESS_INFORMATION *pi;
	/*process information vectors*/
	si=calloc(num_procs, sizeof(STARTUPINFO));
	pi=calloc(num_procs,sizeof(PROCESS_INFORMATION));
	bRes=calloc(num_procs, sizeof(BOOL));
	for(i=0;i<num_procs;i++)
	{
		qname=calloc(NAMESIZE, sizeof(char));
        sprintf(qname, "%s_%d",BASE_QUEUE_NAME,i);
		free(qname);
		cmd=calloc(NAMESIZE, sizeof(char));
		sprintf(cmd, "%s %d %d", argv[3],num_procs,i);
		/*create new process*/
		bRes[i]=CreateProcess(NULL,cmd,NULL,NULL,TRUE,0,NULL,NULL,&si[i],&pi[i]);
		free(cmd);
		if(!bRes[i])
		{
			fprintf(stderr, "failed creating process\n.");
			return EXIT_FAILURE;
		}
	}
	/*wait for child process*/
	for(i=0;i<num_procs;i++)
	{
		if(bRes[i])
		{
			dwRes= WaitForSingleObject(pi[i].hProcess, INFINITE);
			DIE(dwRes==WAIT_FAILED, "wait for process");
			bRes2=GetExitCodeProcess(pi[i].hProcess, &dwRes);
			CloseProcess(&pi[i]);
		}
	}
/*free memory*/
free(bRes);
free(si);
free(pi);
return 0;
}
