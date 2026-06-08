package io.github.cadu;

import com.badlogic.gdx.Game;
import io.github.cadu.screens.MainMenuScreen;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new MainMenuScreen(this)); //agora chama o menu inves de ir direto pro jogo
    }
}