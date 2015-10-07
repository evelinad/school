#!/bin/bash

HMWRK="/home/ccg/tema2"
IMG="/home/ccg/tema2/poze"
EXEC="./tema"
NETCDF_FILE="/home/ccg/tema2/out.nc"
RUNE_TYPE=$1
METRICS_FILE="metrics_$1.csv"
rm -f $METRICS_FILE
touch $METRICS_FILE
echo "HOSTNAME,TIME,NET,MEM,CPU" > $METRICS_FILE

cd $HMWRK


usage() {
        echo "Usage: ./run.sh [torque | mosix]"
	exit 1
}


sanitize() {
        if [ $# -lt 1 ];
        then
                echo > /dev/null
                #usage
        fi

        if [ "$1" != "torque" ] && [ "$1" != "mosix" ];
        then
                echo > /dev/null
                #usage
        fi
}


metrics(){
	ssh mosix1 $HMWRK/metrics.sh $RUN_TYPE
	ssh mosix2 $HMWRK/metrics.sh $RUN_TYPE
	ssh mosix3 $HMWRK/metrics.sh $RUN_TYPE
}


poll_metrics() {
	seconds=$((20*2))
	for i in `seq $seconds`
	do
        	sleep 30
	        metrics
	done

	metrics
}


create_script() {
	script=$1.sh
        touch $script
        chmod +x $script

        echo "#!/bin/bash" > $script
        echo "cd $HMWRK" >> $script
        echo "make clean && make" >> $script
        echo "$HMWRK/$EXEC $2 $NETCDF_FILE" >> $script
}


torque() {
	qdel all &>/dev/null
	metrics

	TORQUE_OUT="$HMWRK/torque_out"
	TORQUE_ERR="$HMWRK/torque_err"
	TORQUE_SCRIPTS="$HMWRK/torque_scripts"

	rm -rf $TORQUE_SCRIPTS
	mkdir -p $TORQUE_SCRIPTS

	rm -rf $TORQUE_OUT
	mkdir -p $TORQUE_OUT

	rm -rf $TORQUE_ERR
	mkdir -p $TORQUE_ERR

	cnt=0

	for i in `find $IMG`
	do
		if [ -f "$i" ]; then
        		cnt=$((cnt + 1))
			create_script $TORQUE_SCRIPTS/$cnt.sh $i
                	qsub -V -N $cnt  -e "localhost:$TORQUE_ERR/$cnt.e" -o "localhost:$TORQUE_OUT/$cnt.o" -q linux-spool -M root@mosix1 $script
        	fi
	done

        poll_metrics

	qdel all &>/dev/null
}


mosix() {
	MOSIX_JOBS="$HMWRK/mosix_jobs"
	MOSIX_LOGS="$HMWRK/mosix.out"

	rm -rf $MOSIX_JOBS
	touch $MOSIX_JOBS
	chmod +x $MOSIX_JOBS

	rm -rf $MOSIX_LOGS
	touch $MOSIX_LOGS

	for i in `find $IMG`
	do
		if [ -f "$i" ]; then
                	cmd="cd $HMWRK && make clean && make && $EXEC $i $NETCDF_FILE >> $MOSIX_LOGS"
	                echo $cmd >> $MOSIX_JOBS
		fi
	done

	metrics

	mosrun -e -b -S3 $MOSIX_JOBS                          
	poll_metrics
}


sanitize
export LD_LIBRARY_PATH=/opt/lib/netcdf-4.3.3.1-mpich/lib:/opt/lib/hdf5/hdf5-1.8.14-mpich/lib:$LD_LIBRARY_PATH

case "$1" in
	mosix)
			mosix
			;;
	torque)
			torque
			;;
	*)
			usage
			;;
esac

exit 0
