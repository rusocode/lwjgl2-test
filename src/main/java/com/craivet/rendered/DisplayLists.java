package com.craivet.rendered;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;

import static com.craivet.Global.*;


/**
 * Renderiza un triangulo de color usando listas de visualizacion.
 */

public class DisplayLists {

    private int displayListHandle;

    private void start() {

        setUpDisplay();
        setUpOpenGL();

        displayListHandle = glGenLists(1);

        glNewList(displayListHandle, GL_COMPILE);
        glBegin(GL_TRIANGLES);
        glColor3f(1, 0, 0);
        glVertex2f(-0.5f, -0.5f);
        glColor3f(0, 1, 0);
        glVertex2f(0.5f, -0.5f);
        glColor3f(0, 0, 1);
        glVertex2f(0.5f, 0.5f);
        glEnd();
        glEndList();

        while (!Display.isCloseRequested()) {

            render();

            Display.update();
            Display.sync(FPS);

        }

        glDeleteLists(displayListHandle, 1);
        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Display Lists Demo");
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
        glLoadIdentity();
        glOrtho(1, -1, 1, -1, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        glCallList(displayListHandle);
    }

    public static void main(String[] args) {
        new DisplayLists().start();
    }

}