package io.github.cadu.entities;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public class SniperEnemy extends Enemy {

    public SniperEnemy(float startX, float startY, MovementType movementType, float minBound, float maxBound, int slot, int currentPhase) {
        super(startX, startY, movementType, minBound, maxBound, slot, currentPhase); 

        textureEnemy.dispose(); 
        textureEnemy = new Texture("sniper_enemy.png"); 

        this.width = 150;
        this.height = 225;
        getHitboxEnemy().setSize(width, height); 

        this.minShootInterval = 3f;
        this.maxShootInterval = 5f; 

        this.hp = 150; 
        this.damage = 100; 
        this.movSpeed = 50; 
        this.maxHp = 150;
    }

    @Override
    public void shootAtPlayer(float playerX, float playerY) {
        float centerX = x + width / 2;
        float centerY = y + height / 2;
        Vector2 direction = new Vector2(playerX - centerX, playerY - centerY).nor();
        
        float offset = height / 2; 
        float spawnX = centerX + (direction.x * offset);
        float spawnY = centerY + (direction.y * offset);
        
        enemyShootSound.play(0.5f); // <--- ADICIONE ESTA LINHA AQUI!
        getBullets().add(new Bullet(spawnX, spawnY, direction, damage));
    }
}