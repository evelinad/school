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
  * Task #2, win
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
#define MAX_WORD_SIZE 5000
#ifndef WIN32


#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <sys/mman.h>
#include <semaphore.h>
#include <mqueue.h>

struct _msgq_t {
	mqd_t	mq;
	char	name[MAX_IPC_NAME];
};



typedef struct _msgq_t*	msgq_t;

#define INVALID (void*)-1

#else

#undef UNICODE
#undef _UNICODE

#include <windows.h>

#define FATAL_ERROR_ACTION(place)

typedef HANDLE msgq_t;

/* should be C99... but Windows is unaware... */
#define snprintf(...)	sprintf_s(__VA_ARGS__)

#endif //WIN32

#ifdef _DEBUG
# define dprintf(...) printf(__VA_ARGS__)
#else
# define dprintf(...) do {} while(0)
#endif



#define BASE_QUEUE_NAME 	"my_queue3"


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
	
};

typedef struct _message_t message_t;


#endif // _COMMON_H
