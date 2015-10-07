#!/bin/bash

function install_mpich {
  SOURCE_DIR="/root/src/mpich-3.1.4"
  INSTALL_DIR="/opt/lib/mpi/mpich-3.1.4-gcc"

  if [ ! -d $INSTALL_DIR ]
  then
        echo "Installation directory does not exists. Creating $INSTALL_DIR ..."
        mkdir -p $INSTALL_DIR
  fi

  echo "Installing mpich ..."

  export CC=gcc
  export CFLAGS=-I/opt/lib/mpi/mpich-3.1.4-gcc/include
  export LDFLAGS=-L/opt/lib/mpi/mpich-3.1.4-gcc/lib

  cd $SOURCE_DIR
  ./configure --prefix=$INSTALL_DIR --disable-fortran

  make -j5
  make -j5 install
  make -j5 distclean > /dev/null 2>&1

  find $INSTALL_DIR/* > mpich_installed
  echo "Finished !"
}

function install_openmpi {
  SOURCE_DIR="/root/src/openmpi-1.8.4"
  INSTALL_DIR="/opt/lib/mpi/openmpi-1.8.4-gcc"

  if [ ! -d $INSTALL_DIR ]
  then
        echo "Installation directory does not exists. Creating $INSTALL_DIR ..."
        mkdir -p $INSTALL_DIR
  fi

  echo "Installing openmpi ..."

  echo $SOURCE_DIR

  export CC=gcc
  export CFLAGS=-I/opt/lib/mpi/openmpi-1.8.4-gcc/include
  export LDFLAGS=-L/opt/lib/mpi/openmpi-1.8.4-gcc/lib

  cd $SOURCE_DIR
  ./configure --prefix=$INSTALL_DIR

  make -j5
  make -j5 install
  make -j5 distclean > /dev/null 2>&1

  find $INSTALL_DIR/* > openmpi_installed
  echo "Finished !"
}

function install_torque {
  SOURCE_DIR="/root/src/torque-5.1.0-1_4048f77c"
  INSTALL_DIR="/opt/batch/torque-5.1.0"

  if [ ! -d $INSTALL_DIR ]
  then
        echo "Installation directory does not exists. Creating $INSTALL_DIR ..."
        mkdir -p $INSTALL_DIR
  fi

  echo "Installing torque ..."

  export CC=gcc
  export CFLAGS=-I/opt/batch/torque-5.1/include
  export LDFLAGS=-L/opt/batch/torque-5.1/lib

  cd $SOURCE_DIR
  ./configure --prefix=$INSTALL_DIR

  make -j5
  #make -j5 install
  #make -j5 distclean > /dev/null 2>&1

  find $INSTALL_DIR/* > torque_installed
  find /var/spool/torque/* >> torque_installed
  echo "Finished !"
}

function install_hdf5_openmpi {

  SOURCE_DIR="/root/src/hdf5-1.8.14"
  INSTALL_DIR="/opt/lib/hdf5/hdf5-1.8.14-openmpi"

  if [ ! -d $INSTALL_DIR ]
  then
	echo "Installation directory does not exists. Creating $INSTALL_DIR ..."
	mkdir -p $INSTALL_DIR
  fi

  echo "Installing hdf5 with openmpi ..."
  cd $SOURCE_DIR

  export CC=/opt/lib/mpi/openmpi-1.8.4-gcc/bin/mpicc
  export LDFLAGS=-L/opt/lib/mpi/openmpi-1.8.4-gcc/lib
  #export CPPFLAGS=-I/opt/lib/mpi/openmpi-1.8.4-gcc/include
  export CFLAGS=-I/opt/lib/mpi/openmpi-1.8.4-gcc/include -D_LARGEFILE_SOURCE -D_LARGEFILE64_SOURCE -D_FILE_OFFSET_BITS=64
  #export PATH=/opt/lib/mpi/openmpi-1.8.4-gcc/bin:$PATH
  #export LD_LIBRARY_PATH=/opt/lib/mpi/openmpi-1.8.4-gcc/lib:$LD_LIBRARY_PATH

  ./configure --prefix=$INSTALL_DIR --enable-parallel --enable-shared

  make -j5
  make -j5 install
  make -j5 distclean > /dev/null 2>&1

  find $INSTALL_DIR/* > hdf5_openmpi_installed
  echo "Finished !"
}


function install_hdf5_mpich {
  SOURCE_DIR="/root/src/hdf5-1.8.14"
  INSTALL_DIR="/opt/lib/hdf5/hdf5-1.8.14-mpich"

  if [ ! -d $INSTALL_DIR ]
  then
	echo "Installation directory does not exists. Creating $INSTALL_DIR ..."
	mkdir -p $INSTALL_DIR
  fi

  echo "Installing hdf5 with mpich ..."
  cd $SOURCE_DIR

  export CC=/opt/lib/mpi/mpich-3.1.4-gcc/bin/mpicc
  export LDFLAGS=-L/opt/lib/mpi/mpich-3.1.4-gcc/lib
  export CFLAGS=-I/opt/lib/mpi/mpich-3.1.4-gcc/include -D_LARGEFILE_SOURCE -D_LARGEFILE64_SOURCE -D_FILE_OFFSET_BITS=64
  #export CPPFLAGS=-I/opt/lib/mpi/mpich-3.1.4-gcc/include
  #export PATH=/opt/lib/mpi/mpich-3.1.4-gcc/bin:$PATH
  #export LD_LIBRARY_PATH=/opt/lib/mpi/mpich-3.1.4-gcc/lib:$LD_LIBRARY_PATH

  ./configure --prefix=$INSTALL_DIR --enable-parallel --enable-shared

  make -j5
  make -j5 install
  make -j5 distclean > /dev/null 2>&1

  find $INSTALL_DIR/* > hdf5_mpich_installed
  echo "Finished !"
}

function install_netcdf_mpich {
  SOURCE_DIR="/root/src/netcdf-4.3.3.1"
  INSTALL_DIR="/opt/lib/netcdf/netcdf-4.3.3.1-mpich"

  if [ ! -d $INSTALL_DIR ]
  then
        echo "Installation directory does not exists. Creating $INSTALL_DIR ..."
        mkdir -p $INSTALL_DIR
  fi


  export CC=/opt/lib/mpi/mpich-3.1.4-gcc/bin/mpicc
  export CFLAGS="-I/opt/lib/mpi/mpich-3.1.4-gcc/include -I/opt/lib/hdf5/hdf5-1.8.14-mpich/include"
  export LDFLAGS="-L/opt/lib/mpi/mpich-3.1.4-gcc/lib -L/opt/lib/hdf5/hdf5-1.8.14-mpich/lib"
  #export CPPFLAGS="-I/opt/lib/mpi/mpich-3.1.4-gcc/include -I/opt/lib/hdf5/hdf5-1.8.14-mpich/include"
  #export PATH=/opt/lib/mpi/mpich-3.1.4-gcc/bin:$PATH
  export LD_LIBRARY_PATH=/opt/lib/hdf5/hdf5-1.8.14-mpich/lib:/opt/lib/mpi/mpich-3.1.4-gcc/lib:$LD_LIBRARY_PATH

  echo "Installing netcdf with mpich ..."
  cd $SOURCE_DIR
  ./configure --prefix=$INSTALL_DIR --enable-shared --enable-parallel --enable-parallel-tests 
  make -j5
  make  check

  if [ $? -ne 0 ]
  then
        echo "make check failed"
  fi

  #make -j5 install
  #make -j5 distclean > /dev/null 2>&1

  find $INSTALL_DIR/* > netcdf_mpich_installed
  #echo "Finished !"
}

function install_netcdf_openmpi {
  SOURCE_DIR="/root/src/netcdf-4.3.3.1"
  INSTALL_DIR="/opt/lib/netcdf/netcdf-4.3.3.1-openmpi"

  if [ ! -d $INSTALL_DIR ]
  then
        echo "Installation directory does not exists. Creating $INSTALL_DIR ..."
        mkdir -p $INSTALL_DIR
  fi


  echo "Installing netcdf with openmpi ..."
  cd $SOURCE_DIR

  export CC=/opt/lib/mpi/openmpi-1.8.4-gcc/bin/mpicc
  export CFLAGS="-I/opt/lib/mpi/openmpi-1.8.4-gcc/include -I/opt/lib/hdf5/hdf5-1.8.14-openmpi/include"
  #export CFLAGS="-I/opt/lib/hdf5/hdf5-1.8.14-openmpi/include"
  #export CFLAGS="-I/opt/lib/hdf5/hdf5-1.8.14-openmpi/include"

  #export CFLAGS=""
  #export LDFLAGS="-L/opt/lib/hdf5/hdf5-1.8.14-openmpi/lib"

  export LDFLAGS="-L/opt/lib/mpi/openmpi-1.8.4-gcc/lib -L/opt/lib/hdf5/hdf5-1.8.14-openmpi/lib"
  #export CPPFLAGS="-I/opt/lib/mpi/openmpi-1.8.4-gcc/include -I/opt/lib/hdf5/hdf5-1.8.14-openmpi/include"
  #export PATH=/opt/lib/mpi/openmpi-1.8.4-gcc/bin:$PATH
  export LD_LIBRARY_PATH=/opt/lib/hdf5/hdf5-1.8.14-openmpi/lib:/opt/lib/mpi/openmpi-1.8.4-gcc/lib:$LD_LIBRARY_PATH

  #./configure --prefix=$INSTALL_DIR  --enable-shared --enable-parallel --enable-parallel-tests 
  #make -j5
  make check

  if [ $? -ne 0 ]
  then
        echo "make check failed !"
  fi

  #make -j5 install
  #make -j5 distclean > /dev/null 2>&1

  find $INSTALL_DIR/* > netcdf_openmpi_installed
  #echo "Finished !"
}

#install_mpich
#install_openmpi
install_torque
#install_hdf5_openmpi
#install_hdf5_mpich
#install_netcdf_openmpi
#install_netcdf_mpich

exit 0


