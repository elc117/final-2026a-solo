package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    private Texture texture;
    private Vector2 position;
    private Vector2 direction;
    private float speed = 500;
    private Rectangle hitboxBullet;

    public Bullet(float x, float y, Vector2 direction) {

        texture = new Texture("bullet.png");
        hitboxBullet = new Rectangle(x, y, 32, 32);
        position = new Vector2(x, y);
        this.direction = direction;
    }

    public void update(float delta) { // atualiza a posição da bala com base na direção e velocidade

        position.x += direction.x * speed * delta;
        position.y += direction.y * speed * delta;
        hitboxBullet.setPosition(position.x, position.y); // atualiza a posição do hitbox para acompanhar a bala
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 32, 32);
    }

    public Rectangle getHitboxBullet() { // método para que GameScreen possa acessar o hitbox da bala
        return hitboxBullet;
    }

    public void dispose() {
        texture.dispose();
    }
}