package io.github.cadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import io.github.cadu.Main;

public class ShopScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; 
    private OrthographicCamera camera;
    private Viewport viewport;
    
    private Texture backgroundShop; 
    private Texture buttonBackTexture;
    private Rectangle buttonBackBounds;
    
    private BitmapFont fontMoedas;
    private GlyphLayout glyphLayout;

    private Texture texPreco15, texPreco20, texPreco25, texPreco30, texPreco35, texPrecoMax;

    private Sound buySound;
    private Sound errorSound;

    private int currentCoins;
    private Preferences prefs;

    private Rectangle[] buyBounds = new Rectangle[6];
    private int[] upgradeLevels = new int[6];
    private final int MAX_LEVEL = 5; 

    private String[] upgradeKeys = {"lvl_atk_speed", "lvl_dmg", "lvl_hp", "lvl_regen", "lvl_ship_speed", "lvl_planet_regen"};

    public ShopScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer(); 
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 960, camera);
        glyphLayout = new GlyphLayout();

        fontMoedas = new BitmapFont();
        fontMoedas.getData().setScale(2.5f); 

        texPreco15 = new Texture("preco_15.png");
        texPreco20 = new Texture("preco_20.png");
        texPreco25 = new Texture("preco_25.png");
        texPreco30 = new Texture("preco_30.png");
        texPreco35 = new Texture("preco_35.png");
        texPrecoMax = new Texture("preco_max.png");

        buySound = Gdx.audio.newSound(Gdx.files.internal("buy.wav"));
        errorSound = Gdx.audio.newSound(Gdx.files.internal("error.wav"));

        backgroundShop = new Texture("shop_bg.png"); 
        buttonBackTexture = new Texture("back_btn.png"); 
        buttonBackBounds = new Rectangle(15, 10, 150, 60); 
        
        prefs = Gdx.app.getPreferences("save");
        currentCoins = prefs.getInteger("moedasTotais", 0);

        for (int i = 0; i < 6; i++) {
            float startY;
            if (i <= 1) startY = 606.5f; 
            else if (i <= 3) startY = 335.5f; 
            else startY = 64.5f;  

            float hitboxX = (i % 2 == 0) ? 180f : 680f;
            float hitboxY = startY - 12.5f;
            
            buyBounds[i] = new Rectangle(hitboxX, hitboxY, 360, 60);

            upgradeLevels[i] = prefs.getInteger(upgradeKeys[i], 0);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Vector3 mouseMundo = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (buttonBackBounds.contains(mouseMundo.x, mouseMundo.y)) {
                game.setScreen(new MainMenuScreen(game)); 
                dispose();
                return;
            }
            
            for (int i = 0; i < 6; i++) {
                if (buyBounds[i].contains(mouseMundo.x, mouseMundo.y)) {

                    int precoAtual = 15 + (upgradeLevels[i] * 5); 

                    if (currentCoins >= precoAtual && upgradeLevels[i] < MAX_LEVEL) {
                        currentCoins -= precoAtual;
                        upgradeLevels[i]++;
                        
                        prefs.putInteger("moedasTotais", currentCoins);
                        prefs.putInteger(upgradeKeys[i], upgradeLevels[i]);
                        prefs.flush();
                        
                        buySound.play(0.7f);
                    } else {
                        errorSound.play(0.5f);
                    }
                }
            }
        }
        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        batch.draw(backgroundShop, 0, 0, 1280, 960);
        batch.draw(buttonBackTexture, buttonBackBounds.x, buttonBackBounds.y, buttonBackBounds.width, buttonBackBounds.height); 

        String textMoedas = String.valueOf(currentCoins);
        glyphLayout.setText(fontMoedas, textMoedas); 
        float coinX = 1040f - (glyphLayout.width / 2f);
        float coinY = 910f + (glyphLayout.height / 2f); 
        fontMoedas.draw(batch, glyphLayout, coinX, coinY);

        float targetWidth = 40f; 
        float targetHeight = 34f; 

        for (int i = 0; i < 6; i++) {
            int level = upgradeLevels[i];
            Texture textureToDraw;

            if (level == 0) textureToDraw = texPreco15;
            else if (level == 1) textureToDraw = texPreco20;
            else if (level == 2) textureToDraw = texPreco25;
            else if (level == 3) textureToDraw = texPreco30;
            else if (level == 4) textureToDraw = texPreco35;
            else textureToDraw = texPrecoMax; 
            
            float startX = (i % 2 == 0) ? 235.5f : (235.5f + 467f);
            float startY;
            if (i <= 1) startY = 606.5f;
            else if (i <= 3) startY = 335.5f;
            else startY = 64.5f;

            float precoX = startX + (4 * 48f) + 35f + 15f; 
            float precoY = (startY + 17.5f) - (targetHeight / 2f); 
            
            batch.draw(textureToDraw, precoX, precoY, targetWidth, targetHeight);
        }

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 6; i++) {
            float startX = (i % 2 == 0) ? 235.5f : (235.5f + 467f); 
            float startY;
            if (i <= 1) startY = 606.5f; 
            else if (i <= 3) startY = 335.5f; 
            else startY = 64.5f;  

            for (int j = 0; j < MAX_LEVEL; j++) {
                float boxX = startX + (j * 48f);

                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(boxX, startY, 35f, 35f); 

                if (upgradeLevels[i] > j) shapeRenderer.setColor(new Color(0.1f, 0.8f, 0.1f, 1f)); 
                else shapeRenderer.setColor(new Color(0.25f, 0.25f, 0.25f, 1f)); 
                shapeRenderer.rect(boxX + 2, startY + 2, 31f, 31f); 

                if (upgradeLevels[i] > j) shapeRenderer.setColor(new Color(0.4f, 1f, 0.4f, 1f)); 
                else shapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1f)); 
                shapeRenderer.rect(boxX + 2, startY + 31, 31f, 2f); 
                shapeRenderer.rect(boxX + 2, startY + 2, 2f, 31f);  
            }
        }
        shapeRenderer.end();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    
    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose(); 
        backgroundShop.dispose();
        buttonBackTexture.dispose();
        fontMoedas.dispose(); 
        
        texPreco15.dispose();
        texPreco20.dispose();
        texPreco25.dispose();
        texPreco30.dispose();
        texPreco35.dispose();
        texPrecoMax.dispose();

        buySound.dispose(); 
        errorSound.dispose();
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}