#! /bin/bash

mkdir /root/kituri
mkdir /root/src

cd /root/kituri
#zypper install -y gcc gcc-c++ make patch libopenssl-devel libxml2-devel boost-devel zlib autoconf

#curl -LOk http://www.mpich.org/static/downloads/3.1.3/mpich-3.1.4.tar.gz
#curl -LOk http://www.mosix.cs.huji.ac.il/mos4/MOSIX-4.2.1.tbz
#curl -LOk http://www.adaptivecomputing.com/download/torque/torque-5.1.0-1_4048f77c.tar.gz
#curl -LOk http://www.open-mpi.org/software/ompi/v1.8/downloads/openmpi-1.8.4.tar.gz
#curl -LOk ftp://ftp.unidata.ucar.edu/pub/netcdf/netcdf-4.3.3.1.tar.gz
#curl -LOk http://www.hdfgroup.org/ftp/HDF5/current/src/hdf5-1.8.14.tar

cp /home/student/mpich-3.1.4.tar.gz /root/kituri
cp /home/student/netcdf-4.3.3.1_1.tar.gz /root/kituri
cp /home/student/torque-5.1.0-1_4048f77c.tar.gz /root/kituri

tar -xvf mpich-3.1.4.tar.gz
mkdir /root/src/mpich-3.1.4
mv /root/kituri/mpich-3.1.4/* /root/src/mpich-3.1.4
tar -xvf MOSIX-4.2.1.tbz
mkdir /root/src/MOSIX-4.2.1
mv /root/kituri/MOSIX-4.2.1/* /root/src/MOSIX-4.2.1
tar -xvf torque-5.1.0-1_4048f77c.tar.gz
mkdir /root/src/torque-5.1.0-1_4048f77c
mv /root/kituri/torque-5.1.0-1_4048f77c/* /root/src/torque-5.1.0-1_4048f77c
tar -xvf openmpi-1.8.4.tar.gz
mkdir /root/src/openmpi-1.8.4.tar
mv /root/kituri/openmpi-1.8.4/* /root/src/openmpi-1.8.4
tar -xvf netcdf-4.3.3.1_1.tar.gz
mkdir /root/src/netcdf-4.3.3.1
mv /root/kituri/netcdf-4.3.3.1/* /root/src/netcdf-4.3.3.1
tar -xvf hdf5-1.8.14.tar
mkdir /root/src/hdf5-1.8.14
mv /root/kituri/hdf5-1.8.14/* /root/src/hdf5-1.8.14

