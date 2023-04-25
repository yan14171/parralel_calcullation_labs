import java.util.Random;

public class SMSConsumer extends Thread {
    private final SMSQueue queue;

    SMSConsumer(SMSQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random random = new Random();

        while(!this.queue.isOver) {
            this.queue.GetItem();
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException ignored) {}

            this.queue.ConsumedCount++;
        }
    }

}
