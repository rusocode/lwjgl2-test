package com.craivet.ejemplos;

import java.io.*;
import java.util.Objects;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class TextureDemo {

    protected boolean running = false;
    private Texture img;

    public static void main(String[] args) {
        try {
            new TextureDemo().start();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
    }

    public void start() throws LWJGLException {

        Display.setTitle("Texture Demo");
        Display.setDisplayMode(new DisplayMode(640, 480));
        Display.create();

        img = loadTexture("brick");

        create();

        running = true;

        while (running && !Display.isCloseRequested()) {

            render();

            Display.update();
            Display.sync(60);

        }

        Display.destroy();

    }

    private void create() {

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 480, 0, 1, -1); // esq superior izquierda, si utilizo la esq inf izq la textura se voltea, xq?
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        // Habilita el texturizado 2D
        glEnable(GL_TEXTURE_2D);

    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);

        img.bind(); // Enlaza la textura antes de renderizarla

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2i(10, 10);
        glTexCoord2f(1, 0);
        glVertex2i(10 + 50, 10);
        glTexCoord2f(1, 1);
        glVertex2i(10 + 50, 10 + 50);
        glTexCoord2f(0, 1);
        glVertex2i(10, 10 + 50);
        glEnd();
    }

    private Texture loadTexture(String key) {

        try {
            return TextureLoader.getTexture("PNG", Objects.requireNonNull(TextureDemo.class.getClassLoader().getResourceAsStream("textures/" + key + ".png")));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de I/O", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

}
