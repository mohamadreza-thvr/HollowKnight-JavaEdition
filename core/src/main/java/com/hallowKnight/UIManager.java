    package com.hallowKnight;

    import com.badlogic.gdx.Gdx;
    import com.badlogic.gdx.scenes.scene2d.ui.Label;
    import com.badlogic.gdx.scenes.scene2d.ui.Skin;
    import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

    import java.awt.*;

    public class UIManager {

        private static UIManager instance;
        private Skin skin;

        private UIManager() {

            skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        }

        public static UIManager getInstance() {
            if (instance == null) {
                instance = new UIManager();
            }
            return instance;
        }


        public TextButton createButton(String text) {
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));

            style.up = null;
            style.down = null;
            style.over = null;

            TextButton button = new TextButton(text, style);

            button.setWidth(300);

            button.getLabel().setFontScale(2.5f);

            return button;
        }

        public Label createLabel(String text) {
            return new Label(text, skin);
        }

        public void changeLanguageSkin(String newSkinPath) {
            skin.dispose();
            skin = new Skin(Gdx.files.internal(newSkinPath));
        }

        public Skin getSkin() {
            return skin;
        }

        public void dispose() {
            if (skin != null) {
                skin.dispose();
            }
        }
    }
