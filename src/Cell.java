import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Cell extends JButton {
    // Positional variables
    int i;
    int j;

    // Data
    int value;
    boolean flag;
    boolean revealed;
    boolean frozen;

    // Colors
    static Color lineBorderColor = new Color(100, 100, 100);
    static Color clickedColor = Color.LIGHT_GRAY;
    static Color unclickedColor = new Color(137, 137, 137);

    // Borders
    static Border bevel = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    static Border line = BorderFactory.createLineBorder(lineBorderColor);

    public Cell(int i, int j, int value) {
        super();
        this.i = i;
        this.j = j;
        this.value = value;
        this.flag = false;
        this.revealed = false;
        this.frozen = false;

        this.setBackground(unclickedColor);
        this.setBorder(bevel);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    toggleFlag();
                }
            }
        });
    } // Cell

    private void toggleFlag() {
        if (!this.revealed && !this.frozen) {
            if (!this.flag) {
                this.setText("F");
                this.setEnabled(false);
                this.flag = true;
            } else {
                this.setText("");
                this.setEnabled(true);
                this.flag = false;
            } // if
        } // if
    } // toggleFlag

    public void setValue(int value) {
        this.value = value;
    } // setValue

    public int getValue() {
        return value;
    } // getValue

    public void setFrozen(boolean frozen) {this.frozen = frozen;}

    public void revealCell() {
        this.setText(Integer.toString(this.value));
        this.setEnabled(false);
        this.setBorder(line);
        this.setBackground(clickedColor);
        this.revealed = true;
    } // revealCell

    public void reset() {
        this.setText("");
        this.setEnabled(true);
        this.setBorder(bevel);
        this.setBackground(unclickedColor);
        this.revealed = false;
    } // reset
}
