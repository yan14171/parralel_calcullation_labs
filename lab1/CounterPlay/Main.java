
public class Main {
  public static void main(String[] args) throws InterruptedException {


      StartTestOnSpecifiedCounter(new NaiveCounter(), 100_000_000);

      StartTestOnSpecifiedCounter(new MethodSyncedCounter(), 100_000);
      StartTestOnSpecifiedCounter(new BlockSyncedCounter(), 100_000);
  }

  public static void StartTestOnSpecifiedCounter(ICountable counter, int count) throws InterruptedException{

      Thread incrementTask = new Thread(() -> {
        for (int i = 0; i < count; i++) counter.Increment();
    });

    Thread decrementTask = new Thread(() -> {
          for (int i = 0; i < count; i++) counter.Decrement();
      });

    incrementTask.start();
    decrementTask.start();
    incrementTask.join();
    decrementTask.join();

    System.out.println("Counter is set to " + counter.count);
  }
}
