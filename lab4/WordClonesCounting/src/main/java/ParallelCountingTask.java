import java.util.*;
import java.util.concurrent.RecursiveTask;

public class ParallelCountingTask extends RecursiveTask<List<Integer>> {
  private final List<String> words;

  public ParallelCountingTask(List<String> words) {
    this.words = words;
  }

  @Override
  protected List<Integer> compute() {
    List<Integer> lenghts = new ArrayList<Integer>();
    if (this.words.size() > 16_000)
    {
      ParallelCountingTask halfTask = new ParallelCountingTask(words.subList(0, words.size() / 2));
      ParallelCountingTask halfTask1 = new ParallelCountingTask(words.subList(words.size() / 2, words.size()));

      lenghts.addAll(halfTask.invoke());
      lenghts.addAll(halfTask1.invoke());
      halfTask1.join();
      halfTask.join();
      return lenghts;
    }
    else
    {
      for(String word : words)
      {
        lenghts.add(word.length());
      }
      return lenghts;
    }
  }
}
