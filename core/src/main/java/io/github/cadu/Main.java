package io.github.cadu;

import com.badlogic.gdx.Game;
import io.github.cadu.screens.GameScreen;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}