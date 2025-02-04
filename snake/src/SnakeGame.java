import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    // constants for game configurations
    private final int TILE_SIZE = 25; // size of each tile (snake segment and food)
    private final int WIDTH = 600; // width of game area
    private final int HEIGHT = 600; // height of game area
    private final int TOTAL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE); // total number tiles in the grid
    
    // Arrays to store the x and y coordinates of each part of the snake's body
    private final int x[] = new int[TOTAL_TILES];
    private final int y[] = new int[TOTAL_TILES];

    private int snakeLength; // The length of the snake
    private int foodX, foodY; // The coordinates of the food
    private char direction = 'R'; // Initial direction of the snake (Right)
    private boolean running = false; // Flag to track whether the game is running
    private Timer timer; // Timer to control the game loop (snake movement and updates)
    private int score = 0; // Player's score
    private JButton playAgainButton; // Button to restart the game after a game over
    
    // Constructor to set up the game panel and start the game
    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Set the preferred size of the game area
        setBackground(Color.BLACK); // Set background color to black
        setFocusable(true);  // Make the panel focusable to listen for key events
        addKeyListener(new KeyHandler()); // Add key listener to handle user input
        startGame(); // Initialize and start the game
    }
    
    // Method to initialize the game state
    private void startGame() {
        snakeLength = 3;  // Set the initial snake length
        score = 0; // Reset the score

         // Set initial positions of the snake (3 segments)
        for (int i = 0; i < snakeLength; i++) {
            x[i] = 100 - i * TILE_SIZE; // Snake starts at x = 100, and segments are spaced apart
            y[i] = 100; // Snake starts at y = 100
        }

        
        spawnFood(); // Spawn food at a random location
        running = true; // Set the game state to running
        timer = new Timer(100, this); // Create a Timer to call actionPerformed every 100ms
        timer.start();  // Start the game timer
    }
    
    // Method to spawn food at a random location
    private void spawnFood() {
        Random rand = new Random();
        foodX = rand.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE; // Random x-coordinate for food
        foodY = rand.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE; // Random y-coordinate for food
    }
    
    // Method to render the game on the screen
    @Override
    protected void paintComponent(Graphics g) {
        // Call the superclass method to ensure proper rendering
        super.paintComponent(g);
        if (running) {
            // Draw the food in red
            g.setColor(Color.RED);
            g.fillRect(foodX, foodY, TILE_SIZE, TILE_SIZE);

            // Draw the snake (first segment in orange, others in yellow)
            for (int i = 0; i < snakeLength; i++) {
                // orange for the head, yellow for the body
                g.setColor(i == 0 ? Color.ORANGE : Color.YELLOW);
                g.fillRect(x[i], y[i], TILE_SIZE, TILE_SIZE);
            }

             // Display the score in white at the top-left corner
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            // Display the game over message when the game ends
            gameOver(g);
        }
    }
    
     // Method to move the snake
     private void move() {
        // Move the body segments to the position of the segment ahead of it
        for (int i = snakeLength; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Update the head's position based on the current direction
        switch (direction) {
            case 'U': y[0] -= TILE_SIZE; break; // move up
            case 'D': y[0] += TILE_SIZE; break; // move down
            case 'L': x[0] -= TILE_SIZE; break; // move left
            case 'R': x[0] += TILE_SIZE; break; // move right
        }
    }
    
   // Method to check if the snake has eaten the food
    private void checkFood() {
        // If the snake's head is at the food location
        if (x[0] == foodX && y[0] == foodY) {
            snakeLength++;  // Increase the snake's length
            score += 10; // Increase the score by 10
            spawnFood(); // Spawn new food
        }
    }
    
    // Method to check for collisions (with the snake's body or the walls)
    private void checkCollisions() {
        // check if the snake's had collides with its body
        for (int i = snakeLength; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false; // end the game if a collision occurs
        }

        // If the game is over, stop the timer and show the "Play Again" button
        if (!running) {
            timer.stop();
            showPlayAgainButton();
        }
    }
    
    // Method to display the game-over message
    private void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String msg = "Game Over! Score: " + score;
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2); // Center the game-over message
    }
    

        // Method to show buttons to play again or exit after game over
        private void showPlayAgainButton() {
            if (playAgainButton != null) {
                // Revalidate the panel to ensure components are placed correctly
                this.remove(playAgainButton);
            }
        
            JButton playAgainButton = new JButton("Play Again");
            playAgainButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 + 50, 150, 50);
            playAgainButton.addActionListener(e -> restartGame()); // restart the game when clicked
            
            // Create the "Exit" button
            JButton exitButton = new JButton("Exit");
            exitButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 + 110, 150, 50);
            exitButton.addActionListener(e -> System.exit(0)); // Exit the game when clicked
        
            this.setLayout(null); // set the layout to null or absolute positioning
            this.add(playAgainButton); // add the buttons to the panel
            this.add(exitButton);
            this.revalidate(); // Revalidate the panel to ensure components are placed correctly
            this.repaint(); // Repaint the panel to show the buttons
        }
 
     // Method to restart the game by resetting all state and starting a new game
     private void restartGame() {
        this.removeAll();  // Remove all components including buttons
        this.revalidate();
        this.repaint();
        startGame(); // Start a new game
    }
    
    
    // Method called on every timer tick to update the game
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move(); // Move the snake
            checkFood(); // Check if the snake has eaten food
            checkCollisions(); // Check for collisions
        }

        // Repaint the game area
        repaint();
    }
    
    // Key handler to change the snake's direction based on user input
    private class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L'; // Prevent reverse direction
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R'; // Prevent reverse direction
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U'; // Prevent reverse direction
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D'; // Prevent reverse direction
                    break;
            }
        }
    }
    
    // Main method to set up the game window and start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the window when exiting
        frame.setVisible(true); // Make the game window visible
    }
}
