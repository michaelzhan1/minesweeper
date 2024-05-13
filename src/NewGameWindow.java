import javax.swing.*;

public class NewGameWindow extends JOptionPane {
    JDialog dialog;

    public NewGameWindow(String message, String title) {
        super(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        dialog = this.createDialog(title);
    }

    public int callPopup() {
        dialog.setVisible(true);
        int status; // 0: yes, 1: no
        if (this.getValue() == null) status = 1;
        else status = Integer.parseInt(this.getValue().toString());
        return status;
    }
}
