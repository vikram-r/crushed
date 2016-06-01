package com.vramesh.crushed;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Kid {
    public enum State {
        IDLE, LEFT, RIGHT, JUMP, DEAD
    }

    Vector2 pos = new Vector2();
    State state = State.IDLE;
    float stateTime = 0;
    public Rectangle hitBox = new Rectangle();

    public Kid(float x, float y) {
        pos.x = x;
        pos.y = y;
        stateTime = 0;

        hitBox.set(pos.x, pos.y, 26f, 40f); //todo maybe center x and y?
    }

    public void update(float delta) {
        stateTime += delta;
    }
}
