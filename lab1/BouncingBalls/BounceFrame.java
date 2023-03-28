import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class BounceFrame extends JFrame {
    private final BallCanvas canvas;
    public static final int WIDTH = 850;
    public static final int HEIGHT = 750;

    public BounceFrame() throws InterruptedException{

        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Bounce program");
        this.canvas = new BallCanvas();

        canvas.addHole(new Hole(canvas, Color.RED, 200, 150));

        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());

        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        JButton blueBall = new JButton("Blue");
        JButton redBall = new JButton("Red");
        JButton timedBall = new JButton("Timed");
        JButton buttonStop = new JButton("Stop");
        JTextArea scoreBoard = new JTextArea(Score.getScore());

        Score.addListener(
                new ScoreListener() {
                    @Override
                    public void actionPerformed() {
                        scoreBoard.setText(Score.getScore());
                        scoreBoard.repaint();
                    }
                });

        blueBall.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color color = Color.BLUE;
                        createBall(color, Thread.NORM_PRIORITY);
                    }
                });

        redBall.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Color color = Color.RED;
                        createBall(color, Thread.NORM_PRIORITY);
                    }
                });

        timedBall.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BallThread createdThread = createBall(Color.BLUE, Thread.MIN_PRIORITY, 15, 300);
                        createdThread.SetTimeout(500);
                    }
                });

        buttonStop.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        System.exit(0);
                    }
                });

        buttonPanel.add(blueBall);
        buttonPanel.add(redBall);
        buttonPanel.add(timedBall);
        buttonPanel.add(buttonStop);
        buttonPanel.add(scoreBoard);
        content.add(buttonPanel, BorderLayout.SOUTH);

        /*for (int i = 0; i < 3000; i++) {
            createBall(Color.BLUE,
                    Thread.MIN_PRIORITY, 0, 0);
        }
        for (int i = 0; i < 1; i++) {
            createBall(Color.RED,
                    Thread.MAX_PRIORITY, 0, 0);
        }*/

        Thread exp = new Thread(
                new Runnable() {
            public void run()
            {
                        for (int i = 0; i < 10; i++) {
                            try {
                                Thread.currentThread().sleep(1000);
                                BallThread createdThread = createBall(Color.BLUE, Thread.MIN_PRIORITY);
                                createdThread.SetTimeout(500);
                                createdThread.join();
                            } catch (InterruptedException e) {}
                        }
            }});

        exp.start();
    }

    private BallThread createBall(Color color, int priority) {
        return createBall(color,
                priority,
                new Random().nextInt(this.canvas.getWidth()),
                new Random().nextInt(this.canvas.getHeight()));
    }
    private BallThread createBall(Color color, int priority, int x, int y) {
        Ball b = new Ball(canvas, color, x, y);
        canvas.addBall(b);

        BallThread thread = new BallThread(b);
        thread.setPriority(priority);
        thread.start();

        return thread;
    }
}