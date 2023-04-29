package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import lib.Kosik;
import lib.Sklad;
import lib.Zbozi;
import com.formdev.flatlaf.IntelliJTheme;
import serdes.GsonSerDes;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Obchod {

    private JPanel hlavniPanel;
    private JMenuBar nabidka;
    private JPanel panelSkladu;
    private JTable tabulkaSkladu;
    private JPanel panelKosiku;
    private JTable tabulkaKosiku;


    private Sklad sklad;
    private Kosik kosik;
    private JLabel lbCena;

    public Obchod() {
        sklad = new Sklad();
        kosik = new Kosik();
        lbCena = new JLabel(kosik.cenaCelkem());

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
        vytvorPanelKosiku();
        
        hlavniPanel = new JPanel(); 
        JTabbedPane tabbedPane = new JTabbedPane();
        hlavniPanel.add(tabbedPane);
        tabbedPane.addTab("Sklad", panelSkladu);
        tabbedPane.addTab("Kosik", panelKosiku);
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
        JButton btKosikPridej = new JButton("Přidat do košíku");

        JPanel pnTlacitka = new JPanel();
        pnTlacitka.setLayout(new GridLayout(0, 1));
        pnTlacitka.add(btPridej);
        pnTlacitka.add(btKosikPridej);
        pnTlacitka.add(btSmaz);
        pnTlacitka.add(btSmazVse);
        
        btSmaz.setEnabled(false);
        btKosikPridej.setEnabled(false);

        
        //checks if a row is selected and if yes, enables the delete button
        //english comment because coding in czech physically hurts me sorry
        tabulkaSkladu.getSelectionModel().addListSelectionListener((e) -> {
            int radek = tabulkaSkladu.getSelectedRow();
            //apparently when nothing is selected it reports as -1 so this works don't question it
            if(radek < 0) {
                btSmaz.setEnabled(false);
                btKosikPridej.setEnabled(false);
            } else {
                btSmaz.setEnabled(true);
                btKosikPridej.setEnabled(true);
            }
        });

        
        btPridej.addActionListener((e)-> {
            sklad.pridejZbozi(new Zbozi("-", 0, 0));
        });

        btKosikPridej.addActionListener((e) -> {
            int radek = tabulkaSkladu.getSelectedRow();
            Zbozi zbozi = sklad.getZbozi(radek);
            //TODO: make this better
            if(zbozi.getPocet() <= 0) {
                JOptionPane.showMessageDialog(null, "Na skladě již žádný produkt " + zbozi.getNazev() + " není!", "Varování", JOptionPane.PLAIN_MESSAGE);
            } else {
                kosik.pridej(zbozi);
                lbCena.setText(kosik.cenaCelkem());
                sklad.odeberJednoZbozi(radek);
            }
        });

        btSmaz.addActionListener((e) -> {
            int radek = tabulkaSkladu.getSelectedRow();
            Zbozi zbozi = sklad.getZbozi(radek);
            System.out.println(radek);
            kosik.odeber(zbozi);
            sklad.smazatZbozi(radek);
            lbCena.setText(kosik.cenaCelkem());
        });

        btSmazVse.addActionListener((e) -> {
            int confrimDialog = JOptionPane.showConfirmDialog(null, "Smažete všechny položky v tabulce!\nOpravdu chcete pokračovat?", "Varování!" ,JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(confrimDialog == JOptionPane.YES_OPTION) {
                sklad.smazatVsechnoZbozi();
                kosik.smazatVsechnoZbozi();
                lbCena.setText(kosik.cenaCelkem());
            }
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
        miAbout.addActionListener((e) -> JOptionPane.showMessageDialog(hlavniPanel, "uwoogh", "Herní košík omg Java momentka", JOptionPane.INFORMATION_MESSAGE));
        JMenuItem miNactiJson = new JMenuItem("Načti JSON");
        miNactiJson.addActionListener((e) -> {
            try {
                JFileChooser dialog = new JFileChooser(".");
                dialog.setFileFilter(new FileNameExtensionFilter("*.json", "json"));
                if (dialog.showOpenDialog(panelSkladu) == JFileChooser.APPROVE_OPTION) {
                    String soubor = dialog.getSelectedFile().getPath();
                    sklad.nacti(new GsonSerDes(), soubor);
                }
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(hlavniPanel,
                        "Při načítání do JSON formátu nastala: "
                                + exp.getLocalizedMessage(), "Chyba načítání",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JMenuItem miUlozJson = new JMenuItem("Ulož JSON jako...");
        miUlozJson.addActionListener((e) -> {
            try {
                JFileChooser dialog = new JFileChooser(".");
                dialog.setFileFilter(new FileNameExtensionFilter("*.json", "json"));
                if (dialog.showSaveDialog(panelSkladu) == JFileChooser.APPROVE_OPTION) {
                    String soubor = dialog.getSelectedFile().getPath();
                    sklad.uloz(new GsonSerDes(), soubor + ".json");
                }
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(hlavniPanel,
                        "Při ukládání do JSON formátu nastala: "
                            + exp.getLocalizedMessage(), "Chyba ukládání",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        menuSoubor.add(miNactiJson);
        menuSoubor.add(miUlozJson);        
        nabidka = new JMenuBar();
        nabidka.add(menuSoubor);
        nabidka.add(menuNapoveda);
    }

    private void vytvorPanelKosiku() {
        tabulkaKosiku = new JTable();
        tabulkaKosiku.setModel(kosik);
        tabulkaKosiku.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabulkaKosiku.setFillsViewportHeight(true);
        JScrollPane spTabulkaKosiku = new JScrollPane(tabulkaKosiku);

        //cart GUI

        JButton btPridej = new JButton("Zvýšit počet");
        JButton btOdeberJeden = new JButton("Snížit počet");
        JButton btSmaz = new JButton("Smazat");
        JButton btKup = new JButton("Koupit");

        btPridej.setEnabled(false);
        btOdeberJeden.setEnabled(false);
        btSmaz.setEnabled(false);

        tabulkaKosiku.getSelectionModel().addListSelectionListener((e) -> {
            int radek = tabulkaKosiku.getSelectedRow();
            //apparently when nothing is selected it reports as -1 so this works don't question it
            if(radek < 0) {
                btPridej.setEnabled(false);
                btOdeberJeden.setEnabled(false);
                btSmaz.setEnabled(false);
            } else {
                btPridej.setEnabled(true);
                btOdeberJeden.setEnabled(true);
                btSmaz.setEnabled(true);
            }
        });
        //button action listeners
        btPridej.addActionListener((e)-> {
            int radek = tabulkaKosiku.getSelectedRow();
            Zbozi zbozi = (Zbozi) tabulkaKosiku.getValueAt(radek, 0);
            if(!sklad.checkIfZero(zbozi)) {
                sklad.odeberJednoZbozi(zbozi);
                kosik.zvysit(radek);
            } else {
                JOptionPane.showMessageDialog(hlavniPanel, "Požadované zboží již není na skladě!", "Varování", JOptionPane.WARNING_MESSAGE);
            }
            lbCena.setText(kosik.cenaCelkem());
        });
        btOdeberJeden.addActionListener((e)-> {
            int radek = tabulkaKosiku.getSelectedRow();
            Zbozi zbozi = (Zbozi) tabulkaKosiku.getValueAt(radek, 0);
            kosik.snizit(radek);
            sklad.pridejJednoZbozi(zbozi);
            if((int) tabulkaKosiku.getValueAt(radek, 2) <= 0) {
                kosik.odeber(radek);
            }
            lbCena.setText(kosik.cenaCelkem());
        });
        btSmaz.addActionListener((e)-> {
            int radek = tabulkaKosiku.getSelectedRow();
            Zbozi zbozi = (Zbozi) tabulkaKosiku.getValueAt(radek, 0);
            int pocet = (int) tabulkaKosiku.getValueAt(radek, 2);
            for (int i = 0; i < pocet; i++) {
                sklad.pridejJednoZbozi(zbozi);
            }
            kosik.odeber(radek);
            lbCena.setText(kosik.cenaCelkem());
        });
        btKup.addActionListener((e)-> {
            JOptionPane.showMessageDialog(hlavniPanel, "bude brzy", "xdd", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel pnTlacitka = new JPanel();
        pnTlacitka.setLayout(new GridLayout(0, 1));
        pnTlacitka.add(lbCena);
        pnTlacitka.add(btPridej);
        pnTlacitka.add(btOdeberJeden);
        pnTlacitka.add(btSmaz);
        pnTlacitka.add(btKup);


        panelKosiku = new JPanel();
        panelKosiku.setLayout(new BorderLayout());
        panelKosiku.add(spTabulkaKosiku, BorderLayout.CENTER);
        panelKosiku.add(pnTlacitka, BorderLayout.EAST);
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
