package com.craivet;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

public class glOrtho {

    public static void main(String[] args) {

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle("Coordinate Systems");
            Display.create();
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy();
            System.exit(1);
        }

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        // glOrtho(0, 640, 480, 0, 1, -1);
        glOrtho(-1, 1, -1, 1, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        while (!Display.isCloseRequested()) {

            glClear(GL_COLOR_BUFFER_BIT);

            glBegin(GL_TRIANGLES);

            // Dibuja un triangulo sin color (blanco)
            glVertex2f(-1, -1); // inf izq
            glVertex2f(1, -1); // inf der
            glVertex2f(1, 1); // sup der

            glEnd();

            Display.update();
            Display.sync(FPS);
        }

        Display.destroy();

    }

}