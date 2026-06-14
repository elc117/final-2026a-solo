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
    private float damage;

    public Bullet(float tipX, float tipY, Vector2 direction, float damage) {
        texture = new Texture("bullet.png");
        
        float startX = tipX - (texture.getWidth() / 2f);
        float startY = tipY - (texture.getHeight() / 2f);
        
        hitboxBullet = new Rectangle(startX, startY, texture.getWidth(), texture.getHeight());
        position = new Vector2(startX, startY);
        this.direction = direction;
        this.damage = damage;
    }

    public void update(float delta) { 
        position.x += direction.x * speed * delta;
        position.y += direction.y * speed * delta;
        hitboxBullet.setPosition(position.x, position.y); 
    }

    public void render(SpriteBatch batch) {
        float width = texture.getWidth();
        float height = texture.getHeight();
        
        float rotation = direction.angleDeg(); 

        batch.draw(
            texture, 
            position.x, position.y, 
            width / 2f, height / 2f, 
            width, height, 
            1f, 1f,                
            rotation,              
            0, 0, 
            (int)width, (int)height, 
            false, false           
        );
    }

    public Rectangle getHitboxBullet() { 
        return hitboxBullet; 
    }

    public float getDamage() { 
        return damage; 
    }

    public void dispose() { 
        texture.dispose(); 
    }
}