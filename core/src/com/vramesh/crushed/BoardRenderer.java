package com.vramesh.crushed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Predicate;

/**
 * This class is responsible for rendering all the entities onto the game "board"
 */
public class BoardRenderer {

    Board board;
    OrthographicCamera camera;
    SpriteBatch batch = new SpriteBatch();


    Animation kidLeft;
    Animation kidRight;
    Animation kidJumpLeft;
    Animation kidJumpRight;
    Animation kidIdleLeft;
    Animation kidIdleRight;
    Animation kidDead;

    public BoardRenderer(Board board) {
        this.board = board;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 800);

        createAnimations();
    }

    public void render(float delta) {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawKid();
        batch.end();
    }

    private void drawKid() {
        Animation anim = kidRight;
        batch.draw(anim.getKeyFrame(board.kid.stateTime, true), board.kid.pos.x, board.kid.pos.y, board.kid.hitBox.width, board.kid.hitBox.height);
    }
    
    private void createAnimations() {
        TextureAtlas kidAtlas = new TextureAtlas(Gdx.files.internal("kid/kid.atlas"));

        Array<TextureAtlas.AtlasRegion> kidRightRegions = kidAtlas.getRegions();
        Array<TextureAtlas.AtlasRegion> kidLeftRegions = kidAtlas.getRegions();
        for (TextureRegion left : kidLeftRegions) {
            left.flip(true, false);
        }

        //todo ugly, couldn't figure out good way to convert iterable to Array, so couldn't use predicates
        kidIdleRight = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidRightRegions.toArray(), 9, 2));
        kidIdleLeft = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidLeftRegions.toArray(), 9, 2));

        kidRight = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidRightRegions.toArray(), 3, 6));
        kidLeft = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidLeftRegions.toArray(), 3, 6));


        System.out.println(new Array<TextureAtlas.AtlasRegion>(false, kidRightRegions.toArray(), 3, 6).toString());

        TextureAtlas atlas;
        atlas = new TextureAtlas(Gdx.files.internal("kid/kid.atlas"));
        for (TextureAtlas.AtlasRegion r : atlas.getRegions()) {
            System.out.println(r.name);
        }

    }

}
