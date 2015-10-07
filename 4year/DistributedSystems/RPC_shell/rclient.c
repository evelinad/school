/**
	Dumitrescu Evelina 341C3 Tema 1 SPRC
 */

#include <stdio.h> 
#include <time.h> 
#include <rpc/rpc.h> 
#include<string.h>
#include "rshell.h" 
#define SIMP_COMM 0
#define COMP_COMM 1
#define EXIT 2
#define NO_TOKENS 10

/* 
    metoda pentru citirea unei comenzi date de user
*/
void get_user_command(char *buffer)
{
    fgets(buffer, BUFFERSIZE, stdin);
}
/*
    afisare prompt
*/
void get_prompt(char *remote_dir)
{
    printf("\n[%s] > ", remote_dir);
}

/*
     mesaj de initializare shell
*/
void init()
{
    printf("Welcome to rshell.");
}

/*
    parsare comanda de input si formarea listei de cerere
*/
int parse_command(char *buffer, request *req, int *n)
{
    buffer[strlen(buffer)-1] = '\0';
    char *ptk = strtok(buffer,";");
    *n=0;
    request *req_aux=req;  
    while(ptk!=NULL)
    {
            req_aux->command.words_val=strdup(ptk);           
	        req_aux->command.words_len=strlen(req_aux->command.words_val);		
            (*n)++;
            ptk = strtok(NULL, ";");    	  
            if(ptk!=NULL)
            {
    	     req_aux->next =(request*)calloc(1,sizeof(request));
    	     req_aux=req_aux->next;
            }
            else 
            {
             req_aux->next=NULL;
            }
    }
    req_aux=req;
    return ((*n) == 1) ;
}

int main(int argc, char *argv[]){

	/* variabila clientului */
	CLIENT *handle;
    char * buffer = calloc(BUFFERSIZE, sizeof(char));
	request req;
	response *resp;
	int no_comm;
	/* initializare client*/
	handle=clnt_create(
		argv[1],		/* numele masinii unde se afla server-ul */
		LOAD_PROG,		/* numele programului disponibil pe server */
		LOAD_VERS,		/* versiunea programului */
		"tcp");			/* tipul conexiunii client-server */
	
	if(handle == NULL) {
		clnt_pcreateerror (argv[1]);
		exit(1);
	}
	/*initializare shell*/
	init();
	/*loop comenzi*/
	while(1)
	{
	    memset(buffer,0,BUFFERSIZE);
	    /*director curent server*/
		resp = get_remotedir_1(NULL, handle); 
		if (resp == NULL)
		{
			clnt_perror (handle, argv[1]);
			exit(1);
		}
	    get_prompt(resp->output.words_val);
	    get_user_command(buffer);
	    parse_command(buffer, &req,&no_comm);
	    if(strcmp(buffer,"exit")==0)
	    {
            clnt_destroy(handle);
            break;
        }
	    /*daca  inputul contine o singura comanda apelez exec-simple_command
	      altfel exec_command
	      afisez continutul de la stdut si in caz se eroare/nu exista output
	      afisez mesaj corespunzator  
	     */
	    if(no_comm==1)
        {
            resp=execute_simple_command_1(&req, handle);
            if (resp == NULL)
		    {
			    clnt_perror (handle, argv[1]);
			    exit(1);
		    }
            if(resp->output.words_val!=NULL)
                printf("%s\n",resp->output.words_val); 
            if(resp->message.words_val!=NULL)     
                printf("%s\n",resp->message.words_val);  
        }
        else 
        {
            resp=execute_command_1(&req, handle);
            if (resp == NULL)
		    {
			    clnt_perror (handle, argv[1]);
			    exit(1);
		    }
            request *req_aux=&req;         
            while(resp)
            {
               if(resp->output.words_val!=NULL)
                    printf("%s\n",resp->output.words_val); 
               if(resp->message.words_val!=NULL)     
                    printf("%s\n",resp->message.words_val);  
               resp=resp->next;
            }
        }
		
	}	
	free(buffer);
	return 0;
}
