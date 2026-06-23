package io.github.cadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.cadu.Main;

public class VictoryScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private Texture background;
    
    private OrthographicCamera camera;
    private Viewport viewport;

    public VictoryScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        background = new Texture("ending.png"); 
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 960, camera);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
            return;
        }
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        batch.draw(background, 0, 0, 1280, 960);
        batch.end();
    }

    @Override 
    public void resize(int width, int height) { 
        viewport.update(width, height, true); 
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}