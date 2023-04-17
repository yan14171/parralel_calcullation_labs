
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Main {
  public static void main(String[] args) throws IOException {

    List<Document> docs = readTxtFiles("compare");

    ForkJoinPool forkJoinPool =
            new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    Set<String> docsResult = forkJoinPool.invoke(new ParallelSearchingTask(docs));

    System.out.println("Keywords were found in the next files:");
    for (String documentPath : docsResult)
    {
      int lastBackslashIndex = documentPath.lastIndexOf("\\");
      String fileName = documentPath.substring(lastBackslashIndex + 1);
      System.out.println(fileName);
    }
  }

  public static List<Document> readTxtFiles(String folderPath) {
    File folder = new File(folderPath);
    List<Document> documents = new ArrayList<>();

    File[] files = folder.listFiles();

    for (File file : files) {
      if (file.isFile() && file.getName().endsWith(".txt")) {
        Document document = new Document(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          String line;
          while ((line = reader.readLine()) != null) {
            document.addLine(line);
          }
          documents.add(document);
        } catch (IOException e) {
          System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
        }
      }
    }

    return documents;
  }
}