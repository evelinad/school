#! /bin/bash

 javac -classpath \
$HADOOP_HOME/share/hadoop/common/hadoop-common-2.7.1.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:$1 \
-d class $(pwd)/util/*
 javac -classpath \
$HADOOP_HOME/share/hadoop/common/hadoop-common-2.7.1.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:$1 \
-d class $(pwd)/src/task1/*
 javac -classpath \
$HADOOP_HOME/share/hadoop/common/hadoop-common-2.7.1.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:$1 \
-d class $(pwd)/src/task2/*
 javac -classpath \
$HADOOP_HOME/share/hadoop/common/hadoop-common-2.7.1.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:$1 \
-d class $(pwd)/src/task3/*
 javac -classpath \
$HADOOP_HOME/share/hadoop/common/hadoop-common-2.7.1.jar:$HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:$1 \
-d class $(pwd)/src/task4/*

jar -cvf task.jar -C class/ .

$HADOOP_HOME/bin/hadoop jar task.jar task1.Task1 $2 $3
$HADOOP_HOME/bin/hadoop jar task.jar task2.Task2 $2 $4
$HADOOP_HOME/bin/hadoop jar task.jar task3.Task3 $2 $5
$HADOOP_HOME/bin/hadoop jar task.jar task4.Task4 $2 $6

