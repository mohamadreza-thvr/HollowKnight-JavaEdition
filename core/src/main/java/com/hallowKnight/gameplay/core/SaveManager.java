package com.hallowKnight.gameplay.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveManager {
    private static final Json json = new Json();

    static {
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public static void save(GameData data, int slotIndex) {
        FileHandle file = Gdx.files.local("saves/saveSlot_" + slotIndex + ".json");
        file.writeString(json.prettyPrint(data), false);
    }

    public static GameData load(int slotIndex) {
        FileHandle file = Gdx.files.local("saves/saveSlot_" + slotIndex + ".json");
        if (file.exists()) {
            return json.fromJson(GameData.class, file.readString());
        }
        return null;
    }

    public static boolean saveExists(int slotIndex) {
        return Gdx.files.local("saves/saveSlot_" + slotIndex + ".json").exists();
    }
}
