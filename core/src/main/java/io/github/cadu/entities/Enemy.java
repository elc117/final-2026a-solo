package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    private Texture textureEnemy;
    private float width = 200;
    private float height = 200;
    private float hpTest = 200;
    private float x = 640;
    private float y = 700;
    private float movSpeed = 100;
    private boolean movingRight = true;
    private Rectangle hitboxEnemy;
    
    public Enemy() {
        textureEnemy = new Texture("enemy.png");
        hitboxEnemy = new Rectangle(x, y, width, height);
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
        // atualiza a posição do hitbox para acompanhar o inimigo
        hitboxEnemy.setPosition(x, y); 
    }
    
    public void render(SpriteBatch batch) {
        batch.draw(textureEnemy, x, y, width, height);
    }

    public Rectangle getHitboxEnemy() {
        return hitboxEnemy;
    }

    public void dispose() {
        textureEnemy.dispose();
    }
    
    public void takeDamage(float damage) {
        hpTest -= damage;
    }

    public void hpStatus() {
        System.out.println("HP do inimigo: " + hpTest);
    }
    
    public boolean verifyDeath() {
        return hpTest <= 0;
    }
}