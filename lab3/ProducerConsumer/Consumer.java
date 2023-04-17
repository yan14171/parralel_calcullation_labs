import java.util.Random;

public class Consumer implements Runnable {
  private final Drop drop;
  private final DropS drops;

  public Consumer(Drop drop, DropS drops) {
    this.drop = drop;
    this.drops = drops;
  }

  public void run() {
    Random random = new Random();
    for (int message = drop.take();
         message != 0;
         message = drop.take()) {
      System.out.format("MESSAGE RECEIVED: %d%n", message);
      //System.out.format("MESSAGE RECEIVED: %s%n", message);
      try {
        Thread.sleep(random.nextInt(5000));
      } catch (InterruptedException e) {
      }
    }
  }

//  public void run() {
//    Random random = new Random();
//    for (String message = drops.take();
//         ! message.equals("DONE");
//         message = drops.take()) {
//      System.out.format("MESSAGE RECEIVED: %s%n", message);
//      try {
//        Thread.sleep(random.nextInt(5000));
//      } catch (InterruptedException e) {}
//    }
//  }
}
