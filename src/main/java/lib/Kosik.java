package lib;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class Kosik extends AbstractTableModel {
    private List<PolozkaKosiku> seznamPolozek;
    private static final int POCET_SLOUPCU = 3;
    private static final int SLOUPEC_NAZEV = 0;
    private static final int SLOUPEC_CENA = 1;
    private static final int SLOUPEC_POCET = 2;

    private static final String[] nazvySloupcu = {"Název", "Cena", "Počet"};

    public Kosik() {
        seznamPolozek = new ArrayList<>();
    }

    //TODO: kontroly pro snad vsechny metody (jestli existuje index atd)
    //pridej
    public void pridej(Zbozi zbozi) {

        //kontroluje, jestli produkt již existuje v košíku
        //pokud ano, zvýší počet daného produktu o jeden
        boolean bruh = false;
        PolozkaKosiku polozkaKosiku = new PolozkaKosiku(zbozi, 1);
        for (PolozkaKosiku kos : seznamPolozek) {
            if(kos.getZbozi().equals(polozkaKosiku.getZbozi())) {
                bruh = true;
                kos.zvysit();
            }
        }
        if(!bruh) {
            seznamPolozek.add(polozkaKosiku);
            polozkaKosiku.setCena(zbozi.getCena());
        }
    }
    //odeber
    public void odeber(int index) {
        seznamPolozek.remove(index);
        fireTableChanged(null);
    }
    public void odeber(Zbozi zbozi) {
        PolozkaKosiku polozkaKosiku = new PolozkaKosiku(zbozi, 1);
        for (PolozkaKosiku kos : seznamPolozek) {
            if(kos.getZbozi().equals(polozkaKosiku.getZbozi())) {
                seznamPolozek.remove(kos);
                break;
            }
        }
    }
    //vysypat
    public void vysypat(){
        seznamPolozek.clear();
    }
    //zvysit
    public void zvysit(int index) {
        seznamPolozek.get(index).zvysit();
        fireTableCellUpdated(index, SLOUPEC_POCET);

    }
    //snizit
    public void snizit(int index) {
        seznamPolozek.get(index).snizit();
        fireTableCellUpdated(index, SLOUPEC_POCET);
    }

    public String cenaCelkem() {
        float cena = 0;
        String result = "";
        for (PolozkaKosiku kos : seznamPolozek) {
            cena += kos.getCena()*kos.getPocet();
        }
        result = Float.toString(cena);
        return result;
    }

    public String getColumnName(int columnIndex) {
        return nazvySloupcu[columnIndex];
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
    public int getColumnCount() {
        return POCET_SLOUPCU;
    }

    @Override
    public int getRowCount() {
        return seznamPolozek.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PolozkaKosiku polozkaKosiku = seznamPolozek.get(rowIndex);
        switch(columnIndex) {
            case SLOUPEC_NAZEV:
                return polozkaKosiku.getZbozi();
            case SLOUPEC_CENA:
                return polozkaKosiku.getCena();
            case SLOUPEC_POCET:
                return polozkaKosiku.getPocet();
            default:
                throw new IllegalArgumentException("Špatný index sloupce tabulky");
        }
    }
}
