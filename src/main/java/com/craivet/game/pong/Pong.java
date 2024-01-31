package com.craivet.game.pong;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.craivet.game.entities.AbstractMovableEntity;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

/**
 * <i>Sin Delta</i>
 * <ul>
 * <li>El tiempo que tardo la bola en colisionar a 60 fps fue de 5 segundos
 * <li>El tiempo que tardo la bola en colisionar a 30 fps fue de 10 segundos
 * </ul>
 * <i>Con Delta</i>
 * <ul>
 * <li>El tiempo que tardo la bola en colisionar a 60 fps fue de 5 segundos
 * <li>El tiempo que tardo la bola en colisionar a 30 fps fue de 5 segundos
 * </ul>
 * En conclusion, con valores de FPS menores el Delta se incrementa, los desplazamientos en estos frames son mayores pero tenemos
 * menos frames, por lo tanto el resultado de la suma final es el mismo. Ahora podemos decir que el juego es <b>framerate independente</b>.
 */

public class Pong {

    private static Bat bat;
    private static Ball ball;

    private static long lastFrame;

    public void start() {

        setUpDisplay();
        setUpOpenGL();
        setUpEntities();
        setUpTimer();

        while (!Display.isCloseRequested()) {

            // Actualiza (usando un valor delta) y dibuja la nueva posicion de las entidades
            update(Delta.getDelta());
            render();
            input();

            // Cantidad de veces que se actualiza la pantalla del juego por segundo
            Display.update();
            Display.sync(FPS); // Mientras mas bajo sean los FPS mayor sera el delta

        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle("Pong");
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
        glOrtho(0, 640, 0, 480, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    private void setUpEntities() {
        // Ubica el bate en el centro del eje y
        bat = new Bat(10, (double) HEIGHT / 2 - (double) 80 / 2, 10, 80);
        // Ubica la bola en el centro de la ventana
        ball = new Ball((double) WIDTH / 2 - (double) 10 / 2, (double) HEIGHT / 2 - (double) 10 / 2, 10, 10);
        // Para que la bola comience a desplazarse hacia la izquierda
        ball.setDX(-0.1);
    }

    private void setUpTimer() {
        lastFrame = Delta.getTime();
    }

    private void update(double delta) {
        ball.update(delta);
        bat.update(delta);
        // Si la bola intersecta con el bate
        if (ball.intersects(bat)) ball.setDX(0.3);
        // Si la bola llega al limite derecho de la ventana
        if (ball.getX() + ball.getWidth() >= WIDTH) ball.setDX(-0.3);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        ball.draw();
        bat.draw();
    }

    private void input() {
        // Si se presiono te tecla de arriba y el bate no llego al alto de la ventana
        if (Keyboard.isKeyDown(Keyboard.KEY_UP) && bat.getY() + bat.getHeight() <= HEIGHT) bat.setDY(0.2);
        else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && bat.getY() >= 0) bat.setDY(-0.2);
        else bat.setDY(0); // Para que el bate quede en su lugar cuando no se presiona ninguna tecla
    }

    private static class Delta {

        private static long getTime() {
            return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        }

        private static double getDelta() {
            long currentTime = getTime();
            double delta = currentTime - lastFrame;
            lastFrame = currentTime;
            return delta;
        }

    }

    private static class Bat extends AbstractMovableEntity {

        public Bat(double x, double y, double width, double height) {
            super(x, y, width, height);
        }

        @Override
        public void draw() {
            glRectd(x, y, x + width, y + height);
        }
    }

    private static class Ball extends AbstractMovableEntity {

        public Ball(double x, double y, double width, double height) {
            super(x, y, width, height);
        }

        @Override
        public void draw() {
            glRectd(x, y, x + width, y + height);
        }
    }

    public static void main(String[] args) {
        new Pong().start();
    }

}