import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Font;

public class MineCounter extends JPanel {
    JLabel label;
    int minesRemaining;

    public MineCounter(int mines) {
        setLayout(new GridLayout(1, 1));
        minesRemaining = mines;
        label = new JLabel("Mines: " + minesRemaining, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label);
    }

    public void decrement() {
        minesRemaining--;
        label.setText("Mines: " + minesRemaining);
    }

    public void increment() {
        minesRemaining++;
        label.setText("Mines: " + minesRemaining);
    }

    public void reset(int mines) {
        minesRemaining = mines;
        label.setText("Mines: " + minesRemaining);
    }
}
