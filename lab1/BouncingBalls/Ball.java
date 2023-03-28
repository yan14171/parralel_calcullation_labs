import java.awt.*;
import java.awt.geom.Ellipse2D;
class Ball {
  private final Component canvas;
  private static final int RADIUS = 10;
  private static final int DIAMETER = RADIUS * 2;
  private int x = 0;
  private int y = 0;
  private int dx = 2;
  private int dy = 2;
  private final Color color;
  public boolean isInPool = false;

  public Ball(Component c, Color color, int x, int y) {
    this.canvas = c;
    this.color = color;

    this.x = x;
    this.y = y;
  }

  public void draw(Graphics2D g2) {
    g2.setColor(this.color);
    g2.fill(new Ellipse2D.Double(x, y, DIAMETER, DIAMETER));
  }

  public void move() {
    x += dx;
    y += dy;
    if (x < 0) {
      x = 0;
      dx = -dx;
    }
    if (x + DIAMETER >= this.canvas.getWidth()) {
      x = this.canvas.getWidth() - DIAMETER;
      dx = -dx;
    }

    if (y < 0) {
      y = 0;
      dy = -dy;
    }
    if (y + DIAMETER >= this.canvas.getHeight()) {
      y = this.canvas.getHeight() - DIAMETER;
      dy = -dy;
    }

    this.canvas.revalidate();
    this.canvas.repaint();
  }

  public boolean intersects(Hole hole) {
    double distance = Math.sqrt(Math.pow(hole.getX() - this.x, 2) + Math.pow(hole.getY() - this.y, 2));

    return distance  < Hole.getRADIUS();
  }
}
