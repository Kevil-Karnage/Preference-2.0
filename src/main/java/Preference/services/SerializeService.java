package main.java.Preference.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import main.java.Preference.models.Game;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class SerializeService {
    private Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

    public SerializeService() {

    }

    public void serialize(Game game) {
        try {
            FileWriter fw = new FileWriter("src/main/Gson/Game.json");
            gson.toJson(game, fw);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Game deserialize() {
        Game g = new Game();

        try {
            FileReader fr = new FileReader("src/main/Gson/Game.json");
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = fr.read()) != -1) {
                sb.append((char) c);
            }
            String json = sb.toString();
            Type game = new TypeToken<Game>() {
            }.getType();
            g = gson.fromJson(json, game);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return g;
    }
}
