package com.vramesh.crushed;

import com.badlogic.gdx.Game;
import com.vramesh.crushed.screens.MainMenuScreen;


public class Crushed extends Game {

    @Override
    public void create () {
        setScreen(new MainMenuScreen(this));
    }
}
