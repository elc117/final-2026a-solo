package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {

    private Texture texture;
    private Vector2 position;
    private Vector2 direction;
    private float speed = 500;

    public Bullet(float x, float y, Vector2 direction) {

        texture = new Texture("bullet.png");

        position = new Vector2(x, y);

        this.direction = direction;
    }

    public void update(float delta) {

        position.x += direction.x * speed * delta;
        position.y += direction.y * speed * delta;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 32, 32);
    }

    public void dispose() {
        texture.dispose();
    }
}