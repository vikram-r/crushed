package com.vramesh.crushed.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.vramesh.crushed.Crushed;

/**
 * Created by vikram on 5/30/16.
 */
public class MainMenuScreen {


    private OrthographicCamera camera;

    public MainMenuScreen(Crushed game) {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, 480, 800);
    }
}
