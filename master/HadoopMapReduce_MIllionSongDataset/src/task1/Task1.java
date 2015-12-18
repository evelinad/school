/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task1;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import util.InputParser;
import util.SongProperty;

public class Task1 {
    public static Set<SongProperty> songProperties = new HashSet<SongProperty>();

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, FloatWritable> {
        private Text word = new Text();

        public void map(LongWritable key, Text value, OutputCollector<LongWritable, FloatWritable> output, Reporter reporter) throws IOException {
            HashMap<SongProperty, String> songProps =InputParser.getSongProperties(value.toString(), songProperties);

            output.collect(new LongWritable(Integer.parseInt(songProps.get(SongProperty.YEAR))), new FloatWritable(Float.parseFloat(songProps.get(SongProperty.LOUDNESS))));
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<LongWritable, FloatWritable, LongWritable, FloatWritable> {
        public void reduce(LongWritable key, Iterator<FloatWritable> values, OutputCollector<LongWritable, FloatWritable> output, Reporter reporter) throws IOException {
            float loudnessAverage = 0;
            int count = 0;

            while (values.hasNext()) {
                loudnessAverage += values.next().get();
                count++;
            }

            output.collect(key, new FloatWritable(loudnessAverage/(float)count));
        }
    }

    static JobConf setUpJobConf() {
        JobConf conf = new JobConf(Task1.class);
        conf.setJobName("loudness");

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(FloatWritable.class);

        conf.setMapperClass(Map.class);
        conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

	return conf;
    }

    static void runJob(String input, String output) {
	JobConf conf = setUpJobConf();
	for (File file : new File(input).listFiles())
            FileInputFormat.addInputPath(conf, new Path(file.getAbsolutePath()));

        FileOutputFormat.setOutputPath(conf, new Path(output));

        try {
            JobClient.runJob(conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Provide the path to the MSD and the output directory.");
            System.exit(1);
        }

        songProperties.add(SongProperty.LOUDNESS);
        songProperties.add(SongProperty.YEAR);

	runJob(args[0], args[1]);
    }
}

