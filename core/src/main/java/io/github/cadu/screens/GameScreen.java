package io.github.cadu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Preferences;

import io.github.cadu.entities.Player;
import io.github.cadu.entities.Planet;
import io.github.cadu.entities.Bullet;
import io.github.cadu.entities.Enemy;
import io.github.cadu.entities.TankEnemy;
import io.github.cadu.entities.SniperEnemy;
import io.github.cadu.Main;

public class GameScreen implements Screen {

    // entidades e variáveis de controle
    private Main game;
    private SpriteBatch batch;
    private Texture background;
    private Player player;
    private Array<Enemy> enemies;
    private Planet[] planets;
    
    // spawn e fases
    private boolean[] slotOccupied = new boolean[3];
    private int[] slotSpawnCount = new int[3]; // conta quantos inimigos já nasceram em cada slot para alternar entre normal e especial
    private int enemiesSpawnedThisPhase = 0;
    private int enemiesKilled = 0;
    private int maxEnemiesOnScreen = 3;
    private int enemiesToKillThisPhase = 8;
    private int currentPhase = 1; // controla a fase, começa no 1

    // hud
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Sound enemyDeathSound;
    private OrthographicCamera camera;
    private Viewport viewport;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("bg.png"); 
        enemyDeathSound = Gdx.audio.newSound(Gdx.files.internal("enemy_death.wav"));
        
        planets = new Planet[3];
        planets[0] = new Planet(80, 150, "planeta3.png");  
        planets[1] = new Planet(480, 150, "planeta1.png");
        planets[2] = new Planet(880, 150, "planeta2.png"); 
        
        camera = new OrthographicCamera();
        viewport = new FitViewport(1280, 960, camera);

        player = new Player(planets); 
        enemies = new Array<>();
        font = new BitmapFont();
        font.getData().setScale(2f);
        shapeRenderer = new ShapeRenderer();
    }

    private void spawnEnemy(int slotIndex) {
        Enemy newEnemy = null;

        if (currentPhase == 1) {
            // fase 1, só inimigos normais
            if (slotIndex == 0) newEnemy = new Enemy(10, 600, Enemy.MovementType.HORIZONTAL, 10, 320, 0, currentPhase);
            else if (slotIndex == 1) newEnemy = new Enemy(540, 600, Enemy.MovementType.VERTICAL, 450, 770, 1, currentPhase);
            else if (slotIndex == 2) newEnemy = new Enemy(1070, 600, Enemy.MovementType.HORIZONTAL, 760, 1070, 2, currentPhase);
        } else {

            // fase 2 em diante alterna entre inimigo normal e especial a cada spawn
            // se o spawn for par, nasce inimigo normal; se for ímpar, nasce inimigo especial (tanque ou sniper)
            boolean isSpecialSpawn = (slotSpawnCount[slotIndex] % 2 != 0); 

            if (!isSpecialSpawn) {
                // nasce inimigo normal
                if (slotIndex == 0) newEnemy = new Enemy(10, 600, Enemy.MovementType.HORIZONTAL, 10, 320, 0, currentPhase);
                else if (slotIndex == 1) newEnemy = new Enemy(540, 600, Enemy.MovementType.VERTICAL, 450, 770, 1, currentPhase);
                else if (slotIndex == 2) newEnemy = new Enemy(1070, 600, Enemy.MovementType.HORIZONTAL, 760, 1070, 2, currentPhase);
            } else {
                // nasce inimigo especial (tanque ou sniper)
                if (slotIndex == 0) newEnemy = new TankEnemy(10, 600, Enemy.MovementType.HORIZONTAL, 10, 320, 0, currentPhase);
                else if (slotIndex == 1) newEnemy = new SniperEnemy(540, 600, Enemy.MovementType.VERTICAL, 450, 770, 1, currentPhase);
                else if (slotIndex == 2) newEnemy = new TankEnemy(1070, 600, Enemy.MovementType.HORIZONTAL, 760, 1070, 2, currentPhase);
            }
        }

        enemies.add(newEnemy);
        slotOccupied[slotIndex] = true;
        slotSpawnCount[slotIndex]++; // Aumenta a contagem de nascimentos dessa vaga
        enemiesSpawnedThisPhase++;
    }

    private void advancePhase() {
        currentPhase++;
        enemiesKilled = 0;
        enemiesSpawnedThisPhase = 0;
        enemiesToKillThisPhase += 2; // aumenta a quantidade de inimigos a matar a cada fase para aumentar a dificuldade
        
        // zera o contador de spawn para cada slot para garantir que o padrão de inimigos se mantenha a cada fase
        for(int i = 0; i < 3; i++) {
            slotSpawnCount[i] = 0;
        }
        System.out.println("passou pra fase " + currentPhase);
    }

    public int sendPhase() {
        return currentPhase;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Vector3 mouseMundo = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        
        if (enemies.size < maxEnemiesOnScreen && enemiesSpawnedThisPhase < enemiesToKillThisPhase) {
            if (!slotOccupied[0]) spawnEnemy(0);
            else if (!slotOccupied[1]) spawnEnemy(1);
            else if (!slotOccupied[2]) spawnEnemy(2);
        }

        player.update(delta, mouseMundo.x, mouseMundo.y);
        
        for (Enemy e : enemies) {
            e.update(delta, player.getX() + 100, player.getY() + 100);
        }

        // colisão dos inimigos
        for (int i = player.getBullets().size - 1; i >= 0; i--) {
            Bullet b = player.getBullets().get(i);
            b.update(delta);
            
            for (int j = enemies.size - 1; j >= 0; j--) {
                Enemy e = enemies.get(j);

                if (!e.isSpawning() && b.getHitboxBullet().overlaps(e.getHitboxEnemy())) { 
                    player.getBullets().removeIndex(i); 
                    
                    // dano dinamico de inimigo normal, sniper ou tanque
                    e.takeDamage(b.getDamage()); 
                    
                    if (e.verifyDeath()) { 
                        player.addCoins(e.getCoinReward());
                        e.dispose(); 
                        enemies.removeIndex(j); 
                        enemiesKilled++;
                        enemyDeathSound.play(0.5f); 
                        slotOccupied[e.getSlot()] = false; 
                        
                        // verifica se o jogador matou a quantidade necessária de inimigos para avançar de fase
                        if (enemiesKilled >= enemiesToKillThisPhase) {
                            advancePhase();
                        }
                    }
                    break;
                }
            }
        }
        //colisão das balas dos inimigos com o player
        for (Enemy e : enemies) {   
            for (int i = e.getBullets().size - 1; i >= 0; i--) {
                Bullet b = e.getBullets().get(i);
                b.update(delta);
                
                if (b.getHitboxBullet().overlaps(player.getHitboxPlayer())) { 
                    e.getBullets().removeIndex(i); 
                    
                    player.takeDamage(b.getDamage()); 
                    
                    if (player.verifyDeath()) { 
                        Preferences prefs = Gdx.app.getPreferences("save");
                        prefs.putInteger("moedasTotais", player.getCoins());
                        prefs.flush(); // usa preferences pra guardar as moedas mesmo apos morrer
                        game.setScreen(new GameOverScreen(game));
                        dispose(); 
                        return; 
                    }
                }
            }
        }
        
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);
        
        for(Planet planet : planets) {
            planet.render(batch);
        }

        player.render(batch);
        
        for (Enemy e : enemies) {
            e.render(batch);
        }
        
        // mostra a vida e a fase atual na tela
        font.draw(batch, "HP: " + player.getHp(), 30, 35);
        font.draw(batch, "Fase: " + currentPhase + " | Inimigos: " + enemiesKilled + "/" + enemiesToKillThisPhase, 30, 80);
        font.draw(batch, "Moedas: " + player.getCoins(), 30, 125);

        batch.end();
        
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Enemy e : enemies) {
            if (!e.isSpawning()) { 
                shapeRenderer.setColor(Color.DARK_GRAY); 
                shapeRenderer.rect(e.getX(), e.getY() + 210, 200, 15);

                shapeRenderer.setColor(Color.GREEN);
                if (e.getHp() / e.getMaxHp() < 0.3f) { 
                    shapeRenderer.setColor(Color.RED);
                }
                float hpPercent = e.getHp() / e.getMaxHp();
                if (hpPercent < 0) hpPercent = 0;
                shapeRenderer.rect(e.getX(), e.getY() + 210, 200 * hpPercent, 15);
            }
        }
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        for (Planet planet : planets) {
            planet.dispose();
        }
        player.dispose();
        
        for (Enemy e : enemies) {
            e.dispose();
        }
        font.dispose();
        shapeRenderer.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) { viewport.update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}