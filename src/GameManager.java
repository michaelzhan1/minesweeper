import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.Arrays;
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

    // State variables
    JButton[][] buttons = new JButton[ROWS][COLS];
    int[][] values = new int[ROWS][COLS];
    int cellsRemaining = ROWS * COLS - NUM_MINES;

    // Reveal cell logic: here for optimization
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> seen = new HashSet<>();

    public GameManager() {
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(ROWS, COLS));

        // Init grid elements and board values
        for (int i = 0; i < WIDTH / BUTTON_WIDTH; i++) {
            for (int j = 0; j < HEIGHT / BUTTON_HEIGHT; j++) {
                JButton newButton = createNewButton(i, j);
                buttons[i][j] = newButton;
                gamePanel.add(newButton);
            } // for
        } // for
        initMines();

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

    private void initMines() {
        int[] minePositions = new Random().ints(0, ROWS * COLS).distinct().limit(NUM_MINES).toArray();
        for (int pos : minePositions) {
            values[pos / COLS][pos % COLS] = -1;
        } // for

        int mineNeighborCount;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (values[i][j] != -1) {
                    // count number of neighboring mines
                    mineNeighborCount = 0;
                    for (int ii = Math.max(0, i - 1); ii < Math.min(ROWS, i + 2); ii++) {
                        for (int jj = Math.max(0, j - 1); jj < Math.min(COLS, j + 2); jj++) {
                            if (values[ii][jj] == -1) {
                                mineNeighborCount++;
                            } // if
                        } // for
                    } // for
                    values[i][j] = mineNeighborCount;
                } // if
            } // for
        } // for
    } // initMines

    private JButton createNewButton(int i, int j) {
        JButton newButton = new JButton();
        newButton.setBackground(new Color(137, 137, 137));
        newButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        newButton.addActionListener(e -> clickCell(i, j));
        return newButton;
    } // createNewButton

    private void revealSingleCell(int i, int j) {
        buttons[i][j].setText(Integer.toString(values[i][j]));
        buttons[i][j].setEnabled(false);
        buttons[i][j].setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        buttons[i][j].setBackground(Color.LIGHT_GRAY);
        cellsRemaining--;
    } // revealCell

    private void revealCells(int startI, int startJ) {
        queue.offer(startI * COLS + startJ);
        seen.add(startI * COLS + startJ);

        int curr, i, j;
        while (!queue.isEmpty()) {
            curr = queue.poll();
            i = curr / COLS;
            j = curr % COLS;

            revealSingleCell(i, j);
            if (values[i][j] == 0) {
                for (int ii = Math.max(0, i - 1); ii < Math.min(ROWS, i + 2); ii++) {
                    for (int jj = Math.max(0, j - 1); jj < Math.min(COLS, j + 2); jj++) {
                        if ((i != ii || j != jj) && !seen.contains(ii * COLS + jj)) {
                            queue.offer(ii * COLS + jj);
                            seen.add(ii * COLS +jj);
                        }
                    } // for
                } // for
            } // if
        } // while
        seen.clear();
    } // revealCells

    private void disableAll() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void clickCell(int i, int j) {
        if (values[i][j] == -1) {
            disableAll();
            System.out.println("You died");
        } // if

        revealCells(i, j);

        if (cellsRemaining == 0) {
            System.out.println("You win!");
        } // if
    } // clickCell

    private void reset() {
        System.out.println("Resetting...");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
                buttons[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                buttons[i][j].setBackground(new Color(137, 137, 137));
            } // for
        } // for
        Arrays.stream(values).forEach(row -> Arrays.fill(row, 0));
        initMines();
        cellsRemaining = ROWS * COLS - NUM_MINES;
    } // reset
} // class GameManager
