import javax.swing.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // Constants
    static final int WIDTH = 400;
    static final int HEIGHT = 400;
    static final int BUTTON_WIDTH = 40;
    static final int BUTTON_HEIGHT = 40;
    static final int ROWS = WIDTH / BUTTON_WIDTH;
    static final int COLS = HEIGHT / BUTTON_HEIGHT;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(ROWS, COLS));

        List<JButton> buttons = new ArrayList<>();

        for (int i = 0; i < WIDTH / BUTTON_WIDTH; i++) {
            for (int j = 0; j < HEIGHT / BUTTON_HEIGHT; j++) {
                JButton newButton = new JButton();
                newButton.setBounds(i * BUTTON_WIDTH, j * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
                buttons.add(newButton);
                frame.add(newButton);
            }
        }

        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
    }
}