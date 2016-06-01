package com.vramesh.crushed;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.vramesh.crushed.screens.MainMenuScreen;


public class Crushed extends Game {

    @Override
    public void create () {
        setScreen(new MainMenuScreen(this));
    }
}
