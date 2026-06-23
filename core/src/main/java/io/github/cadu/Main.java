package io.github.cadu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import io.github.cadu.screens.MainMenuScreen;
import io.github.cadu.screens.StoryScreen;

public class Main extends Game {

    @Override
    public void create() {
        com.badlogic.gdx.Preferences prefs = Gdx.app.getPreferences("save");
        boolean jaViuAHistoria = prefs.getBoolean("jaViuAHistoria", false);
        
        if (!jaViuAHistoria) {
            prefs.putBoolean("jaViuAHistoria", true);
            prefs.flush();
            setScreen(new StoryScreen(this));
        } else {
            setScreen(new MainMenuScreen(this));
        }
    }
}