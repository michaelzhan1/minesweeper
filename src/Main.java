import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(gameManager.gameDisplay);
        frame.setJMenuBar(gameManager.menuBar);
        frame.setSize(GameManager.windowWidth, GameManager.windowHeight);
        frame.setVisible(true);
    }
}