package com.egragames.Minesweeper;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        int rows = 20, cols = 20, difficulty = 4;
        SwingUtilities.invokeLater(() -> {
            MineFrame mf = new MineFrame();
            mf.startGame(rows, cols, difficulty);
        });
    }
}