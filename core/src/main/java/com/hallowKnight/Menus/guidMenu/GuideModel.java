package com.hallowKnight.Menus.guidMenu;

import java.util.LinkedHashMap;
import java.util.Map;

public class GuideModel {

    private Map<String, String> controls;

    public GuideModel() {
        controls = new LinkedHashMap<>();

        controls.put("Move Left / Right", "keys/arrows_lr2.png");
        controls.put("Look Up / Down", "keys/arrows_ud2.png");
        controls.put("Jump / Double Jump", "keys/keys-z.png");
        controls.put("Attack (Nail)", "keys/key-x.png");
        controls.put("Dash", "keys/key-c.png");
        controls.put("Focus (Heal)", "keys/key-a.png");
        controls.put("Inventory / Charms", "keys/key-i.png");
        controls.put("Pause Game", "keys/key-esc.png");
    }

    public Map<String, String> getControls() {
        return controls;
    }
}
