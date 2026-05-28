package io.github.cadu.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private Texture texturePl;

    private float x;
    private float y;

    private int currentPlanet = 1;

    private float width = 200;
    private float height = 200;

    private Planet[] planets;

    public Player(Planet[] planets) {

        this.planets = planets;

        texturePl = new Texture("player.png");

        updatePosition();
    }

    private void updatePosition() {

        x = planets[currentPlanet].x + 60;
        y = planets[currentPlanet].y + 70;
    }

    public void update(float delta) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {

            if(currentPlanet > 0) {
                currentPlanet--;
                updatePosition();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {

            if(currentPlanet < 2) {
                currentPlanet++;
                updatePosition();
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texturePl, x, y, width, height);
    }

    public void dispose() {
        texturePl.dispose();
    }
}