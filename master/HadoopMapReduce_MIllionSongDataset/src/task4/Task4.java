/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task4;

import java.io.*;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import util.Genre;
import util.GenreDetector;
import util.InputParser;
import util.SongProperty;

import task4.MusicGenreKey;
import task4.Importance;

public class Task4 {
    public static Set<SongProperty> songProperties = new HashSet<SongProperty>();

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, MusicGenreKey, Importance> {

        public void map(LongWritable key, Text value, OutputCollector<MusicGenreKey, Importance> output, Reporter reporter) throws IOException {
            HashMap<SongProperty, String> songProps =InputParser.getSongProperties(value.toString(), songProperties);
            GenreDetector gd = new GenreDetector();
            String[] terms = songProps.get(SongProperty.ARTIST_TERMS).split(",");
            String[] freqs = songProps.get(SongProperty.ARTIST_TERMS_FREQ).split(",");
            Importance importance = new Importance();
            importance.importance = gd.computeImportance(terms, freqs);
            for (int i = 0; i < Genre.values().length; i++) {
                Genre genre = Genre.values()[i];
                output.collect(new MusicGenreKey(genre), importance);
            }
        }
    }

    static void computeImportanceInPercentage(Importance importance, int genreID) {
        double genreIDImportance = importance.importance.get(genreID);
        for (int i = 0; i < importance.importance.size(); i++) {
            double fraction = 0.0;
            if (importance.importance.get(i) != 0.0 && genreIDImportance != 0.0) {
                if (importance.importance.get(i) < genreIDImportance) {
                    fraction = 100.0 * importance.importance.get(i) / genreIDImportance;
                } else {
                    fraction = 100.0 * genreIDImportance/ importance.importance.get(i);
                }
            }
            importance.importance.set(i, fraction);
        }

    }


    public static class Reduce extends MapReduceBase implements Reducer<MusicGenreKey, Importance, MusicGenreKey, Importance> {
        public void reduce(MusicGenreKey key, Iterator<Importance> values, OutputCollector<MusicGenreKey, Importance> output, Reporter reporter) throws IOException {
            Importance importance = new Importance();
            while (values.hasNext()) {
                Importance value = values.next();
                importance.increase(value);
            }

            computeImportanceInPercentage(importance, key.genre.ordinal());
            output.collect(key, importance);
        }
    }

    static JobConf setUpJobConf() {
        JobConf conf = new JobConf(Task4.class);
        conf.setJobName("genre-crossover");

        conf.setOutputKeyClass(MusicGenreKey.class);
        conf.setOutputValueClass(Importance.class);

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

        songProperties.add(SongProperty.ARTIST_TERMS);
        songProperties.add(SongProperty.ARTIST_TERMS_FREQ);

        runJob(args[0], args[1]);
    }
}

