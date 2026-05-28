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
    private float movSpeed = 100;
    private boolean movingRight = true;
    
    public Enemy() {
        textureEnemy = new Texture("enemy.png");
    }
    public void basicMovement(float delta) {

        if (movingRight) {

            x += movSpeed * delta;

            if (x >= 1080) {
                movingRight = false;
            }

        } else {

            x -= movSpeed * delta;

            if (x <= 0) {
                movingRight = true;
            }
        }
    }
    public void render(SpriteBatch batch) {
        batch.draw(textureEnemy, x, y, width, height);
    }

    public void dispose() {
        textureEnemy.dispose();
    }
}