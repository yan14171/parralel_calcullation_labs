public class ProducerConsumerExample {
  public static void main(String[] args) {
    Drop drop = new Drop();
    DropS drops = new DropS();
    (new Thread(new Producer(drop, drops))).start();
    (new Thread(new Consumer(drop, drops))).start();
  }
}
