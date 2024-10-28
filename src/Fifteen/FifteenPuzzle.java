package Fifteen;


import javax.swing.*;
import java.awt.*;

// A classic 15-puzzle game implementation.
public class FifteenPuzzle extends JFrame {
    private static final int GRID_SIZE = 4;
    private static final int TILE_SIZE = 100;
    private final JButton[][] tiles;
    private int emptyRow;
    private int emptyCol;

    public FifteenPuzzle() {
        setTitle("Fifteen Puzzle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //Initialize game board
        JPanel boardPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        tiles = new JButton[GRID_SIZE][GRID_SIZE];
    }
    public static void main(String[] args) {
        System.out.println();
    }
}