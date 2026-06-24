package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.Constance;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;

import java.util.List;

public class EnemyController {

    public void updateEnemies(List<EnemyModel> enemies, float delta, MapModel mapModel, KnightModel knight) {
        if (delta > 0.05f) {
            delta = 0.05f;
        }
        for (EnemyModel enemy : enemies) {

            if (enemy.isKnockedBack()) {
                enemy.setKnockbackTimer(enemy.getKnockbackTimer() - delta);
                if (enemy.getKnockbackTimer() <= 0) {
                    enemy.setKnockedBack(false);
                }
            }

            if (!enemy.isDead() && !enemy.isKnockedBack()) {
                if (enemy instanceof CrawlidModel) {
                    updateCrawlid((CrawlidModel) enemy, delta, mapModel);
                } else if (enemy instanceof MosquitoModel) {
                    updateMosquito((MosquitoModel) enemy, delta, knight);
                }
            }

            applyPhysics(enemy, delta, mapModel);
        }
    }

    private void updateCrawlid(CrawlidModel crawlid, float delta, MapModel mapModel) {
        if (crawlid.getCurrentState() == EnemyState.TURN) {
            crawlid.setVelocityX(0);
            crawlid.setStateTimer(crawlid.getStateTimer() + delta);

            if (crawlid.getStateTimer() > 0.2f) {
                crawlid.setFacingRight(!crawlid.isFacingRight());
                crawlid.setCurrentState(EnemyState.WALK);
                crawlid.setStateTimer(0f);
            }
            return;
        }

        float speed = 100f;
        crawlid.setVelocityX(crawlid.isFacingRight() ? speed : -speed);

        if (crawlid.isGrounded()) {
            float checkX = crawlid.isFacingRight()
                ? crawlid.getX() + crawlid.getBounds().width + 5f
                : crawlid.getX() - 10f;
            Rectangle edgeChecker = new Rectangle(checkX, crawlid.getY() - 15f, 5f, 15f);

            boolean isGroundAhead = false;
            for (Rectangle solid : mapModel.getSolidBounds()) {
                if (edgeChecker.overlaps(solid)) {
                    isGroundAhead = true;
                    break;
                }
            }

            if (!isGroundAhead) {
                crawlid.setCurrentState(EnemyState.TURN);
                crawlid.setStateTimer(0f);
            }
        }
    }

    private void updateMosquito(MosquitoModel mosquito, float delta, KnightModel knight) {
        float distance = Vector2.dst(mosquito.getX(), mosquito.getY(), knight.getX(), knight.getY());
        if (!mosquito.isHasAggro() && distance < 400f) {
            mosquito.setHasAggro(true);
        }

        if (mosquito.getCurrentState() == EnemyState.IDLE) {
            mosquito.setStateTimer(mosquito.getStateTimer() + delta); // تایمر کوول‌داون حمله
            mosquito.setVelocityX(0f);
            mosquito.setVelocityY(0f);
            mosquito.setFacingRight(knight.getX() > mosquito.getX());

            if (mosquito.isHasAggro() && mosquito.getStateTimer() >= 1.5f) {
                mosquito.getTargetPosition().set(knight.getX(), knight.getY());
                mosquito.setCurrentState(EnemyState.ANTICIPATE);
                mosquito.setStateTimer(0f);
            }
        }
        else if (mosquito.getCurrentState() == EnemyState.ANTICIPATE) {
            mosquito.setStateTimer(mosquito.getStateTimer() + delta);
            if (mosquito.getStateTimer() >= 0.6f) {
                mosquito.setCurrentState(EnemyState.DASH);
                mosquito.setStateTimer(0f);

                Vector2 direction = new Vector2(
                    mosquito.getTargetPosition().x - mosquito.getX(),
                    mosquito.getTargetPosition().y - mosquito.getY()
                );
                direction.nor();

                float dashSpeed = 450f;
                mosquito.setVelocityX(direction.x * dashSpeed);
                mosquito.setVelocityY(direction.y * dashSpeed);
            }
        }
        else if (mosquito.getCurrentState() == EnemyState.DASH) {
            mosquito.setStateTimer(mosquito.getStateTimer() + delta);

            if (mosquito.getStateTimer() >= 1.0f || (mosquito.getVelocityX() == 0 && mosquito.getVelocityY() == 0)) {
                mosquito.setCurrentState(EnemyState.IDLE);
                mosquito.setStateTimer(0f);
                mosquito.setVelocityX(0);
                mosquito.setVelocityY(0);
            }
        }
    }

    private void applyPhysics(EnemyModel enemy, float delta, MapModel mapModel) {
        enemy.setGrounded(false);

        enemy.setX(enemy.getX() + (enemy.getVelocityX() * delta));
        enemy.updateBounds();

        boolean hitWall = false;
        for (Rectangle solid : mapModel.getSolidBounds()) {
            if (enemy.getBounds().overlaps(solid)) {

                boolean isActualWall = true;
                if (enemy instanceof CrawlidModel) {
                    isActualWall = (solid.y + solid.height > enemy.getY() + 5f);
                }

                if (isActualWall) {
                    if (enemy.getVelocityX() > 0) {
                        enemy.setX(solid.x - enemy.getBounds().width);
                    } else if (enemy.getVelocityX() < 0) {
                        enemy.setX(solid.x + solid.width);
                    }

                    enemy.setVelocityX(0);
                    enemy.updateBounds();
                    hitWall = true;
                }
            }
        }

        if (hitWall && enemy instanceof CrawlidModel && !enemy.isDead() && !enemy.isKnockedBack()) {
            ((CrawlidModel) enemy).setCurrentState(EnemyState.TURN);
            ((CrawlidModel) enemy).setStateTimer(0f);
        }

        if (enemy instanceof CrawlidModel || enemy.isDead() || enemy.isKnockedBack()) {
            enemy.setVelocityY(enemy.getVelocityY() - (Constance.GRAVITY * delta));
        }

        enemy.setY(enemy.getY() + (enemy.getVelocityY() * delta));
        enemy.updateBounds();

        for (Rectangle solid : mapModel.getSolidBounds()) {
            if (enemy.getBounds().overlaps(solid)) {
                if (enemy.getVelocityY() < 0) {
                    enemy.setY(solid.y + solid.height);
                    enemy.setGrounded(true);

                    if (enemy.isDead() && !enemy.isHitGroundAfterDeath()) {
                        enemy.setHitGroundAfterDeath(true);
                        enemy.setVelocityX(0);
                    }
                    enemy.setVelocityY(0);
                }
                else if (enemy.getVelocityY() > 0) {
                    enemy.setY(solid.y - enemy.getBounds().height);

                    if (enemy instanceof MosquitoModel) {
                        enemy.setVelocityY(0);
                    } else {
                        enemy.setVelocityY(0);
                    }
                }
                enemy.updateBounds();
            }
        }
    }
}
