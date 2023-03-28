public class NaiveCounter implements ICountable{
    public int count = 0;

    public void Increment() {
        count++;
    }

    public void Decrement() {
        count--;
    }
}
