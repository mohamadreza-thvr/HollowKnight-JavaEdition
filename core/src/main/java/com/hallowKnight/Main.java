package com.hallowKnight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;
import com.hallowKnight.gameplay.core.AudioManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    MainMenuScreen mainMenuScreen;

    private ShapeRenderer globalShapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        globalShapeRenderer = new ShapeRenderer();

        AudioManager.loadSFX();

        mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
    }

    @Override
    public void render() {
        super.render();

        Preferences prefs = Gdx.app.getPreferences("HollowKnightSettings");
        float brightness = prefs.getFloat("brightness", 1.0f);

        if (brightness < 1.0f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            globalShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            globalShapeRenderer.setColor(0, 0, 0, 1.0f - brightness);
            globalShapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            globalShapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        AudioManager.dispose();
        if (globalShapeRenderer != null) {
            globalShapeRenderer.dispose();
        }
    }
}
