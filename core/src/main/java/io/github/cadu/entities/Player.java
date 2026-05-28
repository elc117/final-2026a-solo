package io.github.cadu.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private Texture texturePl;

    private float x;
    private float y;

    private float speed = 300;

    private float width = 200;
    private float height = 200;

    public Player() {

        texturePl = new Texture("player.png");

        x = 200;
        y = 170;
    }

    public void update(float delta) {

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            x -= speed * delta;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * delta;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texturePl, x, y, width, height);
    }
public void dispose() {
    texturePl.dispose();
}}