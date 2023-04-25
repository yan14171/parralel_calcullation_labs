import java.util.Random;

public class SMSProducer extends Thread {
    private final SMSQueue manager;

    SMSProducer(SMSQueue queue) {
        this.manager = queue;
    }

    @Override
    public void run() {
        Random random = new Random();
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (elapsedTime < 10_000) {
            this.manager.AddItem(random.nextInt(100));

            try {
                Thread.sleep(random.nextInt(15));
            } catch (InterruptedException ignored) {}

            elapsedTime = System.currentTimeMillis() - startTime;
        }

        this.manager.isOver = true;
    }
}
