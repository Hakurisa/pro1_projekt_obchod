package lib;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class Kosik extends AbstractTableModel {
    private List<PolozkaKosiku> seznamPolozek;
    private static final int POCET_SLOUPCU = 2;
    private static final int SLOUPEC_NAZEV = 0;
    private static final int SLOUPEC_POCET = 1;

    private static final String[] nazvySloupcu = {"Název", "Počet"};

    public Kosik() {
        seznamPolozek = new ArrayList<>();
    }

    //TODO: kontroly pro snad vsechny metody (jestli existuje index atd)
    //pridej
    public void pridej(Zbozi zbozi) {
        //TODO: kontrola, jestli tam zbozi uz je
        if(!seznamPolozek.contains(zbozi)) {
            seznamPolozek.add(new PolozkaKosiku(zbozi, 1));
        }
    }
    //odeber
    public void odeber(int index) {
        seznamPolozek.remove(index);
    }
    //vysypat
    public void vysypat(){
        seznamPolozek.clear();
    }
    //zvysit
    public void zvysit(int index) {
        seznamPolozek.get(index).zvysit();

    }
    //snizit
    public void snizit(int index) {
        seznamPolozek.get(index).snizit();
    }

    public String getColumnName(int columnIndex) {
        return nazvySloupcu[columnIndex];
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
            case SLOUPEC_POCET:
                return polozkaKosiku.getPocet();
            default:
                throw new IllegalArgumentException("Špatný index sloupce tabulky");
        }
    }
}
