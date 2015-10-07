/**
  * SO Assignment 2 2013
  *
  * MPI
  *
  * Dumitrescu Evelina 331CA
  */
/**
  * SO, 2011
  * Lab #5
  *
  * Task #2, lin
  *
  * Types and structures
  */
#ifndef COMMON_H_
#define COMMON_H_	1

#include <stdlib.h>
#include <stdio.h>

#include "utils.h"


/**
 * Maximum size for the name of an IPC name
 */
#define MAX_IPC_NAME		128



#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include<sys/types.h>
#include <mqueue.h>

struct _msgq_t {
	mqd_t	mq;
	char	name[MAX_IPC_NAME];
};



typedef struct _msgq_t*	msgq_t;

#define INVALID (void*)-1

#ifdef _DEBUG
# define dprintf(...) printf(__VA_ARGS__)
#else
# define dprintf(...) do {} while(0)
#endif




#define MAX_WORD_SIZE		5000


/**
 * the command message to be sent via the queue, with
 * variable length payload of text
 */
struct _message_t {
	char val[MAX_WORD_SIZE];
	int tag;
	int source;
	int dest;
	int size;

}__attribute__((packed));

typedef struct _message_t message_t;


#endif // _COMMON_H
