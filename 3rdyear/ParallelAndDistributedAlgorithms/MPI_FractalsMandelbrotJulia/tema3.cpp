//DUMITRESCU EVELINA 331CA Tema3 APD

#include<stdio.h>
#include<mpi.h>
#include<iostream>
#include<stdlib.h>
#define NUM_COLORS 256
#define JULIA 1
#define MANDLEBROT 0
using namespace std;


/*
    metoda calculeaza valoarea unui pixel din multimea mandelbrot
*/
int mandelbrot(int x, int y,double xmin,double ymin,double resolution, \
    int MAX_STEPS)
{

int color=0;
double zreal, zimag;
zreal = zimag = 0;
int step = 0;
double zrealtemp, zimagtemp;
while ((zreal * zreal + zimag * zimag) < 4.0
		     && step < MAX_STEPS)
		{
		  step++;
		  zrealtemp = zreal * zreal - zimag * zimag;
		  zimagtemp = 2.0 * zreal * zimag;
		  zreal = zrealtemp;
		  zimag = zimagtemp;
		  zreal += (double) x *resolution + xmin;
		  zimag += ymin + (double) y *resolution;
		}
color = step % NUM_COLORS;		
return color;
}
/*
    metoda calculeaza valoarea unui pixel din multimea julia
*/
int julia(int x, int y,double cx, double cy,double xmin,double ymin,\
    double resolution, int MAX_STEPS)
{
int color=0;
int step = 0;
double zreal, zimag;
double zrealtemp, zimagtemp;
zreal = zimag = 0;
zreal = (double) x *resolution + xmin;
zimag = (double) y *resolution + ymin;

while ((zreal * zreal + zimag * zimag) < 4.0
		     && step < MAX_STEPS)
		{
		  step++;
		  zrealtemp = zreal * zreal - zimag * zimag + cx;
		  zimagtemp = 2.0 * zreal * zimag + cy;
		  zreal = zrealtemp;
		  zimag = zimagtemp;
		}
color = step % NUM_COLORS;
return color;
}

/*
    metoda Main
*/
int main (int argc, char **argv)
{
 
 
  MPI_Status status;
  int rank, size;  
  
  // initializare environment MPI
  MPI_Init(&argc, &argv);

  //determina rankul procesului  
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);
  
  //determina numarul de procese
  MPI_Comm_size(MPI_COMM_WORLD, &size);
  FILE *in = fopen (argv[1], "r");
  FILE *out = fopen (argv[2], "w");
  int type, MAX_STEPS;
  double xmin, xmax, ymin, ymax, cx, cy, resolution;
  int chunklines , leftlines;
  int xint, yint;
  int *temp;
   int **colors;
   
   /*
   daca rankul e 0 
    - citesc datele din fisier 
    - calculez numarul de linii, coloane, dimensiune chunk linii+nr linii ramase
    - initializare matrice de culori
   */
   if(rank ==0)
   {
  fscanf (in, "%d %lf %lf %lf %lf %lf  %d", &type, &xmin, &xmax, &ymin, &ymax,
	  &resolution, &MAX_STEPS);
  if (type == 1)
    fscanf (in, "%lf %lf", &cx, &cy);
    double x, y;
  x = ((double) xmax - (double) xmin) / (double) resolution;
  y = ((double) ymax - (double) ymin) / (double) resolution;

  xint = (int) x;
  yint = (int) y;
  fprintf (out, "P2\n%d %d\n%d\n", xint, yint, 255);
  chunklines = yint/size;
  leftlines = yint%size;
   
    colors=(int **)calloc(sizeof(int *), yint);
   for(int k=0;k<yint;k++)
    colors[k] = (int *) calloc(sizeof(int), xint); 
    }
    
  /*
    trimit valorile catre toate procesele
  */  
  MPI_Bcast(&type,1,MPI_INT,0,MPI_COMM_WORLD);
  MPI_Bcast(&xmin,1,MPI_DOUBLE,0,MPI_COMM_WORLD);
  MPI_Bcast(&xmax,1,MPI_DOUBLE,0,MPI_COMM_WORLD);
  MPI_Bcast(&ymin,1,MPI_DOUBLE,0,MPI_COMM_WORLD);
  MPI_Bcast(&ymax,1,MPI_DOUBLE,0,MPI_COMM_WORLD);
  MPI_Bcast(&resolution,1,MPI_DOUBLE,0,MPI_COMM_WORLD);
  MPI_Bcast(&MAX_STEPS,1,MPI_INT,0,MPI_COMM_WORLD);
  if(type == 1)
  {
    MPI_Bcast(&cx,1,MPI_DOUBLE,0,MPI_COMM_WORLD);
    MPI_Bcast(&cy,1,MPI_DOUBLE,0,MPI_COMM_WORLD);
  }
  
        
  MPI_Bcast(&chunklines,1,MPI_INT,0,MPI_COMM_WORLD);
  MPI_Bcast(&leftlines,1,MPI_INT,0,MPI_COMM_WORLD);
  MPI_Bcast(&xint,1,MPI_INT,0,MPI_COMM_WORLD);
  MPI_Bcast(&yint,1,MPI_INT,0,MPI_COMM_WORLD);
   
  /*
    fractal de tip mandlebrot
  */ 
  if (type == MANDLEBROT)
    {
     
      int *temp;
      int i=0;
   
      int maxlimit = (rank+1) * chunklines;
      if(rank ==  size-1) maxlimit+=leftlines;
      for (int y = rank*chunklines; y <maxlimit; y++)
	{
	    i=0;
      temp= (int *)calloc(sizeof(int), xint+1);
      temp[i++] = y;
	  for (int x = 0; x < xint; x++)
	    {
	      /*
	       calculez valoare pixelului
	      */
	      int color = mandelbrot(x,y,xmin,ymin,resolution,MAX_STEPS);
	      /*
	        il adaug in matricea de culori pe care o trimit mai departe 
	        procesului root
	      */
	      temp[i++] = color;
	    }
        MPI_Send(temp, i, MPI_INT,0,0,MPI_COMM_WORLD);
     
	}
      

    }
  /*
    fractal de tip julia
  */
  else
    {

      int i=0;
      double zreal, zimag;
      double zrealtemp, zimagtemp;
      int maxlimit = (rank+1) * chunklines;
      if(rank ==  size-1) maxlimit+=leftlines;  
      for (int y = rank*chunklines; y <maxlimit; y++)
      {
	
    	  i=0;	  
          temp= (int *)calloc(sizeof(int), xint+1);
          temp[i++] = y;
    	  for (int x = 0; x < xint; x++)
	      {
	        /*
	         calculez valoare pixelului
	        */
	         int color = julia(x,y,cx,cy,xmin,ymin,resolution,MAX_STEPS);
	      /*
	        il adaug in matricea de culori pe care o trimit mai departe 
	        procesului root
	      */
	         temp[i++] = color;


	      }
          MPI_Send(temp, i, MPI_INT,0,0,MPI_COMM_WORLD);

	    }
    }
    /*
        daca rankul e 0 fac receive pentru date
    */
    if(rank ==0)
    {
 
       int *buffer;
       for(int r=0;r<size;r++)     
       {
          int maxlimit = chunklines;
          if(r == size-1) maxlimit+=leftlines;
          for(int i=0;i<maxlimit;i++)
          {
            buffer=(int *)calloc(sizeof(int), xint+1);
            /*
            primesc matricile temporare
            */
            MPI_Recv(buffer,xint+1, MPI_INT,r,0,MPI_COMM_WORLD,&status);
            bool ok = false;
        
            for(int k=0;k<xint;k++)
            {
                if(buffer[k+1]!=0) ok = true;
                /*
                  update matrice culori
                */
                colors[buffer[0]][k]=buffer[k+1];
         
            }

        }
       
    
       }
       /*
        scriu datele in fisier
       */
        for(int p=yint-1;p>0;p--)
        {
            if(p!=yint-1) 
            fprintf(out, "\n");
            for(int q=0;q<xint;q++)
            {
               if(q!=0) fprintf(out, " "); 
               fprintf (out, "%d", colors[p][q]);
               
             }  
          
             
         }
      fclose (in);
      fclose (out);
   

   } 
  /*
   se incheie executia environmentului mpi
  */
  MPI_Finalize();
  return 0;
}
