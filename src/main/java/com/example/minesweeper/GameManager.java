package com.example.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Class to manage minesweeper game
 */
public class GameManager {
    // Game parameters
    static int rows = 10;
    static int cols = 10;
    static int buttonWidth = 40;
    static int buttonHeight = 40;
    static int windowWidth = rows * buttonHeight;
    static int windowHeight = rows * buttonWidth + 40;
    static int numMines = 9;

    // Components
    JPanel gameDisplay;
    JMenuBar menuBar;
    JPanel infoPanel;
    Stopwatch timerPanel;
    MineCounter minePanel;
    JPanel gamePanel;
    NewGameWindow popupWindow;
    DifficultyWindow diffWindow;

    // State tracking
    Cell[][] cells;
    int cellsRemaining;
    Set<Integer> mines = new HashSet<>();
    boolean timerStarted = false;

    // Reveal cell logic: here for optimization
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> seen = new HashSet<>();

    // Random
    Random rand = new Random();

    /**
     * Constructor
     */
    public GameManager() {
        initComponents();
        initMenuBar();
        initMines();
        initInfoPanel();
        initGamePanel();
        initUI();
        finalizeComponents();
    } // GameManager

    /**
     * Add all relevant components to the main display
     */
    private void finalizeComponents() {
        gameDisplay.add(infoPanel, BorderLayout.NORTH);
        gameDisplay.add(gamePanel, BorderLayout.CENTER);
        gameDisplay.revalidate();
        gameDisplay.repaint();
    } // finalizeComponents

    /**
     * Initialize all Swing components
     */
    private void initComponents() {
        gameDisplay = new JPanel();
        gameDisplay.setLayout(new BorderLayout());
        gamePanel = new JPanel();
        menuBar = new JMenuBar();
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 2));

        popupWindow = new NewGameWindow();
        diffWindow = new DifficultyWindow();
    }

    /**
     * Set up the menu bar with options
     */
    private void initMenuBar() {
        JMenu optionsTab = new JMenu("Options");
        JMenuItem diffButton = new JMenuItem("Difficulty");
        JMenuItem resetButton = new JMenuItem("Reset");
        diffButton.addActionListener(e -> changeDifficulty());
        resetButton.addActionListener(e -> reset());

        optionsTab.add(diffButton);
        optionsTab.add(resetButton);
        menuBar.add(optionsTab);
    } // initMenuBar

    /**
     * Initialize the info bar with components (stopwatch and mines remaining counter)
     */
    private void initInfoPanel() {
        minePanel = new MineCounter(numMines);
        timerPanel = new Stopwatch();
        infoPanel.add(timerPanel);
        infoPanel.add(minePanel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    } // initInfoPanel

    /**
     * Initialize mine placement and store in {@code mines}
     */
    private void initMines() {
        mines.clear();
        this.rand.ints(0, rows * cols).distinct().limit(numMines).forEach(mines::add);
    } // initMines

    /**
     * Initialize all cells in the grid
     * <p>
     *     Create {@code Cell} types with proper listeners and values
     * </p>
     */
    private void initCells() {
        cells = new Cell[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int finalI = i, finalJ = j;
                Cell newCell = new Cell(i, j, getCellValue(i, j));
                newCell.addActionListener(e -> handleCellClick(finalI, finalJ));
                newCell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            switch(newCell.toggleFlag()) {
                                case -1:
                                    minePanel.decrement();
                                    break;
                                case 1:
                                    minePanel.increment();
                                    break;
                            }
                        }
                    }
                });

                cells[i][j] = newCell;
                gamePanel.add(newCell);
            } // for
        } // for
    } // initCells

    /**
     * Calculate the number of mines adjacent to a cell
     * @param i the row index of the cell
     * @param j the column index of the cell
     * @return the number of mines adjacent to the cell, or -1 if the cell itself is a mine
     */
    private int getCellValue(int i, int j) {
        int pos = i * cols + j;
        if (mines.contains(pos)) return -1;

        int count = 0;
        for (int ii = Math.max(-1, -i); ii <= Math.min(1, rows - i - 1); ii++) {
            for (int jj = Math.max(-1, -j); jj <= Math.min(1, cols - j - 1); jj++) {
                if (mines.contains(pos + ii * cols + jj)) count++;
            } // for
        } // for
        return count;
    } // getCellValue

    /**
     * Initialize the main game space
     * <p>
     *     Initialize cells, and add keyboard support for resetting
     * </p>
     */
    private void initGamePanel() {
        gamePanel.setLayout(new GridLayout(rows, cols));
        initCells();
        cellsRemaining = rows * cols - numMines;
        // Key listeners
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "press r");
        gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released R"), "release r");
        gamePanel.getActionMap().put("press r", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "none");
                reset();
            } // actionPerformed
        }); // getActionMap
        gamePanel.getActionMap().put("release r", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "press r");
            } // actionPerformed
        }); // getActionMap

        gamePanel.repaint();
        gamePanel.validate();
    } // initGamePanel

    /**
     * Set UI options for certain button colors
     */
    private void initUI() {
        UIManager.put("Button.disabledText", Color.BLACK);
        UIManager.put("Button.select", Color.LIGHT_GRAY);
    } // initUI

    /**
     * Determine behavior upon left-clicking a cell
     * @param i the row index of the cell
     * @param j the column index of the cell
     */
    private void handleCellClick(int i, int j) {
        if (!timerStarted) {
            timerPanel.start();
            timerStarted = true;
        } // if

        revealCells(i, j);

        // Game Over handling
        if (cells[i][j].getValue() == -1 || cellsRemaining == 0) {
            if (cells[i][j].getValue() == -1) {
                this.popupWindow.setMessage("You died. Try again?");
            } else if (cellsRemaining == 0) {
                this.popupWindow.setMessage("You won in " + timerPanel.getTime() + " seconds!\nStart a new game?");
            } // else

            timerPanel.stop();
            disableAll();

            int status = this.popupWindow.getPopupResponse();
            if (status == 0) reset();
        } // if
    } // handleCellClick

    /**
     * Reveal cells based on a clicked cell
     * <p>
     *     Reveal a cell when clicked. If the cell has value 0, then reveal all guaranteed-safe cells
     * </p>
     * @param startI the row index of the cell
     * @param startJ the column index of the cell
     */
    private void revealCells(int startI, int startJ) {
        queue.offer(startI * cols + startJ);
        seen.add(startI * cols + startJ);

        int curr, i, j;
        while (!queue.isEmpty()) {
            curr = queue.poll();
            i = curr / cols;
            j = curr % cols;

            cells[i][j].revealCell();
            cellsRemaining--;
            if (cells[i][j].getValue() == 0) {
                for (int ii = Math.max(0, i - 1); ii < Math.min(rows, i + 2); ii++) {
                    for (int jj = Math.max(0, j - 1); jj < Math.min(cols, j + 2); jj++) {
                        if ((i != ii || j != jj) && !seen.contains(ii * cols + jj)) {
                            queue.offer(ii * cols + jj);
                            seen.add(ii * cols +jj);
                        } // if
                    } // for
                } // for
            } // if
        } // while
    } // revealCells

    /**
     * Disable all cells
     */
    private void disableAll() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                cells[i][j].freeze();
    } // disableAll

    /**
     * Open difficulty-change popup window, and handle a change in difficulty
     */
    private void changeDifficulty() {
        Difficulty status = diffWindow.showPopup();
        if (status == Difficulty.CANCEL) return;

        switch (status) {
            case Difficulty.EASY:
                rows = 10;
                cols = 10;
                numMines = 9;
                buttonHeight = 40;
                buttonWidth = 40;
                break;
            case Difficulty.MEDIUM:
                rows = 15;
                cols = 20;
                numMines = 49;
                buttonHeight = 30;
                buttonWidth = 30;
                break;
            case Difficulty.HARD:
                rows = 20;
                cols = 40;
                numMines = 99;
                buttonHeight = 20;
                buttonWidth = 20;
                break;
        } // switch
        reset();
    } // changeDifficulty

    /**
     * Reset the game
     */
    private void reset() {
        this.seen.clear();
        gamePanel.removeAll();
        timerPanel.reset();
        timerStarted = false;
        minePanel.reset(numMines);

        initMines();
        initGamePanel();

        windowWidth = rows * buttonHeight;
        windowHeight = rows * buttonWidth + 40;
        gamePanel.getParent().setSize(windowWidth, windowHeight);
        gamePanel.repaint();
        gamePanel.revalidate();
    } // reset
} // class GameManager
