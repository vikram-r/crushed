package com.vramesh.crushed.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

/**
 * Helper class to create poolable pairs
 *
 */
//todo maybe make T and U also implement poolable? If so, then can't use with rectangles
public class PoolablePair<T, U> implements Pool.Poolable {

    public T first;
    public U second;

    public PoolablePair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void reset() {
//        first.reset();
        first = null;

//        second.reset();
        second = null;
    }
}
