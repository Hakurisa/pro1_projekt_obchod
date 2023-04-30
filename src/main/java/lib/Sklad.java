package lib;

import serdes.SerDes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class Sklad extends AbstractTableModel {
    private List<Zbozi> seznamZbozi;
    private static final int POCET_SLOUPCU = 3;
    private static final int SLOUPEC_NAZEV = 0;
    private static final int SLOUPEC_CENA = 1;
    private static final int SLOUPEC_POCET = 2;

    private static final String[] nazvySloupcu = {"Název", "Cena", "Počet"};

    public Sklad() {
        seznamZbozi = new ArrayList<>();
    }

    public void pridejZbozi(Zbozi zbozi) {
        seznamZbozi.add(zbozi);
        //int radek = seznamZbozi.size()-1;
        fireTableChanged(null);
        //fireTableCellUpdated(radek, radek);
    }

    public Zbozi getZbozi(int index) {
        try {
            return seznamZbozi.get(index);
        } catch(IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Na tomto indexu žádné zboží neexistuje! Index: " + index + " Délka listu: " + seznamZbozi.size());
        }


    }

    public void smazatZbozi(int index) {
        if (index >= 0 && index < seznamZbozi.size()) {
            seznamZbozi.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    public void smazatVsechnoZbozi() {
        seznamZbozi.clear();
        fireTableChanged(null);
    }

    public void odeberJednoZbozi(int index) {
        if (index >= 0 && index < seznamZbozi.size()) {
           Zbozi zbozi = seznamZbozi.get(index);
           zbozi.setPocet(zbozi.getPocet()-1);
           fireTableCellUpdated(index, SLOUPEC_POCET);
        }
    }
    public void odeberJednoZbozi(Zbozi zbozi) {
        for (Zbozi zboz : seznamZbozi) {
            if(zboz.equals(zbozi)){
                zbozi.setPocet(zbozi.getPocet()-1);
                fireTableChanged(null);
            }
        }
    }
    public void pridejJednoZbozi(Zbozi zbozi) {
        for (Zbozi zboz : seznamZbozi) {
            if(zboz.equals(zbozi)){
                zbozi.setPocet(zbozi.getPocet()+1);
                fireTableChanged(null);
            }
        }
    }

    public boolean checkIfZero(Zbozi zbozi) {
        for (Zbozi zboz : seznamZbozi) {
            if(zboz.equals(zbozi)) {
                if(zboz.getPocet() <= 0) {
                    return true;
                } return false;
            }
        }
        return true;

    }

    public void ulozDoCSV(String soubor) {
        try {
            FileWriter writer = new FileWriter(soubor);
            for (Zbozi zbozi : seznamZbozi) {
                writer.append(zbozi.getNazev() + "," + zbozi.getCena() + "," + zbozi.getPocet());
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void nactiZCSV(String soubor) {
        try {
            FileReader reader = new FileReader(soubor);
            BufferedReader br = new BufferedReader(reader);

            String line;
            while((line = br.readLine())!= null) {
                String[] values = line.split(",");
                String nazev = values[0];
                float cena = Float.parseFloat(values[1]);
                int pocet = Integer.parseInt(values[2]);
                Zbozi zbozi = new Zbozi(nazev, cena, pocet);
                seznamZbozi.add(zbozi);
            }
            br.close();
            reader.close();
            fireTableDataChanged();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void nacti(SerDes serdes, String soubor) throws IOException {
        seznamZbozi = serdes.nacti(soubor);
        fireTableDataChanged();
    }

    public void uloz(SerDes serdes, String soubor) throws IOException {
        serdes.uloz(soubor, seznamZbozi);
    }

    @Override
    public int getColumnCount() {
        return POCET_SLOUPCU;
    }

    @Override
    public int getRowCount() {
        return seznamZbozi.size();
    }

    public String getColumnName(int columnIndex) {
        return nazvySloupcu[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object hodnota, int rowIndex, int columnIndex) {
        Zbozi zbozi = seznamZbozi.get(rowIndex);

        switch (columnIndex) {
            case SLOUPEC_NAZEV:
                zbozi.setNazev((String)hodnota);
                break;
            case SLOUPEC_CENA:
                zbozi.setCena((float)hodnota);
                break;
            case SLOUPEC_POCET:
                zbozi.setPocet((int)hodnota);
                break;
            default:
                throw new IllegalArgumentException("AAAAAAAAAAAA WHAT IS GOING ON");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case SLOUPEC_NAZEV:
                return String.class;
            case SLOUPEC_CENA:
                return Float.class;
            case SLOUPEC_POCET:
                return Integer.class;
            default:
                throw new IllegalArgumentException("Špatný index sloupce tabulky");
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Zbozi zbozi = seznamZbozi.get(rowIndex);
        switch(columnIndex) {
            case SLOUPEC_NAZEV:
                return zbozi.getNazev();
            case SLOUPEC_CENA:
                return zbozi.getCena();
            case SLOUPEC_POCET:
                return zbozi.getPocet();
            default:
                throw new IllegalArgumentException("Špatný index sloupce tabulky");
        }
    }

}