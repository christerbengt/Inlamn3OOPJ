package Fifteen;

import javax.swing.*;
import java.awt.*;

class Tile extends JButton {
    private int value;

    // Constructor, sets the tile value and appearance
    public Tile(int value) {
        this.value = value;
        setText(String.valueOf(value));
        setFont(new Font("Arial", Font.BOLD, 20));
        setFocusPainted(false);
    }

    // Returns tile value
    public int getValue() {
        return value;
    }
}
