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
    JPanel gamePanel;
    JMenuBar menuBar;
    NewGameWindow popupWindow = new NewGameWindow();
    DifficultyWindow diffWindow = new DifficultyWindow();
    JPanel infoPanel = new JPanel();
    Stopwatch timerPanel = new Stopwatch();
    MineCounter minePanel;
    JPanel gameDisplay;

    // State tracking
    Cell[][] cells;
    Set<Integer> mines = new HashSet<>();
    int cellsRemaining;
    boolean timerStarted = false;

    // Reveal cell logic: here for optimization
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> seen = new HashSet<>();

    // Random
    Random rand = new Random();

    public GameManager() {
        gameDisplay = new JPanel();
        gameDisplay.setLayout(new BorderLayout());
        gamePanel = new JPanel();
        menuBar = new JMenuBar();
        initMenuBar();
        initMines();
        initInfoPanel();
        initGamePanel();
        initUI();
        gameDisplay.add(infoPanel, BorderLayout.NORTH);
        gameDisplay.add(gamePanel, BorderLayout.CENTER);
        gameDisplay.revalidate();
        gameDisplay.repaint();
    } // GameManager

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

    private void initInfoPanel() {
        infoPanel.setLayout(new GridLayout(1, 2));
        minePanel = new MineCounter(numMines);
        infoPanel.add(timerPanel);
        infoPanel.add(minePanel);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        infoPanel.setVisible(true);
    } // initInfoPanel

    private void initMines() {
        mines.clear();
        this.rand.ints(0, rows * cols).distinct().limit(numMines).forEach(mines::add);
    } // initMines

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

    private void initUI() {
        UIManager.put("Button.disabledText", Color.BLACK);
        UIManager.put("Button.select", Color.LIGHT_GRAY);
    } // initUI

    private void handleCellClick(int i, int j) {
        if (!timerStarted) {
            timerPanel.start();
            timerStarted = true;
        } // if

        revealCells(i, j);

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

    private void disableAll() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                cells[i][j].setFrozen(true);
    } // disableAll

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

    private void reset() {
        initMines();
        this.seen.clear();
        gamePanel.removeAll();
        initGamePanel();
        timerPanel.reset();
        timerStarted = false;
        minePanel.reset(numMines);

        windowWidth = rows * buttonHeight;
        windowHeight = rows * buttonWidth + 40;
        gamePanel.getParent().setSize(windowWidth, windowHeight);
        gamePanel.repaint();
        gamePanel.revalidate();
    } // reset
} // class GameManager
