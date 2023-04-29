package lib;

public class Zbozi {
    private String nazev;

    private float cena;
    private int pocet;


    public Zbozi(String nazev, float cena, int pocet) {
        this.nazev = nazev;
        this.cena = cena;
        this.pocet = pocet;
    }


    public String getNazev() {
        return this.nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public float getCena() {
        return this.cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }

    public int getPocet() {
        return this.pocet;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;
    }


    @Override
    public String toString() {
        return nazev;
    }


}
