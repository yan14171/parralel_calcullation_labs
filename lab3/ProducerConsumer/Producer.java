import java.util.Arrays;
import java.util.Random;

public class Producer implements Runnable {
  private final Drop drop;
  private final DropS drops;

  public Producer(Drop drop, DropS drops) {
    this.drop = drop;
    this.drops = drops;
  }

  public void run() {
    int[] importantInfo = new int[1000];
    Arrays.fill(importantInfo, 5);
    importantInfo[0] = 1;
    importantInfo[1] = 2;
    importantInfo[2] = 3;

    Random random = new Random();

    for (int i = 0;
         i < importantInfo.length;
         i++) {
      drop.put(importantInfo[i]);
      try {
        Thread.sleep(random.nextInt(5000));
      } catch (InterruptedException e) {}
    }

    drop.put(0);
  }

//  public void run() {
//    String importantInfo[] = {
//            "Mares eat oats",
//            "Does eat oats",
//            "Little lambs eat ivy",
//            "A kid will eat ivy too"
//    };
//    Random random = new Random();
//
//    for (int i = 0;
//         i < importantInfo.length;
//         i++) {
//      drops.put(importantInfo[i]);
//      try {
//        Thread.sleep(random.nextInt(5000));
//      } catch (InterruptedException e) {}
//    }
//    drops.put("DONE");
//  }
}