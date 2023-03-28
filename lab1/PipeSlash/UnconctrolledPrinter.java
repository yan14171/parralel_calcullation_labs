public class UnconctrolledPrinter implements Runnable {
  private String symbol = "";

  public UnconctrolledPrinter(String symbol) {
    this.symbol = symbol;
  }

  @Override
  public void run() {
    for (int i = 0; i < 100; i++) {
      System.out.print(this.symbol);
    }
  }
}
