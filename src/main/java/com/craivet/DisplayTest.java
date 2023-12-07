package com.craivet;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static com.craivet.Global.*;

/**
 * The Display class provided methods to create, display, and manage the application window. Some of the typical operations you
 * might do with the Display class include creating a window with an OpenGL context, manipulating user input events, and updating
 * the window's contents.
 */

public class DisplayTest {

    /**
     * Start the application.
     */
    private void start() {
        setUpDisplay();
        // Updates and renders the frames while the display is not closed
        while (!Display.isCloseRequested()) {
            Display.update();
            Display.sync(FPS);
        }
        Display.destroy();
    }

    /**
     * Set up the display.
     */
    private void setUpDisplay() {
        try {
            Display.setTitle("Display Test");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy(); // TODO Is it necessary?
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new DisplayTest().start();
    }

}
