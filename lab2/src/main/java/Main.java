import java.util.*;
import java.util.concurrent.*;
import com.google.common.base.Stopwatch;

public class Main {
  static final int MATRIX_SIZE = 1000;
  static final int[] THREADS_EXP = new int[] {16, 32, 64, 128, 256, 512};
  static final int[] SIZES_EXP = new int[] {100, 1000, 1500};

  public static void main(String[] args){

    int[][] multiplied = new int[MATRIX_SIZE][MATRIX_SIZE];// {{1, 1, 1}, {1, 1, 1}, {2, 2, 2}};
    int[][] multiplicator = new int[MATRIX_SIZE][MATRIX_SIZE];// {{1, 1, 1}, {1, 1, 1}, {2, 2, 2}};

    generateRandomMatrix(multiplied);
    generateRandomMatrix(multiplicator);

    //(4 | 4 | 4
    // 4 | 4 | 4
    // 8 | 8 | 8)

    Stopwatch timer = Stopwatch.createStarted();
    Result finalMatrix = multiply(multiplied, multiplicator, 3);

    Main.print(finalMatrix.getResultMatrix());
    System.out.print('\n');
    System.out.print('\n');

    Result r = new Result(multiplyF(multiplied, multiplicator, 3));

    Main.print(r.getResultMatrix());

    System.out.println("Time for Striped Algorithm: " + timer.elapsed());

    generateRandomMatrix(multiplied);
    generateRandomMatrix(multiplicator);

    for (int threadNumber : THREADS_EXP) {
      timer = Stopwatch.createStarted();
      multiply(multiplied, multiplicator, threadNumber);

      System.out.println("Striped with " + threadNumber + " threads took " + timer.elapsed() + " ms");
    }

    for (int threadNumber : Arrays.stream(THREADS_EXP).limit(3).toArray()) {
      timer = Stopwatch.createStarted();
      multiplyF(multiplied, multiplicator, threadNumber);

      System.out.println("Fox with " + threadNumber + " threads took " + timer.elapsed() + " ms");
    }

    System.out.println();
    System.out.println("------------------------ ");
    System.out.println();

    for (int size : SIZES_EXP) {
      multiplied = new int[size][size];
      multiplicator = new int[size][size];

      generateRandomMatrix(multiplied);
      generateRandomMatrix(multiplicator);

      timer = Stopwatch.createStarted();
      multiply(multiplied, multiplicator, 12);

      System.out.println("Striped with " + size + " size took " + timer.elapsed() + " ms");

      timer = Stopwatch.createStarted();
      multiplyF(multiplied, multiplicator, 12);

      System.out.println("Fox with " + size + " size took " + timer.elapsed() + " ms");
    }
    }

  static Result multiply(int[][] multiplied, int[][] multiplicator, int threadNumber)
  {
    int[][] result = new int[multiplied.length][multiplied.length];
    ExecutorService executor = Executors.newFixedThreadPool(threadNumber);

    List<Future<int[]>> list = new ArrayList<>();

    for (int i = 0; i < multiplied.length; i++) {
      StripedAlgorithmCallable worker = new StripedAlgorithmCallable(multiplied[i], i, multiplicator);
      Future submit = executor.submit(worker);
      list.add(submit);
    }

    for (int i = 0; i < list.size(); i++) {
      try {
        result[i] = list.get(i).get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    executor.shutdown();

    return new Result(result);
  }

  static void generateRandomMatrix(int[][] matrix) {
    Random random = new Random();
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        matrix[i][j] = random.nextInt(10);
      }
    }
  }

  static void print(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.print('\n');
    }
  }

  static int gcd(int minDivisor, int dividend) {
    int currentDivisor = minDivisor;
    while (currentDivisor > 1 && dividend % currentDivisor != 0) {
      currentDivisor += currentDivisor >= minDivisor ? 1 : -1;
      if (currentDivisor > Math.sqrt(dividend)) {
        currentDivisor = Math.min(minDivisor, dividend / minDivisor) - 1;
      }
    }
    if(currentDivisor == -1)
      currentDivisor*=-1;

    return currentDivisor >= minDivisor ? currentDivisor : currentDivisor != 0 ? dividend / currentDivisor : dividend;
  }

  static int[][] copy(int[][] matrix, int i, int j, int size) {
    int[][] block = new int[size][size];
    for (int k = 0; k < size; k++) {
      System.arraycopy(matrix[k + i], j, block[k], 0, size);
    }
    return block;
  }














  //region F

  static int[][] multiplyF(int[][] multiplied, int[][] multiplicator, int threadNumber) {
    int[][] result = new int[multiplied.length][multiplicator.length];

    threadNumber = gcd(threadNumber, multiplied.length);
    int step = multiplied.length / threadNumber;

    ExecutorService exec = Executors.newFixedThreadPool(threadNumber);

    int[][] blocksRows = new int[threadNumber][threadNumber];
    int[][] blocksColumns = new int[threadNumber][threadNumber];

    GetBlocks(threadNumber, step, blocksRows, blocksColumns);

    ArrayList<Future> threads = MultiplyBlocks(multiplied, multiplicator, threadNumber, result, step, exec, blocksRows, blocksColumns);

    for (Future mapFuture : threads) {
      try {
        mapFuture.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }

    exec.shutdown();

    return result;
  }

  static ArrayList<Future> MultiplyBlocks(int[][] multiplied,
                                          int[][] multiplicator,
                                          int threadNumber,
                                          int[][] result, int step,
                                          ExecutorService exec, int[][] rowsOffsets, int[][] columnsOffsets) {
    ArrayList<Future> threads = new ArrayList<>();

    for (int l = 0; l < threadNumber; l++) {
      for (int i = 0; i < threadNumber; i++) {
        for (int j = 0; j < threadNumber; j++) {

          int offsetRow1 = rowsOffsets[i][(i + l) % threadNumber];
          int offsetColumn1 = columnsOffsets[i][(i + l) % threadNumber];

          int offsetRow2 = rowsOffsets[(i + l) % threadNumber][j];
          int offsetColumn2 = columnsOffsets[(i + l) % threadNumber][j];

          int[][] copiedMultipliedBlock  = copy(multiplied, offsetRow1, offsetColumn1, step);
          int[][] copiedMultiplicatorBlock = copy(multiplicator, offsetRow2, offsetColumn2, step);

          FoxAlgorithmThread t = new FoxAlgorithmThread(copiedMultipliedBlock, copiedMultiplicatorBlock, result, rowsOffsets[i][j], columnsOffsets[i][j]);
          threads.add(exec.submit(t));
        }
      }
    }
    return threads;
  }

  static void GetBlocks(int threadNumber, int step, int[][] blocksRows, int[][] blocksColumns) {
    int offsetRow = 0, offsetColumn = 0;
    for (int i = 0; i < threadNumber; i++) {
      offsetColumn = 0;
      for (int j = 0; j < threadNumber; j++) {
        blocksRows[i][j] = offsetRow;
        blocksColumns[i][j] = offsetColumn;
        offsetColumn += step;
      }
      offsetRow += step;
    }
  }
}

class StripedAlgorithmCallable implements Callable<int[]> {
  private final int[] row;
  private int[][] multiplied;

  public StripedAlgorithmCallable(int[] row, int rowIndex, int[][] multiplied) {
    this.row = row;
    this.multiplied = multiplied;
  }

  @Override
  public int[] call() {
    int[] result = new int[multiplied.length];

    for (int j = 0; j < multiplied.length; j++) {
      for (int i = 0; i < row.length; i++) {
        result[j] += row[i] * multiplied[i][j];
      }
    }
    return result;
  }
}

class FoxAlgorithmThread extends Thread {
  private final int[][] multiplied;
  private final int[][] multiplicator;
  private final int[][] result;
  private final int offsetRow;
  private final int offsetColumn;

  public FoxAlgorithmThread(
          int[][] multiplied,
          int[][] multiplicator,
          int[][] result,
          int offsetRow,
          int offsetColumn) {
    this.multiplied = multiplied;
    this.multiplicator = multiplicator;
    this.result = result;
    this.offsetRow = offsetRow;
    this.offsetColumn = offsetColumn;
  }

  @Override
  public void run() {
    int[][] blockRes = new int[multiplied.length][multiplicator.length];
    for (int i = 0; i < multiplied.length; i++) {
      for (int j = 0; j < multiplicator.length; j++) {
        for (int k = 0; k < multiplied.length; k++) {
          blockRes[i][j] += multiplied[i][k] * multiplicator[k][j];
        }
      }
    }

    for (int i = 0; i < blockRes.length; i++) {
      for (int j = 0; j < blockRes.length; j++) {
        result[i + offsetRow][j + offsetColumn] += blockRes[i][j];
      }
    }
  }
}