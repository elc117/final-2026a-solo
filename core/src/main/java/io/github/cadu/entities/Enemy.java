package io.github.cadu.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.audio.Sound;

public class Enemy {
    public enum MovementType { HORIZONTAL, VERTICAL }
    protected Texture textureEnemy;
    protected float width = 200;
    protected float height = 200;
    protected float hp = 250;
    protected float maxHp = 250;
    protected float movSpeed = 70;
    protected float damage = 60;
    protected float x;
    protected float y;

    private Rectangle hitboxEnemy;
    private Array<Bullet> bulletsEnemy;
    private float timeSinceLastShot = 0f;
    private float currentShootInterval;

    private MovementType movementType;  
    private boolean movingPositive = true;
    private float minBound;
    private float maxBound;
    private int slot;

    private boolean spawning = true;
    private float spawnTimer = 0f;
    private float spawnDuration = 0.4f;
    private Sound enemyShootSound;
    private Sound enemyHitSound;
    
    
    public Enemy(float startX, float startY, MovementType movementType, float minBound, float maxBound, int slot) {
        this.x = startX;
        this.y = startY;
        this.movementType = movementType;
        this.minBound = minBound;
        this.maxBound = maxBound;
        this.slot = slot;

        textureEnemy = new Texture("enemy.png");
        hitboxEnemy = new Rectangle(x, y, width, height);
        bulletsEnemy = new Array<>();
        currentShootInterval = MathUtils.random(2f, 4f); // intervalo aleatório entre 2 e 4 segundos para os tiros

        enemyShootSound = Gdx.audio.newSound(Gdx.files.internal("enemy_shoot.wav"));
        enemyHitSound = Gdx.audio.newSound(Gdx.files.internal("enemy_hit.wav"));
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

        if (spawning) {
            spawnTimer += delta;
            if (spawnTimer >= spawnDuration) {
                spawning = false;
            }
            return; 
        }

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
        if (spawning) {
            float alpha = spawnTimer / spawnDuration;
            batch.setColor(1, 1, 1, alpha);
        }

        batch.draw(textureEnemy, x, y, width, height);

        if (spawning) {
            batch.setColor(1, 1, 1, 1);
        }

        for (Bullet b : bulletsEnemy) {
            b.render(batch);
        }
    }

    public Rectangle getHitboxEnemy() {
        return hitboxEnemy;
    }

    public void dispose() {
        textureEnemy.dispose();
        enemyShootSound.dispose();
        enemyHitSound.dispose();
    }
    
    public void takeDamage(float damage) {
        hp -= damage;
        enemyHitSound.play(0.5f);
    }

    public void hpStatus() {
        System.out.println("HP do inimigo: " + hp);
    }

    protected void shootAtPlayer(float playerX, float playerY) {
        Vector2 direction = new Vector2(playerX - (x + width / 2), playerY - (y + height / 2)).nor();
        enemyShootSound.play(0.5f);
        bulletsEnemy.add(new Bullet(x + width / 2, y + height / 2, direction, damage));
    }
    
    public boolean verifyDeath() {
        return hp <= 0;
    }

        public Array<Bullet> getBullets() {
        return bulletsEnemy;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isSpawning() {
        return spawning;
    }

    // Getters para pos e hp para fazer a render da barra de vida do inimigo
    public float getX() { return x; }
    public float getY() { return y; }
    public float getHp() { return hp; }
    public float getMaxHp() { return maxHp; } // baseado no seu hp inicial
}
