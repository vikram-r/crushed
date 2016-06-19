package com.vramesh.crushed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Kid {
    Board board;

    public enum State {
        IDLE, RUN, JUMP, FALL, DEAD
    }
    static final int RIGHT = 1;
    static final int LEFT = -1;
    static final float MAX_VEL = 100f;
    static final float JUMP_VEL = 550f;
    static final float MASS = 10f;

    Vector2 pos = new Vector2();
    Vector2 vel = new Vector2();
    Vector2 accel = new Vector2();

    State state;
    boolean isGrounded = true;
    boolean isOnBox = false;
    int dir;
    float stateTime;
    public Rectangle hitBox = new Rectangle();

    public Kid(float x, float y, Board board) {
        this.board = board;
        pos.x = x;
        pos.y = y;
        vel.x = 0;
        vel.y = 0;
        accel.x = 0;
        accel.y = Board.GRAVITY * MASS;

        state = State.IDLE;
        dir = RIGHT;

        stateTime = 0;

        hitBox.set(pos.x, pos.y, 26f, 40f);
    }

    public void update(float delta) {
        handleInputs();
        accel.y = Board.GRAVITY * MASS;
        accel.scl(delta);
        vel.add(accel.x, !isGrounded ? accel.y : 0); //accel.x will always be 0 for now
        vel.scl(delta);
        move(delta);
        vel.scl(1f/delta);
        stateTime += delta;
    }

    private void move(float delta) {
        //todo collision check
        hitBox.x += vel.x;
        hitBox.y += vel.y;
        checkBoundsCollisions();
        checkBlockCollisions();

        if (!isGrounded && !isOnBox) {
            state = State.FALL;
        }

        pos.set(hitBox.x, hitBox.y);
    }

    private void checkBoundsCollisions() {
        //check left and right
        if (hitBox.x + hitBox.width >= Board.WIDTH) {
            vel.x = 0;
            hitBox.x = Board.WIDTH - hitBox.width;
        } else if (hitBox.x <= 0) {
            vel.x = 0;
            hitBox.x = 0;
        }

        //check top and bottom
        if (hitBox.y <= 0) {
            vel.y = 0;
            hitBox.y = 0;
            if (!isGrounded) {
                isGrounded = true;
                state = State.IDLE;
            }
        } else if (hitBox.y + hitBox.height >= Board.HEIGHT) {
            vel.y = 0;
            hitBox.y = Board.HEIGHT - hitBox.height;
        }
    }

    private void checkBlockCollisions() {
        boolean nowOnBox = false;
        //check block collisions
        for (int i = 0; i < board.blockManager.getExistingBlocks().size; i++) {
            for (Rectangle blockRect: board.blockManager.getExistingBlocks().get(i).rectangles) {
                if (hitBox.overlaps(blockRect)) {
                    //check y collisions first, then x if necessary
                    if (hitBox.y < blockRect.y + blockRect.height && hitBox.y > blockRect.y) { //block below
                        nowOnBox = true;
                        vel.y = 0;
                        //todo enabling y correction is causing the state to cycle between FALL and IDLE, which makes the animation looks bad. needs fix
//                        hitBox.y = blockRect.y + blockRect.height;
                        if (!isGrounded) {
                            isGrounded = true;
                            state = State.IDLE;
                        }
                    } else if (hitBox.y + hitBox.height > blockRect.y && hitBox.y + hitBox.height < blockRect.y + blockRect.height) { //block above
                        vel.y = 0;
                        hitBox.y = blockRect.y - hitBox.height;
                    } else if (hitBox.x < blockRect.x + blockRect.width && hitBox.x > blockRect.x) { //block to left
                        vel.x = 0;
                        hitBox.x = blockRect.x + blockRect.width;
                    } else if (hitBox.x + hitBox.width > blockRect.x && hitBox.x + hitBox.width < blockRect.x + blockRect.width) { //block to right
                        vel.x = 0;
                        hitBox.x = blockRect.x - hitBox.width;
                    }
                }
            }
        }
        //if stepped of a block, now falling
        if (isOnBox && !nowOnBox) {
            isGrounded = false;
        }
        isOnBox = nowOnBox;
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
            if (isGrounded) {
                isGrounded = false;
                state = State.JUMP;
                vel.y = JUMP_VEL;
            }

            //todo maybe add double jump, or play sound when trying to jump in air
        }

        //right has priority over left
        if (isRightPressed(x1, x2)) {
            dir = RIGHT;
            if (state == State.IDLE && isGrounded) state = State.RUN;
            vel.x = MAX_VEL * dir;
        } else if (isLeftPressed(x1, x2)) {
            dir = LEFT;
            if (state == State.IDLE && isGrounded) state = State.RUN;
            vel.x = MAX_VEL * dir;
        } else {
            if (isGrounded) state = State.IDLE;
            vel.x = 0;
        }

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
