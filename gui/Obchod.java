package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import lib.Sklad;
import lib.Zbozi;
import com.formdev.flatlaf.IntelliJTheme;

import javax.swing.*;

public class Obchod {

    private JPanel hlavniPanel;
    private JMenuBar nabidka;
    private JPanel panelSkladu;
    private JTable tabulkaSkladu;


    private Sklad sklad;

    public Obchod() {
        sklad = new Sklad();

        vytvorKomponenty();
    }

    public JPanel getHlavniPanel() {
        return this.hlavniPanel;
    }
    public JMenuBar getNabidka() {
        return this.nabidka;
    }

    private void vytvorKomponenty() {
        vytvorNabidku();
        vytvorPanelSkladu();
        
        hlavniPanel = new JPanel(); 
        hlavniPanel.add(panelSkladu);
    }

    private void vytvorPanelSkladu() {
        tabulkaSkladu = new JTable();
        tabulkaSkladu.setModel(sklad);
        tabulkaSkladu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabulkaSkladu.setFillsViewportHeight(true);
        Docasne.naplnSklad(sklad);
        JScrollPane spTabulkaSkladu = new JScrollPane(tabulkaSkladu);

        JButton btPridej = new JButton("Přidat");
        JButton btSmaz = new JButton("Smazat");
        JButton btSmazVse = new JButton("Smazat vše");

        JPanel pnTlacitka = new JPanel();
        pnTlacitka.setLayout(new GridLayout(0, 1));
        pnTlacitka.add(btPridej);
        pnTlacitka.add(btSmaz);
        pnTlacitka.add(btSmazVse);

        btPridej.addActionListener((e)-> {
            sklad.pridejZbozi(new Zbozi("-", 0, 0));
        });

        btSmaz.addActionListener((e) -> {
            int radek = tabulkaSkladu.getSelectedRow();
            System.out.println(radek);
            sklad.smazatZbozi(radek);
        });

        panelSkladu = new JPanel();
        panelSkladu.setLayout(new BorderLayout());
        panelSkladu.add(spTabulkaSkladu, BorderLayout.CENTER);
        panelSkladu.add(pnTlacitka, BorderLayout.EAST);
    }

    private void vytvorNabidku() {

        JMenu menuSoubor = new JMenu();
        JMenu menuNapoveda = new JMenu();
        JMenuItem miUkoncit = new JMenuItem();
        JMenuItem miAbout = new JMenuItem("O programu");
        menuSoubor.setText("Soubor");
        miUkoncit.setText("Ukončit");
        menuSoubor.add(miUkoncit);
        menuNapoveda.setText("Nápověda");
        menuNapoveda.add(miAbout);
        miUkoncit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        miAbout.addActionListener((e) -> JOptionPane.showMessageDialog(hlavniPanel, "uwoogh", "Herní košík omg Java momentka", JOptionPane.PLAIN_MESSAGE));

        nabidka = new JMenuBar();
        nabidka.add(menuSoubor);
        nabidka.add(menuNapoveda);
    }

    public static void vytvorHlavniOkno() {
        IntelliJTheme.setup(Obchod.class.getResourceAsStream("/lib/flatlaf/mocha.theme.json"));
        JFrame hlavniOkno = new JFrame();
        hlavniOkno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hlavniOkno.setTitle("Obchod");
        hlavniOkno.setLocationByPlatform(true);

        Obchod obchod = new Obchod();
        hlavniOkno.setContentPane(obchod.getHlavniPanel());
        hlavniOkno.setJMenuBar(obchod.getNabidka());
        hlavniOkno.pack();
        hlavniOkno.setVisible(true);
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> vytvorHlavniOkno());
    }
}
