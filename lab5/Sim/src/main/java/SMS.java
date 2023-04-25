import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SMS implements Callable<double[]> {
    private int queueSize;
    private SMSQueue queue;
    SMS(int queueSize) {
        this.queueSize = queueSize;
        queue = new SMSQueue(queueSize);
    }
    public double[] call() {
        SMSProducer producerThread = new SMSProducer(queue);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < 5; i++) {
            Thread thread = new SMSConsumer(queue);
            executor.execute(thread);
        }

        Thread thread = new Thread(new Runnable() {
            public void run() {
                while(!queue.isOver) {
                    try
                    { Thread.sleep(100); } catch (InterruptedException e) {}
                    System.out.println("Queue length: " + queue.QueueLength() + ", rejected percentage: " + queue.calculateRejectedPercentage());
                }
            }});
        executor.execute(thread);


        executor.execute(producerThread);
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {}

        return new double[] {queue.calculateRejectedPercentage(), queue.calculateAverageQueueSize()};
    }
}
