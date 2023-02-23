import javax.swing.*;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    BounceFrame frame = new BounceFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.setVisible(true);
    System.out.println("Thread name = " + Thread.currentThread().getName());
  }
}
