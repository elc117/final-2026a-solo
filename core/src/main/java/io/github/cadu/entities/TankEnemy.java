package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class TankEnemy extends Enemy {

    public TankEnemy(float startX, float startY, MovementType movementType, float minBound, float maxBound, int slot) {
        super(startX, startY, movementType, minBound, maxBound, slot);

        textureEnemy.dispose(); // descarta a textura do inimigo normal
        textureEnemy = new Texture("tank_enemy.png"); // carrega a textura do tanque
        
        this.hp = 800;
        this.maxHp = 800;
        this.movSpeed = 30;
        this.damage = 30;
    }
    @Override
    public void shootAtPlayer(float playerX, float playerY) {
        Vector2 direction = new Vector2(playerX - (x + width / 2), playerY - (y + height / 2)).nor();
        getBullets().add(new Bullet(x + width / 2, y + height / 2, direction, damage));
    }
}