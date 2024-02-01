package com.craivet.game.minecraft2d;

import java.io.File;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import static com.craivet.game.minecraft2d.World.*;
import static com.craivet.Global.*;

public class Screen {

    private BlockGrid grid;
    private BlockType type = BlockType.BRICK;
    private int x, y;
    private boolean mouseEnabled = true;

    public void start() {

        setUpDisplay();
        setUpOpenGL();

        grid = new BlockGrid();

        while (!Display.isCloseRequested()) {

            update();
            render();
            input();

            Display.update();
            Display.sync(FPS);

            if (Display.wasResized()) resize();

        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Minecraft 2D");
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
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void update() {

    }

    private void render() {
        /* En realidad no se necesita limpiar la pantalla, ya que creamos bloques todo el tiempo y no trabajamos con la misma
         * textura. */
        glClear(GL_COLOR_BUFFER_BIT);
        grid.draw();
    }

    private void input() {
        mouse();
        keyboard();
    }

    private void mouse() {
        // Si el mouse esta habilitado o si se hizo click izquierdo en la pantalla
        if (mouseEnabled || Mouse.isButtonDown(0)) {
            /* Se vuelve a hablitar cuando se hace click izquierdo en la pantalla, ya que al usar el teclado, el mouse queda
             * deshabilitado y esta es la unica forma de habilitarlo (entrando a este metodo por asi decirlo). */
            mouseEnabled = true;
            // Divide la posicion del mouse por el tamanio del bloque y lo redondea para obtener el numero exacto de la grilla
            x = Math.round((float) Mouse.getX() / World.BLOCK_SIZE);
            y = Math.round((float) (HEIGHT - Mouse.getY() - 1) / World.BLOCK_SIZE); // -1 ?
            // Si se hizo click izquierdo
            if (Mouse.isButtonDown(0)) grid.setAt(type, x, y); // Crea un nuevo bloque
        }
        drawSelectionBlock();
    }

    private void keyboard() {

        while (Keyboard.next()) {

            // Calcula los limites de la ventana evitando sumar o restar la posicion del bloque fuera de esta (ArrayIndexOutOfBoundsException)

            /* Para el movimiento KEY_RIGHT, x solo tiene que llegar hasta 19 y no 20 (por eso el "x + 1" sin asignar), ya que 19 *
             * 32 = 608, dejando el espacio sobrante para la textura de 32 pixeles (608 + 32 = 640 limite) sin pasar el limite del
             * ancho de la pantalla. */
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && x + 1 < World.cols) {
                x++;
                /* Deshabilita el mouse cuando se usa el teclado para que no se superpongan los eventos, ya que se van a estar
                 * tomando x e y de ambas entradas y nunca se va a mover el bloque de seleccion. */
                mouseEnabled = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && x > 0) {
                x--;
                mouseEnabled = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_UP) && y > 0) {
                y--;
                mouseEnabled = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && y + 1 < World.rows) {
                y++;
                mouseEnabled = false;
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_S)) grid.save(new File("save.xml"));
            if (Keyboard.isKeyDown(Keyboard.KEY_L)) grid.load(new File("save.xml"));
            if (Keyboard.isKeyDown(Keyboard.KEY_1)) type = BlockType.AIR;
            if (Keyboard.isKeyDown(Keyboard.KEY_2)) type = BlockType.GRASS;
            if (Keyboard.isKeyDown(Keyboard.KEY_3)) type = BlockType.DIRT;
            if (Keyboard.isKeyDown(Keyboard.KEY_4)) type = BlockType.STONE;
            if (Keyboard.isKeyDown(Keyboard.KEY_5)) type = BlockType.BRICK;
            if (Keyboard.isKeyDown(Keyboard.KEY_C)) grid.clear();
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                Display.destroy();
                System.exit(0);
            }

        }

    }

    /**
     * Dibuja el bloque de seleccion.
     */
    private void drawSelectionBlock() {
        glColor4f(1f, 1f, 1f, 0.5f); // Color blanco con 50% de transparencia
        new Block(type, x * World.BLOCK_SIZE, y * World.BLOCK_SIZE).draw();
        glColor4f(1f, 1f, 1f, 1f); // Color blanco con 100% de transparencia
    }

    private void resize() {
        // Especifica los parametros de transformacion de la ventana grafica para todas las ventanas graficas
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        World.setCols(Display.getWidth() / BLOCK_SIZE);
        World.setRows(Display.getHeight() / BLOCK_SIZE);
        System.out.println(Display.getWidth() + "," + Display.getHeight());
    }

    public static int getWidth() {
        return Display.getWidth();
    }

    public static int getHeight() {
        return Display.getHeight();
    }

    public static void main(String[] args) {
        new Screen().start();
    }

}