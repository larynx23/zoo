package gui;

import enums.EledelTipus;
import model.Allat;
import model.Emlos;
import model.Hullo;
import model.Madar;
import model.MenhelyModell;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AllatGUI extends JFrame {

    private static final int FELTOLTES_MENNYISEG = 10;

    private final transient MenhelyModell modell;

    private JPanel allatokPanel;
    private JPanel raktarPanel;

    private AllatTablaModell tablaModell;
    private JTable allatTabla;

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

        allatokPanel = keszitAllatokPanel();
        raktarPanel = keszitRaktarPanel();

        tabok.addTab("Állatok", allatokPanel);
        tabok.addTab("Raktár", raktarPanel);

        setContentPane(tabok);

        // TODO: menusor (Fajl -> Mentes/Betoltes, Allat -> Uj)
        // TODO: Allatok ful: szuro vezerlok (tipus / allapot JComboBox)
        // TODO: Allatok ful: etetes gomb es esemenykezelok
    }


    private JPanel keszitAllatokPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel eszkoztar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton ujGomb = new JButton("Új állat");
        ujGomb.addActionListener(e -> ujAllatDialogus());
        eszkoztar.add(ujGomb);

        JButton torolGomb = new JButton("Törlés");
        torolGomb.addActionListener(e -> kivalasztottTorlese());
        eszkoztar.add(torolGomb);

        panel.add(eszkoztar, BorderLayout.NORTH);

        tablaModell = new AllatTablaModell(modell.getAllatok());
        allatTabla = new JTable(tablaModell);
        allatTabla.setAutoCreateRowSorter(true);

        panel.add(new JScrollPane(allatTabla), BorderLayout.CENTER);
        return panel;
    }

    private void ujAllatDialogus() {
        JComboBox<String> tipusValaszto = new JComboBox<>(new String[]{"Emlős", "Madár", "Hüllő"});
        JTextField nevMezo = new JTextField(15);
        JTextField korMezo = new JTextField(15);
        JTextField sulyMezo = new JTextField(15);

        JTextField szorMezo = new JTextField(15);
        JTextField szarnyMezo = new JTextField(15);
        JCheckBox mergezoMezo = new JCheckBox();

        JPanel emlosKartya = mezoSor("Szőrtípus:", szorMezo);
        JPanel madarKartya = mezoSor("Szárnyfesztáv (m):", szarnyMezo);
        JPanel hulloKartya = mezoSor("Mérgező:", mergezoMezo);

        CardLayout kartyaElrendezes = new CardLayout();
        JPanel kartyak = new JPanel(kartyaElrendezes);
        kartyak.add(emlosKartya, "Emlős");
        kartyak.add(madarKartya, "Madár");
        kartyak.add(hulloKartya, "Hüllő");

        tipusValaszto.addActionListener(e ->
                kartyaElrendezes.show(kartyak, (String) tipusValaszto.getSelectedItem()));

        JPanel urlap = new JPanel();
        urlap.setLayout(new BoxLayout(urlap, BoxLayout.Y_AXIS));
        urlap.add(mezoSor("Típus:", tipusValaszto));
        urlap.add(mezoSor("Név:", nevMezo));
        urlap.add(mezoSor("Kor:", korMezo));
        urlap.add(mezoSor("Súly (kg):", sulyMezo));
        urlap.add(kartyak);

        int valasz = JOptionPane.showConfirmDialog(
                this, urlap, "Új állat felvétele",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (valasz != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String nev = nevMezo.getText().trim();
            if (nev.isEmpty()) {
                throw new IllegalArgumentException("A név nem lehet üres.");
            }
            int kor = Integer.parseInt(korMezo.getText().trim());
            double suly = Double.parseDouble(sulyMezo.getText().trim().replace(',', '.'));
            if (kor < 0 || suly < 0) {
                throw new IllegalArgumentException("A kor és a súly nem lehet negatív.");
            }

            String tipus = (String) tipusValaszto.getSelectedItem();
            Allat allat;
            switch (tipus) {
                case "Emlős":
                    allat = new Emlos(nev, kor, suly, szorMezo.getText().trim());
                    break;
                case "Madár":
                    double szarny = Double.parseDouble(
                            szarnyMezo.getText().trim().replace(',', '.'));
                    allat = new Madar(nev, kor, suly, szarny);
                    break;
                case "Hüllő":
                    allat = new Hullo(nev, kor, suly, mergezoMezo.isSelected());
                    break;
                default:
                    throw new IllegalArgumentException("Ismeretlen típus: " + tipus);
            }

            modell.hozzaad(allat);
            tablaModell.fireTableDataChanged();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "A kor egész szám, a súly és a szárnyfesztáv szám legyen.",
                    "Hibás adat", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Hibás adat", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kivalasztottTorlese() {
        int nezetiSor = allatTabla.getSelectedRow();
        if (nezetiSor < 0) {
            JOptionPane.showMessageDialog(this,
                    "Előbb válassz ki egy állatot a táblázatból.",
                    "Nincs kijelölés", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modellSor = allatTabla.convertRowIndexToModel(nezetiSor);
        Allat allat = modell.getAllatok().get(modellSor);

        int valasz = JOptionPane.showConfirmDialog(this,
                "Biztosan törlöd? " + allat.getNev(),
                "Törlés megerősítése", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (valasz != JOptionPane.YES_OPTION) {
            return;
        }

        modell.torol(allat);
        tablaModell.fireTableDataChanged();
    }

    private JPanel mezoSor(String cimke, java.awt.Component mezo) {
        JPanel sor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(cimke);
        label.setPreferredSize(new java.awt.Dimension(120, label.getPreferredSize().height));
        sor.add(label);
        sor.add(mezo);
        return sor;
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

    private static class AllatTablaModell extends AbstractTableModel {

        private static final String[] OSZLOPOK = {
                "Tipus", "Nev", "Kor", "Suly (kg)", "Allapot", "Utolso etetes", "Extra"
        };

        private static final DateTimeFormatter IDO_FORMAZO =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        private final transient List<Allat> allatok;

        AllatTablaModell(List<Allat> allatok) {
            this.allatok = allatok;
        }

        @Override
        public int getRowCount() {
            return allatok.size();
        }

        @Override
        public int getColumnCount() {
            return OSZLOPOK.length;
        }

        @Override
        public String getColumnName(int column) {
            return OSZLOPOK[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Allat allat = allatok.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return allat.getClass().getSimpleName();
                case 1:
                    return allat.getNev();
                case 2:
                    return allat.getKor();
                case 3:
                    return allat.getSuly();
                case 4:
                    return allat.getAllapot();
                case 5:
                    return allat.getUtolsoEtetesIdopont() == null
                            ? ""
                            : allat.getUtolsoEtetesIdopont().format(IDO_FORMAZO);
                case 6:
                    return allat.getReszletek();
                default:
                    return "";
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
