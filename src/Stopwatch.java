import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Font;

public class Stopwatch extends JPanel {
    JLabel label;
    Timer timer;
    int time;

    public Stopwatch() {
        setLayout(new GridLayout(1, 1));
        time = 0;
        label = new JLabel("Time: 0", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label);

        timer = new Timer(1000, e -> {
            time++;
            label.setText("Time: " + time);
        });
    }

    public int getTime() {return time;}

    public void start() {timer.start();}
    
    public void stop() {timer.stop();}

    public void reset() {
        timer.stop();
        time = 0;
        label.setText("Time: 0");
    }
}
