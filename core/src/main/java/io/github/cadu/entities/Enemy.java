package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy {
    private Texture textureEnemy;
    private float width = 200;
    private float height = 200;
    private float hpTest = 100;
    private float x = 640;
    private float y = 700;

    public Enemy() {
        textureEnemy = new Texture("enemy.png");
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureEnemy, x, y, width, height);
    }

    public void dispose() {
        textureEnemy.dispose();
    }
}