
import com.google.common.base.Stopwatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {
  public static void main(String[] args) {
    List<List<String>> documents = readTxtFiles("compare");

    ForkJoinPool forkJoinPool =
            new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    Set<String> result = forkJoinPool.invoke(new ParallelCountingTask(documents));

    System.out.println(result);
  }

  public static List<List<String>> readTxtFiles(String folderPath) {
    File folder = new File(folderPath);
    List<List<String>> allFilesContent = new ArrayList<>();

    File[] files = folder.listFiles();

    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".txt")) {
        List<String> fileContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          String line;
          while ((line = reader.readLine()) != null) {
            fileContent.add(line);
          }
        } catch (IOException e) {
          System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
        }
        allFilesContent.add(fileContent);
      }
    }

    return allFilesContent;
  }
}
