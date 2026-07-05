package com.hallowKnight.gameplay.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.List;

public class EnemyView {
    private EnemyAnimationManager animationManager;
    private Texture crystalProjTexture;

    public EnemyView() {
        animationManager = new EnemyAnimationManager();
        crystalProjTexture = new Texture(Gdx.files.internal("animation/Crystal_Hunter/Crystal.png"));
    }

    public void render(SpriteBatch batch, float delta, List<EnemyModel> enemies, List<CrystalProjectileModel> projectiles) {
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
                visualHeight = 120;
                offsetY = (visualHeight - enemy.getBounds().height) / 2f;
            } else if (enemy instanceof CrystalCrawlerModel) {
                visualWidth = 70; visualHeight = 90;
                rotation = ((CrystalCrawlerModel) enemy).getRotation();
            } else if (enemy instanceof CrystalHunterModel) {
                visualWidth = 120; visualHeight = 120;
                offsetY = (visualHeight - enemy.getBounds().height) / 2f;
            }

            float offsetX = (visualWidth - enemy.getBounds().width) / 2f;

            batch.draw(frame,
                enemy.getX() - offsetX, enemy.getY() - offsetY,
                visualWidth / 2f, visualHeight / 2f,
                visualWidth, visualHeight,
                1f, 1f,
                rotation);
        }

        for (CrystalProjectileModel proj : projectiles) {
            batch.draw(crystalProjTexture, proj.getX(), proj.getY(), proj.getBounds().width, proj.getBounds().height);
        }
    }

    public void dispose() {
        animationManager.dispose();
        if (crystalProjTexture != null) crystalProjTexture.dispose();
    }
}
