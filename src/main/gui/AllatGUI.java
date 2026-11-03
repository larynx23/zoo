package gui;

import model.MenhelyModell;

import javax.swing.JFrame;

public class AllatGUI extends JFrame {

    private final transient MenhelyModell modell;

    public AllatGUI(MenhelyModell modell) {
        this.modell = modell;
        initUI();
    }

    private void initUI() {
        setTitle("Allatkert Nyilvantarto Rendszer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // TODO: menusor (Fajl -> Mentes/Betoltes, Allat -> Uj)
        // TODO: JTable az allatok listajahoz, TableModel-lel
        // TODO: szuro vezerlok (tipus / allapot JComboBox)
        // TODO: raktar panel + feltoltes gombok
        // TODO: etetes gomb es esemenykezelok
    }

    public MenhelyModell getModell() {
        return modell;
    }
}
