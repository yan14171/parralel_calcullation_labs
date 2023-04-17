
import com.google.common.base.Stopwatch;
import com.sun.javafx.css.CalculatedValue;
import javafx.scene.paint.Stop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) throws IOException {
    List<String> words = GetWords("text.txt");

    System.out.printf("Number of words: %d\n\n", words.size());
    ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());


    List<Integer> res = null;
    Stopwatch timer = Stopwatch.createStarted();
        res = pool.submit(new ParallelCountingTask(words)).join();
    Duration forkJoinTime = timer.elapsed();

    System.out.println("Execution time (ForkJoin):\t\t\t" + forkJoinTime.toMillis());

    System.out.println("Result (ForkJoin):\t\t\t" +
        res.stream().mapToDouble(i -> i).sum()
            / res.stream().mapToDouble(i -> i).count());



    timer = Stopwatch.createStarted();
    ArrayList<Integer> res1 = null;
    res1 = LinearCountingTask.processing(words);
    Duration standartTime = timer.elapsed();

    System.out.println("Execution time (Single Thread):\t\t\t" + standartTime.toMillis());


    System.out.println("Result (Single Thread):\t\t\t" +
            res1.stream().mapToDouble(i -> i).sum()
                    / res1.stream().mapToDouble(i -> i).count());


    System.out.println("SpeadUp =  " + (double)standartTime.getNano() / forkJoinTime.getNano());

    RandomVariable.calculateAndPrint(res);

  }
  public static List<String> GetWords(String filepath) throws IOException {
    List<String> wordList = new ArrayList<>();
    List<String> lines = Files.readAllLines(Paths.get(filepath));

    for (String line : lines) {
      String[] words = line.split("\\s+");
      wordList.addAll(Arrays.stream(words).collect(Collectors.toList()));
    }
    return wordList;
  }


  private static void exp(List<String> words){
    long result = 0;
    for(int i = 0; i < 5; i ++)
      result += CalculateLinear(words);

    System.out.println("Execution time (Single Thread):\t\t\t" + result / 5 );

    long forkresult = 0;
    for(int i = 0; i < 5; i ++)
      forkresult += CalculateLinear(words);

    System.out.println("Execution time (ForkJoin):\t\t\t" + forkresult / 5);
    System.out.println("SpeadUp =  " + ((double)result/5) / ((double)forkresult/5));
  }

  private static long CalculateLinear(List<String> words){
    Stopwatch timer = Stopwatch.createStarted();
    ArrayList<Integer> res1 = null;
    res1 = LinearCountingTask.processing(words);
    Duration standartTime = timer.elapsed();

//    System.out.println("Result (Single Thread):\t\t\t" +
//            res1.stream().mapToDouble(i -> i).sum()
//                    / res1.stream().mapToDouble(i -> i).count());

    return standartTime.toMillis();
  }

  private static long CalculateFork(List<String> words){
    ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    List<Integer> res = null;
    Stopwatch timer = Stopwatch.createStarted();
    res = pool.submit(new ParallelCountingTask(words)).join();
    Duration forkJoinTime = timer.elapsed();

    System.out.println("Result (ForkJoin):\t\t\t" +
            res.stream().mapToDouble(i -> i).sum()
                    / res.stream().mapToDouble(i -> i).count());

    return  forkJoinTime.toMillis();
  }
}

