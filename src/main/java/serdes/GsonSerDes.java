package serdes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lib.Zbozi;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class GsonSerDes implements SerDes {
    @Override
    public List<Zbozi> nacti(String soubor) throws IOException {
        List<Zbozi> seznam = null;

        try (FileReader vstup = new FileReader(soubor)) {
            Gson gson = new Gson();
            seznam = gson.fromJson(vstup, new TypeToken<List<Zbozi>>(){}.getType());
            //vstup.close();
        }

        return seznam;
    }

    @Override
    public void uloz(String soubor, List<Zbozi> seznam) throws IOException {
        try (FileWriter vystup = new FileWriter(soubor)) {
            Gson gson = new Gson();
            String json = gson.toJson(seznam);
            vystup.write(json);
            //vystup.close();
        }
    }
}
