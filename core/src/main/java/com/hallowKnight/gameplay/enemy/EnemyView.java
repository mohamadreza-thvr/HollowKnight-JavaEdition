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

            float visualWidth = enemy instanceof CrawlidModel ? 180 : 150;
            float visualHeight = enemy instanceof CrawlidModel ? 120 : 150;

            float offsetX = (visualWidth - enemy.getBounds().width) / 2f;

            float offsetY = 0;
            if (enemy instanceof CrawlidModel) {
                offsetY = 0f;
            } else if (enemy instanceof MosquitoModel){
                offsetY = (visualHeight - enemy.getBounds().height) / 2f;
            } else if(enemy instanceof HuskModel){
                offsetY = 0f;
            }

            batch.draw(frame, enemy.getX() - offsetX, enemy.getY() - offsetY, visualWidth, visualHeight);
        }
    }

    public void dispose() {
        animationManager.dispose();
    }
}
