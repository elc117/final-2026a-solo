package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class Enemy {
    private Texture textureEnemy;
    public enum MovementType { HORIZONTAL, VERTICAL }
    private float width = 200;
    private float height = 200;
    private float hpTest = 250;

    private Rectangle hitboxEnemy;
    private Array<Bullet> bulletsEnemy;
    private float timeSinceLastShot = 0f;
    private float currentShootInterval;

    private MovementType movementType;  
    private boolean movingPositive = true;
    private float minBound;
    private float maxBound;
    private float x;
    private float y;
    private float movSpeed = 70;
    
    
    public Enemy(float startX, float startY, MovementType movementType, float minBound, float maxBound) {
        this.x = startX;
        this.y = startY;
        this.movementType = movementType;
        this.minBound = minBound;
        this.maxBound = maxBound;

        textureEnemy = new Texture("enemy.png");
        hitboxEnemy = new Rectangle(x, y, width, height);
        bulletsEnemy = new Array<>();
        currentShootInterval = MathUtils.random(2f, 4f); // intervalo aleatório entre 2 e 4 segundos para os tiros
    }
    
    public void basicMovement(float delta) {
        if (movementType == MovementType.HORIZONTAL) {
            if (movingPositive) {
                x += movSpeed * delta;
                if (x >= maxBound) movingPositive = false;
            } else {
                x -= movSpeed * delta;
                if (x <= minBound) movingPositive = true;
            }
        } 
        else if (movementType == MovementType.VERTICAL) {
            if (movingPositive) {
                y += movSpeed * delta;
                if (y >= maxBound) movingPositive = false; 
            } else {
                y -= movSpeed * delta;
                if (y <= minBound) movingPositive = true;
            }
        }
            
        hitboxEnemy.setPosition(x, y); 
    }

    public void update(float delta, float playerX, float playerY) {
        basicMovement(delta);
        timeSinceLastShot += delta; 
        
        if (timeSinceLastShot >= currentShootInterval) {
            shootAtPlayer(playerX, playerY);
            
            timeSinceLastShot = 0f;
            currentShootInterval = MathUtils.random(2f, 4f);
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

        public Array<Bullet> getBullets() {
        return bulletsEnemy;
    }
}
