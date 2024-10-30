package Fifteen;

import java.io.Serializable;

// Score class for high scores
class Score implements Serializable {
    String playerName;
    int moves;
    long timeSeconds;

    // Constructor, sets score details
    public Score(String playerName, int moves, long timeSeconds) {
        this.playerName = playerName;
        this.moves = moves;
        this.timeSeconds = timeSeconds;
    }
}
