package io.github.cadu.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences; // usa pra guardar as moedas

public class Player {

    private Texture texturePl;
    private float x;
    private float y;
    
    private float targetX;
    private float targetY;
    
    private int currentPlanet = 1;
    private Array<Bullet> bullets;
    private float width = 200;
    private float height = 200;
    private Rectangle hitboxPlayer;
    private float rotation = 0f;
    private Sound shootSound;
    private Sound PlayerHitSound;

    private float hp;
    private float maxHp;
    private float baseDamage;
    private float moveSpeed; 
    private int coins = 0;
    
    private float fireRate;
    private float timeSinceLastShot = 0f; 
    
    private int lvlRegen;
    private float regenTimer = 0f; 

    private Planet[] planets;

    public Player(Planet[] planets) {
        this.planets = planets;
        
        Preferences prefs = Gdx.app.getPreferences("save");
        this.coins = prefs.getInteger("moedasTotais", 0);
        
        // pega os niveis comprados na loja
        int lvlHp = prefs.getInteger("lvl_hp", 0);
        int lvlDmg = prefs.getInteger("lvl_dmg", 0);
        int lvlAtkSpeed = prefs.getInteger("lvl_atk_speed", 0);
        int lvlShipSpeed = prefs.getInteger("lvl_ship_speed", 0);
        this.lvlRegen = prefs.getInteger("lvl_regen", 0);

        // aplica os calculos dos upgrades
        // vida maxima começa com 300 e ganha 50 por nivel
        this.maxHp = 300f + (lvlHp * 50f);
        this.hp = this.maxHp; 
        
        // dano comeca com 50 e ganha 15 por nivel
        this.baseDamage = 50f + (lvlDmg * 15f);
        
        // velocidade comeca em 2500 e ganha 300 por nivel
        this.moveSpeed = 2500f + (lvlShipSpeed * 300f);
        
        // tempo de espera do tiro diminui 0.05s por nivel
        this.fireRate = 0.4f - (lvlAtkSpeed * 0.05f);

        texturePl = new Texture("player.png");
        bullets = new Array<>();
        
        x = planets[currentPlanet].x + 60;
        y = planets[currentPlanet].y + 70;
        targetX = x;
        targetY = y;
        
        hitboxPlayer = new Rectangle(x, y, width, height);
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.wav"));
        PlayerHitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
    }

    // agora esse metodo da a posição alvo e não altera a posição do player
    private void updateTargetPosition() {
        targetX = planets[currentPlanet].x + 60;
        targetY = planets[currentPlanet].y + 70;
    }

    public void update(float delta, float mouseX, float mouseY) {

        // logica de regeneracao de vida
        // so tenta curar se tiver upgrade e a vida nao estiver cheia
        if (lvlRegen > 0 && hp < maxHp) {
            regenTimer += delta; 
            
            if (regenTimer >= 1.0f) { 
                hp += (lvlRegen * 3); // cura 3 de hp por nivel a cada segundo
                
                if (hp > maxHp) { 
                    hp = maxHp; 
                }
                regenTimer = 0f; 
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            if(currentPlanet > 0) {
                currentPlanet--;
                updateTargetPosition();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if(currentPlanet < 2) {
                currentPlanet++;
                updateTargetPosition();
            }
        }
        
        float distX = targetX - x;
        float distY = targetY - y;
        float distance = (float) Math.sqrt(distX * distX + distY * distY);

        // nova movementação.
        if (distance > moveSpeed * delta) {
            x += (distX / distance) * moveSpeed * delta;
            y += (distY / distance) * moveSpeed * delta;
        } else {
            // caso passe do local
            x = targetX;
            y = targetY;
        }

        if (hitboxPlayer != null) {
            hitboxPlayer.setPosition(x, y);
        }

        Vector2 mousePos = new Vector2(mouseX, mouseY);
        Vector2 playerPos = new Vector2(x + width / 2, y + height / 2);
        Vector2 aimDirection = mousePos.sub(playerPos).nor();
        
        rotation = aimDirection.angleDeg() - 90f;

        timeSinceLastShot += delta; 

        // isbuttonpressed pra segurar o clique e firerate pra limitar a velocidade
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && timeSinceLastShot >= fireRate) {
            shootSound.play(0.5f);
            
            float offset = height / 2; 
            float spawnX = playerPos.x + (aimDirection.x * offset);
            float spawnY = playerPos.y + (aimDirection.y * offset);
            
            bullets.add(new Bullet(spawnX, spawnY, new Vector2(aimDirection), baseDamage));
            
            // reseta o tempo do tiro
            timeSinceLastShot = 0f; 
        }
        
        for(Bullet bullet : bullets) {
            bullet.update(delta);
        } 
    }

    public void render(SpriteBatch batch) { 
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

    public void addCoins(int amount) {
        this.coins += amount;
    }

    public int getCoins() {
        return coins;
    }
}