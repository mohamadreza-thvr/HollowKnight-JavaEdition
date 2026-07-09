package com.hallowKnight.Menus.startGameMenu;

import com.hallowKnight.gameplay.core.SaveManager;

import java.util.ArrayList;
import java.util.List;

public class StartGameModel {
    private List<String> saveSlots;

    public StartGameModel() {
        saveSlots = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            if (SaveManager.saveExists(i)) {
                saveSlots.add("Save Slot " + (i + 1) + " (Continue)");
            } else {
                saveSlots.add("Save Slot " + (i + 1) + " (Empty)");
            }
        }

        saveSlots.add("Start New Game");
    }

    public List<String> getSaveSlots() {
        return saveSlots;
    }
}
