/*
 * Evelina Dumitrescu SCPD Tema 3 CCG
 *
 * */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <cblas.h>
#include <netcdf.h>
#include <opencv/cv.h>
#include <opencv/highgui.h>


#define NETCDF_DIE(e) do { \
     printf("Got error in file %s, line %d, error:%s.\n", __FILE__, __LINE__, nc_strerror(e)); \
     exit(EXIT_FAILURE); \
} while (0)

#define DIE(msg) do { \
     fprintf(stderr, msg); \
     exit(EXIT_FAILURE); \
} while (0)

#define LOG(msg) do { \
     fprintf(stdout, msg); \
} while (0)


/* result matrix for cblas_dgemm transformation */
double *A;

/* matrix dimension and size */
long long N;
long long size;


void opencv(char * image_file)
{
  IplImage* image;
  double* sqA;

  /* do some sanitizing checks for the image file */
  image = cvLoadImage(image_file, CV_LOAD_IMAGE_GRAYSCALE);
  if(!image) {
    DIE("Could not open or find image!\n");
  }

  if(!image->imageData) {
    DIE("Could not open or find image!\n");
  }

  if(image->depth != IPL_DEPTH_8U) {
    DIE("Invalid image, expecting IPL_DEPTH_8U!\n");
  }

  if (image->width != image->height) {
    DIE("Invalid image, expecting width = height!\n");
  }

  N = image->width;
  size = image->imageSize;

  if(size != N * N) {
    DIE("Invalid image, expecting size = N * N!\n");
  }

  A = calloc(size, sizeof(double));
  if (!A){
    DIE("Could not allocate memory for A!\n");
  }

  sqA = calloc(size,  sizeof(double));  
  if (!sqA) {
    DIE("Could not allocate memory for A^2!\n");
  }

  LOG("Successfully loaded image!\n");

  int i;
  for(i = 0; i < size; i++) {
    A[i] = (unsigned char)(image->imageData[i]);
  }


  cblas_dgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, N, N, N, 1.0, A, N, A, N, 0.0, sqA, N);
  
  cblas_dgemm(CblasRowMajor, CblasNoTrans, CblasNoTrans, N, N, N, 3, A, N, sqA, N, 5, A, N);

  LOG("Successfully computed the transformations!");
  free(sqA);
  cvReleaseImage(&image);
}

void netcdf(char *netcdf_file)
{
  const int ndim = 1;
  int dimids[ndim];
  int ncid;
  int nvid;

  int res;

  if ((res = nc_create(netcdf_file, NC_CLOBBER, &ncid))) {
    NETCDF_DIE(res);
  }

  LOG("Created netcdf-4 file!\n");

  if ((res = nc_def_dim(ncid, "d", size, &(dimids[0])))) {
    NETCDF_DIE(res);
  }

  if ((res = nc_def_var(ncid, "v", NC_DOUBLE, ndim, dimids, &nvid))) {
    NETCDF_DIE(res);
  }

  if ((res = nc_enddef(ncid))) {
    NETCDF_DIE(res);
  }

  LOG("Writing data to netcdf-4 file!\n");

  if ((res = nc_put_var_double(ncid, nvid, A))) {
    NETCDF_DIE(res);
  }

  if ((res = nc_close(ncid))) {
    NETCDF_DIE(res);
  }
}


int main(int argc, char* argv[])
{
  if(argc != 3) {
    DIE("Usage: ./tema image netcdf_file!\n");
  }

  opencv(argv[1]);
  netcdf(argv[2]);
  free(A);
  return 0;
}
