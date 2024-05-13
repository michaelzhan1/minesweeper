import javax.swing.*;

public class NewGameWindow extends JOptionPane {
    JDialog dialog;

    public NewGameWindow() {
        super("", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        dialog = this.createDialog("Alert");
    } // NewGameWindow

    public int getPopupResponse() {
        dialog.setVisible(true);
        return (this.getValue() == null) ? 1 : Integer.parseInt(this.getValue().toString());
    } // getPopupResponse
} // class NewGameWindow
