package lib;

public class PolozkaKosiku {
    private Zbozi zbozi;
    private float cena;
    private int pocet;


    public PolozkaKosiku(Zbozi zbozi, int pocet) {
        this.zbozi = zbozi;
        this.pocet = pocet;
    }

    public float getCena() {
        return this.cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    public Zbozi getZbozi() {
        return this.zbozi;
    }

    public void setZbozi(Zbozi zbozi) {
        this.zbozi = zbozi;
    }

    public int getPocet() {
        return this.pocet;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;
    }

    public void zvysit() {
        ++pocet;
    }
    public void snizit() {
        if(pocet > 0) {
            --pocet;
        }
    }
}
