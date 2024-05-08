import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

        for (int i = 0; i < WIDTH / BUTTON_WIDTH; i++) {
            for (int j = 0; j < HEIGHT / BUTTON_HEIGHT; j++) {
                JButton newButton = createNewButton(i, j);
                buttons[i][j] = newButton;
                add(newButton);

                values[i][j] = i % 10;
                // old code to generate text
//                JTextField newText = new JTextField(String.valueOf(values[i][j]));
//                newText.setEditable(false);
//                newText.setHorizontalAlignment(JTextField.CENTER);
//                newText.setBounds(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT);
//                newText.setFocusable(false);
            }
        }
    }

    private JButton createNewButton(int i, int j) {
        JButton newButton = new JButton();
        newButton.setBounds(i * BUTTON_WIDTH, j * BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
        newButton.setBackground(new Color(137, 137, 137));
        newButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        newButton.addActionListener(new ActionListener() { // note: may be replaced with lambda, but unsure if we need e
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button clicked! Row: " + i + " , Col: " + j);
                System.out.println("Setting button to invisible");
                removeButton(i, j);
            }
        });
        return newButton;
    }


    private void removeButton(int i, int j) {
        buttons[i][j].setVisible(false);
//        this.repaint();
    }
}
