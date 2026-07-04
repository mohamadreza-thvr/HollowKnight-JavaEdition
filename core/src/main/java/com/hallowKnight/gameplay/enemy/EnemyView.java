package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class EnemyView {
    private EnemyAnimationManager animationManager;

    public EnemyView() {
        animationManager = new EnemyAnimationManager();
    }

    public void render(SpriteBatch batch, float delta, List<EnemyModel> enemies) {
        for (EnemyModel enemy : enemies) {
            TextureRegion frame = animationManager.getCurrentFrame(delta, enemy);

            float visualWidth = 150;
            float visualHeight = 150;
            float offsetY = 0f;
            float rotation = 0f;

            if (enemy instanceof CrawlidModel) {
                visualWidth = 160; visualHeight = 80;
            } else if (enemy instanceof MosquitoModel) {
                visualWidth = 120;
                visualHeight= 120;
                offsetY = (visualHeight - enemy.getBounds().height) / 2f;
            } else if (enemy instanceof CrystalCrawlerModel) {
                visualWidth = 70; visualHeight = 90;
                rotation = ((CrystalCrawlerModel) enemy).getRotation();
            }

            float offsetX = (visualWidth - enemy.getBounds().width) / 2f;

            batch.draw(frame,
                enemy.getX() - offsetX, enemy.getY() - offsetY,
                visualWidth / 2f, visualHeight / 2f,
                visualWidth, visualHeight,
                1f, 1f,
                rotation);
        }
    }

    public void dispose() {
        animationManager.dispose();
    }
}
