package Fifteen;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

// A classic 15-puzzle game implementation.
public class FifteenPuzzle extends JFrame {
    private static final int GRID_SIZE = 4;
    private static final int TILE_SIZE = 100;
    private final JButton[][] tiles;
    private int emptyRow;
    private int emptyCol;

    public FifteenPuzzle() {
        setTitle("Fifteen Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //Initialize game board
        JPanel boardPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        tiles = new JButton[GRID_SIZE][GRID_SIZE];

        // Create tiles and add action listeners
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                JButton tile = new JButton();
                tile.setFont(new Font("Arial", Font.BOLD, 24));
                tile.setFocusPainted(false);

                final int currentRow = row;
                final int currentCol = col;
                tile.addActionListener(e -> handleTileClick(currentRow, currentCol));

                tiles[row][col] = tile;
                boardPanel.add(tile);
            }
        }
        // Create New Game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> startNewGame());

        // Layout setup
        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(newGameButton, BorderLayout.SOUTH);

        // Set window size
        setSize(GRID_SIZE * TILE_SIZE + 16, GRID_SIZE * TILE_SIZE + 62);
        setLocationRelativeTo(null);

        startNewGame();
    }

    /**
     * Starts a new game by shuffling the tiles
     */
    private void startNewGame() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < GRID_SIZE * GRID_SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (row == GRID_SIZE - 1 && col == GRID_SIZE - 1) {
                    tiles[row][col].setText("");
                    emptyRow = row;
                    emptyCol = col;
                } else {
                    tiles[row][col].setText(String.valueOf(numbers.get(index++)));
                }
            }
        }
    }

    /**
     * Handles tile clicks by checking if the move is valid and performing the move
     */
    private void handleTileClick(int row, int col) {
        // Check if clicked tile is adjacent to empty space
        if (isAdjacent(row, col, emptyRow, emptyCol)) {
            // Swap tiles
            String temp = tiles[row][col].getText();
            tiles[row][col].setText("");
            tiles[emptyRow][emptyCol].setText(temp);

            // Update empty position
            emptyRow = row;
            emptyCol = col;

            // Check if puzzle is solved
            if (isPuzzleSolved()) {
                JOptionPane.showMessageDialog(this, "Grattis, du vann!");
            }
        }
    }

    /**
     * Checks if two positions are adjacent
     */
    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        return (Math.abs(row1 - row2) == 1 && col1 == col2) ||
                (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    /**
     * Checks if the puzzle is solved by verifying that all tiles are in order
     */
    private boolean isPuzzleSolved() {
        int expectedNumber = 1;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                String tileText = tiles[row][col].getText();
                if (row == GRID_SIZE - 1 && col == GRID_SIZE - 1) {
                    return tileText.isEmpty();
                }
                if (tileText.isEmpty() || Integer.parseInt(tileText) != expectedNumber) {
                    return false;
                }
                expectedNumber++;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FifteenPuzzle puzzle = new FifteenPuzzle();
            puzzle.setVisible(true);
        });
    }
}