import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
  public static final int NTEST = 10000;
  private final int[] accounts;
  private long ntransacts = 0;
  public Bank(int n, int initialBalance) {
    accounts = new int[n];
    //accounts = new AtomicIntegerArray(new int[n]);
    for (int i = 0; i < accounts.length; i++)
        accounts[i] = initialBalance;
    ntransacts = 0;
   //  for (i = 0; i < accounts.length(); i++) accounts.set(i, initialBalance);
   //  ntransacts.set(0);
  }

//  private final AtomicIntegerArray accounts;
//  public void transfer(int from, int to, int amount) throws InterruptedException {
//    accounts.addAndGet(from, -amount);
//    accounts.addAndGet(to, amount);
//    ntransacts++;
//    if (ntransacts % NTEST == 0) test();
//  }

//  private final ReentrantLock lock = new ReentrantLock();
//  public void transfer(int from, int to, int amount) throws InterruptedException {
//    lock.lock();
//    accounts[from] -= amount;
//    accounts[to] += amount;
//    ntransacts++;
//    if (ntransacts % NTEST == 0) test();
//    lock.unlock();
//  }

//  public void transfer(int from, int to, int amount) throws InterruptedException {
//    synchronized (this){
//      accounts[from] -= amount;
//      accounts[to] += amount;
//      ntransacts++;
//      if (ntransacts % NTEST == 0) test();
//    }
//  }

//  public synchronized void transfer(int from, int to, int amount) throws InterruptedException {
//    accounts[from] -= amount;
//    accounts[to] += amount;
//    ntransacts++;
//    if (ntransacts % NTEST == 0) test();
//  }

  public void test() {
    int sum = 0;
    synchronized(accounts) {
      for(int account : accounts) {
        sum += account;
      }
    }
    System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
  }

  public int size() {
    return accounts.length;
  }
}
