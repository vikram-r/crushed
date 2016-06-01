package com.vramesh.crushed.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.vramesh.crushed.Board;
import com.vramesh.crushed.BoardRenderer;


public class GameScreen extends CrushedScreen {

    BoardRenderer renderer;
    Board board;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        board = new Board();
        renderer = new BoardRenderer(board);
    }

    @Override
    public void render(float delta) {
        //update board state
        board.update(delta);
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //draw the board
        renderer.render(delta);
    }

    @Override
    public void hide() {

    }
}
