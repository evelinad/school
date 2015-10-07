#!/bin/bash

cpu_metrics() {
	echo `uptime | tr -s " " | cut -f 11 -d " " | tr -d ","`
}

mem_metrics() {
	echo `free | grep Mem  | tr -s " " | cut -f 3 -d " "`
}

disk_metrics() {
	echo `df | grep /dev/vda1 | tr -s " " | cut -d " " -f3`
}

current_time() {
	echo `date +%s`
}

METRICS_FILE="metrics_$1.csv"
TIMESTAMP=$(current_time)
CPU_VALUE=$(cpu_metrics)
MEM_VALUE=$(mem_metrics)
DISK_VALUE=$(disk_metrics)
HOSTNAME=`hostname`
METRIC_BASE="tema2.evelinad.$HOSTNAME"
CPU_METRIC="$METRIC_BASE.$1.cpu"
MEM_METRIC="$METRIC_BASE.$1.mem"
DISK_METRIC="$METRIC_BASE.$1.disk"

DASHBOARD_IP=10.9.3.212
DASHBOARD_PORT=2003


echo "$HOSTNAME,$TIMESTAMP,$DISK_VALUE,$MEM_VALUE,$CPU_VALUE"

echo "$HOSTNAME,$TIMESTAMP,$DISK_VALUE,$MEM_VALUE,$CPU_VALUE" >> $METRICS_FILE
echo "$DISK_METRIC $DISK_VALUE $TIMESTAMP" | nc $DASHBOARD_IP $DASHBOARD_PORT;
echo "$MEM_METRIC $MEM_VALUE $TIMESTAMP" | nc $DASHBOARD_IP $DASHBOARD_PORT;
echo "$CPU_METRIC $CPU_VALUE $TIMESTAMP" | nc $DASHBOARD_IP $DASHBOARD_PORT;

exit 0
