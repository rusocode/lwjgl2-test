package com.craivet.rendered;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import static com.craivet.Global.*;

/**
 * Renderiza un triangulo de color usando el modo inmediato.
 * <p>
 * Actualmente el modo inmediato esta obsoleto y la razon por la que no es optimo es que la tarjeta grafica esta vinculada
 * directamente al flujo de tu programa. El controlador no puede decirle a la GPU que comience a renderizar antes de glEnd porque
 * no sabe cuando terminara de enviar datos.
 * <br><br>
 * Recursos:
 * <a href="https://stackoverflow.com/questions/6733934/what-does-immediate-mode-mean-in-opengl">What does "immediate mode" mean in OpenGL?</a>
 */

public class ImmediateMode {

    private void start() {
        setUpDisplay();
        setUpOpenGL();
        while (!Display.isCloseRequested()) {
            render();
            Display.update();
            Display.sync(FPS);
        }
        Display.destroy();
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);

        glBegin(GL_TRIANGLES);
        glColor3f(1, 0, 0);
        glVertex2f(-0.5f, -0.5f);
        glColor3f(0, 1, 0);
        glVertex2f(0.5f, -0.5f);
        glColor3f(0, 0, 1);
        glVertex2f(0.5f, 0.5f);
        glEnd();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Immediate Mode");
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

    public static void main(String[] args) {
        new ImmediateMode().start();
    }

}
