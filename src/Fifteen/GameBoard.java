package Fifteen;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Game board class
class GameBoard extends JPanel {
    private Tile[][] tiles;
    private Point emptyTile;
    private int rows, cols;
    private Color backgroundColor = Color.WHITE;
    private Color tileColor = new Color(200, 200, 255);
    private TileClickListener clickListener;

    // Constructor, initializes the board
    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        setLayout(new GridLayout(rows, cols, 2, 2));
        initializeTiles();
    }

    private void initializeTiles() {
        tiles = new Tile[rows][cols];
        int value = 1;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (value < rows * cols) {
                    tiles[i][j] = new Tile(value++);
                    tiles[i][j].addActionListener(e -> handleTileClick((Tile)e.getSource()));
                } else {
                    tiles[i][j] = null;
                    emptyTile = new Point(i, j);
                }
                add(tiles[i][j] != null ? tiles[i][j] : new JPanel());
            }
        }
    }

    // Shuffles the tiles randomly
    public void shuffleTiles() {
        Random rand = new Random();
        int shuffles = rows * cols * 100;

        for (int i = 0; i < shuffles; i++) {
            java.util.List<Point> possibleMoves = getPossibleMoves();
            Point move = possibleMoves.get(rand.nextInt(possibleMoves.size()));
            swapTiles(move.x, move.y);
        }
    }

    // Finds all valid moves in a current position. Used during shuffling to ensure the puzzle is possible to solve.
    // Inspired and helped by claude.ai
    private java.util.List<Point> getPossibleMoves() {
        java.util.List<Point> moves = new ArrayList<>();
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};

        for (int[] dir : directions) {
            int newRow = emptyTile.x + dir[0];
            int newCol = emptyTile.y + dir[1];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                moves.add(new Point(newRow, newCol));
            }
        }

        return moves;
    }

    // Checks if tiles can be moved to the empty square
    public boolean moveTiles(int row, int col) {
        // Check if tiles are in line with empty tile
        if (row == emptyTile.x || col == emptyTile.y) {
            // Calculate all tiles that need to move
            List<Point> tilesToMove = new ArrayList<>();
            if (row == emptyTile.x) {
                int start = Math.min(col, emptyTile.y);
                int end = Math.max(col, emptyTile.y);
                for (int c = start; c <= end; c++) {
                    if (c != emptyTile.y) {
                        tilesToMove.add(new Point(row, c));
                    }
                }
            } else {
                int start = Math.min(row, emptyTile.x);
                int end = Math.max(row, emptyTile.x);
                for (int r = start; r <= end; r++) {
                    if (r != emptyTile.x) {
                        tilesToMove.add(new Point(r, col));
                    }
                }
            }

            // Move all tiles
            for (Point p : tilesToMove) {
                swapTiles(p.x, p.y);
            }
            return true;
        }
        return false;
    }

    // Shifts tiles on the board
    private void swapTiles(int row, int col) {
        tiles[emptyTile.x][emptyTile.y] = tiles[row][col];
        tiles[row][col] = null;
        removeAll();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                add(tiles[i][j] != null ? tiles[i][j] : new JPanel());
            }
        }

        emptyTile = new Point(row, col);
        revalidate();
        repaint();
    }

    // Checks if the game has been won
    public boolean isGameWon() {
        int value = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tiles[i][j] != null) {
                    if (tiles[i][j].getValue() != value++) {
                        return false;
                    }
                } else if (value != rows * cols) {
                    return false;
                }
            }
        }
        return true;
    }

    // Sets background colour of the board
    public void setBackgroundColor(Color color) {
        backgroundColor = color;
        setBackground(color);
        repaint();
    }

    // Sets the colour of the tiles
    public void setTileColor(Color color) {
        tileColor = color;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null) {
                    tile.setBackground(color);
                }
            }
        }
    }

    // Gets current tile colour
    public Color getTileColor() {
        return tileColor;
    }

    // Adds listener for tile click events
    public void addTileClickListener(TileClickListener listener) {
        this.clickListener = listener;
    }

    // Handles tile click event
    private void handleTileClick(Tile tile) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tiles[i][j] == tile) {
                    if (clickListener != null) {
                        clickListener.onTileClick(i, j);
                    }
                    return;
                }
            }
        }
    }
}
