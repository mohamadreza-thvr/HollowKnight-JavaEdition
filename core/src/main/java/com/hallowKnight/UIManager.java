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

        // متد کمکی برای ساخت دکمه
        public TextButton createButton(String text) {
            // ۱. گرفتن استایل پیش‌فرض از اسکین
            TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));

            style.up = null;   // حالت عادی
            style.down = null; // حالتی که کلیک شده
            style.over = null; // حالتی که موس روی آن است (Hover)

            // ساخت دکمه با استایل جدید
            TextButton button = new TextButton(text, style);

            button.setWidth(300);

            button.getLabel().setFontScale(2.5f);

            return button;
        }

        // متد کمکی برای ساخت لیبل (متن)
        public Label createLabel(String text) {
            return new Label(text, skin);
        }

        // متد برای تغییر زبان (آینده‌نگری برای تسک Localization)
        public void changeLanguageSkin(String newSkinPath) {
            skin.dispose(); // پاک کردن اسکین قبلی
            skin = new Skin(Gdx.files.internal(newSkinPath));
        }

        public Skin getSkin() {
            return skin;
        }

        // برای جلوگیری از نشت حافظه هنگام خروج از بازی
        public void dispose() {
            if (skin != null) {
                skin.dispose();
            }
        }
    }
