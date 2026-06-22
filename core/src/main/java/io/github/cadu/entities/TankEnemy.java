package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class TankEnemy extends Enemy {

    public TankEnemy(float startX, float startY, MovementType movementType, float minBound, float maxBound, int slot, int currentPhase) {
        super(startX, startY, movementType, minBound, maxBound, slot, currentPhase); 

        textureEnemy.dispose(); 
        textureEnemy = new Texture("tank_enemy.png"); 

        this.width = 180; 
        this.height = 270;
        getHitboxEnemy().setSize(width, height); 

        this.minShootInterval = 1.5f;
        this.maxShootInterval = 3f; 

        this.hp = 800;
        this.maxHp = 800;
        this.movSpeed = 30;
        this.damage = 20;
        this.coinReward = currentPhase + 2; // especiais dao mais moedas
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