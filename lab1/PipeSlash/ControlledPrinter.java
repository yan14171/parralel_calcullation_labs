public class ControlledPrinter extends Thread {
  private String symbol = "";
  private final Control control;
  private final int n_max = 500;

  public ControlledPrinter(String symbol, Control control) {
    this.symbol = symbol;
    this.control = control;
  }

  @Override
  public void run(){
    synchronized (control) {
      for (int i = 0; i < n_max; i++) {

        try {
          while (control.flag && this.symbol.equals("-"))
            control.wait();
          while (!control.flag && this.symbol.equals("|"))
            control.wait();
        } catch(InterruptedException ex) {}

        System.out.print(this.symbol);
        control.flag = !control.flag;
        control.notifyAll();
      }
    }
  }
}
