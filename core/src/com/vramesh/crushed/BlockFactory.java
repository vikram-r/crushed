package com.vramesh.crushed;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Singleton responsible for generating blocks. It should use object pooling
 */
public class BlockFactory {

    private Array<Block> existingBlocks;

    public BlockFactory() {
        existingBlocks = new Array<Block>();
    }

    /**
     * Get all of currently existing blocks
     * @return
     */
    public Array<Block> getExistingBlocks() {
        return existingBlocks;
    }

    /**
     * Generates a new SingleBlock
     * @return a reference to the created SingleBlock
     */
    public Block newSingleBlock(Rectangle rectangle, float mass) {
        Block singleBlock = new BlockBuilder()
                .withRectangle(rectangle)
                .withMass(mass)
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
        private Float mass;

        public BlockBuilder withRectangle(Rectangle rect) {
            if (rectangles == null) rectangles = new Array<Rectangle>();
            rectangles.add(rect);
            return this;
        }

        public BlockBuilder withMass(float mass) {
            this.mass = mass;
            return this;
        }

        public Block build() {
            if (rectangles == null || mass == null) {
                throw new IllegalArgumentException("Not all parameters set");
            }
            return new Block(rectangles, mass);
        }
    }
}
