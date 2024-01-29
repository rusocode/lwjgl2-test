package com.craivet.rendered;

import javax.swing.*;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

/**
 * Renderiza un triangulo de color usando un objeto de matriz de vertice (vertex array object).
 */

public class VAO {

    private final int amountOfVertices = 3; // Cantidad de vertices (puntos)
    private final int vertexSize = 2; // Tamanio del vertice
    private final int colorSize = 3; // Tamanio del color

    private FloatBuffer vertexData; // Datos del vertice
    private FloatBuffer colorData; // Datos del color

    private void start() {

        setUpDisplay();
        setUpOpenGL();

        vertexData = BufferUtils.createFloatBuffer(amountOfVertices * vertexSize);// Crea un bufer de 6 posiciones
        vertexData.put(new float[]{-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f}); // Crea un array con los vertices
        vertexData.flip(); // Voltea el bufer a 0

        colorData = BufferUtils.createFloatBuffer(amountOfVertices * colorSize);
        colorData.put(new float[]{1, 0, 0, 0, 1, 0, 0, 0, 1});
        colorData.flip();

        while (!Display.isCloseRequested()) {

            render();

            Display.update();
            Display.sync(FPS);

        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Vertex Arrays Demo");
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

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);

        glVertexPointer(vertexSize, 0, vertexData);
        glColorPointer(colorSize, 0, colorData);

        glDrawArrays(GL_TRIANGLES, 0, amountOfVertices);

        glDisableClientState(GL_COLOR_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);

    }

    public static void main(String[] args) {
        new VAO().start();
    }

}