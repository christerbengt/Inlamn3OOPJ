package Fifteen;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class HighScoreManager {
    private static final String SCORES_FILE = "highscores.ser";
    private List<Score> scores;

    public HighScoreManager() {
        loadScores();
    }

    @SuppressWarnings("unchecked")
    private void loadScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORES_FILE))) {
            scores = (List<Score>) ois.readObject();
        } catch (Exception e) {
            scores = new ArrayList<>();
        }
    }

    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORES_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addScore(Score score) {
        scores.add(score);
        scores.sort((a, b) -> (int)(a.timeSeconds - b.timeSeconds));
        saveScores();
    }

    public List<Score> getTopScores(int limit) {
        return scores.stream()
                .sorted((a, b) -> (int)(a.timeSeconds - b.timeSeconds))
                .limit(limit)
                .toList();
    }
}
