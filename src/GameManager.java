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


public class GameManager extends JPanel {
    // Constants
    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    static final int BUTTON_WIDTH = 40;
    static final int BUTTON_HEIGHT = 40;
    static final int ROWS = WIDTH / BUTTON_WIDTH;
    static final int COLS = HEIGHT / BUTTON_HEIGHT;
    static final int NUM_MINES = 9;

    // State variables
    JButton[][] buttons = new JButton[ROWS][COLS];
    int[][] values = new int[ROWS][COLS];
    int minesRemaining = ROWS * COLS - NUM_MINES;

    // Reveal cell logic: here for optimization
    Queue<Integer> queue = new LinkedList<>();
    Set<Integer> seen = new HashSet<>();

    public GameManager() {
        setLayout(new GridLayout(ROWS, COLS));

        // Init grid elements and board values
        for (int i = 0; i < WIDTH / BUTTON_WIDTH; i++) {
            for (int j = 0; j < HEIGHT / BUTTON_HEIGHT; j++) {
                JButton newButton = createNewButton(i, j);
                buttons[i][j] = newButton;
                this.add(newButton);
            } // for
        } // for
        initMines();

        // UI changes
        UIManager.put("Button.disabledText", Color.BLACK);
        UIManager.put("Button.select", Color.LIGHT_GRAY);

        // Key listeners
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "press r");
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released R"), "release r");
        this.getActionMap().put("press r", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameManager.this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "none");
                reset();
            } // actionPerformed
        }); // getActionMap
        this.getActionMap().put("release r", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameManager.this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "press r");
            } // actionPerformed
        }); // getActionMap
    } // GameManager

    private void initMines() {
        Random rand = new Random();
        int minePos;
        for (int i = 0; i < NUM_MINES; i++) {
            minePos = rand.nextInt(ROWS * COLS);
            values[minePos / COLS][minePos % COLS] = -1;
        } // for

        int mineNeighborCount;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (values[i][j] != -1) {
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
            }
        }

        seen.clear();
    }

    private void clickCell(int i, int j) {
        if (values[i][j] == -1) {
            System.out.println("You died");
        } // if

        revealCells(i, j);
        minesRemaining--;

        if (minesRemaining == 0) {
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
        minesRemaining = ROWS * COLS - NUM_MINES;
    } // reset
} // class GameManager
