import javax.xml.soap.Text;

public class Main {
  public static void main(String[] args) throws InterruptedException {
/*
    Thread slash = new Thread(new UnconctrolledPrinter("-"));
    Thread pipe = new Thread(new UnconctrolledPrinter("|"));

    slash.start();
    pipe.start();
*/
    Control control = new Control();
    ControlledPrinter slash = new ControlledPrinter("-", control);
    ControlledPrinter pipe = new ControlledPrinter("|", control);

    slash.start();
    pipe.start();
  }
}
