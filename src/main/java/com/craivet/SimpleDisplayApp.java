package com.craivet;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static com.craivet.Global.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * La clase Display proporciona metodos para crear, mostrar y administrar la ventana de la aplicacion. Algunas de las operaciones
 * tipicas que puede realizar con la clase Display incluyen crear una ventana con un contexto OpenGL, manipular eventos de entrada
 * del usuario y actualizar el contenido de la ventana.
 * <p>
 * Un monitor de computadora es una superficie 2D. Una escena 3D renderizada por OpenGL debe proyectarse en la pantalla de
 * la computadora como una imagen 2D. La matriz GL_PROJECTION se utiliza para esta transformacion de proyeccion:
 * <br><br>
 * {@code glMatrixMode(GL_PROJECTION);}
 * <br><br>
 * Recursos: <a href="https://github.com/mattdesl/lwjgl-basics/wiki/Display">Display</a>
 * <a href="http://www.songho.ca/opengl/gl_projectionmatrix.html">Projection Matrix</a>
 */

public class SimpleDisplayApp {

    /**
     * Inicia la aplicacion.
     */
    private void start() {

        setUpDisplay();
        setUpOpenGL();

        // Actualiza y renderiza los fotogramas mientras la pantalla no esta cerrada
        while (!Display.isCloseRequested()) {
            // Si se cambio el tamaño de la ventana, actualiza la proyeccion
            if (Display.wasResized()) resize();
            render();
            // Voltea los buffers y sincroniza a 60 FPS
            Display.update();
            Display.sync(FPS);
        }

        Display.destroy();
    }

    /**
     * Configura la pantalla.
     */
    private void setUpDisplay() {
        try {
            Display.setTitle("Display Test");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setResizable(true);
            // Display.setVSyncEnabled(true);
            Display.create();
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy();
            System.exit(1);
        }
    }

    /**
     * Configura OpenGL.
     */
    private void setUpOpenGL() {
        // left = xmin, right = xmax, bottom = ymin, top = ymax, near = zmin, far = zmax
        glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
    }

    /**
     * Renderiza la ventana.
     */
    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);

        // Como esta primitiva requiere cuatro vertices, tendremos que llamar a glVertex cuatro veces
        glBegin(GL_QUADS);
        glColor3f(1.0f, 0.0f, 0.0f); // Green
        glVertex2i(0, 0);
        glColor3b((byte) 0, (byte) 127, (byte) 0); // Red
        glVertex2d(640.0, 0.0);
        glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
        glVertex2f(640.0f, 480.0f);
        glColor3d(0.0d, 0.0d, 1.0d); // Blue
        glVertex2i(0, 480);
        glEnd();

    }

    /**
     * Cambia el tamaño de la ventana.
     */
    private void resize() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        // Actualice la matriz de proyeccion aqui...
    }

    public static void main(String[] args) {
        new SimpleDisplayApp().start();
    }

}
