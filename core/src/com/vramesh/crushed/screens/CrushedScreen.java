package com.vramesh.crushed.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public abstract class CrushedScreen implements Screen {

    Game game;

    public CrushedScreen (Game game) {
        this.game = game;
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void show () {
    }

    @Override
    public void hide () {
    }

    @Override
    public void pause () {
    }

    @Override
    public void resume () {
    }

    @Override
    public void dispose () {
    }
}
