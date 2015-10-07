build:
	gcc -Wall -g tema.c -o tema -lgsl -lgslcblas -lz -ldl `pkg-config --cflags --libs opencv` \
	-L/opt/lib/netcdf/netcdf-4.3.3.1-mpich/lib -lnetcdf \
        -I/opt/lib/netcdf/netcdf-4.3.3.1-mpich/include \
        -Wl,-rpath=/opt/lib/netcdf/netcdf-4.3.3.1-mpich/lib

clean:
	rm -rf tema
