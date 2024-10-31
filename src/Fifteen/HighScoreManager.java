package Fifteen;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class HighScoreManager {
    private static final String SCORES_FILE = "highscores.txt";
    private List<Score> scores;

    // Constructor, loads high scores from a file
    public HighScoreManager() {
        loadScores();
    }

    // Loads high scores from a file
    @SuppressWarnings("unchecked")
    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORES_FILE))) {
            scores = (List<Score>) ois.readObject();
        } catch (Exception e) {
            scores = new ArrayList<>();
        }
    }

    // Saves high scores to a file
    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORES_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adds new high score
    public void addScore(Score score) {
        scores.add(score);
        scores.sort((a, b) -> (int)(a.timeSeconds - b.timeSeconds));
        saveScores();
    }

    // Gets the top high scores
    public List<Score> getTopScores(int limit) {
        return scores.stream()
                .sorted((a, b) -> (int)(a.timeSeconds - b.timeSeconds))
                .limit(limit)
                .toList();
    }
}
