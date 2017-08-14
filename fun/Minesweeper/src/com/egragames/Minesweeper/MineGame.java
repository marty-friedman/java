package com.egragames.Minesweeper;

import java.util.Random;

class MineGame {


    /*==========================Fields==========================*/


    private final GameActable UIFRAME;
    private final int
            DIFFICULTY,
            ROWS,
            COLS;
    private int freeCells;
    private int[][] matrix;
    private boolean[][] opened;


    /*=======================Constructors=======================*/


    MineGame(int r, int c, int d, GameActable gui){
        ROWS = r;
        COLS = c;
        freeCells = r*c;
        DIFFICULTY = d;
        UIFRAME = gui;
        matrix = new int[ROWS][COLS];
        opened = new boolean[ROWS][COLS];
        fillMatrix();
    }


    /*======================Private Methods======================*/


    private void fillMatrix(){
        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0; i< ROWS; i++)
            for (int j = 0; j< COLS; j++) {
                if (rand.nextInt(20) <= DIFFICULTY) {
                    matrix[i][j] = -1;
                    freeCells--;
                }
            }

        for (int i = 0; i< ROWS; i++)
            for (int j = 0; j< COLS; j++) {
                if (matrix[i][j] != -1) continue;
                for (int dx = -1; dx <= 1; dx++)
                    for (int dy = -1; dy <= 1; dy++) {
                        if ((dx == 0 && dy == 0) || i + dy < 0 || i + dy >= ROWS || j + dx < 0 || j + dx >= COLS || matrix[i + dy][j + dx] == -1)
                            continue;
                        matrix[i + dy][j + dx]++;
                    }
            }

    }

    private void expand(int x, int y){
        openCell(x, y, matrix[y][x]);
        if (matrix[y][x]==0){
            for(int dy=-1; dy<=1; dy++){
                for(int dx=-1; dx<=1; dx++){
                    if (y+dy<0 || y+dy>= ROWS || x+dx<0 || x+dx>= COLS || matrix[y+dy][x+dx]==-1 || opened[y+dy][x+dx]) continue;
                    expand(x+dx, y+dy);
                }
            }
        }
    }

    private void openCell(int x, int y, int val){
        opened[y][x] = true;
        freeCells--;
        UIFRAME.openCell(x, y, val);
        if (freeCells==0)
            UIFRAME.winGame();
    }


    /*======================Public Methods======================*/


    void pushCell(int x, int y){
        if (matrix[y][x]==-1)
            UIFRAME.loseGame(x, y);
        else if (matrix[y][x]==0)
            expand(x, y);
        else
            openCell(x, y, matrix[y][x]);
    }

    void getHint(){
        Random rand = new Random();
        int x=0, y=0;
        boolean found = false;

        for (int i=0; i<100; i++){
            x = rand.nextInt(COLS);
            y = rand.nextInt(ROWS);
            if (matrix[y][x]==0){
                found = true;
                break;
            }
        }

        if (!found)
            UIFRAME.refuseHint();
        else
            expand(x, y);
    }

    /* Getters */

    int getDifficulty(){ return DIFFICULTY; }
    int getRows(){ return ROWS; }
    int getCols(){ return COLS; }
    int getFreeCellsNumber(){ return freeCells; }
    boolean isOpened(int x, int y){ return opened[y][x]; }

}