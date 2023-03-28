public class BlockSyncedCounter implements ICountable {
    public int count = 0;

    @Override
    public void Increment() {
        synchronized (this) {
            count++;
        }
    }

    @Override
    public void Decrement() {
        synchronized (this) {
            count--;
        }
    }

}
