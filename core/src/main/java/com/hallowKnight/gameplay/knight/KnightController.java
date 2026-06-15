package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KnightController {
    private KnightModel model;

    public KnightController(KnightModel model) {
        this.model = model;
    }

    public void update(float delta) {
        handleInput();
        applyPhysics(delta);
    }

    private void handleInput() {
        model.setVelocityX(0); // توقف در صورت رها کردن دکمه‌ها

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            model.setVelocityX(-model.getSpeed());
            model.setFacingRight(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            model.setVelocityX(model.getSpeed());
            model.setFacingRight(true);
        }

        // پرش
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && model.isGrounded()) {
            model.setVelocityY(model.getJumpForce());
            model.setGrounded(false);
        }

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
