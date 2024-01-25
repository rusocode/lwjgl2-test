package com.craivet.ejemplos;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

/**
 * Muestra el uso de transiciones "suaves" (no lineales) usando la funcion Math.sin().
 */

public class SmoothTransitions {

    private enum State {

        /**
         * El estado donde las aplicaciones espera la entrada del usuario y no dibuja nada. Si se presiona ENTER, el estado esta
         * configurado para desvanecerse.
         */
        INTRO,

        /**
         * El estado donde sucede el desvanecimiento real. Aparece lentamente un rectangulo azul. Cuando la opacidad es al 100%,
         * el estado se establece en Main.
         */
        FADING,

        /**
         * El estado donde se muestra el rectangulo sin transparencia. Si el usuario presiona ENTER, el estado se establece en
         * INTRO.
         */
        MAIN
    }

    private static State state = State.INTRO;

    // Cantidad de desvanecimiento (en grados, que van desde 0 a 90)
    float fade;

    private void start() {

        setUpDisplay();
        setUpOpenGL();

        while (!Display.isCloseRequested()) {
            render();
            input();
            Display.update();
            Display.sync(60);
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

            case FADING:

                if (fade < 90) {
                    fade += 1.5f;
                    /* Ajusta el color en azul y calcula la opacidad con la funcion Math.sin().
                     * Debido a que la funcion Math.sin() toma radianes en lugar de grados, convierte el fade a grados utilizando
                     * Math.toRadians(fade). */
                    glColor4f(0.5f, 0.5f, 1f, (float) Math.sin(Math.toRadians(fade)));
                    // Dibuja el rectangulo
                    glRectf(-0.5f, -0.5f, 0.5f, 0.5f);
                }

                // Si el fade es 90 o mayor, lo restablece y dibuja un rectangulo azul completamente opaco, y configura el estado en MAIN
                else {

                    fade = 0;

                    // TODO Hace falta cambiar el color desde aca? Porque se hace desde el case MAIN
                    glColor3f(0.5f, 0.5f, 1f);
                    glRectf(-0.5f, -0.5f, 0.5f, 0.5f);

                    state = State.MAIN;

                    break; // TODO Hace falta?
                }

                break;

            case INTRO:

                if (fade > 0) {
                    fade -= 1.5F;
                    glColor4f(0.5f, 0.5f, 1f, (float) Math.sin(Math.toRadians(fade)));
                    glRectf(-0.5f, -0.5f, 0.5f, 0.5f);
                }

                break;

            case MAIN:
                // Dibuja un rectangulo azul totalmente opaco
                glColor3f(0.5f, 0.5f, 1f);
                glRectf(-0.5f, -0.5f, 0.5f, 0.5f);
                break;
        }
    }

    private void input() {

        while (Keyboard.next()) {

            // Si se presiono ENTER, entonces...
            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {

                // intro -> fading
                // fading -> main
                // main -> intro
                switch (state) {
                    case INTRO:
                        state = State.FADING;
                        System.out.println("Cambio de estado a: " + state);
                        break;
                    case FADING:
                        fade = 0;
                        state = State.MAIN;
                        System.out.println("Cambio de estado a: " + state);
                        break;
                    case MAIN:
                        fade = 90;
                        state = State.INTRO;
                        System.out.println("Cambio de estado a: " + state);
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        new SmoothTransitions().start();
    }

}
