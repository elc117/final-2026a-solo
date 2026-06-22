package io.github.cadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color; 
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont; 
import com.badlogic.gdx.graphics.g2d.GlyphLayout; 
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator; 
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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

    private Texture buttonShopTexture;
    private Rectangle buttonShopBounds;

    private BitmapFont fontTitle;
    private GlyphLayout glyphLayout;
    private final String GAME_TITLE = "Planet Protector";

    public MainMenuScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        
        backgroundFrames = new Texture[50];
        
        for (int i = 0; i < 50; i++) {
            backgroundFrames[i] = new Texture("menu_frames/bg" + i + ".png");
        }
        
        stateTime = 0f; 
        
        buttonStartTexture = new Texture("start_game.png"); 
        buttonStartBounds = new Rectangle(565, 450, 150, 60); 
        buttonShopTexture = new Texture("shop_btn.png"); 
        buttonShopBounds = new Rectangle(565, 380, 150, 60);
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 960, camera);

        glyphLayout = new GlyphLayout();
        
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("jungle.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        
        parameter.size = 100; 
        parameter.color = Color.valueOf("FF0000"); 
        parameter.borderWidth = 5; 
        parameter.borderColor = Color.BLACK;
        parameter.shadowOffsetX = 6; 
        parameter.shadowOffsetY = 6;
        parameter.shadowColor = new Color(0, 0, 0, 0.8f);
        
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.magFilter = Texture.TextureFilter.Nearest;
        
        fontTitle = generator.generateFont(parameter);
        generator.dispose();
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
            if (buttonShopBounds.contains(mouseMundo.x, mouseMundo.y)) {
                game.setScreen(new ShopScreen(game)); 
                dispose();
                return;
            }
        }
        // use tab + q, para resetar as moedas e upgrades.
        if (Gdx.input.isKeyPressed(Input.Keys.TAB) && Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("save");
            prefs.clear(); // limpa todos os dados
            prefs.flush(); // salva o arquivo vazio 
            System.out.println("reset funcionou"); // aviso no console
            Gdx.app.exit(); // fecha o jogo para aplicar o reset na proxima inicializaçao
        }
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        batch.draw(backgroundFrames[currentFrameIndex], 0, 0, 1280, 960); 

        glyphLayout.setText(fontTitle, GAME_TITLE);
        float titleX = (1280f - glyphLayout.width) / 2f;
        
        float offsetAnimacao = (float) Math.sin(stateTime * 3f) * 15f; 
        
        float titleY = 850f + offsetAnimacao; 
        
        fontTitle.draw(batch, glyphLayout, titleX, titleY);

        batch.draw(buttonStartTexture, 440, 400, 400, 160);
        batch.draw(buttonShopTexture, buttonShopBounds.x, buttonShopBounds.y, buttonShopBounds.width, buttonShopBounds.height);
        
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
        buttonShopTexture.dispose();
        fontTitle.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}