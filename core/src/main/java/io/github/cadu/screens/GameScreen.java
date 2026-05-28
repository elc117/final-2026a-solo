package io.github.cadu.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.cadu.entities.Player;

public class GameScreen implements Screen {

    private SpriteBatch batch;

    private Texture background;

    private Player player;

    public GameScreen() {

        batch = new SpriteBatch();

        background = new Texture("bg.png");

        player = new Player();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        player.update(delta);

        batch.begin();

        batch.draw(background, 0, 0);

        player.render(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        player.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}