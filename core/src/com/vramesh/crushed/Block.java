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

    static final float MAX_VEL = 100f;
    Vector2 vel = new Vector2();
    Vector2 accel = new Vector2();
    Array<Rectangle> rectangles;
    float mass;
    float stateTime;

    //this method should not be called directly. BlockManager.BlockBuilder should be used
    public Block(Array<Rectangle> rectangles, float mass) {
        this.rectangles = rectangles;
        this.mass = mass;
        this.state = State.FALL;
        this.stateTime = 0;
        accel.y = Board.GRAVITY * this.mass;
    }

    public void update(float delta) {
        accel.y = Board.GRAVITY * mass;

        accel.scl(delta);
        vel.add(0, state == State.FALL ? accel.y : 0); //blocks only fall down for now (no horizontal movement)
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
                System.out.println("here");
                state = State.IDLE;
                shiftUp = Math.max(0 - rect.y, shiftUp);
            }
        }
        //if a block has gone too far, we also need to shift all rectangles up to compensate
//        if (shiftUp != 0) {
//            for (Rectangle rect : rectangles) {
//                rect.setY(rect.getY() - shiftUp);
//            }
//        }

        //todo more intelligent stuff
    }

    @Override
    public void reset() {
        rectangles = null;
        //todo
    }
}
