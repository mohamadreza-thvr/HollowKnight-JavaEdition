package com.hallowKnight.in_game_menus.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.hallowKnight.UIManager;
import com.hallowKnight.gameplay.knight.KnightModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryView {
    private Stage stage;
    private Texture darkOverlay;
    private KnightModel knightModel;

    private Map<KnightModel.Charm, Texture> charmTextures;
    private Map<KnightModel.Charm, String> charmDescriptions;
    private Texture emptyNotchTex;
    private Image[] notchImages;
    private Map<KnightModel.Charm, Image> gridImages;

    private Label descriptionLabel;

    public InventoryView(KnightModel knightModel) {
        this.knightModel = knightModel;
        stage = new Stage(new StretchViewport(1280, 720));
        UIManager uiManager = UIManager.getInstance();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.1f, 0.1f, 0.2f, 0.9f));
        pixmap.fill();
        darkOverlay = new Texture(pixmap);
        pixmap.dispose();

        Image backgroundImage = new Image(darkOverlay);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        charmTextures = new HashMap<>();
        charmTextures.put(KnightModel.Charm.SOUL_CATCHER, new Texture(Gdx.files.internal("charms/SoulCatcher.png")));
        charmTextures.put(KnightModel.Charm.DASHMASTER, new Texture(Gdx.files.internal("charms/Dashmaster.png")));
        charmTextures.put(KnightModel.Charm.UNBREAKABLE_STRENGTH, new Texture(Gdx.files.internal("charms/Unbreakable Strength.png")));
        charmTextures.put(KnightModel.Charm.QUICK_SLASH, new Texture(Gdx.files.internal("charms/QuickSlash.png")));
        charmTextures.put(KnightModel.Charm.QUICK_FOCUS, new Texture(Gdx.files.internal("charms/QuickFocus.png")));
        charmTextures.put(KnightModel.Charm.HEAVY_BLOW, new Texture(Gdx.files.internal("charms/HeavyBlow.png")));
        charmTextures.put(KnightModel.Charm.SHARP_SHADOW, new Texture(Gdx.files.internal("charms/SharpShadow.png")));
        charmTextures.put(KnightModel.Charm.VOID_HEART, new Texture(Gdx.files.internal("charms/VoidHeart.png")));

        charmDescriptions = new HashMap<>();
        charmDescriptions.put(KnightModel.Charm.SOUL_CATCHER, "Soul Catcher: Increases the amount of Soul gained per successful Nail strike on enemies.");
        charmDescriptions.put(KnightModel.Charm.DASHMASTER, "Dashmaster: Allows the player to dash in shorter time intervals.");
        charmDescriptions.put(KnightModel.Charm.UNBREAKABLE_STRENGTH, "Unbreakable Strength: Strengthens the Knight, increasing damage dealt to enemies by normal Nail strikes.");
        charmDescriptions.put(KnightModel.Charm.QUICK_SLASH, "Quick Slash: Greatly increases Nail attack speed and reduces the cooldown after each normal strike.");
        charmDescriptions.put(KnightModel.Charm.QUICK_FOCUS, "Quick Focus: Increases the speed of focusing Soul to heal, shortening the time the player remains stationary.");
        charmDescriptions.put(KnightModel.Charm.HEAVY_BLOW, "Heavy Blow: Increases the knockback force of strikes, knocking enemies further back.");
        charmDescriptions.put(KnightModel.Charm.SHARP_SHADOW, "Sharp Shadow: Dash through enemies to deal damage without taking damage. Increases dash length by 20%.");
        charmDescriptions.put(KnightModel.Charm.VOID_HEART, "Void Heart: Upgrades abilities. Ability damage is increased by 50% and their dark animations are activated.");

        Pixmap notchPix = new Pixmap(80, 80, Pixmap.Format.RGBA8888);
        notchPix.setColor(Color.DARK_GRAY);
        notchPix.drawCircle(40, 40, 38);
        emptyNotchTex = new Texture(notchPix);
        notchPix.dispose();

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Label title = uiManager.createLabel("INVENTORY & CHARMS");
        title.setFontScale(1.5f);
        Label info = uiManager.createLabel("Press 'I' again to return");
        info.setColor(Color.GRAY);

        mainTable.add(title).padBottom(10).row();
        mainTable.add(info).padBottom(40).row();

        Table notchesTable = new Table();
        notchImages = new Image[3];
        for (int i = 0; i < 3; i++) {
            notchImages[i] = new Image(emptyNotchTex);
            final int notchIndex = i;

            notchImages[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    List<KnightModel.Charm> equipped = knightModel.getEquippedCharms();
                    if (notchIndex < equipped.size()) {
                        KnightModel.Charm charmToRemove = equipped.get(notchIndex);
                        knightModel.unequipCharm(charmToRemove);
                        updateUI();
                    }
                }
            });

            notchesTable.add(notchImages[i]).size(80).pad(15);
        }
        mainTable.add(notchesTable).padBottom(30).row();

        Table gridTable = new Table();
        gridImages = new HashMap<>();

        int index = 0;
        for (final KnightModel.Charm charm : KnightModel.Charm.values()) {
            Texture tex = charmTextures.get(charm);
            Image charmImg = new Image(tex);

            charmImg.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (knightModel.isCharmEquipped(charm)) {
                        knightModel.unequipCharm(charm);
                    } else {
                        knightModel.equipCharm(charm);
                    }
                    updateUI();
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    descriptionLabel.setText(charmDescriptions.get(charm));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    descriptionLabel.setText("");
                }
            });

            gridImages.put(charm, charmImg);
            gridTable.add(charmImg).size(80).pad(15);

            index++;
            if (index % 4 == 0) {
                gridTable.row();
            }
        }

        mainTable.add(gridTable).padBottom(40).row();

        descriptionLabel = uiManager.createLabel("");
        descriptionLabel.setColor(Color.CYAN);
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);

        mainTable.add(descriptionLabel).width(800).height(80).row();

        stage.addActor(mainTable);

        updateUI();
    }

    public void updateUI() {
        for (KnightModel.Charm charm : KnightModel.Charm.values()) {
            Image img = gridImages.get(charm);
            if (knightModel.isCharmEquipped(charm)) {
                img.setColor(Color.DARK_GRAY);
            } else {
                img.setColor(Color.WHITE);
            }
        }

        List<KnightModel.Charm> equipped = knightModel.getEquippedCharms();
        for (int i = 0; i < 3; i++) {
            if (i < equipped.size()) {
                Texture tex = charmTextures.get(equipped.get(i));
                notchImages[i].setDrawable(new TextureRegionDrawable(tex));
            } else {
                notchImages[i].setDrawable(new TextureRegionDrawable(emptyNotchTex));
            }
        }
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() { return stage; }

    public void dispose() {
        stage.dispose();
        darkOverlay.dispose();
        emptyNotchTex.dispose();
        for (Texture tex : charmTextures.values()) {
            tex.dispose();
        }
    }
}
