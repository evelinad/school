build: mpirun libsompi
unix_queue.o:unix_queue.c
	gcc -Wall -g -c unix_queue.c -o unix_queue.o
tema2.o:tema2.c
	gcc -Wall -g -c tema2.c -o tema2.o
mpirun:tema2.o unix_queue.o
	gcc -Wall -g tema2.o unix_queue.o  -o mpirun -lrt
libsompi:libsompi.o unix_queue.o 
	gcc -Wall -g -shared libsompi.o unix_queue.o  -o libsompi.so -lrt
libsompi.o:sompi.c
	gcc -Wall -g -fPIC -c sompi.c -o libsompi.o
clean:
	rm -f *.o *.so mpirun
