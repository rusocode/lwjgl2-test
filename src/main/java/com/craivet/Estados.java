package com.craivet;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static com.craivet.Global.HEIGHT;
import static com.craivet.Global.WIDTH;
import static org.lwjgl.opengl.GL11.*;

/**
 * Controla los estados del juego.
 */

public class Estados {

    private enum States {
        INTRO, MAIN_MENU, GAME
    }

    private static States state = States.INTRO;

    public void start() {

        setUpDisplay();
        setUpOpenGL();

        while (!Display.isCloseRequested()) {

            render();
            checkInput();

            Display.update();
            Display.sync(60);

        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Game states");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy();
            System.exit(1);
        }
    }

    private void setUpOpenGL() {
        glMatrixMode(GL_PROJECTION);
        glOrtho(0, 640, 480, 0, 1, -1); // esq sup izquierda
        glMatrixMode(GL_MODELVIEW);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        switch (state) {
            case INTRO:
                glColor3f(1.0f, 0f, 0f);
                glRectf(0, 0, 640, 480);
                break;
            case GAME:
                glColor3f(0f, 1.0f, 0f);
                glRectf(0, 0, 640, 480);
                break;
            case MAIN_MENU:
                glColor3f(0f, 0f, 1.0f);
                glRectf(0, 0, 640, 480);
                break;
        }
    }

    private void checkInput() {
        switch (state) {
            case INTRO:
                if (Keyboard.isKeyDown(Keyboard.KEY_A)) state = States.MAIN_MENU;
                break;
            case GAME:
                if (Keyboard.isKeyDown(Keyboard.KEY_B)) state = States.MAIN_MENU;
                break;
            case MAIN_MENU:
                if (Keyboard.isKeyDown(Keyboard.KEY_C)) state = States.GAME;
                if (Keyboard.isKeyDown(Keyboard.KEY_D)) state = States.INTRO;
                break;
        }
    }

    public static void main(String[] args) {
        new Estados().start();
    }

}