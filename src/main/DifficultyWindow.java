import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Component;


/**
 * Control dialog for changing difficulty
 */
public class DifficultyWindow extends JOptionPane {
    JDialog dialog;
    JPanel buttonPanel;
    ButtonGroup buttonGroup;
    JRadioButton[] radioButtons;

    /**
     * Constructor
     */
    public DifficultyWindow() {
        super("Change main.Difficulty", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

        String[] options = {"Easy", "Medium", "Hard"};
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonGroup = new ButtonGroup();
        radioButtons = new JRadioButton[3];
        for (int i = 0; i < 3; i++) {
            JRadioButton button = new JRadioButton(options[i]);
            buttonGroup.add(button);
            buttonPanel.add(button);
            radioButtons[i] = button;
        } // for

        // re-order components so that confirmation buttons appear last
        Component[] components = this.getComponents();
        this.removeAll();
        this.add(components[0]);
        this.add(buttonPanel);
        this.add(components[1]);
        dialog = this.createDialog("Choose a difficulty level");
    } // main.DifficultyWindow

    /**
     * Open the dialog and record the response
     * @return a main.Difficulty enum representing either the new difficulty or cancelling the option
     */
    public Difficulty showPopup() {
        dialog.setVisible(true);
        int status = (this.getValue() == null) ? 2 : Integer.parseInt(this.getValue().toString());
        if (status == 2) return Difficulty.CANCEL;

        for (int i = 0; i < 3; i++) if (radioButtons[i].isSelected()) return Difficulty.values()[i];

        return Difficulty.CANCEL;
    } // showPopup
} // class main.DifficultyWindow
