import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private boolean isPlayerX = true;
    private boolean playWithAI = false;
    private Random random = new Random();

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        JButton aiButton = new JButton("Play with AI");
        JButton twoPlayerButton = new JButton("Two Players");
        topPanel.add(aiButton);
        topPanel.add(twoPlayerButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                board.add(buttons[i][j]);
            }
        }
        add(board, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        JButton playAgain = new JButton("Play Again");
        JButton exit = new JButton("Exit");
        bottomPanel.add(playAgain);
        bottomPanel.add(exit);
        add(bottomPanel, BorderLayout.SOUTH);
        
        aiButton.addActionListener(e -> playWithAI = true);
        twoPlayerButton.addActionListener(e -> playWithAI = false);
        playAgain.addActionListener(e -> resetGame());
        exit.addActionListener(e -> System.exit(0));
    }

    private class ButtonClickListener implements ActionListener {
        private int row, col;
        
        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!buttons[row][col].getText().equals("")) return;

            buttons[row][col].setText(isPlayerX ? "X" : "O");
            if (checkWin()) {
                JOptionPane.showMessageDialog(TicTacToe.this, (isPlayerX ? "X" : "O") + " Wins!");
                disableButtons();
                return;
            }

            if (isBoardFull()) {
                JOptionPane.showMessageDialog(TicTacToe.this, "It's a Draw!");
                disableButtons();
                return;
            }

            isPlayerX = !isPlayerX;

            if (playWithAI && !isPlayerX) {
                aiMove();
            }
        }
    }

    private void aiMove() {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().equals(""));

        buttons[row][col].setText("O");
        if (checkWin()) {
            JOptionPane.showMessageDialog(this, "O Wins!");
            disableButtons();
        }
        if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a Draw!");
            disableButtons();
        }
        isPlayerX = true;
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                !buttons[i][0].getText().equals("")) {
                return true;
            }
            if (buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                buttons[1][i].getText().equals(buttons[2][i].getText()) &&
                !buttons[0][i].getText().equals("")) {
                return true;
            }
        }
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][2].getText()) &&
            !buttons[0][0].getText().equals("")) {
            return true;
        }
        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
            buttons[1][1].getText().equals(buttons[2][0].getText()) &&
            !buttons[0][2].getText().equals("")) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void disableButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        isPlayerX = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe().setVisible(true));
    }
}
