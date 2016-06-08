package com.vramesh.crushed;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Block implements Pool.Poolable {
    public enum State {
        FALL, IDLE
    }
    State state;

    Vector2 vel = new Vector2();
    Array<Rectangle> rectangles;
    float speed;
    float stateTime;

    //this method should not be called directly. BlockManager.BlockBuilder should be used
    public Block(Array<Rectangle> rectangles, float speed) {
        this.rectangles = rectangles;
        this.speed = speed;
        this.state = State.FALL;
        this.stateTime = 0;
    }

    public void update(float delta) {
        vel.add(0, state == State.FALL ? speed : 0); //blocks only fall down for now (no horizontal movement)
        vel.scl(delta);
        move();
        vel.scl(1.0f / delta);

        stateTime += delta;
    }

    private void move() {
        checkCollisions();
        if (state == State.FALL) {
            for (Rectangle rect : rectangles) {
                rect.y += vel.y;
            }
        }
    }

    private void checkCollisions() {
        float shiftUp = 0;
        for (Rectangle rect : rectangles) {
            if (rect.y <= 0) {
                vel.y = 0;
                state = State.IDLE;
                shiftUp = Math.max(0 - rect.y, shiftUp);
            }
        }
        System.out.println(shiftUp);
        //if a block has gone too far, we also need to shift all rectangles up to compensate
        if (shiftUp != 0) {
            System.out.println("here");
            for (Rectangle rect : rectangles) {
                rect.setY(rect.getY() + shiftUp);
            }
        }

        //todo more intelligent stuff
    }

    @Override
    public void reset() {
        rectangles = null;
        //todo
    }
}
