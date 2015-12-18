/*
Dumitrescu Evelina
SCPD
Assignment 3
*/

package task3;

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

import task3.MusicalNotes;
import task3.SongResult;
import task3.MusicGenreKey;

public class Task3 {
    public static Set<SongProperty> songProperties = new HashSet<SongProperty>();

    public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, MusicGenreKey, MusicalNotes> {
        MusicGenreKey detectGenre(String[] terms, String[] freqs) {
            float[] importance = new float[freqs.length];
            GenreDetector gd = new GenreDetector();

            for (int i = 0; i < freqs.length; i++) {
                if (freqs[i].isEmpty()) {
                    importance[i] = 0;
                } else {
                    importance[i] = Float.parseFloat(freqs[i]);
                }
            }

            return new MusicGenreKey(gd.detectGenre(terms, importance));
        }

        public void map(LongWritable key, Text value, OutputCollector<MusicGenreKey, MusicalNotes> output, Reporter reporter) throws IOException {
            HashMap<SongProperty, String> songProps =InputParser.getSongProperties(value.toString(), songProperties);
            MusicalNotes mn = new MusicalNotes();
            String[] terms = songProps.get(SongProperty.ARTIST_TERMS).split(",");
            String[] freqs = songProps.get(SongProperty.ARTIST_TERMS_FREQ).split(",");

            mn.loudness = Float.parseFloat(songProps.get(SongProperty.LOUDNESS));
            mn.tempo = Float.parseFloat(songProps.get(SongProperty.TEMPO));
            mn.count = 1;

            output.collect(detectGenre(terms, freqs), mn);
        }
    }

    public static class Reduce extends MapReduceBase implements Reducer<MusicGenreKey, MusicalNotes, MusicGenreKey, MusicalNotes> {
        public void reduce(MusicGenreKey key, Iterator<MusicalNotes> values, OutputCollector<MusicGenreKey, MusicalNotes> output, Reporter reporter) throws IOException {
            MusicalNotes mn = new MusicalNotes();

            while (values.hasNext()) {
                MusicalNotes value = values.next();
                mn.loudness += value.loudness;
                mn.tempo += value.tempo;
                mn.count++;
            }

            if (mn.count != 0) {
                mn.loudness /= mn.count;
                mn.tempo /= mn.count;
            }

            output.collect(key, mn);
        }
    }

    static ArrayList<SongResult> parseSongResults(String outputFile) {
        File output = new File(outputFile + "/part-00000");
        ArrayList<SongResult> queue = new ArrayList<SongResult>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(output));
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("[ \t|]");
                queue.add(new SongResult(words[0], Float.parseFloat(words[2]), Float.parseFloat(words[3]), (int)Float.parseFloat(words[4])));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return queue;
    }

    static void sortSongResult(String input) {
        ArrayList<SongResult> results = parseSongResults(input);

        Comparator<SongResult> countComparator = new Comparator<SongResult>() {
            public int compare(SongResult result1, SongResult result2) {
                if (result1.count > result2.count) {
                    return 1;
                } else if (result1.count < result2.count) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        Collections.sort(results, countComparator);
        for (SongResult result : results) {
            System.out.println("Genre " + result.genre + " number of songs " + result.count);
        }
        System.out.println();

        Comparator<SongResult> loudnessComparator = new Comparator<SongResult>() {
            public int compare(SongResult result1, SongResult result2) {
                if (result1.loudness > result2.loudness) {
                    return 1;
                } else if (result1.loudness < result2.loudness) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        Collections.sort(results, loudnessComparator);
        for (SongResult result : results) {
            System.out.println("Genre " + result.genre + " loudness " + result.loudness);
        }
        System.out.println();


        Comparator<SongResult> tempoComparator = new Comparator<SongResult>() {
            public int compare(SongResult result1, SongResult result2) {
                if (result1.tempo > result2.tempo) {
                    return 1;
                } else if (result1.tempo < result2.tempo) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };

        Collections.sort(results, tempoComparator);
        for (SongResult result : results) {
            System.out.println("Genre " + result.genre + " tempo " + result.tempo);
        }

    }

    static JobConf setUpJobConf() {
        JobConf conf = new JobConf(Task3.class);
        conf.setJobName("sort-nrsongs-avgloudness-avgtempo");

        conf.setOutputKeyClass(MusicGenreKey.class);
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
        songProperties.add(SongProperty.ARTIST_TERMS);
        songProperties.add(SongProperty.ARTIST_TERMS_FREQ);

        runJob(args[0], args[1]);

        sortSongResult(args[1]);
    }
}

