package com.vramesh.crushed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * This class is responsible for rendering all the entities onto the game "board"
 */
public class BoardRenderer {

    Board board;
    OrthographicCamera camera;
    SpriteBatch batch = new SpriteBatch();

    Animation kidRight;
    Animation kidLeft;
    Animation kidJumpRight;
    Animation kidJumpLeft;
    Animation kidFallRight;
    Animation kidFallLeft;
    Animation kidIdleRight;
    Animation kidIdleLeft;
    Animation kidDead;

    TextureRegion block;

    public BoardRenderer(Board board) {
        this.board = board;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Board.WIDTH, Board.HEIGHT);

        createAnimations();
    }

    public void render(float delta) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawKid();
        drawBlocks();
        batch.end();
    }

    private void drawKid() {
        Animation anim;
//        System.out.println(board.kid.state);
        switch (board.kid.state) {
            case IDLE:
                anim = board.kid.dir == Kid.RIGHT ? kidIdleRight : kidIdleLeft;
                break;
            case RUN:
                anim = board.kid.dir == Kid.RIGHT ? kidRight : kidLeft;
                break;
            case JUMP:
                anim = board.kid.dir == Kid.RIGHT ? kidJumpRight : kidJumpLeft;
                break;
            case FALL:
                anim = board.kid.dir == Kid.RIGHT ? kidFallRight : kidFallLeft;
                break;
            case DEAD:
                anim = kidDead;
                break;
            default:
                throw new IllegalStateException();
        }

        batch.draw(anim.getKeyFrame(board.kid.stateTime, true), board.kid.pos.x, board.kid.pos.y, board.kid.hitBox.width, board.kid.hitBox.height);
    }

    private void drawBlocks() {
        for (Block blockObj : board.blockManager.getExistingBlocks()) {
            for (Rectangle rect : blockObj.rectangles) {
                batch.draw(block, rect.x, rect.y);
            }
        }
    }

    private void createAnimations() {
        TextureAtlas kidAtlas = new TextureAtlas(Gdx.files.internal("kid/kid.atlas"));

        Array<TextureAtlas.AtlasRegion> kidRightRegions = kidAtlas.getRegions();
        Array<TextureAtlas.AtlasRegion> kidLeftRegions = new Array<TextureAtlas.AtlasRegion>();
        for (TextureAtlas.AtlasRegion right : kidRightRegions) {
            TextureAtlas.AtlasRegion left = new TextureAtlas.AtlasRegion(right);
            left.flip(true, false);
            kidLeftRegions.add(left);
        }

        //todo ugly, couldn't figure out good way to convert iterable to Array, so couldn't use predicates
        kidIdleRight = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidRightRegions.toArray(), 9, 2));
        kidIdleLeft = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidLeftRegions.toArray(), 9, 2));

        kidRight = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidRightRegions.toArray(), 3, 6));
        kidLeft = new Animation(.1f, new Array<TextureAtlas.AtlasRegion>(true, kidLeftRegions.toArray(), 3, 6));

        kidJumpRight = new Animation(.1f, kidRightRegions.get(2));
        kidJumpLeft = new Animation(.1f, kidLeftRegions.get(2));

        kidFallRight = new Animation(.1f, kidRightRegions.get(1));
        kidFallLeft = new Animation(.1f, kidLeftRegions.get(1));

        TextureAtlas tilesAtlas = new TextureAtlas(Gdx.files.internal("tiles/tiles.atlas"));
        System.out.println(tilesAtlas.getRegions().toString());
        block = tilesAtlas.findRegion("grey_block");

    }

}
