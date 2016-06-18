package com.vramesh.crushed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Kid {

    Board board;

    Body body;

    public enum State {
        IDLE, RUN, JUMP, FALL, DEAD
    }
    static final int RIGHT = 1;
    static final int LEFT = -1;
    static final float MAX_VEL = 100f;
    static final float JUMP_VEL = 550f;
//    static final float MASS = 10f;
//
    Vector2 pos = new Vector2();
//    Vector2 accel = new Vector2();
//
    State state;
    int dir;
    float stateTime;
//    public Rectangle hitBox = new Rectangle();

    public Kid(Board board, float x, float y) {
        this.board = board;


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        Body body = board.world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();

        //26f x 40f
        shape.setAsBox(13f, 20f);
        body.createFixture(shape, 1);

        this.body = body;

        shape.dispose();

        dir = RIGHT;

//        pos.x = x;
//        pos.y = y;
//        vel.x = 0;
//        vel.y = 0;
//        accel.x = 0;
//        accel.y = Board.GRAVITY * MASS;
//
        state = State.IDLE;
//        dir = RIGHT;
//
        stateTime = 0;
//
//        hitBox.set(pos.x, pos.y, 26f, 40f);
    }

    public void update(float delta) {

        handleInputs();

        pos = body.getPosition();

        stateTime += delta;
    }

//    public void handleInputs() {
//        float x1 = (Gdx.input.getX() / (float)Gdx.graphics.getWidth()) * Board.WIDTH; //first touch
//        float x2 = (Gdx.input.getX() / (float)Gdx.graphics.getWidth()) * Board.WIDTH; //second touch
//
//        if (isLeftPressed(x1, x2)) {
//            body.setLinearVelocity(-1 * MAX_VEL, 0f);
//            dir = LEFT;
//        } else if (isRightPressed(x1, x2)) {
//            body.setLinearVelocity(MAX_VEL, 0f);
//            dir = RIGHT;
//        } else {
//            body.setLinearVelocity(0, 0);
//        }
//    }

//    public void update(float delta) {
//        handleInputs();
//        accel.y = Board.GRAVITY * MASS;
//        accel.scl(delta);
//        vel.add(accel.x, isAirborne() ? accel.y : 0); //accel.x will always be 0 for now
//        vel.scl(delta);
//        move(delta);
//        vel.scl(1f/delta);
//        stateTime += delta;
//    }
//
//    private void move(float delta) {
//        //todo collision check
//        hitBox.x += vel.x;
//        checkXCollisions();
//
//        hitBox.y += vel.y;
//        checkYCollisions();
//
//        pos.set(hitBox.x, hitBox.y);
//    }
//
//    private void checkXCollisions() {
//        if (hitBox.x + hitBox.width >= Board.WIDTH) {
//            vel.x = 0;
//            hitBox.x = Board.WIDTH - hitBox.width;
//        } else if (hitBox.x <= 0) {
//            vel.x = 0;
//            hitBox.x = 0;
//        }
//    }
//
//    private void checkYCollisions() {
//        if (hitBox.y <= 0) {
//            vel.y = 0;
//            hitBox.y = 0;
//            if (isAirborne()) state = State.IDLE;
//        } else if (hitBox.y >= Board.HEIGHT) {
//            vel.y = 0;
//            hitBox.y = Board.HEIGHT - hitBox.height;
//        }
//    }

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
            if (!isAirborne()) {
                state = State.JUMP;
//                vel.y = JUMP_VEL;
                body.setLinearVelocity(body.getLinearVelocity().x, JUMP_VEL);
            }

            //todo maybe add double jump, or play sound when trying to jump in air
        }

        //right has priority over left
        if (isRightPressed(x1, x2)) {
            dir = RIGHT;
            if (state == State.IDLE && !isAirborne()) state = State.RUN;
//            vel.x = MAX_VEL * dir;
            body.setLinearVelocity(MAX_VEL, body.getLinearVelocity().y);

        } else if (isLeftPressed(x1, x2)) {
            dir = LEFT;
            if (state == State.IDLE && !isAirborne()) state = State.RUN;
//            vel.x = MAX_VEL * dir;
            body.setLinearVelocity(-1 * MAX_VEL, body.getLinearVelocity().y);

        } else {
            if (!isAirborne()) state = State.IDLE;
//            vel.x = 0;
            body.setLinearVelocity(0, body.getLinearVelocity().y);
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

    public float getWidth() {
        return 26f;
    }

    public float getHeight() {
        return 40f;
    }

}
