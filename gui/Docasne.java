package gui;

import lib.Sklad;
import lib.Zbozi;

public class Docasne {
    public static void naplnSklad(Sklad sklad) {
        sklad.pridejZbozi(new Zbozi("moje stará nokie", 420, 1));
        sklad.pridejZbozi(new Zbozi("chleba", 30, 40));
        sklad.pridejZbozi(new Zbozi("kafíčko", 15, 200));
        sklad.pridejZbozi(new Zbozi("touhou orin fumo", 69420, 1));
    }
}
