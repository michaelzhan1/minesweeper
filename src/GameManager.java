import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.Random;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;


public class GameManager {
    // Constants
    static int rows = 10;
    static int cols = 10;
    static int buttonWidth = 40;
    static int buttonHeight = 40;
    static int windowWidth = rows * buttonHeight;
    static int windowHeight = rows * buttonWidth + 20;
    static int numMines = 9;

    // Components
    JPanel gamePanel;
    NewGameWindow popupWindow = new NewGameWindow("", "Alert");
    JMenuBar menuBar;
    DifficultyWindow diffWindow = new DifficultyWindow();

    // State tracking
    Cell[][] cells = new Cell[rows][cols];
    Set<Integer> mines = new HashSet<>();
    int cellsRemaining = rows * cols - numMines;

    // Reveal cell logic: here for optimization
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> seen = new HashSet<>();

    // Random
    Random rand = new Random();


    public GameManager() {
        // Init game panel
        gamePanel = new JPanel();


        // Init menu bar
        initMenuBar();

        // Init mine positions
        resetMines();

        initGamePanel();
        // UI changes
        UIManager.put("Button.disabledText", Color.BLACK);
        UIManager.put("Button.select", Color.LIGHT_GRAY);
    } // GameManager

    private void initGamePanel() {
        gamePanel.setLayout(new GridLayout(rows, cols));
        initCells();
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
    }

    private void initCells() {
        // Init cells based on number of neighboring mines
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int finalI = i, finalJ = j;
                Cell newCell = new Cell(i, j, getCellValue(i, j));
                newCell.addActionListener(e -> handleCellClick(finalI, finalJ));
                cells[i][j] = newCell;
                gamePanel.add(newCell);
            } // for
        } // for
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();
        JMenu optionsTab = new JMenu("Options");
        JMenuItem difficulty = new JMenuItem("Difficulty");
        JMenuItem resetButton = new JMenuItem("Reset");
        difficulty.addActionListener(e -> changeDifficulty());
        resetButton.addActionListener(e -> reset());

        optionsTab.add(difficulty);
        optionsTab.add(resetButton);
        menuBar.add(optionsTab);
    }

    private void changeDifficulty() {
        Difficulty status = diffWindow.showPopup();
        if (status == Difficulty.CANCEL) return;

        System.out.println("switching");
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
        }
        reset();
    }

    private void resetMines() {
        mines.clear();
        this.rand.ints(0, rows * cols).distinct().limit(numMines).forEach(mines::add);
    } // resetMines

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
                cells[i][j].setEnabled(false);
    } // disableAll

    private void handleCellClick(int i, int j) {
        revealCells(i, j);

        if (cells[i][j].getValue() == -1 || cellsRemaining == 0) {
            if (cells[i][j].getValue() == -1) {
                this.popupWindow.setMessage("You died. Try again?");
            } else if (cellsRemaining == 0) {
                this.popupWindow.setMessage("You win! Start a new game?");
            } // else

            disableAll();

            int status = this.popupWindow.getPopupResponse();
            if (status == 0) reset();
        } // if
    } // handleCellClick

    private void reset() {
        resetMines();
        this.seen.clear();
        cellsRemaining = rows * cols - numMines;
        cells = new Cell[rows][cols];
        gamePanel.removeAll();
        initGamePanel();
        gamePanel.repaint();
        gamePanel.validate();

        windowWidth = rows * buttonHeight;
        windowHeight = rows * buttonWidth + 20;
    } // reset
} // class GameManager
