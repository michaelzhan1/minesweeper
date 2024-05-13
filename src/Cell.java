import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.Color;


public class Cell extends JButton {
    // Positional variables
    int i;
    int j;

    // Data
    int value;

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

        this.setBackground(unclickedColor);
        this.setBorder(bevel);
    } // Cell

    public void setValue(int value) {
        this.value = value;
    } // setValue

    public int getValue() {
        return value;
    } // getValue

    public void revealCell() {
        this.setText(Integer.toString(this.value));
        this.setEnabled(false);
        this.setBorder(line);
        this.setBackground(clickedColor);
    } // revealCell

    public void reset() {
        this.setText("");
        this.setEnabled(true);
        this.setBorder(bevel);
        this.setBackground(unclickedColor);
    } // reset
}
