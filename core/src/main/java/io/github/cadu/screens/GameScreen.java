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

import io.github.cadu.entities.Player;
import io.github.cadu.entities.Planet;
import io.github.cadu.entities.Bullet;
import io.github.cadu.entities.Enemy;
import io.github.cadu.Main;

public class GameScreen implements Screen {

    private Main game;
    private SpriteBatch batch;
    private Texture background;
    private Player player;
    private Array<Enemy> enemies;
    private Planet[] planets;
    private boolean[] slotOccupied = new boolean[3];
    private int enemiesSpawnedThisPhase = 0;
    private int enemiesKilled = 0;
    private int maxEnemiesOnScreen = 3;
    private int enemiesToKillThisPhase = 10;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Sound enemyDeathSound;
    private Sound enemyHitSound;
    private OrthographicCamera camera;
    private Viewport viewport;


    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("bg.png"); 
        enemyDeathSound = Gdx.audio.newSound(Gdx.files.internal("enemy_death.wav"));
        enemyHitSound = Gdx.audio.newSound(Gdx.files.internal("enemy_hit.wav"));
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
        if (slotIndex == 0) { // vaga da esquerda
            enemies.add(new Enemy(10, 600, Enemy.MovementType.HORIZONTAL, 10, 320,0));
        } else if (slotIndex == 1) { // vaga do meio
            enemies.add(new Enemy(540, 600, Enemy.MovementType.VERTICAL, 450, 770,1));
        } else if (slotIndex == 2) { // vaga da direita
            enemies.add(new Enemy(1070, 600, Enemy.MovementType.HORIZONTAL, 760, 1070,2));
        }
        slotOccupied[slotIndex] = true;
        enemiesSpawnedThisPhase++;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Vector3 mouseMundo = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        // usa o novo sistema de slots para respawnar inimigos, garantindo que só tenha 1 inimigo por vaga e um máximo de inimigos na tela
        if (enemies.size < maxEnemiesOnScreen && enemiesSpawnedThisPhase < enemiesToKillThisPhase) {
            if (!slotOccupied[0]) {
                spawnEnemy(0);
            } else if (!slotOccupied[1]) {
                spawnEnemy(1);
            } else if (!slotOccupied[2]) {
                spawnEnemy(2);
            }
        }

        player.update(delta, mouseMundo.x, mouseMundo.y);
        
        for (Enemy e : enemies) {
            e.update(delta, player.getX(), player.getY());
        }

        for (int i = player.getBullets().size - 1; i >= 0; i--) {
            Bullet b = player.getBullets().get(i);
            b.update(delta);
            
            for (int j = enemies.size - 1; j >= 0; j--) {
                Enemy e = enemies.get(j);

                if (!e.isSpawning() && b.getHitboxBullet().overlaps(e.getHitboxEnemy())) { 
                    System.out.println("acertou inimigo"); 
                    player.getBullets().removeIndex(i); // destroi a bala
                    e.takeDamage(50); // causa dano ao inimigo
                    e.hpStatus(); // mostra o HP do inimigo no console
                    enemyHitSound.play(0.5f); // toca o som de hit do inimigo
                    
                    if (e.verifyDeath()) { // verifica se o inimigo morreu
                        System.out.println("inimigo morto");
                        e.dispose(); // limpa a memória de vídeo antes de anular a variável
                        enemies.removeIndex(j); // destroi o inimigo
                        enemiesKilled++;
                        enemyDeathSound.play(0.5f); // toca o som de morte do inimigo
                        slotOccupied[e.getSlot()] = false; // libera a vaga do inimigo morto pra spawnar denovo
                    }
                    break;
                }
            }
        }
        for (Enemy e : enemies) {   
        for (int i = e.getBullets().size - 1; i >= 0; i--) {
            Bullet b = e.getBullets().get(i);
            b.update(delta);
            
            if (b.getHitboxBullet().overlaps(player.getHitboxPlayer())) { 
                System.out.println("acertou player"); 
                e.getBullets().removeIndex(i); // destroi a bala
                player.takeDamage(60); // causa dano ao player
                player.hpStatus(); // mostra o HP do player no console
                if (player.verifyDeath()) { // verifica se o player morreu
                    System.out.println("player morto");
                    game.setScreen(new GameOverScreen(game));
                    dispose(); // limpa a memória de vídeo antes de anular a variável
                    return; // sai do método render para evitar tentar acessar o player depois de destruído
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
        font.draw(batch, "HP: " + player.getHp(), 30, 35);

        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Enemy e : enemies) {
            if (!e.isSpawning()) { 
            
                shapeRenderer.setColor(Color.DARK_GRAY); //fundo da barra pra contraste
                shapeRenderer.rect(e.getX(), e.getY() + 210, 200, 15);

                shapeRenderer.setColor(Color.GREEN);
                if (e.getHp() / e.getMaxHp() < 0.3f) { // se o HP estiver abaixo de 30%, pinta a barra de vermelho
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
    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}