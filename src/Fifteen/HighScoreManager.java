package Fifteen;

import java.util.*;
import java.io.*;

public class HighScoreManager {
    private static final String HIGHSCORE_FILE = "highscores.txt";
    private List<Player> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    public void addScore(Player player) {
        highScores.add(player);
        Collections.sort(highScores);
        if (highScores.size() > 10) {
            highScores = highScores.subList(0, 10);
        }
        saveHighScores();
    }

    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                highScores.add(new Player(parts[0], Integer.parseInt(parts[1]), Long.parseLong(parts[2])));
            }
        } catch (IOException e) {
            System.out.println("No existing high scores found.");
        }
    }

    private void saveHighScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HIGHSCORE_FILE))) {
            for (Player player : highScores) {
                writer.println(String.format("%s,%d,%d",
                        player.getName(), player.getMoves(), player.getTimeInSeconds()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Player> getHighScores() {
        return new ArrayList<>(highScores);
    }
}
