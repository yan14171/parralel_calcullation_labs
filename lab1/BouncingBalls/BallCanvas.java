import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.swing.*;

public class BallCanvas extends JPanel {
  private final ArrayList<Ball> balls = new ArrayList<>();
  private final ArrayList<Hole> holes = new ArrayList<>();

  public void addBall(Ball b) {
    this.balls.add(b);
  }

  public void addHole(Hole p) {
    this.holes.add(p);
  }
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    try {
      for (Hole p : holes) {
        for (Ball b : balls) {
          if (b.intersects(p))
          {
            Score.increase();
            this.balls.remove(b);
          }
          else
            b.draw(g2);
        }
        p.draw(g2);
      }
    } catch (ConcurrentModificationException ex) {}
  }
}
