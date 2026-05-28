package io.github.cadu.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.cadu.entities.Player;
import io.github.cadu.entities.Planet;

public class GameScreen implements Screen {

    private SpriteBatch batch;

    private Texture background;

    private Player player;

    private Planet[] planets;

    public GameScreen() {

        batch = new SpriteBatch();

        background = new Texture("bg.png");

        player = new Player();

        planets = new Planet[3];
        planets[0] = new Planet(80, 150);  
        planets[1] = new Planet(480, 150);
        planets[2] = new Planet(880, 150);  
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        player.update(delta);

        batch.begin();

        batch.draw(background, 0, 0);
        
        for(Planet planet : planets) {
            planet.render(batch);
        }

        player.render(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        for (Planet planet : planets) {
            planet.dispose();
        }
        player.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}