package io.github.cadu.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Planet {

    public float x;
    public float y;

    private Texture texture;

    public Planet(float x, float y) {

        this.x = x;
        this.y = y;

        texture = new Texture("planeta1.png");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, 320, 320);
    }

    public void dispose() {
        texture.dispose();
    }
}