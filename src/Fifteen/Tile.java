package Fifteen;

import javax.swing.*;
import java.awt.*;

class Tile extends JButton {
    private int value;

    public Tile(int value) {
        this.value = value;
        setText(String.valueOf(value));
        setFont(new Font("Arial", Font.BOLD, 20));
        setFocusPainted(false);
    }

    public int getValue() {
        return value;
    }
}
