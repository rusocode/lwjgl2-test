package com.craivet;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

/**
 * Muestra el uso de transiciones "suaves" (no lineales) usando un rectangulo como ejemplo.
 */

public class SmoothTransitions {

    private enum State {
        /**
         * Estado que va de totalmente opaco a totalmente transparente.
         */
        FADE_OUT,
        /**
         * Estado que va de totalmente transparente a totalmente opaco.
         */
        FADE_IN,
        /**
         * Estado totalmente opaco.
         */
        OPAQUE
    }

    private static State state = State.FADE_OUT;

    // Cantidad de desvanecimiento en grados de 0 a 90
    float fade;

    private void start() {

        setUpDisplay();
        setUpOpenGL();

        while (!Display.isCloseRequested()) {
            render();
            input();
            Display.update();
            Display.sync(FPS);
        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Smooth Transitions");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setVSyncEnabled(true);
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
        // (0, 0) es el centro, (1, 1) es la parte superior derecha, (-1, -1) es la parte inferior izquierda
        glOrtho(1, -1, 1, -1, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        // Habilita la translucidez
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);

        switch (state) {

            // Aparece
            case FADE_IN:
                if (fade < 90) {
                    fade += 1.5F;
                    /* Ajusta el color en azul y calcula la opacidad con la funcion Math.sin(). Debido a que la funcion Math.sin()
                     * toma radianes en lugar de grados, convierte el fade a grados utilizando Math.toRadians(fade). */
                    glColor4f(0.5F, 0.5F, 1F, (float) Math.sin(Math.toRadians(fade)));
                    // Dibuja el rectangulo
                    glRectf(-0.5F, -0.5F, 0.5F, 0.5F);
                }
                // Si el fade es igual o mayor a 90
                else {
                    // Restablece el fade
                    fade = 0;
                    // Dibuja un rectangulo azul totalmente opaco para evitar un parpadeo de este al cambiar de estado
                    glColor3f(0.5F, 0.5F, 1F);
                    glRectf(-0.5F, -0.5F, 0.5F, 0.5F);
                    state = State.OPAQUE;
                }
                break;

            // Desaparece
            case FADE_OUT:
                if (fade > 0) {
                    fade -= 1.5F;
                    glColor4f(0.5F, 0.5F, 1F, (float) Math.sin(Math.toRadians(fade)));
                    glRectf(-0.5F, -0.5F, 0.5F, 0.5F);
                }
                break;

            case OPAQUE:
                fade = 90;
                // Dibuja un rectangulo azul totalmente opaco
                glColor3f(0.5F, 0.5F, 1F);
                glRectf(-0.5F, -0.5F, 0.5F, 0.5F);
                break;

        }
    }

    private void input() {
        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                switch (state) {
                    case FADE_OUT:
                        state = State.FADE_IN;
                        break;
                    case FADE_IN:
                        state = State.OPAQUE;
                        break;
                    case OPAQUE:
                        state = State.FADE_OUT;
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new SmoothTransitions().start();
    }

}
