import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.Color;

/**
 * Class representing a cell in the minesweeper grid
 */
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

    /**
     * Constructor
     * @param i the row index in the grid
     * @param j the column index in the grid
     * @param value the value of the cell (-1 means a mine)
     */
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
    } // main.Cell

    /**
     * Toggle display of a flag.
     * <p>
     * If a flag is displayed, then we can't reveal the cell.
     * </p>
     * @return an int representing the effect on the number of mines remaining
     */
    public int toggleFlag() {
        if (!this.revealed && !this.frozen) {
            if (!this.flag) {
                this.setText("\uD83D\uDEA9");
                this.setEnabled(false);
                this.flag = true;
                return -1;
            } else {
                this.setText(null);
                this.setEnabled(true);
                this.flag = false;
                return 1;
            } // if
        } // if
        return 0;
    } // toggleFlag

    /**
     * Get value of the cell
     * @return the value of the cell
     */
    public int getValue() {return value;}

    /**
     * Freeze in case of game over and prevent any more actions
     */
    public void freeze() {
        this.frozen = true;
        this.setEnabled(false);
    } // setFrozen

    /**
     * Reveal the value of the cell and make the cell un-clickable
     */
    public void revealCell() {
        if (this.value == -1) {
            this.setText("\uD83D\uDCA3");
        } else if (this.value != 0){
            this.setText(Integer.toString(this.value));
        }
        this.setEnabled(false);
        this.setBorder(line);
        this.setBackground(clickedColor);
        this.revealed = true;
    } // revealCell
} // class main.Cell
