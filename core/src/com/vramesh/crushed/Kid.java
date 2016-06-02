package com.vramesh.crushed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Kid {
    public enum State {
        IDLE, RUN, JUMP, FALL, DEAD
    }
    static final int RIGHT = 1;
    static final int LEFT = -1;

    static final float ACCELERATION = 5f;
    static final float DECELERATION = -5f;
    static final float MAX_VEL = 5f;


    Vector2 pos = new Vector2();
    Vector2 vel = new Vector2();
    Vector2 accel = new Vector2();

    State state;
    int dir;
    float stateTime;
    public Rectangle hitBox = new Rectangle();

    public Kid(float x, float y) {
        pos.x = x;
        pos.y = y;
        vel.x = 0;
        vel.y = 0;
        accel.y = Board.GRAVITY;

        state = State.IDLE;
        dir = RIGHT;

        stateTime = 0;

        hitBox.set(pos.x, pos.y, 26f, 40f); //todo maybe center x and y?
    }

    public void update(float delta) {
        stateTime += delta;

        handleInputs();

        accel.scl(delta);
        vel.add(accel.x, state == State.JUMP ? accel.y : 0);
        if (accel.x == 0) vel.x *= DECELERATION;

        if (vel.x > MAX_VEL) vel.x = MAX_VEL;
        if (vel.x < -MAX_VEL) vel.x = -MAX_VEL;
        vel.scl(delta);

        move(delta);
    }

    private void move(float delta) {
        //todo collision check
        pos.add(vel.scl(delta));
    }

    /**
     *
     * 0    160   320   480
     * |-----|-----|-----| 800
     * |     |     |     |
     * |     |     |     |
     * |     |     |     |
     * |     |     |     |
     * |  L  |  J  |  R  |
     * |_____|_____|_____| 0
     *
     */
    private void handleInputs() {
        float x1 = (Gdx.input.getX() / (float)Gdx.graphics.getWidth()) * Board.WIDTH; //first touch
        float x2 = (Gdx.input.getX() / (float)Gdx.graphics.getWidth()) * Board.WIDTH; //second touch

        if (isJumpPressed(x1, x2)) {
            if (state != State.FALL) state = State.JUMP;
        }

        //right has priority over left
        if (isRightPressed(x1, x2)) {
            dir = RIGHT;
            if (state == State.IDLE && !isAirborne()) state = State.RUN;
            accel.x = ACCELERATION * dir;
        } else if (isLeftPressed(x1, x2)) {
            dir = LEFT;
            if (state == State.IDLE && !isAirborne()) state = State.RUN;
            accel.x = ACCELERATION * dir;
        } else {
            if (!isAirborne()) state = State.IDLE;
            accel.x = 0;
        }

    }

    private boolean isAirborne() {
        return (state == State.JUMP || state == State.FALL);
    }

    private boolean isRightPressed(float x1, float x2) {
        return (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.D)
                || (Gdx.input.isTouched() && x1 > 2 * (Board.WIDTH / 3) && x1 <= Board.WIDTH)
                || (Gdx.input.isTouched() && x2 > 2 * (Board.WIDTH / 3) && x2 <= Board.WIDTH));
    }

    private boolean isLeftPressed(float x1, float x2) {
        return (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.A)
                || (Gdx.input.isTouched() && x1 >= 0 && x1 <= 1 * (Board.WIDTH / 3))
                || (Gdx.input.isTouched() && x2 >= 0 && x2 <= 1 * (Board.WIDTH / 3)));
    }

    private boolean isJumpPressed(float x1, float x2) {
        return (Gdx.input.isKeyPressed(Input.Keys.UP)
                || Gdx.input.isKeyPressed(Input.Keys.W)
                || (Gdx.input.isTouched() && x1 > 1 * (Board.WIDTH / 3) && x1 <= 2 * (Board.WIDTH / 3))
                || (Gdx.input.isTouched() && x2 > 1 * (Board.WIDTH / 3) && x2 <= 2 * (Board.WIDTH / 3)));
    }

}
