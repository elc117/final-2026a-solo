package io.github.cadu.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private Texture texturePl;
    private float x;
    private float y;
    private int currentPlanet = 1;
    private Array<Bullet> bullets;
    private float width = 200;
    private float height = 200;

    private Planet[] planets;

    public Player(Planet[] planets) {

        this.planets = planets;
        texturePl = new Texture("player.png");
        bullets = new Array<>();
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
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            Vector2 mousePos = new Vector2(
                Gdx.input.getX(),
                Gdx.graphics.getHeight() - Gdx.input.getY()
            );

            Vector2 playerPos = new Vector2(
                x + width / 2,
                y + height / 2
            );

        Vector2 direction = mousePos.sub(playerPos).nor();

            bullets.add(new Bullet(
            playerPos.x,
            playerPos.y,
            direction
        ));
        }
            for(Bullet bullet : bullets) {
                bullet.update(delta);
            } 
    }

    public void render(SpriteBatch batch) {
        batch.draw(texturePl, x, y, width, height);
        for(Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    public void dispose() {
        texturePl.dispose();
    }
}