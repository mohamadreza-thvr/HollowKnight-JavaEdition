package com.hallowKnight.startGameMenu;

import java.util.ArrayList;
import java.util.List;

public class StartGameModel {
    private List<String> saveSlots;

    public StartGameModel() {
        saveSlots = new ArrayList<>();
        saveSlots.add("Save Slot 1 (Empty)");
        saveSlots.add("Save Slot 2 (Empty)");
        saveSlots.add("Save Slot 3 (Empty)");
    }

    public List<String> getSaveSlots() {
        return saveSlots;
    }
}
