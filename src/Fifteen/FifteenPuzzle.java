package Fifteen;


import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.border.*;

public class FifteenPuzzle extends JFrame {
    private static final int TILE_SIZE = 100;
    private final JButton[][] tiles;
    private int emptyRow;
    private int emptyCol;
    private final GameConfig config;
    private final HighScoreManager highScoreManager;
    private int moveCount;
    private long startTime;
    private boolean gameInProgress;
    private JLabel statusLabel;

    public FifteenPuzzle() {
        config = new GameConfig();
        highScoreManager = new HighScoreManager();
        tiles = new JButton[config.getRows()][config.getCols()];

        setupMenu();
        setupUI();
        startNewGame();
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Game menu
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem settings = new JMenuItem("Settings");
        JMenuItem highScores = new JMenuItem("High Scores");

        newGame.addActionListener(e -> startNewGame());
        settings.addActionListener(e -> showSettingsDialog());
        highScores.addActionListener(e -> showHighScores());

        gameMenu.add(newGame);
        gameMenu.add(settings);
        gameMenu.add(highScores);
        menuBar.add(gameMenu);

        setJMenuBar(menuBar);
    }

    private void setupUI() {
        setTitle("15 Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel boardPanel = new JPanel(new GridLayout(config.getRows(), config.getCols()));

        // Create tiles
        for (int row = 0; row < config.getRows(); row++) {
            for (int col = 0; col < config.getCols(); col++) {
                JButton tile = new JButton();
                tile.setFont(new Font("Arial", Font.BOLD, 24));
                tile.setFocusPainted(false);
                tile.setBackground(config.getTileColor());

                final int currentRow = row;
                final int currentCol = col;
                tile.addActionListener(e -> handleTileClick(currentRow, currentCol));

                tiles[row][col] = tile;
                boardPanel.add(tile);
            }
        }

        // Status panel
        statusLabel = new JLabel("Moves: 0 | Time: 0s");
        statusLabel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Add components
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        add(mainPanel);

        // Set size
        pack();
        setLocationRelativeTo(null);
    }

    private void showSettingsDialog() {
        JDialog dialog = new JDialog(this, "Settings", true);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        // Grid size settings
        dialog.add(new JLabel("Rows:"));
        JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(config.getRows(), 2, 10, 1));
        dialog.add(rowSpinner);

        dialog.add(new JLabel("Columns:"));
        JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(config.getCols(), 2, 10, 1));
        dialog.add(colSpinner);

        // Color settings
        dialog.add(new JLabel("Background Color:"));
        JButton bgColorButton = new JButton("Choose");
        bgColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(dialog, "Choose Background Color",
                    config.getBackgroundColor());
            if (newColor != null) {
                config.setBackgroundColor(newColor);
            }
        });
        dialog.add(bgColorButton);

        dialog.add(new JLabel("Tile Color:"));
        JButton tileColorButton = new JButton("Choose");
        tileColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(dialog, "Choose Tile Color",
                    config.getTileColor());
            if (newColor != null) {
                config.setTileColor(newColor);
            }
        });
        dialog.add(tileColorButton);

        // Player name
        dialog.add(new JLabel("Player Name:"));
        JTextField nameField = new JTextField(config.getPlayerName());
        dialog.add(nameField);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            config.setRows((Integer) rowSpinner.getValue());
            config.setCols((Integer) colSpinner.getValue());
            config.setPlayerName(nameField.getText());

            // Recreate the game board with new settings
            dispose();
            new FifteenPuzzle().setVisible(true);
            dialog.dispose();
        });
        dialog.add(saveButton);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showHighScores() {
        JDialog dialog = new JDialog(this, "High Scores", true);
        dialog.setLayout(new BorderLayout());

        JPanel scoresPanel = new JPanel();
        scoresPanel.setLayout(new BoxLayout(scoresPanel, BoxLayout.Y_AXIS));

        List<Player> highScores = highScoreManager.getHighScores();
        for (Player player : highScores) {
            scoresPanel.add(new JLabel(player.toString()));
        }

        dialog.add(new JScrollPane(scoresPanel), BorderLayout.CENTER);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void startNewGame() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < config.getRows() * config.getCols(); i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int index = 0;
        for (int row = 0; row < config.getRows(); row++) {
            for (int col = 0; col < config.getCols(); col++) {
                if (row == config.getRows() - 1 && col == config.getCols() - 1) {
                    tiles[row][col].setText("");
                    emptyRow = row;
                    emptyCol = col;
                } else {
                    tiles[row][col].setText(String.valueOf(numbers.get(index++)));
                }
                tiles[row][col].setBackground(config.getTileColor());
            }
        }

        moveCount = 0;
        startTime = System.currentTimeMillis();
        gameInProgress = true;
        updateStatus();

        // Start timer
        Timer timer = new Timer(1000, e -> updateStatus());
        timer.start();
    }

    private void handleTileClick(int row, int col) {
        if (!gameInProgress) return;

        // Check if the clicked tile is in line with the empty space
        if (row == emptyRow || col == emptyCol) {
            // Calculate direction and number of tiles to move
            int rowDir = Integer.compare(emptyRow, row);
            int colDir = Integer.compare(emptyCol, col);

            // Move all tiles between clicked position and empty space
            while (row != emptyRow || col != emptyCol) {
                int nextRow = emptyRow - rowDir;
                int nextCol = emptyCol - colDir;

                // Swap tiles
                tiles[emptyRow][emptyCol].setText(tiles[nextRow][nextCol].getText());
                tiles[nextRow][nextCol].setText("");

                emptyRow = nextRow;
                emptyCol = nextCol;
            }

            moveCount++;
            updateStatus();

            if (isPuzzleSolved()) {
                gameInProgress = false;
                long timeInSeconds = (System.currentTimeMillis() - startTime) / 1000;
                Player player = new Player(config.getPlayerName(), moveCount, timeInSeconds);
                highScoreManager.addScore(player);
                JOptionPane.showMessageDialog(this,
                        String.format("Grattis, du vann!\nMoves: %d\nTime: %d seconds",
                                moveCount, timeInSeconds));
            }
        }
    }

    private void updateStatus() {
        if (gameInProgress) {
            long currentTime = (System.currentTimeMillis() - startTime) / 1000;
            statusLabel.setText(String.format("Moves: %d | Time: %ds", moveCount, currentTime));
        }
    }

    private boolean isPuzzleSolved() {
        int expectedNumber = 1;
        for (int row = 0; row < config.getRows(); row++) {
            for (int col = 0; col < config.getCols(); col++) {
                String tileText = tiles[row][col].getText();
                if (row == config.getRows() - 1 && col == config.getCols() - 1) {
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