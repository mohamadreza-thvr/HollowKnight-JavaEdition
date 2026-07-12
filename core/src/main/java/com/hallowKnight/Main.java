package com.hallowKnight;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hallowKnight.Menus.mainMenu.MainMenuScreen;
import com.hallowKnight.gameplay.core.AudioManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    MainMenuScreen mainMenuScreen;

    private ShapeRenderer globalShapeRenderer;

    private static final String CURSOR_IMAGE_PATH = "Cursor.png";
    private static final int CURSOR_HOTSPOT_X = 0;
    private static final int CURSOR_HOTSPOT_Y = 0;

    private Cursor customCursor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        globalShapeRenderer = new ShapeRenderer();

        AudioManager.loadSFX();

        setCustomCursor();

        mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
    }

    private void setCustomCursor() {

        Pixmap originalPixmap = new Pixmap(Gdx.files.internal(CURSOR_IMAGE_PATH));

        int po2Width = nextPowerOfTwo(originalPixmap.getWidth());
        int po2Height = nextPowerOfTwo(originalPixmap.getHeight());

        Pixmap cursorPixmap = originalPixmap;
        if (po2Width != originalPixmap.getWidth() || po2Height != originalPixmap.getHeight()) {
            cursorPixmap = new Pixmap(po2Width, po2Height, originalPixmap.getFormat());
            cursorPixmap.setFilter(Pixmap.Filter.BiLinear);
            cursorPixmap.drawPixmap(
                originalPixmap,
                0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
                0, 0, po2Width, po2Height
            );
            originalPixmap.dispose();
        }

        customCursor = Gdx.graphics.newCursor(cursorPixmap, CURSOR_HOTSPOT_X, CURSOR_HOTSPOT_Y);
        Gdx.graphics.setCursor(customCursor);
        cursorPixmap.dispose();
    }

    private static int nextPowerOfTwo(int value) {
        int result = 1;
        while (result < value) {
            result <<= 1;
        }
        return result;
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
        if (customCursor != null) {
            customCursor.dispose();
        }
    }
}
