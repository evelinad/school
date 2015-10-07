/**
	Dumitrescu Evelina 341C3 Tema 1 SPRC
 */

#include <stdio.h> 
#include <time.h> 
#include <rpc/rpc.h> 
#include<string.h>
#include "rshell.h"

/*functie rpc pt executia unei comenzi simple*/
response *execute_simple_command_1_svc(request *req, struct svc_req *cl)
{
    static response load;	
	FILE *fp;
	/*executie comanda data ca parametru in lista de comenzi*/
	fp = popen(req->command.words_val,"r");	
	load.output.words_val = calloc(BUFFERSIZE, sizeof(char));
	/*citire date din file descriptor*/
    fread(load.output.words_val,1,BUFFERSIZE,fp);
    int ret=pclose(fp);
	load.output.words_len=strlen(load.output.words_val);
	/*verificare cod de eroare*/	
    if(ret !=0)
    {
       /*comanda a returnat un cod de eroare*/
       load.message.words_val=calloc(BUFFERSIZE, sizeof(char));
       strcpy(load.message.words_val,"Command execution failed.");
       load.message.words_len=strlen(load.message.words_val);

    }
    else 
    if(load.output.words_len == 0)
    {
       /*nu exista output*/ 
       load.message.words_val=calloc(BUFFERSIZE, sizeof(char));
       strcpy(load.message.words_val,"Command execution succeded.");
       load.message.words_len=strlen(load.message.words_val);
    }
    else
    {
        load.message.words_val=NULL;
        load.message.words_len=0;
        	
	}
	return &load;
}

/*functie rpc pt executia unei comenzi compuse*/
response *execute_command_1_svc(request *req, struct svc_req *cl)
{
    static response load, *loadaux;	
	FILE *fp;
	loadaux=&load;
	while(req!=NULL)
	{
		/*executie comanda data ca parametru in lista de comenzi*/
    	fp = popen(req->command.words_val,"r");	
    	loadaux->output.words_val = calloc(BUFFERSIZE, sizeof(char));
    	/*citire date din file descriptor*/    	
        fread(loadaux->output.words_val,1,BUFFERSIZE,fp);
        int ret=pclose(fp);
    	loadaux->output.words_len=strlen(loadaux->output.words_val);	
        if(ret !=0)
        {
            /*comanda a returnat un cod de eroare*/
            loadaux->message.words_val=calloc(BUFFERSIZE, sizeof(char));
            strcpy(loadaux->message.words_val,"Command execution failed.");
            loadaux->message.words_len=strlen(loadaux->message.words_val);

        }
        else 
        if(loadaux->output.words_len == 0)
        {
            /*nu exista output*/ 
            loadaux->message.words_val=calloc(BUFFERSIZE, sizeof(char));
            strcpy(loadaux->message.words_val,"Command execution succeded.");
            loadaux->message.words_len=strlen(loadaux->message.words_val);
            
        }
        else
        {
            loadaux->message.words_val=NULL;
            loadaux->message.words_len=0;
        	
	    }
	    if(req->next!=NULL)
	    {
	        loadaux->next=calloc(1,sizeof(response));
	        loadaux=loadaux->next;
	    }
	    req=req->next;
	}
	return &load;
}

/*functie rpc pt returnarea directorului curent*/
response *get_remotedir_1_svc(void *p, struct svc_req *cl)
{
	static response load;	
	FILE *fp;
	fp = popen("pwd","r");	
	load.output.words_val = calloc(BUFFERSIZE, sizeof(char));
    fscanf(fp, "%s",load.output.words_val);
    load.output.words_len=strlen(load.output.words_val);
	fclose(fp);
	return &load;
}



