package com.vramesh.crushed;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Singleton responsible for managing and generating blocks.
 */
public class BlockManager {

    //todo should blocks be fixed size/weight?
    static final float MAX_WIDTH = 32f;
    static final float MAX_HEIGHT = 32f;

    private long lastCreateTime = 0;
    private Array<Block> existingBlocks;

    public BlockManager() {
        existingBlocks = new Array<Block>();
    }

    public void spawnBlocks() {
        if(TimeUtils.nanoTime() - lastCreateTime > 3000000000L) {
            //todo make this choose a random type of block
            //32,32
            float randX = MathUtils.random(0f, Board.WIDTH - MAX_WIDTH);
            //todo make sure not spawning on top of another box!
            newSingleBlock(new Rectangle(randX, Board.HEIGHT - MAX_HEIGHT, MAX_WIDTH, MAX_HEIGHT), 1);
            lastCreateTime = TimeUtils.nanoTime();
        }
    }

    public void updateBlocks(float delta) {
        for (Block block : existingBlocks) {
            block.update(delta);
        }
    }

    /**
     * Get all of currently existing blocks
     * @return all currently existing blocks
     */
    public Array<Block> getExistingBlocks() {
        return existingBlocks;
    }

    /**
     * Generates a new Single Block (a block with only 1 rectangle)
     * @return a reference to the created Single Block
     */
    public Block newSingleBlock(Rectangle rectangle, float speed) {
        Block singleBlock = new BlockBuilder()
                .withRectangle(rectangle)
                .withSpeed(speed)
                .build();
        //todo make sure no existing blocks overlap with this new one

        existingBlocks.add(singleBlock);
        return singleBlock;
    }


    /**
     * Builder to create a block of any shape
     */
    public static class BlockBuilder {

        private Array<Rectangle> rectangles;
        private Float speed;

        public BlockBuilder withRectangle(Rectangle rect) {
            if (rectangles == null) rectangles = new Array<Rectangle>();
            //todo ensure this rectangle connects with one of the previous
            rectangles.add(rect);
            return this;
        }

        public BlockBuilder withSpeed(float speed) {
            this.speed = -1 * Math.abs(speed);
            return this;
        }

        public Block build() {
            if (rectangles == null) {
                throw new IllegalArgumentException("Not all parameters set");
            }
            return new Block(rectangles, speed != null ? speed : 1);
        }
    }
}
