#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "mpi.h"
#include "hdf5.h"
#include "netcdf.h"
#include "netcdf_par.h"

#define BAIL(e) do { \
     printf("Bailing out in file %s, line %d, error:%s.\n", __FILE__, __LINE__, nc_strerror(e)); \
     return e; \
} while (0)

#define FILENAME "tst_parallel3.nc"
#define FILENAME_BEGIN "tst_parallel3"
#define FILENAME_EXT ".nc"

#define NDIMS1 2
#define DIMSIZE 768*2
#define DIMSIZE2 4

char filename[100];


void renamefile(int argc, char *argv)
{
        if (argc != 0){
                snprintf(filename, 100, "%s%s%s", FILENAME_BEGIN, argv, FILENAME_EXT);
        }else{
                snprintf(filename, 100, "%s", FILENAME);
        }

}


int main(int argc, char **argv) {
        renamefile((argc == 2)?1:0, argv[1]);

	/* MPI stuff. */
    	int mpi_size, mpi_rank;
    	int res = NC_NOERR;

    	MPI_Comm comm = MPI_COMM_WORLD;
    	MPI_Info info = MPI_INFO_NULL;

	/* Netcdf-4 stuff. */
	int facc_type;
 	int ncid;
    	int nvid;

   	/* two dimensional integer data test */
    	int dimids[NDIMS1];

	/* Initialize MPI. */
    	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &mpi_size);
        MPI_Comm_rank(MPI_COMM_WORLD, &mpi_rank);

	/* Create a parallel netcdf-4 file. */
	facc_type = NC_NETCDF4|NC_MPIIO;

	if ((res = nc_create_par(filename, facc_type, comm, info, &ncid)))
	BAIL(res);

	/* The first case is two dimensional variables, no unlimited
	dimension */

	/* Create two dimensions. */
	if ((res = nc_def_dim(ncid, "d1", DIMSIZE2, dimids)))
	BAIL(res);
	if ((res = nc_def_dim(ncid, "d2", DIMSIZE, &dimids[1])))
	BAIL(res);

	/* Create one var. */
	if ((res = nc_def_var(ncid, "v1", NC_INT, NDIMS1, dimids, &nvid)))
	BAIL(res);

	if ((res = nc_enddef(ncid)))
	BAIL(res);

	/* Close the netcdf file. */
    	if ((res = nc_close(ncid)))
	BAIL(res);

	printf("MPI_Finalize!\n");	
	MPI_Finalize();
	
	printf("Executed run!\n");

	return 0;
}
