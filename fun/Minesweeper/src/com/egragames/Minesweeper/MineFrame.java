package com.egragames.Minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ResourceBundle;

public class MineFrame extends JFrame implements GameActable {
    
    
    /*==========================Fields==========================*/
    
    
    /* General stuff */
    
    private MineGame game;
    private static ResourceBundle strings = ResourceBundle.getBundle("StringResources");

    /* Frame Components */

    private JLabel jlbMinesLeft;
    private JButton
            cellButtons[][],
            jbtHint;

    /* Frame Sizes and Offsets */

    private int
            cellSize = 30,
            topPanelHeight = 37;
    private Dimension fieldGaps = new Dimension(1, 1);
    private Border borderTopPanel = BorderFactory.createEmptyBorder(5, 14, 0, 14);


    /* Fonts */

    private Font
            jlbMinesLeftFont = new Font("Arial", Font.BOLD, 15),
            jbtFont = new Font("Arial", Font.PLAIN, 15);

    /* Colors */

    private Color
            jlbMinesLeftFontColor = new Color(255, 0, 0),
            jbtFontColor = new Color(0, 0, 0),
            frameBackgroundColor = new Color(255, 255, 255);

    /* Images */

    private ImageIcon
            imgNumbers[] = new ImageIcon[9],
            imgQuestion,
            imgMine,
            imgFlag;

    /* Mode limits */

    private int
            minRows = 20,
            maxRows = 30,
            minCols = 20,
            maxCols = 50,
            minDifficulty = 1,
            maxDifficulty = 8;
    
    
    /*=======================Constructors=======================*/
    

    MineFrame(){
        super(strings.getString("heading"));
        try{
            loadImages();
        } catch(IOException | IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, strings.getString("exceptionLoadGraphics"), strings.getString("titleException"), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    
    /*======================Private Methods======================*/


    private void loadImages() throws IOException{
        Image tmp = ImageIO.read(getClass().getResource("/com/egragames/Minesweeper/graphics/question.png"));
        imgQuestion = new ImageIcon(tmp.getScaledInstance(cellSize-3, cellSize-3, java.awt.Image.SCALE_SMOOTH));
        tmp = ImageIO.read(getClass().getResource("/com/egragames/Minesweeper/graphics/flag.png"));
        imgFlag = new ImageIcon(tmp.getScaledInstance(cellSize-3, cellSize-3, java.awt.Image.SCALE_SMOOTH));
        tmp = ImageIO.read(getClass().getResource("/com/egragames/Minesweeper/graphics/mine.png"));
        imgMine = new ImageIcon(tmp.getScaledInstance(cellSize-3, cellSize-3, java.awt.Image.SCALE_SMOOTH));

        for (int i=0; i<=8; i++){
            tmp = ImageIO.read(getClass().getResource("/com/egragames/Minesweeper/graphics/" + String.valueOf(i) + ".png"));
            imgNumbers[i] = new ImageIcon(tmp.getScaledInstance(cellSize-3, cellSize-3, java.awt.Image.SCALE_SMOOTH));
        }
    }
    
    private void buildFrame(){
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize();
        Point framePlacement = new Point();
        int fieldWidth = game.getCols()*cellSize, fieldHeight = game.getRows()*cellSize;
        framePlacement.setLocation((screenRes.width-fieldWidth)/2, (screenRes.height-fieldHeight-topPanelHeight)/2);

        setBounds(framePlacement.x, framePlacement.y, fieldWidth, fieldHeight+topPanelHeight);
        setBackground(frameBackgroundColor);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        buildComponents();
        setVisible(true);
    }

    private void buildComponents(){
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(frameBackgroundColor);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.setBorder(borderTopPanel);
        topPanel.setPreferredSize(new Dimension(getWidth(), topPanelHeight));
        topPanel.setBackground(frameBackgroundColor);

        JPanel fieldPanel = new JPanel(new GridLayout(game.getRows(), game.getCols(), fieldGaps.width, fieldGaps.height));
        fieldPanel.setBackground(frameBackgroundColor);

        JButton jbtRestart = new JButton(strings.getString("titleRestart"));
        jbtRestart.setFocusPainted(false);
        jbtRestart.setForeground(jbtFontColor);
        jbtRestart.setFont(jbtFont);
        jbtRestart.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)){
                    int choice = JOptionPane.showOptionDialog(null, strings.getString("messageRestart"), strings.getString("titleRestart"),
                            JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{strings.getString("optionYes"),
                                    strings.getString("optionNo")}, strings.getString("optionYes"));
                    if (choice == JOptionPane.YES_OPTION)
                        restartGame();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        jbtHint = new JButton(strings.getString("titleHint"));
        jbtHint.setFocusPainted(false);
        jbtHint.setForeground(jbtFontColor);
        jbtHint.setFont(jbtFont);
        jbtHint.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (!jbtHint.isEnabled()) return;
                    jbtHint.setEnabled(false);
                    game.getHint();
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        JButton jbtChangeMode = new JButton(strings.getString("titleChangeMode"));
        jbtChangeMode.setFocusPainted(false);
        jbtChangeMode.setForeground(jbtFontColor);
        jbtChangeMode.setFont(jbtFont);
        jbtChangeMode.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JLabel jlbSelRows = new JLabel();
                JLabel jlbSelCols = new JLabel();
                JLabel jlbSelDif = new JLabel();

                JSlider sldRows = new JSlider(SwingConstants.HORIZONTAL, minRows, maxRows, game.getRows());
                sldRows.addChangeListener(el ->
                        jlbSelRows.setText(strings.getString("textSelRows") + sldRows.getValue())
                );
                JSlider sldCols = new JSlider(SwingConstants.HORIZONTAL, minCols, maxCols, game.getCols());
                sldCols.addChangeListener(el ->
                        jlbSelCols.setText(strings.getString("textSelCols") + sldCols.getValue())
                );
                JSlider sldDifficulty = new JSlider(SwingConstants.HORIZONTAL, minDifficulty, maxDifficulty, game.getDifficulty());
                sldDifficulty.addChangeListener(el ->
                        jlbSelDif.setText(strings.getString("textSelDif") + sldDifficulty.getValue())
                );

                jlbSelRows.setText(strings.getString("textSelRows") + sldRows.getValue());
                jlbSelCols.setText(strings.getString("textSelCols") + sldCols.getValue());
                jlbSelDif.setText(strings.getString("textSelDif") + sldDifficulty.getValue());

                final JComponent[] inputs = {
                        jlbSelRows, sldRows,
                        jlbSelCols, sldCols,
                        jlbSelDif, sldDifficulty
                };
                int choice = JOptionPane.showOptionDialog(null, inputs, strings.getString("titleChangeMode"),
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{strings.getString("optionYes"),
                                strings.getString("optionNo")}, strings.getString("optionYes"));
                if (choice == JOptionPane.NO_OPTION) return;
                restartGame(sldRows.getValue(), sldCols.getValue(), sldDifficulty.getValue());
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        jlbMinesLeft = new JLabel(strings.getString("textCellsLeft") + String.valueOf(game.getFreeCellsNumber()));
        jlbMinesLeft.setForeground(jlbMinesLeftFontColor);
        jlbMinesLeft.setFont(jlbMinesLeftFont);

        cellButtons = new JButton[game.getRows()][game.getCols()];
        for(int i=0; i<game.getRows(); i++){
            for(int j=0; j<game.getCols(); j++){
                cellButtons[i][j] = new JButton();
                cellButtons[i][j].setFocusPainted(false);
                cellButtons[i][j].setMargin(new Insets(0, 0, 0, 0));
                int finalI = i, finalJ = j;
                cellButtons[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (game.isOpened(finalJ, finalI)) return;

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (imgFlag.equals(cellButtons[finalI][finalJ].getIcon())) return;
                            if (jbtHint.isEnabled())
                                jbtHint.setEnabled(false);
                            game.pushCell(finalJ, finalI);
                        } else if (SwingUtilities.isRightMouseButton(e)){
                            if (!imgFlag.equals(cellButtons[finalI][finalJ].getIcon()))
                                cellButtons[finalI][finalJ].setIcon(imgFlag);
                            else
                                cellButtons[finalI][finalJ].setIcon(null);
                        } else if (SwingUtilities.isMiddleMouseButton(e)){
                            if (!imgQuestion.equals(cellButtons[finalI][finalJ].getIcon()))
                                cellButtons[finalI][finalJ].setIcon(imgQuestion);
                            else
                                cellButtons[finalI][finalJ].setIcon(null);
                        }
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                });
                fieldPanel.add(cellButtons[i][j]);
            }
        }

        topPanel.add(jbtRestart);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(jbtHint);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(jbtChangeMode);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(jlbMinesLeft);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void restartGame(int rows, int cols, int difficulty){
        dispose();
        getContentPane().removeAll();
        startGame(rows, cols, difficulty);
        setVisible(true);
    }

    private void restartGame(){
        for (int i=0; i<game.getRows(); i++)
            for (int j=0; j<game.getCols(); j++)
                cellButtons[i][j].setIcon(null);
        game = new MineGame(game.getRows(), game.getCols(), game.getDifficulty(), this);
        jlbMinesLeft.setText(strings.getString("textCellsLeft") + String.valueOf(game.getFreeCellsNumber()));
        jbtHint.setEnabled(true);
    }


    /*======================Public Methods======================*/


    void startGame(int rows, int cols, int difficulty){
        game = new MineGame(rows, cols, difficulty, this);
        buildFrame();
    }


    /*=====================Inherited Methods=====================*/


    @Override
    public void loseGame(int x, int y) {
        cellButtons[y][x].setIcon(imgMine);
        int choice = JOptionPane.showOptionDialog(null, strings.getString("messageLose"), strings.getString("titleLose"),
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{strings.getString("optionYes"),
                        strings.getString("optionNo")}, strings.getString("optionYes"));
        if (choice == JOptionPane.YES_OPTION){
            restartGame();
        } else
            System.exit(0);
    }

    @Override
    public void winGame() {
        int choice = JOptionPane.showOptionDialog(null, strings.getString("messageWin"), strings.getString("titleWin"),
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{strings.getString("optionYes"),
                        strings.getString("optionNo")}, strings.getString("optionYes"));
        if (choice == JOptionPane.YES_OPTION){
            restartGame();
        } else
            System.exit(0);
    }

    @Override
    public void openCell(int x, int y, int val) {
        cellButtons[y][x].setIcon(imgNumbers[val]);
        jlbMinesLeft.setText(strings.getString("textCellsLeft") + String.valueOf(game.getFreeCellsNumber()));
    }

    @Override
    public void refuseHint() {
        JOptionPane.showMessageDialog(null, strings.getString("messageHintRefused"),
                strings.getString("titleHint"), JOptionPane.PLAIN_MESSAGE);
    }


}