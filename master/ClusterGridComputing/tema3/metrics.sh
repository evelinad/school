#!/bin/bash

usage(){
	echo "Usage: ./metrics.sh [nehalem|opteron|quad] metrics_file"
	exit 1
}

sanitize(){
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

network_metrics(){
	local result=`cat /proc/net/dev | grep eth | head -n 1 | tr -s ' ' | cut -d ':' -f2 | cut -d ' ' -f1 | head -n 1`
	echo "$result"
}

cpu_metrics(){
	local result=`uptime | tr -s " " | cut -f 11 -d " " | tr -d ","`
        echo "$result"
}

mem_metrics(){
	local result=`free | grep Mem  | tr -s " " | cut -f 3 -d " "`
        echo "$result"
}

current_time(){
	local result=`date +%s`
        echo "$result"
}

sanitize

ARCHITECTURE=$1
METRICS_FILE="metrics_$ARCHITECTURE.csv"
DASHBOARD_IP=10.9.3.212
DASHBOARD_PORT=2003

minutes=20
intervals=$(($minutes*2))

rm -f $METRICS_FILE
touch $METRICS_FILE
echo "HOSTNAME,TIME,NET,MEM,CPU" > $METRICS_FILE

HOSTNAME=`hostname`
METRIC_BASE="tema3.evelinad.$ARCHITECTURE"
CPU_METRIC="$METRIC_BASE.cpu"
MEM_METRIC="$METRIC_BASE.mem"
NET_METRIC="$METRIC_BASE.net"
NETOLD_VALUE=$(network_metrics)

sleep 2

metrics(){
	TIMESTAMP=$(current_time)
        CPU_VALUE=$(cpu_metrics)
        MEM_VALUE=$(mem_metrics)
        NETNEW_VALUE=$(network_metrics)
        NET_VALUE=$(($NETNEW_VALUE-$NETOLD_VALUE))
        NETOLD_VALUE=$NETNEW_VALUE

        echo "$HOSTNAME,$TIMESTAMP,$NET_VALUE,$MEM_VALUE,$CPU_VALUE" >> $METRICS_FILE
        echo "$NET_METRIC $NET_VALUE $TIMESTAMP" | nc $DASHBOARD_IP $DASHBOARD_PORT
        echo "$MEM_METRIC $MEM_VALUE $TIMESTAMP" | nc $DASHBOARD_IP $DASHBOARD_PORT
        echo "$CPU_METRIC $CPU_VALUE $TIMESTAMP" | nc $DASHBOARD_IP $DASHBOARD_PORT
}

for i in `seq $intervals`
do
	metrics
	sleep 30

done

exit 0
