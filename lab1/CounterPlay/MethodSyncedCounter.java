public class MethodSyncedCounter implements ICountable{
    public int count = 0;

    public synchronized void Increment() {
        count++;
    }

    public synchronized void Decrement() {
        count--;
    }
}
