package com.craivet.ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

/**
 * Muestra una aplicacion de desplazamiento lateral para juegos 2D usando glPushMatrix y glPopMatrix.
 */

public class SideScrollerDemo {

    private float translate_x;

    private void start() {

        setUpDisplay();
        setUpOpenGL();

        // Traslacion a lo largo del eje x
        translate_x = 0;

        while (!Display.isCloseRequested()) {
            render();
            Display.update();
            Display.sync(FPS);
        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Demostracion de desplazamiento lateral");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create();
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy();
            System.exit(1);
        }
    }

    private void setUpOpenGL() {
        /* Configure una presentacion ortografica donde (0, 0) es la esquina superior izquierda y (640, 480) es la esquina
         * inferior derecha. */
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);

        // Coloca otra matriz, un clon de la actual, en la pila de matrices
        glPushMatrix();

        // Empuja la pantalla a la izquierda o hacia la derecha dependiendo de translate_x
        glTranslatef(translate_x, 0, 0);

        /*
         * Si la barra espaciadora esta presionada y el mouse se encuentra dentro de los limites horizontales de la ventana,
         * entonces se aumenta/disminuye el translate_x por el movimiento dinamico X del mouse.
         */
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Mouse.getX() >= 0 && Mouse.getX() < WIDTH) translate_x += Mouse.getDX();

        // Recupera las coordenadas "verdaderas" del mouse
        int mousex = Mouse.getX();
        int mousey = 480 - Mouse.getY() - 1;

        System.out.println("X:" + mousex + ", Y:" + mousey);

        // Hace un poco de representacion de OpenGL (codigo de SimpleoglRenderer .Java)
        glBegin(GL_QUADS);
        glVertex2i(400, 400); // Upper-left
        glVertex2i(450, 400); // Upper-right
        glVertex2i(450, 450); // Bottom-right
        glVertex2i(400, 450); // Bottom-left
        glEnd();

        glBegin(GL_LINES);
        glVertex2i(100, 100);
        glVertex2i(200, 200);
        glEnd();

        // Desecha las traducciones en la matriz
        glPopMatrix();

    }

    public static void main(String[] args) {
        new SideScrollerDemo().start();
    }

}