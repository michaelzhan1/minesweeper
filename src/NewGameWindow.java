import javax.swing.*;

public class NewGameWindow extends JOptionPane {
    JDialog dialog;

    public NewGameWindow(String message, String title) {
        super(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        dialog = this.createDialog(title);
    } // NewGameWindow

    public int getPopupResponse() {
        dialog.setVisible(true);
        return (this.getValue() == null) ? 1 : Integer.parseInt(this.getValue().toString());
    } // getPopupResponse
} // class NewGameWindow
