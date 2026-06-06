package io.github.cadu.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.cadu.entities.Player;
import io.github.cadu.entities.Planet;
import io.github.cadu.entities.Bullet;
import io.github.cadu.entities.Enemy;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Texture background;
    private Player player;
    private Enemy enemy;
    private Planet[] planets;

    public GameScreen() {
        batch = new SpriteBatch();
        background = new Texture("bg.png"); 

        planets = new Planet[3];
        planets[0] = new Planet(80, 150, "planeta3.png");  
        planets[1] = new Planet(480, 150, "planeta1.png");
        planets[2] = new Planet(880, 150, "planeta2.png"); 
        
        player = new Player(planets); 
        enemy = new Enemy();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        player.update(delta);
        
        // so move o inimigo se ele ainda existir
        if (enemy != null) {
            enemy.basicMovement(delta);
        }

        for (int i = player.getBullets().size - 1; i >= 0; i--) {
            Bullet b = player.getBullets().get(i);
            b.update(delta);
            
            // verifica se o inimigo ainda existe antes de tentar acessar o hitbox
            if (enemy != null && b.getHitboxBullet().overlaps(enemy.getHitboxEnemy())) { 
                System.out.println("acertou inimigo"); 
                player.getBullets().removeIndex(i); // destroi a bala
                enemy.takeDamage(50); // causa dano ao inimigo
                enemy.hpStatus(); // mostra o HP do inimigo no console
                
                if (enemy.verifyDeath()) { // verifica se o inimigo morreu
                    System.out.println("inimigo morto");
                    enemy.dispose(); // limpa a memória de vídeo antes de anular a variável
                    this.enemy = null; // destroi o inimigo
                }
            }
        }

        batch.begin();
        batch.draw(background, 0, 0);
        
        for(Planet planet : planets) {
            planet.render(batch);
        }

        player.render(batch);
        
        // verfica se o inimigo ainda existe antes de tentar renderizar
        if (enemy != null) {
            enemy.render(batch);
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
        
        // proteção pra caso o inimigo já tenha sido destruido durante o jogo, evitando tentar limpar algo que já foi limpo (tava crashando o jogo)
        if (enemy != null) {
            enemy.dispose();
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}