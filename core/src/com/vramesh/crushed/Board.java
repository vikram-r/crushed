package com.vramesh.crushed;

/**
 * This class is responsible for maintaining the states of entities on the game "board"
 */
public class Board {

    Kid kid;

    public void update(float delta) {
        kid = new Kid(240, 400);
        kid.update(delta);
    }
}
