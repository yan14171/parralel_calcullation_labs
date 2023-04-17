import com.google.common.base.Stopwatch;
import javafx.scene.paint.Stop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
  static Stopwatch timer;

  public static void main(String[] args) {
    int[][] A = new int[1000][1000];
    int[][] B = new int[1000][1000];
    int[][] C = new int[A.length][B[0].length];

    generateRandomMatrix(A);
    generateRandomMatrix(B);

    timer=Stopwatch.createStarted();
    for(int i = 0; i < 5; i++)
      C = multiply(A, B, 12, false);



    int [][]D = new int[A.length][B[0].length];

    timer = Stopwatch.createStarted();
    for(int i = 0; i < 5; i++) {
      D = new int[A.length][B[0].length];
      ParallelStripeForkMatrixMultiplication.multiply(A, B, D);
    }

    System.out.println("Fork");
    print(D[0]);
    System.out.println("Stripe");
    print(C[0]);
  }

  static int[][] multiply(int[][] multiplied, int[][] multiplicator, int threadNumber, boolean show) {
    int[][] result = new int[multiplied.length][multiplied.length];
    ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

    List<Future<int[]>> list = new ArrayList<>();

    for(int i = 0; i < multiplied.length; i++) {
      StripedAlgorithmCallable worker = new StripedAlgorithmCallable(multiplied[i], i, multiplicator);
      Future submit = executor.submit(worker);
      list.add(submit);
    }

    for(int i = 0; i < list.size(); i++) {
      try {
        result[i] = list.get(i).get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
    if(show)print(result);
    executor.shutdown();

    return result;
  }









  //utils
  static void generateRandomMatrix(int[][] matrix) {
    Random random = new Random();
    for(int i = 0; i < matrix.length; i++) {
      for(int j = 0; j < matrix.length; j++) {
        matrix[i][j] = random.nextInt(10);
      }
    }
  }

  static void print(int[][] matrix) {
    for(int i = 0; i < matrix.length; i++) {
      System.out.print(matrix[0][i] + " ");
    }
    System.out.print('\n');
    System.out.println("ForkJoin took " + timer.elapsed().toMillis() / 5);

  }

  static void print(int[] matrix) {
    for(int i = 0; i < matrix.length; i++) {
      System.out.print(matrix[i] + " ");
    }
    System.out.println();
    System.out.println("Alogrithm took " + timer.elapsed().toMillis() / 5);
    try{
      Thread.sleep(new Random().nextInt(101) + 300);
    }catch (Exception ex)
    {}
  }
}
