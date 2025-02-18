import gui.MainMenu;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Typer Cats");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 540);
        frame.setResizable(false);

        // Create MainMenu and set it as the content pane
        MainMenu mainMenu = new MainMenu();
        frame.setContentPane(mainMenu.getMainPanel());

        // Display the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}