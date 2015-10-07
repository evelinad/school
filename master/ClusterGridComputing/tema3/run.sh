#!/bin/bash

usage() {
	echo "Usage: ./run.sh [nehalem | opteron | quad]"
exit 1
}

sanitize() {
        if [ $# -lt 1 ];
        then
                echo > /dev/null
                #usage
        fi

        if [ "$1" != "nehalem" ] && [ "$1" != "opteron" ] && [ "$1" != "quad" ];
        then
                echo > /dev/null
                #usage
        fi
}

ARCHITECTURE=$1
IMG="/export/exp_soft/ascg_2011/map_1/3/"
HMWRK="/export/home/fils/stud/e/evelina.dumitrescu/tema3/"
PROG="tema"
METRICS="metrics.sh"
SCRIPTS="$HMWRK/scripts/"
RESULTS="$HMWRK/results/"
OUT_FILE="$HMWRK/out.nc"
OUT="$HMWRK/out"
ERR="$HMWRK/err"


load_modules(){
	module load compilers/gnu-4.6.3
	module load libraries/atlas-3.10.1-gcc-4.4.6-$ARCHITECTURE
	module load libraries/openmpi-1.6-gcc-4.6.3
	module load libraries/netcdf-4.1.3-openmpi-1.6-gcc-4.6.3
	module load libraries/opencv-2.4.1-gcc-4.4.6
}

compile(){
	make -f Makefile_$ARCHITECTURE clean
	PKG_CONFIG_PATH=$PKG_CONFIG_PATH:/opt/tools/libraries/opencv/2.4.1-gcc-4.4.6/lib/pkgconfig PLATFORM=$ARCHITECTURE make -f Makefile_$ARCHITECTURE
	export LD_LIBRARY_PATH=/opt/tools/libraries/openmpi/1.6-gcc-4.4.6/lib:/opt/tools/libraries/netcdf/4.1.3-hdf5-1.8.8-openmpi-1.6-gcc-4.4.6/lib:/opt/tools/libraries/opencv/2.4.1-gcc-4.4.6/lib:/opt/tools/libraries/atlas/3.10.1-$ARCHITECTURE-gcc-4.4.6/lib:$LD_LIBRARY_PATH
}

create_script() {
        script="$SCRIPTS/$ARCHITECTURE-$1.sh"
        touch $script
        chmod +x $script
        echo "#!/bin/bash" >> $script
        echo "cd $HMWRK" >> $script
        echo "$HMWRK/$METRICS $ARCHITECTURE $RESULTS/export-$ARCHITECTURE-$1.csv &" >> $script
        echo "export LD_LIBRARY_PATH=/opt/tools/libraries/openmpi/1.6-gcc-4.4.6/lib:/opt/tools/libraries/netcdf/4.1.3-hdf5-1.8.8-openmpi-1.6-gcc-4.4.6/lib:/opt/tools/libraries/opencv/2.4.1-gcc-4.4.6/lib:/opt/tools/libraries/atlas/3.10.1-$ARCHITECTURE-gcc-4.4.6/lib:\$LD_LIBRARY_PATH" >> $script
}

add_task() {
	script="$SCRIPTS/$ARCHITECTURE-$1.sh"	
	echo "$HMWRK/$PROG $2 $OUT_FILE" >> $script
}


submit_job() {
	script="$SCRIPTS/$ARCHITECTURE-$1.sh"
	qsub -V -e "localhost:$ERR/$ARCHITECTURE-$1.e" -o "localhost:$OUT/$ARCHITECTURE-$1.o" -q ibm-$ARCHITECTURE.q -pe openmpi*1 4 -cwd $script
}

finish_jobs() {
	sleep $((20 * 60))
	qdel -f -u `whoami`
}


sanitize
load_modules
compile


rm -rf $OUT 
rm -rf $ERR
rm -rf $SCRIPTS
mkdir $OUT $ERR $SCRIPTS

for i in `seq 0 2`
do
	create_script $i
done

cnt=0
for i in `find $IMG`
do
	if [ -f "$i" ]; then
		cnt=$((cnt + 1))
		mod=$((cnt%3))
		add_task $cnt $i
	fi
done

for i in `seq 0 2`
do
	submit_job $i
done

finish_jobs

exit 0
