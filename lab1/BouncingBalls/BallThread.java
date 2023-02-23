
public class BallThread extends Thread {
  private final Ball ball;
  private int _timeout = 1;
  private boolean _isTimed = false;

  public BallThread(Ball ball) {
    this.ball = ball;
  }

  @Override
  public void run() {
    try {

      while(!ball.isInPool && _timeout > 0)
      {
        ball.move();
        Thread.sleep(5);
        if(_isTimed)
          _timeout--;
      }
    } catch (InterruptedException ignored) {}
  }

  public void SetTimeout(int time) {
    this._timeout = time;
    this._isTimed = true;
  }
}
