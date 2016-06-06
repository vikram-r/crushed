package com.vramesh.crushed;

/**
 * This class is responsible for maintaining the states of entities on the game "board"
 */
public class Board {

    final static float WIDTH = 480f;
    final static float HEIGHT = 800f;

    final static float GRAVITY = -200f;

    Kid kid;

    public Board() {
        kid = new Kid(240, 400);
    }

    public void update(float delta) {
        kid.update(delta);
    }
}
