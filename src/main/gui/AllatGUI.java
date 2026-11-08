package gui;

import enums.EledelTipus;
import model.MenhelyModell;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.EnumMap;
import java.util.Map;

public class AllatGUI extends JFrame {

    private static final int FELTOLTES_MENNYISEG = 10;

    private final transient MenhelyModell modell;

    private JPanel allatokPanel;
    private JPanel raktarPanel;

    private final Map<EledelTipus, JLabel> keszletCimkek = new EnumMap<>(EledelTipus.class);

    public AllatGUI(MenhelyModell modell) {
        this.modell = modell;
        initUI();
    }

    private void initUI() {
        setTitle("Állatkert Nyílvántartó Rendszer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabok = new JTabbedPane();

        allatokPanel = new JPanel();
        raktarPanel = keszitRaktarPanel();

        tabok.addTab("Állatok", allatokPanel);
        tabok.addTab("Raktár", raktarPanel);

        setContentPane(tabok);

        // TODO: menusor (Fajl -> Mentes/Betoltes, Allat -> Uj)
        // TODO: Allatok ful: JTable az allatok listajahoz, TableModel-lel
        // TODO: Allatok ful: szuro vezerlok (tipus / allapot JComboBox)
        // TODO: Allatok ful: etetes gomb es esemenykezelok
    }

    private JPanel keszitRaktarPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.anchor = GridBagConstraints.WEST;

        int sor = 0;
        for (EledelTipus tipus : EledelTipus.values()) {
            gbc.gridy = sor;

            gbc.gridx = 0;
            panel.add(new JLabel(eledelCimke(tipus) + ":"), gbc);

            JLabel keszletCimke = new JLabel();
            keszletCimkek.put(tipus, keszletCimke);
            gbc.gridx = 1;
            panel.add(keszletCimke, gbc);

            JButton feltoltGomb = new JButton("Rendelés (+" + FELTOLTES_MENNYISEG + ")");
            feltoltGomb.addActionListener(e -> {
                modell.getRaktar().feltoltKeszlet(tipus, FELTOLTES_MENNYISEG);
                frissitRaktarKijelzo();
            });
            gbc.gridx = 2;
            panel.add(feltoltGomb, gbc);

            sor++;
        }

        frissitRaktarKijelzo();
        return panel;
    }

    private void frissitRaktarKijelzo() {
        for (Map.Entry<EledelTipus, JLabel> bejegyzes : keszletCimkek.entrySet()) {
            int keszlet = modell.getRaktar().getKeszlet(bejegyzes.getKey());
            bejegyzes.getValue().setText(String.valueOf(keszlet));
        }
    }

    private String eledelCimke(EledelTipus tipus) {
        switch (tipus) {
            case HUS:
                return "Hús (emlősök)";
            case MAG:
                return "Mag (madarak)";
            case ROVAR:
                return "Rovar (hűllők)";
            default:
                return tipus.name();
        }
    }

    public MenhelyModell getModell() {
        return modell;
    }
}
