package com.hallowKnight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hallowKnight.Menus.achievements.AchievementsScreen;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    MainMenuScreen mainMenuScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
