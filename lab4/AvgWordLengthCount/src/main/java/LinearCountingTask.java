import java.util.ArrayList;
import java.util.List;

public class LinearCountingTask {
  public static ArrayList<Integer> processing(List<String> words) {
    ArrayList<Integer> wordCounts = new ArrayList<>();

    for (String word : words) {
      wordCounts.add(word.length());
    }

    return wordCounts;
  }
}
