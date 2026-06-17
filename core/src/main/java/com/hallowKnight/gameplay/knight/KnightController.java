package com.hallowKnight.gameplay.knight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.hallowKnight.Constance;

public class KnightController {
    private KnightModel model;

    public KnightController(KnightModel model) {
        this.model = model;
    }

    public void update(float delta) {
        model.setWasGrounded(model.isGrounded());

        handleInput();
        applyPhysics(delta);

        if (!model.wasGrounded() && model.isGrounded()) {
            model.setLanding(true);
            model.setLandingTimer(0.2f);
        }

        if (model.isLanding()) {
            model.setLandingTimer(model.getLandingTimer() - delta);
            if (model.getLandingTimer() <= 0) {
                model.setLanding(false);
            }
        }
    }

    private void handleInput() {
        model.setVelocityX(0);
        model.setLookingUp(false);
        model.setLookingDown(false);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            model.setVelocityX(-Constance.KNIGHT_SPEED);
            model.setFacingRight(false);
            model.setLanding(false);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            model.setVelocityX(Constance.KNIGHT_SPEED);
            model.setFacingRight(true);
            model.setLanding(false);
        }

        if (model.isGrounded() && model.getVelocityX() == 0) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if(!model.isLanding()){
                    model.setLookingUp(true);
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                model.setLookingDown(true);
                model.setLanding(false);
            }
        }

        // پرش
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && model.isGrounded()) {
            model.setVelocityY(Constance.JUMP_FORCE);
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
            model.setVelocityY(model.getVelocityY() - (Constance.GRAVITY * delta));
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
