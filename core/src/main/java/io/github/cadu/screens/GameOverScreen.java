package io.github.cadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.cadu.Main;

public class GameOverScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    
    private Texture backgroundMenu;
    private Texture buttonStartTexture;
    private Rectangle buttonStartBounds;

    public GameOverScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        backgroundMenu = new Texture("gameoverbg.png"); 
        buttonStartTexture = new Texture("gameoverbtn.png"); 
        buttonStartBounds = new Rectangle(440, 400, 400, 160); 
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            
            if (buttonStartBounds.contains(mouseX, mouseY)) {
                game.setScreen(new MainMenuScreen(game)); // volta pra main menu
                dispose();
                return;
            }
        }
        batch.begin();
        batch.draw(backgroundMenu, 0, 0);
        batch.draw(buttonStartTexture, buttonStartBounds.x, buttonStartBounds.y, buttonStartBounds.width, buttonStartBounds.height);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundMenu.dispose();
        buttonStartTexture.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}