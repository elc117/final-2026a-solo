package io.github.cadu.entities;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public class SniperEnemy extends Enemy {

    public SniperEnemy(float startX, float startY, MovementType movementType, float minBound, float maxBound, int slot) {
        super(startX, startY, movementType, minBound, maxBound, slot);

        textureEnemy.dispose(); // descarta a textura do inimigo normal
        textureEnemy = new Texture("sniper_enemy.png"); // carrega a textura do sniper

        this.hp = 150; // tem menos hp que o inimigo normal
        this.damage = 120; // dobro de dano do inimigo normal
        this.movSpeed = 50; // velocidade intermediária entre o inimigo normal e o tanque
        this.maxHp = 150;
    }

    @Override
    public void shootAtPlayer(float playerX, float playerY) {
        Vector2 direction = new Vector2(playerX - (x + width / 2), playerY - (y + height / 2)).nor();
        getBullets().add(new Bullet(x + width / 2, y + height / 2, direction, damage));
    }
}