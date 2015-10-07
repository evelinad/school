/**
 * Operating Sytems 2013 - Assignment 1
 *
 */

#ifndef _UTILS_H
#define _UTILS_H

#include "parser.h"
#include <stdio.h>
#include <stdlib.h>
#define MAX_ARGS_NO 15
#define CHUNK_SIZE 100
#define ERR_ALLOCATION "unable to allocate memory"

#define SHELL_EXIT -100

/**
 * Readline from mini-shell.
 */
char *read_line();

/**
 * Parse and execute a command.
 */
int parse_command(command_t *cmd1, int level, command_t *cmd2);


/* useful macro for handling error codes */
#define DIE(assertion, call_description)				\
	do {								\
		if (assertion) {					\
			fprintf(stderr, "(%s, %d): ",			\
					__FILE__, __LINE__);		\
			perror(call_description);			\
			exit(EXIT_FAILURE);				\
		}							\
	} while(0)

#endif
