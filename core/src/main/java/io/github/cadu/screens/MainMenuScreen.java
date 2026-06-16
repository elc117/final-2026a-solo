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

public class MainMenuScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    
    private Texture[] backgroundFrames; 
    private float stateTime;            
    private final float FRAME_DURATION = 0.05f;
    
    private Texture buttonStartTexture;
    private Rectangle buttonStartBounds;
    
    private OrthographicCamera camera;
    private Viewport viewport;

    public MainMenuScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        backgroundFrames = new Texture[50];
        
        for (int i = 0; i < 50; i++) {
            backgroundFrames[i] = new Texture("menu_frames/bg" + i + ".png");
        }
        
        stateTime = 0f; 
        
        buttonStartTexture = new Texture("start_game.png"); 
        buttonStartBounds = new Rectangle(440, 400, 400, 160); 
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 960, camera);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stateTime += delta; 

        int cycleLength = (50 * 2) - 2;
        int currentTick = (int) (stateTime / FRAME_DURATION) % cycleLength;

        int currentFrameIndex;
        if (currentTick < 50) {
            currentFrameIndex = currentTick;
        } else {
            currentFrameIndex = cycleLength - currentTick; 
        }

        Vector3 mouseMundo = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (buttonStartBounds.contains(mouseMundo.x, mouseMundo.y)) {
                game.setScreen(new GameScreen(game)); 
                dispose();
                return;
            }
        }
        
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        batch.draw(backgroundFrames[currentFrameIndex], 0, 0, 1280, 960); 
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
        
        if (backgroundFrames != null) {
            for (int i = 0; i < 50; i++) {
                if (backgroundFrames[i] != null) {
                    backgroundFrames[i].dispose();
                }
            }
        }
        
        buttonStartTexture.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}