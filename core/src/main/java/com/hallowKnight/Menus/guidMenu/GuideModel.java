package com.hallowKnight.Menus.guidMenu;

import java.util.LinkedHashMap;
import java.util.Map;

public class GuideModel {

    private Map<String, String> controls;
    private Map<String, String> cheatCodes;
    private Map<String, String> mechanics;

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

        cheatCodes = new LinkedHashMap<>();
        cheatCodes.put("Boss Teleport", "keys/key-b.png");
        cheatCodes.put("Noclip Mode", "keys/key-n.png");
        cheatCodes.put("Emergency Heal", "keys/key-h.png");
        cheatCodes.put("Refill Soul", "keys/key-r.png");
        cheatCodes.put("God Mode", "keys/key-g.png");
        cheatCodes.put("Insta-Kill Enemies", "keys/key-k.png");

        mechanics = new LinkedHashMap<>();
        mechanics.put("Health (Masks)", "Take damage from enemies or hazards. Heal by using Focus.");
        mechanics.put("Soul (Vessel)", "Gain Soul by striking enemies with your Nail. Use it to Heal or cast Spells.");
        mechanics.put("Abilities", "Dash (C) allows quick evasion. Spells (S) consume Soul to deal heavy damage.");
    }

    public Map<String, String> getControls() { return controls; }
    public Map<String, String> getCheatCodes() { return cheatCodes; }
    public Map<String, String> getMechanics() { return mechanics; }
}
