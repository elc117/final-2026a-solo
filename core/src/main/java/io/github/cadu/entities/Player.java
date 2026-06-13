package io.github.cadu.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;

public class Player {

    private Texture texturePl;
    private float x;
    private float y;
    private int currentPlanet = 1;
    private Array<Bullet> bullets;
    private float width = 200;
    private float height = 200;
    private float hp = 300;
    private Rectangle hitboxPlayer;
    private float rotation = 0f;
    private Sound shootSound;
    private Sound PlayerHitSound;
    private float baseDamage = 50f;
    private float fireRate = 1.0f;

    private Planet[] planets;

    public Player(Planet[] planets) {

        this.planets = planets;
        texturePl = new Texture("player.png");
        bullets = new Array<>();
        updatePosition();
        hitboxPlayer = new Rectangle(x, y, width, height);
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.wav"));
        PlayerHitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
    }

    private void updatePosition() {

        x = planets[currentPlanet].x + 60;
        y = planets[currentPlanet].y + 70;

        if  (hitboxPlayer != null) {
            hitboxPlayer.setPosition(x, y);
        }
    }

    public void update(float delta, float mouseX, float mouseY) {

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {

            if(currentPlanet > 0) {
                currentPlanet--;
                updatePosition();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {

            if(currentPlanet < 2) {
                currentPlanet++;
                updatePosition();
            }
        }
        
        Vector2 mousePos = new Vector2(mouseX, mouseY);

        Vector2 playerPos = new Vector2(
            x + width / 2,
            y + height / 2
        );

        Vector2 aimDirection = mousePos.sub(playerPos).nor();
        
        rotation = aimDirection.angleDeg() - 90f;
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            shootSound.play(0.5f);
            bullets.add(new Bullet(playerPos.x,playerPos.y,new Vector2(aimDirection),baseDamage));
        }
        
        for(Bullet bullet : bullets) {
            bullet.update(delta);
        } 
    }

    public void render(SpriteBatch batch) { // desenha agora pra fazer a nave seguir o mouse (usei IA para ajudar)
        batch.draw(
            texturePl, 
            x, y, 
            width / 2, height / 2,
            width, height, 
            1f, 1f,             
            rotation,   
            0, 0, 
            texturePl.getWidth(), texturePl.getHeight(), 
            false, false  
        );
        
        for(Bullet bullet : bullets) {
            bullet.render(batch);
        }
    }

    public void takeDamage(float damage) {
        hp -= damage;
        PlayerHitSound.play(0.5f);
    }

    public Rectangle getHitboxPlayer() {
        return hitboxPlayer;
    }
    
    public float getHp() {
        return hp;
    }
    
    public void dispose() {
        texturePl.dispose();
        shootSound.dispose();
        PlayerHitSound.dispose();
    }

    public boolean verifyDeath() {
        return hp <= 0;
    }

    public void hpStatus() {
        System.out.println("HP do player: " + hp);
    }

    public Array<Bullet> getBullets() {
        return bullets;
    }
    
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}