package io.github.cadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import io.github.cadu.Main;

public class GameOverScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    
    private Texture backgroundMenu;
    private Texture buttonStartTexture;
    private Rectangle buttonStartBounds;
    
    private OrthographicCamera camera;
    private Viewport viewport;

    public GameOverScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        backgroundMenu = new Texture("gameoverbg.png"); 
        buttonStartTexture = new Texture("gameoverbtn.png"); 
        buttonStartBounds = new Rectangle(440, 400, 400, 160); 
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 960, camera);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        Vector3 mouseMundo = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (buttonStartBounds.contains(mouseMundo.x, mouseMundo.y)) {
                game.setScreen(new MainMenuScreen(game)); // Volta pro menu principal de forma segura
                dispose();
                return;
            }
        }
        
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        batch.draw(backgroundMenu, 0, 0, 1280, 960);
        batch.draw(buttonStartTexture, buttonStartBounds.x, buttonStartBounds.y, buttonStartBounds.width, buttonStartBounds.height);
        batch.end();
    }

    @Override 
    public void resize(int width, int height) { 
        viewport.update(width, height, true); 
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundMenu.dispose();
        buttonStartTexture.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}