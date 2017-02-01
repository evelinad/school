#! /bin/bash
mkdir class/
rm -rf class/*
javac -classpath $SPARK_HOME/jars/scala-library-2.11.8.jar:$SPARK_HOME/jars/spark-core_2.11-2.0.2.jar:$SPARK_HOME/jars/hadoop-common-2.7.3.jar -d /home/evelinad/tema3/class  JavaWordCount.java
jar -cvf class/wordcount.jar -C /home/evelinad/tema3/class .
cd class
$SPARK_HOME/bin/spark-submit  --class JavaWordCount  --master  spark://HP:7077 wordcount.jar  $1
