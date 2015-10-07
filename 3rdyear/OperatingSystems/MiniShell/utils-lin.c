/**
 * Operating Sytems 2013 - Assignment 1
 * DUMITRESCU EVELINA 331CA
 */

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <unistd.h>
#include "utils.h"
#define ERROR_MSG "Execution failed for '%s'\n"


/*
 * Concatenate parts of the word to obtain the command
 */
static char *get_word(word_t *s)
{
	int string_length = 0;
	int substring_length = 0;

	char *string = NULL;
	char *substring = NULL;

	while (s != NULL) {
		substring = strdup(s->string);

		if (substring == NULL) {
			return NULL;
		}

		if (s->expand == true) {
			char *aux = substring;
			substring = getenv(substring);

			/* prevents strlen from failing */
			if (substring == NULL) {
				substring = (char *)calloc(1, sizeof(char));
				if (substring == NULL) {
					free(aux);
					return NULL;
				}
			}

			free(aux);
		}

		substring_length = strlen(substring);

		string = (char *)realloc(string, string_length
				 + substring_length + 1);
		if (string == NULL) {
			if (substring != NULL)
				free(substring);
			return NULL;
		}

		memset(string + string_length, 0, substring_length + 1);

		strcat(string, substring);
		string_length += substring_length;

		if (s->expand == false) {
			free(substring);
		}

		s = s->next_part;
	}

	return string;
}

/*
 * Concatenate command arguments in a NULL terminated list in order to pass
 * them directly to execv.
 */
static char **get_argv(simple_command_t *command, int *size)
{
	char **argv;
	word_t *param;

	int argc = 0;
	argv = (char **)calloc(argc + 1, sizeof(char *));
	assert(argv != NULL);

	argv[argc] = get_word(command->verb);
	assert(argv[argc] != NULL);

	argc++;

	param = command->params;
	while (param != NULL) {
		argv = (char **)realloc(argv, (argc + 1) * sizeof(char *));
		assert(argv != NULL);

		argv[argc] = get_word(param);
		assert(argv[argc] != NULL);

		param = param->next_word;
		argc++;
	}

	argv = (char **)realloc(argv, (argc + 1) * sizeof(char *));
	assert(argv != NULL);

	argv[argc] = NULL;
	*size = argc;

	return argv;
}
/*
 * restore file descriptors to stdin/stdout/stderr
 */
static void restore_fd(int fdin, int fdout, int fderr)
{


    int rc;
    if(fdin>-1)
    {
        rc = dup2(fdin, STDIN_FILENO);
        DIE(rc==-1, "Error executing dup2.");
        close(fdin); 
    }

    if(fdout>-1)
    {
        rc = dup2(fdout, STDOUT_FILENO);
        DIE(rc==-1, "Error executing dup2.");
        close(fdout);
    }

    if(fderr>-1)
    {
        rc = dup2(fderr, STDERR_FILENO);
        DIE(rc==-1, "Error executing dup2.");
        close(fderr);
    }

}

/*
 * performs redirection of input/output/error according to cmd->io_flags
 */
static void redirect_fd(simple_command_t * cmd, int * fdin_bk, int *fdout_bk,
			int * fderr_bk)
{

    int fdin, fdout, fderr;
    fdin=fdout=fderr=-1;
    int rc;
    if (cmd->in != NULL) {
    	/*backup input fd for later fd restore*/
        *fdin_bk=dup( STDIN_FILENO);
	    DIE( *fdin_bk < 0, "dup");
        fdin = open(get_word(cmd->in), O_RDONLY);
        DIE( fdin < 0, "Error executing open.");
        rc = dup2(fdin, STDIN_FILENO);
        close(fdin);
	    DIE( rc < 0, "Error executing dup2.");

    }
    if(cmd->out!=NULL)
    {
    	/*backup output fd for later fd restore*/
        *fdout_bk=dup(STDOUT_FILENO);
		DIE( *fdout_bk < 0, "dup");
		/*check file type */
        if(cmd->io_flags == IO_REGULAR)
        {
            fdout = open(get_word(cmd->out),
			 O_CREAT | O_TRUNC | O_WRONLY | O_FSYNC, 0644);
			DIE( fdout < 0, "Error executing open.");
			rc = dup2(fdout, STDOUT_FILENO);
			close (fdout);
		    DIE( rc < 0, "Error executing dup2.");
        }
        if(cmd->io_flags == IO_OUT_APPEND)
        {
            fdout = open(get_word(cmd->out),
			 O_CREAT | O_WRONLY | O_APPEND | O_FSYNC, 0644);
     		DIE( fdout < 0, "Error executing open.");
     		rc = dup2(fdout, STDOUT_FILENO);
			close(fdout);
		    DIE( rc < 0, "Error executing dup2.");
        }
    }
     if (cmd->err != NULL) {
		/*backup error fd for later fd restore*/
        *fderr_bk = dup( STDERR_FILENO);
      	DIE( *fderr_bk < 0, "dup");
      	/*if output & error files are identical*/
        if((cmd->out != NULL)
		&&(strcmp(get_word(cmd->out), get_word(cmd->err))==0))
         {
        	rc = dup2(STDOUT_FILENO,STDERR_FILENO);
		    DIE( rc < 0, "Error executing dup2.");

         }

         else
         {
           /*error redirection without output*/
           if(cmd->out==NULL)
	       {
	         if(cmd->io_flags == IO_REGULAR)
		     {
                         fdout = open(get_word(cmd->err),
				O_CREAT | O_TRUNC | O_WRONLY | O_FSYNC, 0644);
	                 DIE( fdout < 0, "Error executing open.");
			 rc = dup2(fdout, STDERR_FILENO);
			 close (fdout);
			 DIE( rc < 0, "Error executing dup2.");
	         }
	         if(cmd->io_flags == IO_ERR_APPEND)
             	{
	                 fdout = open(get_word(cmd->err),
				O_CREAT | O_WRONLY | O_APPEND | O_FSYNC, 0644);
		         DIE( fdout < 0, "Error executing open.");
    	 		 rc = dup2(fdout, STDERR_FILENO);
			 close(fdout);
			 DIE( rc < 0, "Error executing dup2.");
    	    }
         }
         /*both error and output redirection , but in different files*/
         else
         {
            fdout = open(get_word(cmd->err),
    			 O_CREAT | O_TRUNC | O_WRONLY | O_FSYNC, 0644);
	    DIE( fdout < 0, "Error executing open.");
	    rc = dup2(fdout, STDERR_FILENO);
	    close (fdout);
            DIE( rc < 0, "Error executing dup2.");
         }
       }
    }
    if(fdout!=-1)
    {
        close(fdout);
    }
    if(fderr!=-1)
    {
        close(fderr);
    }
    if(fdin!=-1)
    {
        close(fdin);
    }
}
/**
 * Parse a simple command (internal, environment variable assignment,
 * external command).
 */
static int parse_simple(simple_command_t *cmd, int level, command_t *father)
{
    int fdin_cp=-1, fdout_cp=-1, fderr_cp=-1;
	char **argv=NULL;
    int argc ;
    int status = 0;
    pid_t pid;
    /*
     * for exit/quit commands return SHELL_EXIT for breaking the loop
     * from  shell_start in main.c
     */
    if((strcmp (cmd->verb->string, "exit") == 0) 
	||(strcmp (cmd->verb->string, "quit") == 0))
    {
        fflush(stderr);
        fflush(stdout);
        status= SHELL_EXIT;
    }
    else
    /*change current directory command*/
    if(strcmp(cmd->verb->string, "cd") == 0 )
    {
        redirect_fd(cmd, &fdin_cp, &fdout_cp, &fderr_cp);
        status=chdir(cmd->params->string);
    }
    /*
     *external command:
     *1.fork new process
	 *2c.perform redirections in child
     *3c.load executable in child
     *2.wait for child
     *3.return exit status
     */
    else
    {
         pid = fork();
         if(pid == -1)
         {
            fprintf(stderr, "ERROR forking.\n");
            return EXIT_FAILURE;
         }
         else if(pid == 0)
         {

                redirect_fd(cmd, &fdin_cp, &fdout_cp, &fderr_cp);
                argv=get_argv(cmd, &argc);
                execvp(argv[0],(char * const *)argv);
                fprintf(stderr, ERROR_MSG, argv[0]);
                exit(EXIT_FAILURE);
         }
         else
         {
            waitpid(pid, &status, 0);
         }
    }
    restore_fd(fdin_cp, fdout_cp, fderr_cp);
    if(argv!=NULL)
    {
    int i;
    for(i=0;i<argc;i++)
    	free(argv[i]);
    free(argv)	;
    }
    /*actual exit status */
	return status;
}

/**
 * Process two commands in parallel, by creating two children.
 */
static bool do_in_parallel(command_t *cmd1, command_t *cmd2,
			 int level, command_t *father)
{
    /*execute cmd1 and cmd2 simultaneously */
    pid_t pid;
    int status;
    int retcode1,  retcode ;
    int i;
    for(i=0;i<2;i++)
    {
        pid= fork();
        if(pid  == -1)
        {
            exit(EXIT_FAILURE);
        }
        if(pid == 0)
        {
            if(i%2 == 0)
                retcode1=parse_command(cmd1,level+1, father);
            else
                retcode1=parse_command(cmd2,level+1, father);
            exit(retcode1);
        }
	}
	for(i=0;i<2;i++)
	{
	    wait(&status);
	    retcode|=WEXITSTATUS(status);
    }
    /*actual exit status */
	return retcode;
}

/*
 * Run commands by creating an anonymous pipe (cmd1 | cmd2)
 */
static bool do_on_pipe(command_t *cmd1, command_t *cmd2,
			int level, command_t *father)
{
   /*redirect the output of cmd1 to the input of cmd2 */
   int pipefd[2];
   int rc;
   rc=pipe(pipefd);
   DIE(rc==-1, "pipe error");
   int pid = fork();
   int status;
   int ret=0;
   int pid_pipe;
   switch(pid)
   {
    case 0:
		  switch(fork())
		  {
		    case 0:
		  	    close(pipefd[0]);
		  	    rc=dup2(pipefd[1], STDOUT_FILENO);
 			    DIE( rc < 0, "Error executing dup2.");
 		  	    close(pipefd[1]);
		  	    ret|= parse_command(cmd1, level+1, father);
			    exit(EXIT_SUCCESS);
		    default:
		    	exit(EXIT_SUCCESS);
		    }
    default:
    		waitpid(pid, &status, 0);
    		if (WIFEXITED(status))
				ret|= WEXITSTATUS(status);
    }
   switch(pid_pipe = fork())
    {
	case 0:
	   close(pipefd[1]);
           dup2(pipefd[0], STDIN_FILENO);
	   DIE( rc < 0, "Error executing dup2.");
	   close(pipefd[0]);
           ret|= parse_command(cmd2, level+1, father);
	   exit(EXIT_SUCCESS);
        default:
           close(pipefd[1]);
           close(pipefd[0]);
	   waitpid(pid_pipe, &status, 0);
	   if (WIFEXITED(status))
	      ret|= WEXITSTATUS(status);

   }
    /* actual exit status */
	return ret;
}


/*
 * update environment variables
 */
void update_env(char * key, char * value)
{
     char * val = value;
     setenv(key, val,1);
}
/**
 * Parse and execute a command.
 */
int parse_command(command_t *c, int level, command_t *father)
{
    int ret=0;
	if (c->op == OP_NONE) {
		/*execute a simple command */
        if (c->scmd!=NULL){
           if(c->scmd->verb->next_part
             && c->scmd->verb->next_part->next_part->expand == false)
                update_env((char *)c->scmd->verb->string,
                           (char *)c->scmd->verb->next_part->next_part->string);
           else
               ret= parse_simple(c->scmd, 0, NULL);
        }
        /*actual exit code of command */
        return ret;

	}

	switch (c->op) {
	case OP_SEQUENTIAL:
		/*execute the commands one after the other */
		ret = parse_command(c->cmd1, level+1, c);
		return ret | parse_command(c->cmd2, level+1, c);
		break;

	case OP_PARALLEL:
		/*execute the commands simultaneously */
		do_in_parallel(c->cmd1, c->cmd2, level,c);
		break;

	case OP_CONDITIONAL_NZERO:
		/*execute the second command only if the first one
          returns non zero */
         ret = parse_command(c->cmd1, level+1, c);
         if(!ret) return ret;
         return parse_command(c->cmd2, level+1, c);
		break;

	case OP_CONDITIONAL_ZERO:
		/*execute the second command only if the first one
         returns zero */
         ret = parse_command(c->cmd1, level +1, c);
         if(ret) return ret;
         return parse_command(c->cmd2, level+1, c);
		break;

	case OP_PIPE:
		/* redirect the output of the first command to the
		 input of the second */
		return do_on_pipe(c->cmd1, c->cmd2, level, c);
		break;

	default:
		assert(false);
	}
	/*actual exit code of command */
	return ret;
}

/*
 * Readline from mini-shell.
 */
char *read_line()
{
	char *instr;
	char *chunk;
	char *ret;

	int instr_length;
	int chunk_length;

	int endline = 0;

	instr = NULL;
	instr_length = 0;

	chunk = (char *)calloc(CHUNK_SIZE, sizeof(char));
	if (chunk == NULL) {
		fprintf(stderr, ERR_ALLOCATION);
		return instr;
	}

	while (!endline) {
		ret = fgets(chunk, CHUNK_SIZE, stdin);
		if (ret == NULL) {
			break;
		}

		chunk_length = strlen(chunk);
		if (chunk[chunk_length - 1] == '\n') {
			chunk[chunk_length - 1] = 0;
			endline = 1;
		}

		ret = instr;
		instr = (char *)realloc(instr, instr_length + CHUNK_SIZE + 1);
		if (instr == NULL) {
			free(ret);
			return instr;
		}
		memset(instr + instr_length, 0, CHUNK_SIZE);
		strcat(instr, chunk);
		instr_length += chunk_length;
	}

	free(chunk);

	return instr;
}

