/*
 * Dumitrescu Evelina  331CA
 * Tema 2 ASC
 *
 * */

#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <string.h>
#include <time.h>
#include "cblas.h"
#define SEC_MIL 1000
#define USEC_MIL 0.001
/*uset to set verbosity level*/
#define VERBOSE 1

/*for debugging*/
void print_vector(double *v, int n)
{
	int i;
	for(i=0;i<n;i++)
		printf("%lf ",v[i]);
	printf("\n");		
		
}

void print_matrix(double **m, int nrows,int ncols )
{
	int i,j;
	for(i=0;i<nrows;i++)
	{
		for(j=0;j<ncols;j++)
			printf("%lf ",m[i][j]);
		printf("\n");
	}
	printf("\n");		

}
/*main*/
int main(int argc, char **argv)
{

	double **A, *Avect;
	double *x, *y;
	double alfa, beta;
	int i,j;
	struct timeval begin,finish;
	double *aux;
	double *result;
	int size;

	/*error message for incorrect usage*/
	if(argc!=3)
	{
		fprintf(stderr, "USAGE: ./%s SIZE output_file\n.", argv[0]);
		exit(EXIT_FAILURE);
	}	
	size=atoi(argv[1]);
	FILE *fout=fopen(argv[2],"w" );

	/*memory allocation*/
	A=(double **)calloc(size, sizeof(double *));
	for(i=0;i<size;i++)
		A[i]=(double *)calloc(size, sizeof(double));

	x=(double *) calloc(size, sizeof(double));
	y=(double *) calloc(size, sizeof(double));
	aux=(double*) calloc(size, sizeof(double));
	result=(double*) calloc(size, sizeof(double));	
	/*generate data*/
	srand(time(NULL));
	alfa = rand()%size;
	beta=rand()%size;
	for(i=0;i<size;i++)
	{
		x[i]=rand()%size;
		y[i]=rand()%size;
		for(j=0;j<=i;j++)
			A[i][j]=A[j][i]=rand()%size;
	}
	
	gettimeofday(&begin, NULL);

	/*hand optimized multiplication*/
	for(i=0;i<size;i++)
	{
		double *aptr = &(A[i][0]);
		register double auxreg=0.0;
		for(j=0;j<size;j++)
		{
		  
		   auxreg+=(*aptr) * x[j];
		   aptr++;
		}
		aux[i]=auxreg;
	}
	for(i=0;i<size;i++)
		result[i] = alfa * aux[i]+beta * y[i];
	gettimeofday(&finish, NULL);

	double duration = finish.tv_sec * SEC_MIL + finish.tv_usec * USEC_MIL - begin.tv_sec * SEC_MIL - begin.tv_usec * USEC_MIL;
	
	fprintf(fout,"%lf\n",duration);
	/*debug*/
        if(VERBOSE!=0)
        {
	        Avect=calloc(size * size, sizeof(double));

        	int k=0;
	        for(i=0;i<size;i++)
        	   for(j=0;j<size;j++)
                        Avect[k++]= A[i][j];
		ATL_dsymv(121,size,alfa,Avect,size,x,1,beta,y,1);
		if(VERBOSE == 2)
		{
                printf("alfa = %lf\n", alfa);
                printf("beta = %lf\n", beta);
                printf("x=\n");
                print_vector(x,size);
                printf("y=\n");
                print_vector(y, size);
                printf("A=\n");
                print_matrix(A, size, size);
                printf("plain multiplication result =\n");
                print_vector(result,size);
                printf("blas multiplication  result =\n");
                print_vector(y,size);
                }
		int ok =0;		
		/*check if manual multiplication is correct*/
		for(i=0;i<size && ok == 0;i++)
                        if(result[i]!=y[i])
                        {
                           printf("\nERROR: Matrix multiplication result is wrong.\n");
                           ok =1;	
			}
		if(ok == 0)
			printf("\nMatrix multiplication result is correct.\n");	
        }
	/*cleanup*/
	free(x);
	free(y);
	free(aux);
	for(i=0;i<size;i++)
		free(A[i]);
	free(A);
	fclose(fout);
	return 0;			
			
}
