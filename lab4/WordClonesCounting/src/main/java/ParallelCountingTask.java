import java.util.*;
import java.util.concurrent.RecursiveTask;

public class ParallelCountingTask extends RecursiveTask<Set<String>> {
  private final List<List<String>> documents;
  public ParallelCountingTask(List<List<String>> documents) {
    this.documents = documents;
  }

  @Override
  protected Set<String> compute(){
    List<RecursiveTask<Set<String>>> forks = new ArrayList<>();

    if(this.documents.stream().count() > 1)
    {
      for(List<String> doc : documents)
      {
        ArrayList<List<String>> singleDoc = new ArrayList<>();
        singleDoc.add(doc);
        ParallelCountingTask task =
                new ParallelCountingTask(singleDoc);
        forks.add(task);
        task.fork();
      }
    }
    else if (documents.get(0).size() > 16_000)
    {
      forks.addAll(createSubSearches(documents.get(0)));
    }
    else if(forks.size() == 0)
    {
      HashSet<String> distinctWords = new HashSet<>();
      for (String line : documents.get(0)) {
        distinctWords.addAll(getWordsFromLine(line));
      }
      return distinctWords;
    }

      Set<String> result = forks.get(0).join();
      forks.remove(0);
      for (RecursiveTask<Set<String>> task : forks) {
        result.retainAll(task.join());
      }
      return result;
  }

  private static List<String> getWordsFromLine(String line) {
    String[] words = line.trim().split("\\s+");
    return Arrays.asList(words);
  }

  private static ArrayList<ParallelCountingTask> createSubSearches(List<String> words) {
    List<String> doc1 = words.subList(0, words.size() / 2);
    ArrayList<List<String>> singleHalfDoc = new ArrayList<>();
    singleHalfDoc.add(doc1);

    List<String> doc2 = words.subList(words.size() / 2, words.size());
    ArrayList<List<String>> singleDoc = new ArrayList<>();
    singleDoc.add(doc2);

    ParallelCountingTask halfTask = new ParallelCountingTask(singleHalfDoc);
    ParallelCountingTask halfTask1 = new ParallelCountingTask(singleDoc);

    ArrayList<ParallelCountingTask> result = new ArrayList<>();
    result.add(halfTask1);
    halfTask1.fork();
    result.add(halfTask);
    halfTask.fork();

    return result;
  }
}
