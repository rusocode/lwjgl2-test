package com.craivet.game.entities;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

public class EntityDemo {

    private static long lastFrame;

    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private static int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }

    public void start() {

        setUpDisplay();

        MoveableEntity box = new Box(100, 100, 50, 50);
        Entity point = new Point(50, 50);

        lastFrame = getTime();

        setUpOpenGL();

        while (!Display.isCloseRequested()) {

            render(box, point);

            Display.update();
            Display.sync(120);

        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Entity Demo");
            Display.setDisplayMode(new DisplayMode(640, 480));
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

    private void render(MoveableEntity box, Entity point) {
        glClear(GL_COLOR_BUFFER_BIT);

        // Establece la posicion del point en la ubicacion del mouse
        point.setLocation(Mouse.getX(), Mouse.getY() - 1); // -1 ?

        int delta = getDelta();

        box.update(delta);
        point.update(delta);

        // Si hay colision entre el punto y la caja
        if (box.intersects(point)) box.setDX(0.2); // Mueva la caja horizontalmente a una velocidad reducida a 9 veces

        calculateLimits(box);

        point.draw();
        box.draw(); // Rellena la caja por asi decirlo
    }

    /**
     * Calcula los limites del ancho de la ventana.
     *
     * @param box caja.
     */
    private void calculateLimits(MoveableEntity box) {
        // Si llego al final rebota hacia atras
        if (box.getX() + box.getWidth() >= WIDTH) box.setDX(-0.2);
        // Si llego al principio rebota hacia adelante
        if (box.getX() <= 0) box.setDX(0.2);
    }

    private void setInicio(MoveableEntity box) {
        // Establece los valores en el eje de coordenadas y deja quieta la entidad (cuadrado)
        box.setLocation(100, 100);
        box.setDX(0);
    }

    private static class Box extends AbstractMovableEntity {

        public Box(double x, double y, double width, double height) {
            super(x, y, width, height);
        }

        @Override
        public void draw() {
            glRectd(x, y, x + width, y + height);
        }
    }

    private static class Point extends AbstractEntity {

        public Point(double x, double y) {
            super(x, y, 10, 10);
        }

        @Override
        public void draw() {
            glBegin(GL_POINTS);
            glVertex2d(x, y);
            glEnd();
        }

        @Override
        public void update(double delta) {
        }

    }

    public static void main(String[] args) {
        new EntityDemo().start();
    }

}