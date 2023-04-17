import java.util.ArrayList;
import java.util.List;

public class Teacher implements Runnable {
  private final ArrayList<List<Double>> journal;

  public Teacher(ArrayList<List<Double>> journal) {
    this.journal = journal;
  }

  @Override
  public void run() {
      for (List<Double> group: journal) {
        for (int j = 0 ; j < 7 ; j++) {
          synchronized (group) {

            group.add((double) (Math.round(100 * Math.random() * 100)) / 100);
            //group.add((double) (100));

          }
        }
      }
  }
}
