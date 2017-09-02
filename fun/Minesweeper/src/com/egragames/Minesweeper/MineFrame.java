package com.egragames.Minesweeper;

import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MineFrame extends JFrame implements GameActable {
    
    
    /*==========================Fields==========================*/
    
    
    /* General stuff */
    
    private MineGame game;
    private static ResourceBundle strings = ResourceBundle.getBundle("StringResources");

    /* Frame Components */

    private UILabel jlbMinesLeft;
    private CellButton cellButtons[][];
    private UIButton jbtHint;
    private JPanel fieldPanel;

    /* Frame Sizes and Offsets */

    private int cellSize = 30, topPanelHeight = 37;
    private Dimension fieldGaps = new Dimension(1, 1);
    private Border borderTopPanel = BorderFactory.createEmptyBorder(5, 14, 0, 14);

    /* Colors */

    private Color frameBackgroundColor = new Color(255, 255, 255);

    /* Images */

    private ImageIcon imgNumbers[] = new ImageIcon[9],
            imgQuestion,
            imgMine,
            imgFlag;

    /* Mode limits */

    private int minRows = 20,
            maxRows = 30,
            minCols = 20,
            maxCols = 50,
            minDifficulty = 1,
            maxDifficulty = 8;

    
    /*=======================Constructors=======================*/
    

    private MineFrame(){
        super(strings.getString("heading"));
        try{
            loadImages();
        } catch(IOException | IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, strings.getString("exceptionLoadGraphics"), strings.getString("titleException"), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        game = new MineGame(20, 20, 4, this);
        initFrameAndComponents();
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
    
    private void initFrameAndComponents(){
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize(),
                fieldRes = new Dimension(game.getCols()*cellSize, game.getRows()*cellSize);
        setBounds((screenRes.width-fieldRes.width)/2, (screenRes.height-fieldRes.height)/2, fieldRes.width, fieldRes.height+topPanelHeight);
        setBackground(frameBackgroundColor);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(frameBackgroundColor);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.setBorder(borderTopPanel);
        topPanel.setPreferredSize(new Dimension(getWidth(), topPanelHeight));
        topPanel.setBackground(frameBackgroundColor);

        fieldPanel = new JPanel(new GridLayout(game.getRows(), game.getCols(), fieldGaps.width, fieldGaps.height));
        fieldPanel.setBackground(frameBackgroundColor);

        JButton jbtRestart = new UIButton(strings.getString("titleRestart"));
        jbtRestart.addMouseListener(new RestartButtonMouseListener());

        jbtHint = new UIButton(strings.getString("titleHint"));
        jbtHint.addMouseListener(new HintButtonMouseListener());

        JButton jbtChangeMode = new UIButton(strings.getString("titleChangeMode"));
        jbtChangeMode.addMouseListener(new ChangeModeButtonMouseListener());

        jlbMinesLeft = new UILabel(strings.getString("textCellsLeft") + String.valueOf(game.getFreeCellsNumber()));

        cellButtons = new CellButton[game.getRows()][game.getCols()];
        for(int i=0; i<game.getRows(); i++){
            for(int j=0; j<game.getCols(); j++){
                cellButtons[i][j] = new CellButton();
                cellButtons[i][j].addMouseListener(new CellButtonMouseListener(i, j));
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

        setVisible(true);
    }

    private void restartGame(int rows, int cols, int difficulty){
        dispose();
        fieldPanel.removeAll();
        game = new MineGame(rows, cols, difficulty, this);
        Dimension screenRes = Toolkit.getDefaultToolkit().getScreenSize(),
                fieldRes = new Dimension(game.getCols()*cellSize, game.getRows()*cellSize);
        setBounds((screenRes.width-fieldRes.width)/2, (screenRes.height-fieldRes.height)/2, fieldRes.width, fieldRes.height+topPanelHeight);
        cellButtons = new CellButton[game.getRows()][game.getCols()];
        for(int i=0; i<game.getRows(); i++){
            for(int j=0; j<game.getCols(); j++){
                cellButtons[i][j] = new CellButton();
                cellButtons[i][j].addMouseListener(new CellButtonMouseListener(i, j));
                fieldPanel.add(cellButtons[i][j]);
            }
        }
        jlbMinesLeft.setText(strings.getString("textCellsLeft") + String.valueOf(game.getFreeCellsNumber()));
        jbtHint.setEnabled(true);

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


    public static void main(String[] args){
        SwingUtilities.invokeLater(MineFrame::new);
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


    /*======================Nested Classes======================*/


    private class RestartButtonMouseListener implements MouseListener{
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
    }

    private class HintButtonMouseListener implements MouseListener{
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
    }

    private class ChangeModeButtonMouseListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            JLabel jlbSelRows = new JLabel(),
                    jlbSelCols = new JLabel(),
                    jlbSelDif = new JLabel();

            JSlider sldRows = new JSlider(SwingConstants.HORIZONTAL, minRows, maxRows, game.getRows()),
                    sldCols = new JSlider(SwingConstants.HORIZONTAL, minCols, maxCols, game.getCols()),
                    sldDif = new JSlider(SwingConstants.HORIZONTAL, minDifficulty, maxDifficulty, game.getDifficulty());

            ChangeListener clRows = e1 -> jlbSelRows.setText(strings.getString("textSelRows") + sldRows.getValue()),
                    clCols = el -> jlbSelCols.setText(strings.getString("textSelCols") + sldCols.getValue()),
                    clDif = el -> jlbSelDif.setText(strings.getString("textSelDif") + sldDif.getValue());


            sldRows.addChangeListener(clRows);
            sldCols.addChangeListener(clCols);
            sldDif.addChangeListener(clDif);

            jlbSelRows.setText(strings.getString("textSelRows") + sldRows.getValue());
            jlbSelCols.setText(strings.getString("textSelCols") + sldCols.getValue());
            jlbSelDif.setText(strings.getString("textSelDif") + sldDif.getValue());

            final JComponent[] inputs = {
                    jlbSelRows, sldRows,
                    jlbSelCols, sldCols,
                    jlbSelDif, sldDif
            };
            int choice = JOptionPane.showOptionDialog(null, inputs, strings.getString("titleChangeMode"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{strings.getString("optionYes"),
                            strings.getString("optionNo")}, strings.getString("optionYes"));

            sldRows.removeChangeListener(clRows);
            sldCols.removeChangeListener(clCols);
            sldDif.removeChangeListener(clDif);

            if (choice == JOptionPane.NO_OPTION) return;
            restartGame(sldRows.getValue(), sldCols.getValue(), sldDif.getValue());
        }
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
    }

    private class CellButtonMouseListener implements MouseListener{
        int i, j;
        CellButtonMouseListener(int i, int j){ this.i = i; this.j = j; }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (game.isOpened(j, i)) return;

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (imgFlag.equals(cellButtons[i][j].getIcon())) return;
                if (jbtHint.isEnabled())
                    jbtHint.setEnabled(false);
                game.pushCell(j, i);
            } else if (SwingUtilities.isRightMouseButton(e)){
                if (!imgFlag.equals(cellButtons[i][j].getIcon()))
                    cellButtons[i][j].setIcon(imgFlag);
                else
                    cellButtons[i][j].setIcon(null);
            } else if (SwingUtilities.isMiddleMouseButton(e)){
                if (!imgQuestion.equals(cellButtons[i][j].getIcon()))
                    cellButtons[i][j].setIcon(imgQuestion);
                else
                    cellButtons[i][j].setIcon(null);
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
    }

    private static class UIButton extends JButton{
        private static Font jbtFont = new Font("Arial", Font.PLAIN, 15);
        private static Color jbtFontColor = new Color(0, 0, 0);
        UIButton(String text){
            super(text);
            setFocusPainted(false);
            setForeground(jbtFontColor);
            setFont(jbtFont);
        }
    }

    private static class CellButton extends JButton {
        CellButton(){
            setFocusPainted(false);
            setMargin(new Insets(0, 0, 0, 0));
        }
    }

    private static class UILabel extends JLabel{
        private static Font jlbFont = new Font("Arial", Font.BOLD, 15);
        private static Color jlbFontColor = new Color(255, 0, 0);
        UILabel(String text){
            super(text);
            setForeground(jlbFontColor);
            setFont(jlbFont);
        }
    }


}