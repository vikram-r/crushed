package com.vramesh.crushed.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vramesh.crushed.Crushed;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = "Crushed";
        config.width = 480;
        config.height = 800;

        new LwjglApplication(new Crushed(), config);
    }
}
