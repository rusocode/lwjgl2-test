package com.craivet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static com.craivet.Global.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * getX() devuelve la posicion absoluta del eje x
 * getDX() devuelve el movimiento en el eje x desde la ultima vez que se llamo a getDX()
 * <p>
 * Para usar el metodo getEventKey(), es necesario controlar las teclas con un getEventKeyState() ya que asegura de que solo
 * registre las teclas que se presionan, no las que se liberan. El metodo isKeyDown() se puede usar para verificar las teclas
 * presionadas, en lugar de presionar una tecla una vez con getKeyEvent().
 */

public class Input {

    // Lista de formas (shapes) de tipo Box
    private static final List<Box> shapes = new ArrayList<>(5);

    private static final int BOX_HEIGHT = 50;
    private static final int BOX_WIDHT = 50;

    public void start() {

        setUpDisplay();

        // Crea una caja en el eje de coordenadas especificado y la agrega a la coleccion de tipo List
        shapes.add(new Box(10, 10));

        setUpOpenGL();

        while (!Display.isCloseRequested()) {
            render();
            Display.update();
            Display.sync(60);
        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Input");
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
        glOrtho(0, WIDTH, 0, HEIGHT, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);

        System.out.println("x = " + Mouse.getX() + ", y = " + Mouse.getY());

        // Mientras se haya leido un evento del teclado
        while (Keyboard.next()) { // Keyboard.next() obtiene el proximo evento del teclado, capturando una sola pulsacion de tecla
            if (Keyboard.isKeyDown(Keyboard.KEY_C))
                // Resta el ancho y alto de la caja a los limites de la ventana para no pasarse de estos
                shapes.add(new Box((int) (Math.random() * (WIDTH - BOX_WIDHT)), (int) (Math.random() * (HEIGHT - BOX_HEIGHT))));

            // Elimina todas las cajas
            if (Keyboard.isKeyDown(Keyboard.KEY_L)) shapes.clear();

        }

        // Si se pulso la tecla escape, cierra la aplicacion
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

        // Itera las cajas
        for (final Box box : shapes) {
            // Si se selecciono la caja y si el cursor esta dentro de los limites de esta
            if (Mouse.isButtonDown(0) && box.isInBounds(Mouse.getX(), Mouse.getY())) box.selected = true;
            // Si se solto la caja
            if (Mouse.isButtonDown(1)) box.selected = false;
            // Si la caja esta seleccionada
            if (box.selected) box.update(Mouse.getDX(), Mouse.getDY());
            // Dibuja la caja
            box.draw();
        }

    }

    private static class Box {

        public int x, y;
        public boolean selected = false;
        private final float colorRed;
        private final float colorBlue;
        private final float colorGreen;

        Box(int x, int y) {
            this.x = x;
            this.y = y;

            Random randomGenerator = new Random();
            colorRed = randomGenerator.nextFloat();
            colorBlue = randomGenerator.nextFloat();
            colorGreen = randomGenerator.nextFloat();
        }

        // Si se selecciono la caja dentro sus limites
        boolean isInBounds(int mouseX, int mouseY) {
            return mouseX >= x && mouseX <= x + BOX_WIDHT && mouseY >= y && mouseY <= y + BOX_HEIGHT;
        }

        // dx/dy significan delta-x y delta-y
        void update(int dx, int dy) {
            x += dx;
            y += dy;
        }

        void draw() {

            glColor3f(colorRed, colorGreen, colorBlue);

            // La mayoria de los dosificadores de sprites usan dos triangulos adyacentes para representar un sprite rectangular
            glBegin(GL_QUADS);
            // Conjuntos de vertices (puntos) que juntos forman una primitiva (triangulos), dando un cuadrado como resultado
            glVertex2i(x, y); // Posicion del primer vertice x=0;y=0
            glVertex2i(x + BOX_WIDHT, y); // Posicion del segundo vertice x=x+50;y=0
            glVertex2i(x + BOX_WIDHT, y + BOX_HEIGHT); // Posicion del tercer vertice x=x+50;y=y+50
            glVertex2i(x, y + BOX_HEIGHT); // Posicion del cuarto vertice x=0;y=y+50
            glEnd();

        }

    }

    public static void main(String[] args) {
        new Input().start();
    }

}