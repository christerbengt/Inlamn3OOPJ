package Fifteen;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

// Main game window class
public class FifteenPuzzle extends JFrame {
    private GameBoard gameBoard;
    private JPanel controlPanel;
    private JLabel timerLabel;
    private JLabel movesLabel;
    private Instant startTime;
    private int moves;
    private String currentPlayer;
    private HighScoreManager highScoreManager;
    private javax.swing.Timer timer;

    // Constructor, sets up the main user interface
    public FifteenPuzzle() {
        highScoreManager = new HighScoreManager();
        setupUI();
        initializeGame();
    }
    // Sets up the main game window UI
    private void setupUI() {
        setTitle("15 Puzzle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Control panel setup
        controlPanel = new JPanel();
        JButton newGameButton = new JButton("New Game");
        JButton colorButton = new JButton("Change Colors");
        JButton viewScoresButton = new JButton("View High Scores");
        timerLabel = new JLabel("Time: 0:00");
        movesLabel = new JLabel("Moves: 0");


        // Size selection
        String[] sizes = {"3x3", "4x4", "5x5", "6x6"};
        JComboBox<String> sizeSelector = new JComboBox<>(sizes);

        // Player name input
        JTextField playerNameField = new JTextField(10);
        JButton setPlayerButton = new JButton("Set Player");

        controlPanel.add(newGameButton);
        controlPanel.add(colorButton);
        controlPanel.add(sizeSelector);
        // controlPanel.add(playerNameField);
        controlPanel.add(setPlayerButton);
        controlPanel.add(timerLabel);
        controlPanel.add(movesLabel);
        controlPanel.add(viewScoresButton);

        add(controlPanel, BorderLayout.NORTH);

        // Action listeners
        newGameButton.addActionListener(e -> startNewGame());
        colorButton.addActionListener(e -> changeColors());
        sizeSelector.addActionListener(e -> changeBoardSize(sizeSelector.getSelectedItem().toString()));
        setPlayerButton.addActionListener(e -> setPlayer());
        viewScoresButton.addActionListener(e -> showHighScores());

        // Initialize timer
        timer = new Timer(1000, e -> updateTimer());
    }
    // Initializes a new game
    private void initializeGame() {
        moves = 0;
        gameBoard = new GameBoard(4, 4); // Default 4x4
        add(gameBoard, BorderLayout.CENTER);
        gameBoard.addTileClickListener(this::handleTileClick);
        startNewGame();
        pack();
        setLocationRelativeTo(null);

    }
    // Starts a new game, shuffling the tiles
    private void startNewGame() {
        gameBoard.shuffleTiles();
        moves = 0;
        movesLabel.setText("Moves: 0");
        startTime = Instant.now();
        timer.start();
    }
    // Handles a tile click event
    private void handleTileClick(int row, int col) {
        if (gameBoard.moveTiles(row, col)) {
            moves++;
            movesLabel.setText("Moves: " + moves);

            if (gameBoard.isGameWon()) {
                timer.stop();
                Duration duration = Duration.between(startTime, Instant.now());
                String timeString = String.format("%d:%02d",
                        duration.toMinutes(),
                        duration.toSecondsPart());

                JOptionPane.showMessageDialog(this,
                        "Congratulations, you won!\nTime: " + timeString +
                                "\nMoves: " + moves);

                if (currentPlayer != null) {
                    highScoreManager.addScore(new Score(
                            currentPlayer,
                            moves,
                            duration.toSeconds()));
                    showHighScores();
                }
            }
        }
    }
    // Updates the timer label
    private void updateTimer() {
        timer = new javax.swing.Timer(1000, e -> updateTimer());
        Duration duration = Duration.between(startTime, Instant.now());
        timerLabel.setText(String.format("Time: %d:%02d",
                duration.toMinutes(),
                duration.toSecondsPart()));
    }
    // Allows the user to change background and tile colours
    private void changeColors() {
        Color backgroundColor = JColorChooser.showDialog(
                this, "Choose Background Color", gameBoard.getBackground());
        if (backgroundColor != null) {
            gameBoard.setBackgroundColor(backgroundColor);
        }

        Color tileColor = JColorChooser.showDialog(
                this, "Choose Tile Color", gameBoard.getTileColor());
        if (tileColor != null) {
            gameBoard.setTileColor(tileColor);
        }
    }
    // Allows the user to change the board size
    private void changeBoardSize(String sizeString) {
        int size = Integer.parseInt(sizeString.split("x")[0]);
        remove(gameBoard);
        gameBoard = new GameBoard(size, size);
        gameBoard.addTileClickListener(this::handleTileClick);
        add(gameBoard, BorderLayout.CENTER);
        pack();
        startNewGame();
    }
    // Allows the user to set their name
    private void setPlayer() {
        String name = JOptionPane.showInputDialog(this, "Enter your name:");
        if (name != null && !name.trim().isEmpty()) {
            currentPlayer = name.trim();
            JOptionPane.showMessageDialog(this, "Player set to: " + currentPlayer);
        }
    }
    // Shows the high score list
    private void showHighScores() {
        StringBuilder sb = new StringBuilder("High Scores:\n\n");
        List<Score> scores = highScoreManager.getTopScores(10);

        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            sb.append(String.format("%d. %s - Moves: %d, Time: %d:%02d\n",
                    i + 1,
                    score.playerName,
                    score.moves,
                    score.timeSeconds / 60,
                    score.timeSeconds % 60));
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }
    // Main method, the starting point of executing the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FifteenPuzzle().setVisible(true);
        });
    }
}

