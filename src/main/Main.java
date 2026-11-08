import gui.AllatGUI;
import model.MenhelyModell;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        MenhelyModell modell = new MenhelyModell();
        SwingUtilities.invokeLater(() -> new AllatGUI(modell).setVisible(true));
    }
}
