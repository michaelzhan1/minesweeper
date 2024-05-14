import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Font;

/**
 * JPanel-type to display the number of remaining mines, based on flags placed
 */
public class MineCounter extends JPanel {
    JLabel label;
    int minesRemaining;

    /**
     * Constructor
     * @param mines the number of mines
     */
    public MineCounter(int mines) {
        setLayout(new GridLayout(1, 1));
        minesRemaining = mines;
        label = new JLabel("Mines: " + minesRemaining, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label);
    } // MineCounter

    /**
     * Decrement the number of mines, and update the display
     */
    public void decrement() {
        minesRemaining--;
        label.setText("Mines: " + minesRemaining);
    } // decrement

    /**
     * Increment the number of mines, and update the display
     */
    public void increment() {
        minesRemaining++;
        label.setText("Mines: " + minesRemaining);
    } // increment

    /**
     * Reset the number of mines, and update the display
     */
    public void reset(int mines) {
        minesRemaining = mines;
        label.setText("Mines: " + minesRemaining);
    } // reset
} // class MineCounter
