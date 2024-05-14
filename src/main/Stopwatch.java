import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Font;

/**
 * JPanel-type that counts up from 0 every second
 */
public class Stopwatch extends JPanel {
    JLabel label;
    Timer timer;
    int time;

    /**
     * Constructor
     */
    public Stopwatch() {
        setLayout(new GridLayout(1, 1));
        time = 0;
        label = new JLabel("Time: 0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label);

        timer = new Timer(1000, e -> {
            time++;
            label.setText("Time: " + time);
        }); // Timer
    } // main.Stopwatch

    /**
     * Get current time in seconds
     * @return the current time
     */
    public int getTime() {return time;}

    /**
     * Start the timer
     */
    public void start() {timer.start();}

    /**
     * Stop the timer
     */
    public void stop() {timer.stop();}

    /**
     * Reset the timer
     */
    public void reset() {
        timer.stop();
        time = 0;
        label.setText("Time: 0");
    } // reset
} // class main.Stopwatch
