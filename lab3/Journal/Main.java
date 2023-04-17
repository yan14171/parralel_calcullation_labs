import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
    ArrayList<List<Double>> journal = new ArrayList<>();
        journal.add(new ArrayList<Double>());
        journal.add(new ArrayList<Double>());
        journal.add(new ArrayList<Double>());

    Runnable r = () ->
      {
              for(int i = 0; i < 4; i++) {
                  new Thread(new Teacher(journal)).start();
              }

      };

    Thread t = new Thread(r);
    t.start();
    t.join();
    Thread.sleep(2000);
    System.out.println(t.getState());
    for(List<Double> group : journal)
      {
          System.out.println("Group " + group.hashCode());
          for(int i = 0 ; i < group.size(); i++) {
              if(i % 7 == 0) {
                  System.out.println();
                  System.out.println(i / 7 == 0 ? "\tTeacher" : "\tAssistant " + i / 7);
              }
              System.out.print("\t" + group.get(i) + " ");
          }
              System.out.println();
          System.out.println(group.toArray().length);
      }
  }
}
