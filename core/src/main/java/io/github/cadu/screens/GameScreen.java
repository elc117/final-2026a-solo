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
        enemy.basicMovement(delta);

        for (int i = player.getBullets().size - 1; i >= 0; i--) {
            Bullet b = player.getBullets().get(i);
            b.update(delta);
            if (b.getHitboxBullet().overlaps(enemy.getHitboxEnemy())) { // utiliza o overlap para detectar colisão entre a bala e o inimigo
                System.out.println("acertou inimigo"); // mensagem pra testar colisão, ta funcionando ja testei
                player.getBullets().removeIndex(i); // destroi a bala
            }
        }

        batch.begin();

        batch.draw(background, 0, 0);
        
        for(Planet planet : planets) {
            planet.render(batch);
        }

        player.render(batch);
        enemy.render(batch);
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
        enemy.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}