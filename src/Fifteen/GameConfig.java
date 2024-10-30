package Fifteen;

import java.awt.*;

public class GameConfig {
    private int rows;
    private int cols;
    private Color backgroundColor;
    private Color tileColor;
    private String playerName;

    public GameConfig() {
        this.rows = 4;
        this.cols = 4;
        this.backgroundColor = Color.WHITE;
        this.tileColor = Color.LIGHT_GRAY;
        this.playerName = "Anonymous";
    }

    // Getters and setters
    public int getRows() { return rows; }
    public void setRows(int rows) { this.rows = rows; }
    public int getCols() { return cols; }
    public void setCols(int cols) { this.cols = cols; }
    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
    public Color getTileColor() { return tileColor; }
    public void setTileColor(Color tileColor) { this.tileColor = tileColor; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
}
