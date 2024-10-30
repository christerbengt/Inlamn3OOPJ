package Fifteen;

public class Player implements Comparable<Player> {
    private String name;
    private int moves;
    private long timeInSeconds;

    public Player(String name, int moves, long timeInSeconds) {
        this.name = name;
        this.moves = moves;
        this.timeInSeconds = timeInSeconds;
    }

    public String getName() { return name; }
    public int getMoves() { return moves; }
    public long getTimeInSeconds() { return timeInSeconds; }

    @Override
    public int compareTo(Player other) {
        return Long.compare(this.timeInSeconds, other.timeInSeconds);
    }

    @Override
    public String toString() {
        return String.format("%s - Moves: %d, Time: %d seconds", name, moves, timeInSeconds);
    }
}
