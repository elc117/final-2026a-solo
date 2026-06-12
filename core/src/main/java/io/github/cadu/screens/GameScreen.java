package io.github.cadu.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Array;

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

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("bg.png"); 

        planets = new Planet[3];
        planets[0] = new Planet(80, 150, "planeta3.png");  
        planets[1] = new Planet(480, 150, "planeta1.png");
        planets[2] = new Planet(880, 150, "planeta2.png"); 
        
        player = new Player(planets); 
        enemies = new Array<>();

        enemies.add(new Enemy(10, 600, Enemy.MovementType.HORIZONTAL, 10, 320));
        enemies.add(new Enemy(540, 600, Enemy.MovementType.VERTICAL, 450, 770));
        enemies.add(new Enemy(1070, 600, Enemy.MovementType.HORIZONTAL, 760, 1070));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        player.update(delta);
        
        for (Enemy e : enemies) {
            e.update(delta, player.getX(), player.getY());
        }

        for (int i = player.getBullets().size - 1; i >= 0; i--) {
            Bullet b = player.getBullets().get(i);
            b.update(delta);
            
            for (int j = enemies.size - 1; j >= 0; j--) {
                Enemy e = enemies.get(j);

                if (b.getHitboxBullet().overlaps(e.getHitboxEnemy())) { 
                    System.out.println("acertou inimigo"); 
                    player.getBullets().removeIndex(i); // destroi a bala
                    e.takeDamage(50); // causa dano ao inimigo
                    e.hpStatus(); // mostra o HP do inimigo no console
                    
                    if (e.verifyDeath()) { // verifica se o inimigo morreu
                        System.out.println("inimigo morto");
                        e.dispose(); // limpa a memória de vídeo antes de anular a variável
                        enemies.removeIndex(j); // destroi o inimigo
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

        batch.begin();
        batch.draw(background, 0, 0);
        
        for(Planet planet : planets) {
            planet.render(batch);
        }

        player.render(batch);
        
        for (Enemy e : enemies) {
            e.render(batch);
        }
        batch.end();
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
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}