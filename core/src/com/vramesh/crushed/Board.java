package com.vramesh.crushed;

/**
 * This class is responsible for maintaining the states of entities on the game "board"
 */
public class Board {

    Kid kid;

    public Board() {
        kid = new Kid(240, 400);
    }

    public void update(float delta) {
        kid.update(delta);
    }
}
