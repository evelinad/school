#!/bin/bash

TESTS_DIR="/home/ccg/"
NETCDF_DIR="$TESTS_DIR/netcdf"
MOSIX_DIR="$TESTS_DIR/mosix"

CWD=`pwd`

USER=`whoami`

check_user(){
	if [ $USER == "root" ]
	then
		echo "Please run as a user different from root in order to use qsub!"
		exit 1
	fi
}

usage(){
	echo "Usage is ./run.sh [mpich | openmpi | qsub_mpich | qsub_openmpi]"
	exit 1
}

check_user

if [ $# -ne 1 ]
then
	usage
fi

clean_compile_run(){
	make -f $1 clean
	make -f $1 build
	make -f $1 run
}


case "$1" in
	mpich)
			cd $NETCDF_DIR
			clean_compile_run Makefile_mpich
			;;
	openmpi)
			cd $NETCDF_DIR
			clean_compile_run Makefile_openmpi
			;;
	qsub_mpich)
			cd $NETCDF_DIR
			clean_compile_run Makefile_qsubmpich
			;;
	qsub_openmpi)
			cd $NETCDF_DIR
			clean_compile_run Makefile_qsubopenmpi
			;;
	mosix)
			cd $MOSIX_DIR
                        clean_compile_run Makefile_mosix
			;;
	*)
			usage
			;;
esac

exit 0
