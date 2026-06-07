package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

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
    private Array<Bullet> bulletsEnemy;
    private float timeSinceLastShot = 0f;
    private float currentShootInterval;
    
    public Enemy() {
        textureEnemy = new Texture("enemy.png");
        hitboxEnemy = new Rectangle(x, y, width, height);
        bulletsEnemy = new Array<>();
        currentShootInterval = MathUtils.random(3f, 5f); // intervalo aleatório entre 3 e 5 segundos para os tiros
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

    public void update(float delta, float playerX, float playerY) {
        basicMovement(delta);
        timeSinceLastShot += delta; 
        
        if (timeSinceLastShot >= currentShootInterval) {
            shootAtPlayer(playerX, playerY);
            
            timeSinceLastShot = 0f;
            currentShootInterval = MathUtils.random(3f, 5f);
        }

        for (int i = bulletsEnemy.size - 1; i >= 0; i--) {
            bulletsEnemy.get(i).update(delta);
        }
    }
    
    public void render(SpriteBatch batch) {
        batch.draw(textureEnemy, x, y, width, height);
        for (Bullet b : bulletsEnemy) {
            b.render(batch);
        }
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

    public void shootAtPlayer(float playerX, float playerY) {
        Vector2 direction = new Vector2(playerX - (x + width / 2), playerY - (y + height / 2)).nor();
        bulletsEnemy.add(new Bullet(x + width / 2, y + height / 2, direction));
    }
    
    public boolean verifyDeath() {
        return hpTest <= 0;
    }
}