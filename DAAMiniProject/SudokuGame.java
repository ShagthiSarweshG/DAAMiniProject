import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudokuGame extends JFrame {
    private JTextField[][] cells;
    private JButton newGameButton;
    private JButton solveButton;
    private JButton checkButton;
    private JButton hintButton;

    public SudokuGame() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 9));

        cells = new JTextField[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField(2);
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                add(cells[row][col]);
            }
        }

        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateNewPuzzle();
            }
        });
        add(newGameButton);

        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        add(solveButton);

        checkButton = new JButton("Check");
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkSolution();
            }
        });
        add(checkButton);

        hintButton = new JButton("Hint");
        hintButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provideHint();
            }
        });
        add(hintButton);

        pack();
        setVisible(true);
        generateNewPuzzle();
    }

    private void setPuzzle(int[][] puzzle) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[row][col] != 0) {
                    cells[row][col].setText(String.valueOf(puzzle[row][col]));
                    cells[row][col].setEditable(false);
                } else {
                    cells[row][col].setText("");
                    cells[row][col].setEditable(true);
                }
            }
        }
    }

    private void generateNewPuzzle() {
        int[][] puzzle = generateRandomPuzzle();
        setPuzzle(puzzle);
    }

    private int[][] generateRandomPuzzle() {
        int[][] puzzle = new int[9][9];
        solveSudoku(puzzle); // Start with an empty grid and solve it to get a valid puzzle
        int numToRemove = 40 + new Random().nextInt(20); // Vary the number of cells to remove for difficulty
        while (numToRemove > 0) {
            int row = new Random().nextInt(9);
            int col = new Random().nextInt(9);
            if (puzzle[row][col] != 0) {
                puzzle[row][col] = 0;
                numToRemove--;
            }
        }
        return puzzle;
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num ||
                    board[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private void solveSudoku() {
        int[][] puzzle = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                try {
                    puzzle[row][col] = Integer.parseInt(cells[row][col].getText());
                } catch (NumberFormatException e) {
                    puzzle[row][col] = 0;
                }
            }
        }
        if (solveSudoku(puzzle)) {
            setPuzzle(puzzle);
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists for the current puzzle.");
        }
    }

    private void checkSolution() {
        int[][] puzzle = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                try {
                    puzzle[row][col] = Integer.parseInt(cells[row][col].getText());
                } catch (NumberFormatException e) {
                    puzzle[row][col] = 0;
                }
            }
        }
        if (solveSudoku(puzzle)) {
            JOptionPane.showMessageDialog(this, "Congratulations! The solution is correct.");
        } else {
            JOptionPane.showMessageDialog(this, "The solution is incorrect.");
        }
    }

    private void provideHint() {
        int[][] puzzle = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                try {
                    puzzle[row][col] = Integer.parseInt(cells[row][col].getText());
                } catch (NumberFormatException e) {
                    puzzle[row][col] = 0;
                }
            }
        }
        int[][] solution = new int[9][9];
        if (solveSudoku(puzzle)) {
            setPuzzle(puzzle);
        } else {
            JOptionPane.showMessageDialog(this, "No solution exists for the current puzzle.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SudokuGame();
            }
        });
    }
}