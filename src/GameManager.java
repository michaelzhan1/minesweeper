import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


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

    public GameManager() {
        setLayout(new GridLayout(ROWS, COLS));
        setBorder(BorderFactory.createLineBorder(Color.black));

        for (int i = 0; i < WIDTH / BUTTON_WIDTH; i++) {
            for (int j = 0; j < HEIGHT / BUTTON_HEIGHT; j++) {
                JButton newButton = new JButton();
                newButton.setBounds(i * BUTTON_WIDTH, j * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
                newButton.setBackground(new Color(137, 137, 137));
                buttons[i][j] = newButton;
                add(newButton);
            }
        }
    }
}
