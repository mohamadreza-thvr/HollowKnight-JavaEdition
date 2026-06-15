package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KnightController {
    private KnightModel model;

    public KnightController(KnightModel model) {
        this.model = model;
    }

    public void update(float delta) {
        // قبل از هر آپدیتی، وضعیت قبلیِ روی زمین بودن را ذخیره می‌کنیم
        model.setWasGrounded(model.isGrounded());

        handleInput();
        applyPhysics(delta);

        // --- سیستم تشخیص فرود (Landing) ---
        // اگر در فریم قبلی در هوا بوده و الان روی زمین است = برخورد با زمین
        if (!model.wasGrounded() && model.isGrounded()) {
            model.setLanding(true);
            model.setLandingTimer(0.2f); // زمان انیمیشن فرود (مثلا 0.2 ثانیه)
        }

        // اگر در حال فرود است، تایمر را کم کن
        if (model.isLanding()) {
            model.setLandingTimer(model.getLandingTimer() - delta);
            if (model.getLandingTimer() <= 0) {
                model.setLanding(false); // زمان فرود تمام شد، برگرد به حالت عادی
            }
        }
    }

    private void handleInput() {
        model.setVelocityX(0);
        model.setLookingUp(false);
        model.setLookingDown(false);

        // حرکت به چپ و راست (لغو کردن فرود در صورت حرکت)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            model.setVelocityX(-model.getSpeed());
            model.setFacingRight(false);
            model.setLanding(false); // اگر دکمه حرکت را زد، فرود را لغو کن تا بازی روان بماند
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            model.setVelocityX(model.getSpeed());
            model.setFacingRight(true);
            model.setLanding(false);
        }

        // نگاه به بالا و پایین (فقط در صورتی که روی زمین باشد و حرکت نکند)
        if (model.isGrounded() && model.getVelocityX() == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                model.setLookingUp(true);
                model.setLanding(false); // لغو فرود برای نگاه به بالا
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                model.setLookingDown(true);
                model.setLanding(false);
            }
        }

        // پرش
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && model.isGrounded()) {
            model.setVelocityY(model.getJumpForce());
            model.setGrounded(false);
            model.setLanding(false);
        }

        // کنترل ارتفاع پرش
        if (!Gdx.input.isKeyPressed(Input.Keys.Z) && model.getVelocityY() > 0) {
            model.setVelocityY(model.getVelocityY() * 0.5f);
        }
    }

    private void applyPhysics(float delta) {
        // اعمال جاذبه
        if (!model.isGrounded()) {
            model.setVelocityY(model.getVelocityY() - (model.getGravity() * delta));
        }

        // اعمال سرعت به موقعیت
        model.setX(model.getX() + (model.getVelocityX() * delta));
        model.setY(model.getY() + (model.getVelocityY() * delta));

        //  زمین موقت
        if (model.getY() <= 100) {
            model.setY(100);
            model.setVelocityY(0);
            model.setGrounded(true);
        } else {
            model.setGrounded(false);
        }
    }
}
