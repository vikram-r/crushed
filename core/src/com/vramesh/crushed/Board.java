package com.vramesh.crushed;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * This class is responsible for maintaining the states of entities on the game "board"
 */
public class Board {

    final static float WIDTH = 480f;
    final static float HEIGHT = 800f;

    final static float GRAVITY = -100f;

    Kid kid;
    BlockManager blockManager;
    World world;

    public Board() {
        this.blockManager = new BlockManager();
        this.world = new World(new Vector2(0, GRAVITY), true);
        this.kid = new Kid(this, 240, 400);



    }

    public void update(float delta) {
        kid.update(delta);
        blockManager.spawnBlocks();
        blockManager.updateBlocks(delta);
    }
}
