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

            float visualWidth = enemy instanceof CrawlidModel ? 150 : 150;
            float visualHeight = enemy instanceof CrawlidModel ? 100 : 150;

            float offsetX = (visualWidth - enemy.getBounds().width) / 2f;
            float offsetY = 0f;

            batch.draw(frame, enemy.getX() - offsetX, enemy.getY() - offsetY, visualWidth, visualHeight);
        }
    }

    public void dispose() {
        animationManager.dispose();
    }
}
