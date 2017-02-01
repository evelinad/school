// Dumitrescu Evelina Homework 3
import scala.Tuple2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class JavaWordCount {
	private static final Pattern SPACE = Pattern.compile("[^a-zA-Z0-9']");

	public static void main(String[] args) throws Exception {

		if (args.length < 1) {
			System.err.println("Usage: JavaWordCount <file or directory>");
			System.exit(1);
		}

		SparkConf sparkConf = new SparkConf().setAppName("JavaWordCount");
		JavaSparkContext ctx = new JavaSparkContext(sparkConf);
		
		ctx.hadoopConfiguration().set("mapreduce.input.fileinputformat.input.dir.recursive","true");
		
		JavaRDD<String> lines = ctx.textFile(args[0], 1);

		JavaRDD<Tuple2<String, String>> words = lines.flatMap(new FlatMapFunction<String, Tuple2<String, String>>() {
			@Override
			public Iterator<Tuple2<String, String>> call(String s) {
				if (s.length() < 3) {
					return new ArrayList<Tuple2<String, String>>().iterator();
				}
				String document = s.substring(1, s.indexOf('}'));
				String[] words = SPACE.split(s.substring(s.indexOf('}') + 2));

				ArrayList<Tuple2<String, String>> words_document = new ArrayList<Tuple2<String, String>>();
				for(String word : words) {
					if (!word.equals("")) {
						words_document.add(new Tuple2<>(word.toLowerCase(), document.toLowerCase()));	
					}
				}
				return words_document.iterator();
			}
		});
		JavaPairRDD<Tuple2<String, String>, Integer> ones = words.mapToPair(new PairFunction<Tuple2<String, String>, Tuple2<String, String>, Integer>() {
			@Override
			public Tuple2<Tuple2<String, String>, Integer> call(Tuple2<String, String> s) {
				return new Tuple2<>(s, 1);
			}
		});
	
		JavaPairRDD<Tuple2<String, String>, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
			@Override
			public Integer call(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});

		JavaPairRDD<String, Tuple2<String, Integer>> counts_remapped = counts.mapToPair(new PairFunction<Tuple2<Tuple2<String, String>, Integer>, String, Tuple2<String, Integer>>() {
			@Override
			public Tuple2<String, Tuple2<String, Integer>> call(Tuple2<Tuple2<String, String>, Integer> s) {
				Tuple2<String, String> word_document = (Tuple2<String, String>)s._1();
				String word = word_document._1();
				String document = word_document._2();
				Integer count = s._2();
				Tuple2<String, Integer> document_count = new Tuple2<String, Integer>(document, count);
				return new Tuple2<String, Tuple2<String, Integer>>(word, document_count);
			}
		});
		List<Tuple2<String, Iterable<Tuple2<String, Integer>>>> result = counts_remapped.groupByKey().collect();
		for (Tuple2<String, Iterable<Tuple2<String, Integer>>> element : result) {
			String word = element._1();
			System.out.print(word + " : ");
			Iterable<Tuple2<String, Integer>> counters = element._2();
			for (Tuple2<?,?> counter : counters) {
				System.out.print("(" + counter._1() + ", " + counter._2() + ") ");	
			}
			System.out.println();
		}
		ctx.stop();
	}
}
