package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hallowKnight.Constance;
import com.hallowKnight.gameplay.knight.KnightModel;
import com.hallowKnight.gameplay.map.MapModel;

import java.util.List;

public class EnemyController {

    public void updateEnemies(List<EnemyModel> enemies, float delta, MapModel mapModel, KnightModel knight) {
        if (delta > 0.05f) delta = 0.05f;

        for (EnemyModel enemy : enemies) {
            if (enemy.isKnockedBack()) {
                enemy.setKnockbackTimer(enemy.getKnockbackTimer() - delta);
                if (enemy.getKnockbackTimer() <= 0) enemy.setKnockedBack(false);
            }

            if (!enemy.isDead() && !enemy.isKnockedBack()) {
                if (enemy instanceof CrawlidModel) {
                    updateCrawlid((CrawlidModel) enemy, delta, mapModel);
                } else if (enemy instanceof CrystalCrawlerModel) {
                    updateCrystalCrawler((CrystalCrawlerModel) enemy, delta, mapModel);
                } else if (enemy instanceof MosquitoModel) {
                    updateMosquito((MosquitoModel) enemy, delta, knight);
                } else if (enemy instanceof HuskModel) {
                    updateHusk((HuskModel) enemy, delta, mapModel, knight);
                }
            }

            applyPhysics(enemy, delta, mapModel);
        }
    }

    private void updateCrystalCrawler(CrystalCrawlerModel crawler, float delta, MapModel mapModel) {
        if (crawler.getPlatform() == null) {
            Rectangle searchBox = new Rectangle(crawler.getX() - 10, crawler.getY() - 10, crawler.getBounds().width + 20, crawler.getBounds().height + 20);
            for (Rectangle solid : mapModel.getSolidBounds()) {
                if (solid.overlaps(searchBox)) {
                    crawler.setPlatform(solid);
                    crawler.setEdge(0);
                    break;
                }
            }
            if (crawler.getPlatform() == null) return;
        }

        Rectangle p = crawler.getPlatform();

        float speed = 100f * delta;

        if (crawler.getEdge() == 0) {
            crawler.setX(crawler.getX() + speed);
            crawler.setY(p.y + p.height);
            crawler.setRotation(0f);
            if (crawler.getX() > p.x + p.width - crawler.getBounds().width / 2) {
                crawler.setEdge(1); crawler.setX(p.x + p.width);
            }
        }
        else if (crawler.getEdge() == 1) {
            crawler.setX(p.x + p.width);
            crawler.setY(crawler.getY() - speed);
            crawler.setRotation(-90f);
            if (crawler.getY() < p.y - crawler.getBounds().height / 2) {
                crawler.setEdge(2); crawler.setY(p.y - crawler.getBounds().height);
            }
        }
        else if (crawler.getEdge() == 2) {
            crawler.setX(crawler.getX() - speed);
            crawler.setY(p.y - crawler.getBounds().height);
            crawler.setRotation(-180f);
            if (crawler.getX() < p.x - crawler.getBounds().width / 2) {
                crawler.setEdge(3); crawler.setX(p.x - crawler.getBounds().width);
            }
        }
        else if (crawler.getEdge() == 3) {
            crawler.setX(p.x - crawler.getBounds().width);
            crawler.setY(crawler.getY() + speed);
            crawler.setRotation(90f);
            if (crawler.getY() > p.y + p.height - crawler.getBounds().height / 2) {
                crawler.setEdge(0); crawler.setY(p.y + p.height);
            }
        }

        crawler.updateBounds();
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
            float checkX = crawlid.isFacingRight() ? crawlid.getX() + crawlid.getBounds().width + 5f : crawlid.getX() - 10f;
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
            mosquito.setStateTimer(mosquito.getStateTimer() + delta);
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

    private void updateHusk(HuskModel husk, float delta, MapModel mapModel, KnightModel knight) {
        float visionWidth = 350f;
        if (husk.isFacingRight()) {
            husk.getVisionBounds().set(husk.getX() + husk.getBounds().width, husk.getY(), visionWidth, husk.getBounds().height);
        } else {
            husk.getVisionBounds().set(husk.getX() - visionWidth, husk.getY(), visionWidth, husk.getBounds().height);
        }

        if (!husk.isAggro() && (husk.getCurrentState() == EnemyState.WALK || husk.getCurrentState() == EnemyState.IDLE)) {
            if (husk.getVisionBounds().overlaps(knight.getBounds())) {
                husk.setAggro(true);
                husk.setCurrentState(EnemyState.ANTICIPATE);
                husk.setStateTimer(0f);
                husk.setVelocityX(0);
            }
        }

        if (husk.getCurrentState() == EnemyState.IDLE) {
            husk.setVelocityX(0);
            husk.setStateTimer(husk.getStateTimer() + delta);
            if (husk.getStateTimer() > 2.0f) {
                husk.setCurrentState(EnemyState.WALK);
                husk.setStateTimer(0f);
            }
        }
        else if (husk.getCurrentState() == EnemyState.WALK) {
            float speed = 80f;
            husk.setVelocityX(husk.isFacingRight() ? speed : -speed);
            husk.setStateTimer(husk.getStateTimer() + delta);

            if (husk.getStateTimer() > 4.0f) {
                husk.setCurrentState(EnemyState.IDLE);
                husk.setStateTimer(0f);
            }
        }
        else if (husk.getCurrentState() == EnemyState.TURN) {
            husk.setVelocityX(0);
            husk.setStateTimer(husk.getStateTimer() + delta);
            if (husk.getStateTimer() > 0.2f) {
                husk.setFacingRight(!husk.isFacingRight());
                husk.setCurrentState(EnemyState.WALK);
                husk.setStateTimer(0f);
            }
        }
        else if (husk.getCurrentState() == EnemyState.ANTICIPATE) {
            husk.setVelocityX(0);
            husk.setStateTimer(husk.getStateTimer() + delta);
            if (husk.getStateTimer() >= 1f) {
                husk.setCurrentState(EnemyState.DASH);
                husk.setStateTimer(0f);
            }
        }
        else if (husk.getCurrentState() == EnemyState.DASH) {
            float dashSpeed = 400f;
            husk.setVelocityX(husk.isFacingRight() ? dashSpeed : -dashSpeed);

            if (husk.getVelocityX() == 0) {
                husk.setCurrentState(EnemyState.IDLE);
                husk.setAggro(false);
                husk.setStateTimer(0f);
            }
        }

        if (husk.isGrounded()) {
            float checkX = husk.isFacingRight() ? husk.getX() + husk.getBounds().width + 5f : husk.getX() - 10f;
            Rectangle edgeChecker = new Rectangle(checkX, husk.getY() - 15f, 5f, 15f);

            boolean isGroundAhead = false;
            for (Rectangle solid : mapModel.getSolidBounds()) {
                if (edgeChecker.overlaps(solid)) {
                    isGroundAhead = true;
                    break;
                }
            }

            if (!isGroundAhead) {
                if (husk.getCurrentState() == EnemyState.WALK) {
                    husk.setCurrentState(EnemyState.TURN);
                    husk.setStateTimer(0f);
                } else if (husk.getCurrentState() == EnemyState.DASH || husk.getCurrentState() == EnemyState.ANTICIPATE) {
                    husk.setCurrentState(EnemyState.IDLE);
                    husk.setAggro(false);
                    husk.setVelocityX(0);
                    husk.setStateTimer(0f);
                }
            }
        }
    }

    private void applyPhysics(EnemyModel enemy, float delta, MapModel mapModel) {

        if (enemy instanceof CrystalCrawlerModel && !enemy.isDead() && !enemy.isKnockedBack()) {
            return;
        }

        enemy.setGrounded(false);
        enemy.setX(enemy.getX() + (enemy.getVelocityX() * delta));
        enemy.updateBounds();

        boolean hitWall = false;
        for (Rectangle solid : mapModel.getSolidBounds()) {
            if (enemy.getBounds().overlaps(solid)) {

                boolean isActualWall = true;
                if (enemy instanceof CrawlidModel || enemy instanceof HuskModel) {
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

        if (hitWall && !enemy.isDead() && !enemy.isKnockedBack()) {
            if (enemy instanceof CrawlidModel) {
                ((CrawlidModel) enemy).setCurrentState(EnemyState.TURN);
                ((CrawlidModel) enemy).setStateTimer(0f);
            }
            else if (enemy instanceof HuskModel) {
                HuskModel husk = (HuskModel) enemy;
                if (husk.getCurrentState() == EnemyState.WALK) {
                    husk.setCurrentState(EnemyState.TURN);
                    husk.setStateTimer(0f);
                }
                else if (husk.getCurrentState() == EnemyState.DASH) {
                    husk.setCurrentState(EnemyState.IDLE);
                    husk.setAggro(false);
                    husk.setStateTimer(0f);
                }
            }
        }

        if (enemy instanceof CrawlidModel || enemy instanceof HuskModel || enemy.isDead() || enemy.isKnockedBack()) {
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
                    enemy.setVelocityY(0);
                }
                enemy.updateBounds();
            }
        }
    }
}
