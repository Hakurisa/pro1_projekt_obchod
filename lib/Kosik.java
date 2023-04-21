package lib;

import java.util.ArrayList;
import java.util.List;

public class Kosik {
    private List<PolozkaKosiku> seznamPolozek;

    public Kosik() {
        seznamPolozek = new ArrayList<>();
    }

    //TODO: kontroly pro snad vsechny metody (jestli existuje index atd)
    //pridej
    public void pridej(Zbozi zbozi) {
        //TODO: kontrola, jestli tam zbozi uz je
        seznamPolozek.add(new PolozkaKosiku(zbozi, 1));
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
}
