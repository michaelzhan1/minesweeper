import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.Color;


public class GameManager extends JPanel {
    // Constants
    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    static final int BUTTON_WIDTH = 40;
    static final int BUTTON_HEIGHT = 40;
    static final int ROWS = WIDTH / BUTTON_WIDTH;
    static final int COLS = HEIGHT / BUTTON_HEIGHT;

    // State variables
    JButton[][] buttons = new JButton[ROWS][COLS];
    int[][] values = new int[ROWS][COLS];

    public GameManager() {
        setLayout(new GridLayout(ROWS, COLS));

        // Init grid elements and board values
        for (int i = 0; i < WIDTH / BUTTON_WIDTH; i++) {
            for (int j = 0; j < HEIGHT / BUTTON_HEIGHT; j++) {
                JButton newButton = createNewButton(i, j);
                buttons[i][j] = newButton;
                values[i][j] = i % 10; // TODO: change to random generation with mines
                this.add(newButton);
            } // for
        } // for

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

    private JButton createNewButton(int i, int j) {
        JButton newButton = new JButton();
        newButton.setBackground(new Color(137, 137, 137));
        newButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        newButton.addActionListener(e -> clickCell(i, j));
        return newButton;
    } // createNewButton

    private void clickCell(int i, int j) {
        buttons[i][j].setText(Integer.toString(values[i][j]));
        buttons[i][j].setEnabled(false);
        buttons[i][j].setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        buttons[i][j].setBackground(Color.LIGHT_GRAY);
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

        // TODO: reset values as well
        // TODO: when completion counter is added, reset here as well
    } // reset
} // class GameManager
