package com.vramesh.crushed;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.vramesh.crushed.utils.PoolablePair;

import java.util.Optional;
import java.util.function.Function;

public class Block implements Pool.Poolable {
    public enum State {
        FALL, IDLE
    }
    State state;

    Vector2 vel = new Vector2();
    Array<Rectangle> rectangles;
    float speed;
    float stateTime;
    BlockManager blockManager;

    //this method should not be called directly. BlockManager.BlockBuilder should be used
    public Block(BlockManager blockManager, Array<Rectangle> rectangles, float speed) {
        this.blockManager = blockManager;
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
        //if a block has gone too far, we also need to shift all rectangles up to compensate
        shiftUp(shiftUp);

        //check for collisions with other blocks
        collidesWithBlocks().map(new Function<PoolablePair<Rectangle,Rectangle>, Void>() {
            @Override
            public Void apply(PoolablePair<Rectangle, Rectangle> collided) {
                state = State.IDLE;
                vel.y = 0;
                shiftUp((collided.second.y + collided.second.height) - collided.first.y);
                //todo remove pair from pool
                return null;
            }
        });
    }

    //todo there probably needs to be some refactoring to make this more efficient

    /**
     * Compares this block with all existing blocks, to see if there is a collision.
     * A collision occurs if any of the rectangles between 2 blocks overlap, and this
     * block is above the other block (since blocks only fall downwards)
     * @return Optional containing the pair of rectangles that collided. Empty if no collision.
     *         The first element in the pair is the rectangle from this Block instance.
     */
    private Optional<PoolablePair<Rectangle, Rectangle>> collidesWithBlocks() {
        //compare this block with all other blocks
        for (int i = 0; i < blockManager.getExistingBlocks().size; i++) {
            //this is checking reference equality
            if (this != blockManager.getExistingBlocks().get(i)) {
                for (Rectangle thisRect : this.rectangles) {
                    for (Rectangle otherRect : blockManager.getExistingBlocks().get(i).rectangles) {
                        if (thisRect.overlaps(otherRect) && thisRect.y > otherRect.y) {
                            //only stop if this block is above the other
                            //todo set up pooling for these pairs
                            return Optional.of(new PoolablePair<Rectangle, Rectangle>(thisRect, otherRect));
                            //todo consider returning the rectangle with the biggest difference in y, so shiftUp is more accurate
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }


    /**
     * Helper method to shift all rectangles in the block up, to correct overstepping collision bounds
     */
    private void shiftUp(float amount) {
        if (amount != 0) {
            for (Rectangle rect : rectangles) {
                rect.setY(rect.getY() + amount);
            }
        }
    }

    @Override
    public void reset() {
        rectangles = null;
        //todo
    }
}
