/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task2;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import util.InputParser;
import util.SongProperty;

import task2.MusicalNotes;

public class Task2 {
    public static Set<SongProperty> songProperties = new HashSet<SongProperty>();

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, LongWritable, MusicalNotes> {

        public void map(LongWritable key, Text value, OutputCollector<LongWritable, MusicalNotes> output, Reporter reporter) throws IOException {
            HashMap<SongProperty, String> songProps =InputParser.getSongProperties(value.toString(), songProperties);
            MusicalNotes mn = new MusicalNotes();
            int year = Integer.parseInt(songProps.get(SongProperty.YEAR));

            int mode = Integer.parseInt(songProps.get(SongProperty.MODE));
            if (mode == 0) {
                mn.minorLoudness = Float.parseFloat(songProps.get(SongProperty.LOUDNESS));
                mn.minorTempo = Float.parseFloat(songProps.get(SongProperty.TEMPO));
                mn.minorCount = 1;
            } else {
                mn.majorLoudness = Float.parseFloat(songProps.get(SongProperty.LOUDNESS));
                mn.majorTempo = Float.parseFloat(songProps.get(SongProperty.TEMPO));
                mn.majorCount = 1;
            }

            output.collect(new LongWritable(year), mn);
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<LongWritable, MusicalNotes, LongWritable, MusicalNotes> {
        public void reduce(LongWritable key, Iterator<MusicalNotes> values, OutputCollector<LongWritable, MusicalNotes> output, Reporter reporter) throws IOException {
            MusicalNotes mn = new MusicalNotes();
            while (values.hasNext()) {
                MusicalNotes value = values.next();
                mn.minorLoudness += value.minorLoudness;
                mn.minorTempo += value.minorTempo;
                mn.minorCount += value.minorCount;
                mn.majorLoudness += value.majorLoudness;
                mn.majorTempo += value.majorTempo;
                mn.majorCount += value.majorCount;
            }

            if (mn.majorCount != 0) {
                mn.majorLoudness /= (float)mn.majorCount;
                mn.majorTempo /= (float)mn.majorCount;
            }

            if (mn.minorCount != 0) {
                mn.minorLoudness /= (float)mn.minorCount;
                mn.minorTempo /=(float) mn.minorCount;
            }

            output.collect(key, mn);
        }
    }


    static JobConf setUpJobConf() {
        JobConf conf = new JobConf(Task2.class);
        conf.setJobName("mode-loudness-tempo");

        conf.setOutputKeyClass(LongWritable.class);
        conf.setOutputValueClass(MusicalNotes.class);

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
        songProperties.add(SongProperty.TEMPO);
        songProperties.add(SongProperty.YEAR);
        songProperties.add(SongProperty.MODE);

        runJob(args[0], args[1]);

    }
}

