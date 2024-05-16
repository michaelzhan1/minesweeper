package com.example.minesweeper;

import javax.swing.*;

/**
 * Control dialog for starting a new game
 */
public class NewGameWindow extends JOptionPane {
    JDialog dialog;

    /**
     * Constructor
     */
    public NewGameWindow() {
        super("", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
        dialog = createDialog("Alert"); // the created dialog is not visible yet
    } // NewGameWindow

    /**
     * Open the dialog popup and record the response
     * @return the result of the response. 0 means yes, 1 means no
     */
    public int getPopupResponse() {
        dialog.setVisible(true);
        return (this.getValue() == null) ? 1 : Integer.parseInt(this.getValue().toString());
    } // getPopupResponse
} // class NewGameWindow
