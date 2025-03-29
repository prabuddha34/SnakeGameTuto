import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
public class SnakeGame extends JPanel implements ActionListener {
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 400;
    private static final int GRID_SIZE = 20;
    private static final int INITIAL_SNAKE_LENGTH = 3;
    private static final int DELAY = 100; // m
    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean running;
    private Timer timer;
    private int score;
    public SnakeGame() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        snake = new ArrayList<>();
        direction = KeyEvent.VK_RIGHT; // initial direction
        running = true;
        score = 0;
        for (int i = 0; i < INITIAL_SNAKE_LENGTH; i++) {
            snake.add(new Point(SCREEN_WIDTH / 2 - i * GRID_SIZE, SCREEN_HEIGHT / 2));
        }
        spawnFood();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT ||
                        key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) && running) {
                    direction = key;
                }
            }
        });
        timer = new Timer(DELAY, this);
        timer.start();
    }
    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(SCREEN_WIDTH / GRID_SIZE) * GRID_SIZE;
        int y = random.nextInt(SCREEN_HEIGHT / GRID_SIZE) * GRID_SIZE;
        food = new Point(x, y);
    }
    private void move() {
        Point head = snake.get(0);
        Point newHead = new Point(head.x, head.y);
        switch (direction) {
            case KeyEvent.VK_LEFT:
                newHead.x -= GRID_SIZE;
                break;
            case KeyEvent.VK_RIGHT:
                newHead.x += GRID_SIZE;
                break;
            case KeyEvent.VK_UP:
                newHead.y -= GRID_SIZE;
                break;
            case KeyEvent.VK_DOWN:
                newHead.y += GRID_SIZE;
                break;
        }
        if (newHead.x < 0 || newHead.x >= SCREEN_WIDTH || newHead.y < 0 || newHead.y >= SCREEN_HEIGHT
                || snake.contains(newHead)) {
            running = false;
            timer.stop();
        } else {
            snake.add(0, newHead);
            if (newHead.equals(food)) {
                score=score+10;
                spawnFood();
            } else {
                snake.remove(snake.size() - 1);
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 18));
        String titleLabel = "Prabuddha's Snake Game";
        int titleWidth = g.getFontMetrics().stringWidth(titleLabel);
        g.drawString(titleLabel, SCREEN_WIDTH / 2 - titleWidth / 2, 30);
        g.setColor(Color.GREEN);
        for (Point segment : snake) {
            g.fillRect(segment.x, segment.y, GRID_SIZE, GRID_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x, food.y, GRID_SIZE, GRID_SIZE);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        String scoreLabel = "Score: " + score;
        g.drawString(scoreLabel, 10, 20);
        if (!running) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 24));
            String gameOverMsg = "Game Over LOL!";
            int msgWidth = g.getFontMetrics().stringWidth(gameOverMsg);
            g.drawString(gameOverMsg, SCREEN_WIDTH / 2 - msgWidth / 2, SCREEN_HEIGHT / 2);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new SnakeGame(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}