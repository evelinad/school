#! /bin/bash

memcached -d -m 512 -l $2 -p 11211 -u root &
java -classpath /home/evelinad/zookeeper/zookeeper-3.4.9.jar:/home/evelinad/zookeeper/lib/log4j-1.2.16.jar:/home/evelinad/zookeeper/lib/netty-3.10.5.Final.jar:/home/evelinad/zookeeper/lib/slf4j-api-1.6.1.jar:/home/evelinad/zookeeper/lib/slf4j-log4j12-1.6.1.jar:/home/evelinad/Downloads/jna-libmemcached-master/dist/libmemcached-0.04.jar:/home/evelinad/Downloads/jna-libmemcached-master/lib/jna.jar:bin/ MemcachedWatchdog $1 $2 &

