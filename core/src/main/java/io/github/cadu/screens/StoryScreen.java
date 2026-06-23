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
import com.badlogic.gdx.audio.Sound;

public class StoryScreen implements Screen {

    private enum FadeState { FADE_IN, ESPERANDO, FADE_OUT }
    private FadeState state = FadeState.FADE_IN;
    
    private float alpha = 0f;
    private float fadeSpeed = 1.5f;

    private Main game;
    private SpriteBatch batch;
    private Texture[] frames;
    private int currentFrame = 0; 
    private Sound next;
    
    private OrthographicCamera camera;
    private Viewport viewport;

    public StoryScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        frames = new Texture[4];
        frames[0] = new Texture("story1.png");
        frames[1] = new Texture("story2.png");
        frames[2] = new Texture("story3.png");
        frames[3] = new Texture("story4.png");
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 960, camera);
        next = Gdx.audio.newSound(Gdx.files.internal("shift.wav"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1); 

        if (state == FadeState.FADE_IN) {
            alpha += fadeSpeed * delta;
            if (alpha >= 1f) {
                alpha = 1f;
                state = FadeState.ESPERANDO;
            }
        } 
        else if (state == FadeState.ESPERANDO) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
                next.play(0.5f);
                state = FadeState.FADE_OUT;
            }
        } 
        else if (state == FadeState.FADE_OUT) {
            alpha -= fadeSpeed * delta; 
            if (alpha <= 0f) {
                alpha = 0f;
                currentFrame++; 
                
                if (currentFrame >= 4) {
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                    return;
                } else {
                    state = FadeState.FADE_IN;
                }
            }
        }
        
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        
        batch.begin();
        batch.setColor(1f, 1f, 1f, alpha); 
        batch.draw(frames[currentFrame], 0, 0, 1280, 960);
        batch.setColor(1f, 1f, 1f, 1f); 
        batch.end();
    }

    @Override 
    public void resize(int width, int height) { 
        viewport.update(width, height, true); 
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (int i = 0; i < 4; i++) {
            if (frames[i] != null) frames[i].dispose();
        }
        if (next != null) {
            next.dispose();
        }
    }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}