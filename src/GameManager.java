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
    static final int ROWS = 10;
    static final int COLS = 10;
    static final int BUTTON_WIDTH = 40;
    static final int BUTTON_HEIGHT = 40;
    static final int WIDTH = ROWS * BUTTON_HEIGHT;
    static final int HEIGHT = ROWS * BUTTON_WIDTH;
    static final int NUM_MINES = 9;

    // Components
    JPanel gamePanel;

    // State tracking
    Cell[][] cells = new Cell[ROWS][COLS];
    Set<Integer> mines = new HashSet<>();
    int cellsRemaining = ROWS * COLS - NUM_MINES;

    // Reveal cell logic: here for optimization
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> seen = new HashSet<>();

    // Random
    Random rand = new Random();

    public GameManager() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(ROWS, COLS));

        // Init mine positions
        resetMines();

        // Init cells based on number of neighboring mines
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Cell newCell = new Cell(i, j, getCellValue(i, j));
                int finalI = i;
                int finalJ = j;
                newCell.addActionListener(e -> handleCellClick(finalI, finalJ));
                cells[i][j] = newCell;
                gamePanel.add(newCell);
            } // for
        } // for

        // UI changes
        UIManager.put("Button.disabledText", Color.BLACK);
        UIManager.put("Button.select", Color.LIGHT_GRAY);

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
    } // GameManager

    private void resetMines() {
        mines.clear();
        this.rand.ints(0, ROWS * COLS).distinct().limit(NUM_MINES).forEach(mines::add);
    } // resetMines

    private int getCellValue(int i, int j) {
        int pos = i * COLS  + j;
        if (mines.contains(pos)) return -1;

        int count = 0;
        for (int ii = -1; ii < 2; ii++) {
            for (int jj = -1; jj < 2; jj++) {
                if (mines.contains(pos + ii * COLS + jj)) count++;
            } // for
        } // for
        return count;
    } // getCellValue

    private void revealCells(int startI, int startJ) {
        queue.offer(startI * COLS + startJ);
        seen.add(startI * COLS + startJ);

        int curr, i, j;
        while (!queue.isEmpty()) {
            curr = queue.poll();
            i = curr / COLS;
            j = curr % COLS;

            cells[i][j].revealCell();
            cellsRemaining--;
            if (cells[i][j].getValue() == 0) {
                for (int ii = Math.max(0, i - 1); ii < Math.min(ROWS, i + 2); ii++) {
                    for (int jj = Math.max(0, j - 1); jj < Math.min(COLS, j + 2); jj++) {
                        if ((i != ii || j != jj) && !seen.contains(ii * COLS + jj)) {
                            queue.offer(ii * COLS + jj);
                            seen.add(ii * COLS +jj);
                        } // if
                    } // for
                } // for
            } // if
        } // while

    } // revealCells

    private void disableAll() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].setEnabled(false);
            }
        }
    }

    private void handleCellClick(int i, int j) {
        if (cells[i][j].getValue() == -1) {
            disableAll();
            System.out.println("You died");
        } // if
        revealCells(i, j);

        if (cellsRemaining == 0) {
            System.out.println("You win!");
        } // if
    } // handleCellClick

    private void reset() {
        System.out.println("Resetting...");
        resetMines();
        this.seen.clear();
        cellsRemaining = ROWS * COLS - NUM_MINES;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                cells[i][j].reset();
                cells[i][j].setValue(getCellValue(i, j));
            } // for
        } // for
    } // reset
} // class GameManager
