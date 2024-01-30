package com.craivet.game.pong;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.craivet.entities.AbstractMovableEntity;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

public class Pong {

    private static Bat bat;
    private static final int BAT_WIDTH = 10;
    private static final int BAT_HEIGHT = 80;

    private static Ball ball;
    private static final int BALL_WIDTH = 10;
    private static final int BALL_HEIGHT = 10;

    private static long lastFrame;

    public void start() {

        setUpDisplay();
        setUpOpenGL();
        setUpEntities();
        setUpTimer();

        while (!Display.isCloseRequested()) {

            // Esto sucede tan rapido que da un efecto de movimiento
            update(Delta.getDelta()); // Actualiza la nueva posicion
            render(); // Borra la bola de la posicion anterior y la dibuja en la nueva posicion

            input();

            Display.update();
            Display.sync(FPS); // Mientras mas bajo sean los FPS mayor sera el delta

            /* Sin Delta */
            // El tiempo que tardo la bola en colisionar a 60 fps fue de 5 segundos
            // El tiempo que tardo la bola en colisionar a 30 fps fue de 10 segundos
            /* Con Delta */
            // El tiempo que tardo la bola en colisionar a 60 fps fue de 5 segundos
            // El tiempo que tardo la bola en colisionar a 30 fps fue de 5 segundos
            /* En conclusion, con valores de FPS menores el Delta se incrementa, los desplazamientos en estos frames son mayores
             * pero tenemos menos frames, por lo tanto el resultado de la suma final es el mismo. Ahora podemos decir que el juego
             * es framerate independente. */

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
        bat = new Bat(10, (double) HEIGHT / 2 - (double) BAT_HEIGHT / 2, BAT_WIDTH, BAT_HEIGHT); // Ubica la barra en el centro del eje y
        // Ubica la bola en el centro de la ventana y la mueve hacia atras
        ball = new Ball((double) WIDTH / 2 - (double) BALL_WIDTH / 2, (double) HEIGHT / 2 - (double) BALL_HEIGHT / 2, BALL_WIDTH, BALL_HEIGHT);
        ball.setDX(-0.1);
    }

    private void setUpTimer() {
        lastFrame = Delta.getTime();
    }

    private void update(int delta) {
        ball.update(delta);
        bat.update(delta);
        if (ball.getX() <= bat.getX() + bat.getWidth() && ball.getX() >= bat.getX() && ball.getY() >= bat.getY()
                && ball.getY() <= bat.getY() + bat.getHeight())
            ball.setDX(0.3);
        // if (ball.intersects(bat)) ball.setDX(0.3);

        if (ball.getX() + ball.getWidth() >= WIDTH) ball.setDX(-0.3);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);
        ball.draw();
        bat.draw();
    }

    private void input() {
        // Si se presiono te tecla de arriba y la barra no llego al alto de la ventana, entonces...
        if (Keyboard.isKeyDown(Keyboard.KEY_UP) && bat.getY() + bat.getHeight() <= HEIGHT) {
            bat.setDY(0.2);
            if (ball.intersects(bat)) {
                ball.setDX(0.3);
                ball.setDY(0.3);
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && bat.getY() >= 0) bat.setDY(-0.2);
        else bat.setDY(0); // Para que la barra quede en su lugar
    }

    private static class Delta {

        private static long getTime() {
            return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        }

        private static int getDelta() {
            long currentTime = getTime();
            int delta = (int) (currentTime - lastFrame);
            lastFrame = getTime();
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