import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SMSQueue {
    private final int queueSize;
    private int countRejected;
    private final Queue<Integer> queue;
    private List<Integer> queueSizeMeasures;


    public int ConsumedCount;
    public boolean isOver;
    public synchronized int QueueLength() {
        return this.queue.size();
    }

    SMSQueue(int queueSize) {
        this.ConsumedCount = 0;
        this.countRejected = 0;
        this.isOver = false;
        this.queue = new ArrayDeque<>();
        this.queueSize = queueSize;
        queueSizeMeasures = new ArrayList<>();
    }

    public synchronized void AddItem(int item) {
        if(this.queue.size() >= this.queueSize) {
            this.countRejected += 1;
            return;
        }

        this.queue.add(item);
        notifyAll();
    }
    public synchronized int GetItem() {
        this.queueSizeMeasures.add(this.queue.size());
        while(this.queue.size() == 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {}
        }
        return this.queue.poll();
    }
    public double calculateRejectedPercentage() {
        return (double) this.countRejected / (this.countRejected + this.ConsumedCount);
    }
    public double calculateAverageQueueSize(){
        int sum = 0;
        for (int i : queueSizeMeasures) {
            sum += i;
        }
        return (double) sum / queueSizeMeasures.size();
    }

}
