package com.egragames.Minesweeper;

interface GameActable {
    void loseGame(int x, int y);
    void winGame();
    void openCell(int x, int y, int val);
    void refuseHint();
}