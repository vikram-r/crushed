package com.vramesh.crushed.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MainMenuScreen extends CrushedScreen {
    
    SpriteBatch batch;
    BitmapFont font;

    public MainMenuScreen(Game game) {
        super(game);
        batch = new SpriteBatch();
        font = new BitmapFont();
        batch.getProjectionMatrix().setToOrtho2D(0, 0, 480, 800);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Crushed", 100, 150);
        font.draw(batch, "Tap to start", 100, 100);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void hide() {
        batch.dispose();
        font.dispose();
    }
}
