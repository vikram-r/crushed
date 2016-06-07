package com.vramesh.crushed;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Block implements Pool.Poolable {

    Array<Rectangle> rectangles;
    Float mass;

    public Block(Array<Rectangle> rectangles, Float mass) {
        this.rectangles = rectangles;
        this.mass = mass;
    }

    @Override
    public void reset() {
        rectangles = null;
        mass = null;
        //todo
    }
}
