package com.craivet.rendered;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import static com.craivet.Global.*;

/**
 * Muestra una simulacion de particulas similar al espacio con tecnicas de renderizado avanzadas.
 */

public class AdvancedRendering {

    private enum RenderMode {IMMEDIATE, DISPLAY_LISTS, VERTEX_ARRAY_OBJECT, VERTEX_BUFFER_OBJECT}

    RenderMode mode = RenderMode.IMMEDIATE;

    // Velocidad a la que viaja la camara
    private static float speed;

    private Point[] points;

    private int displayList;
    private FloatBuffer vertexArray;
    private int vertexBufferObject;

    private float delta;
    private static long lastFrame;

    private static float getDelta() {
        long time = getTime();
        float delta = (float) (time - lastFrame);
        lastFrame = time;
        return delta;
    }

    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private void start() {

        setUpDisplay();
        setUpOpenGL();
        setUpEntities();

        /* Display lists */
        displayList = glGenLists(1); // Genera un ID para la lista de visualizacion
        /* Crea la lista de visualizacion usando el ID displayList. Todas las llamadas posteriores para renderizado se almacenaran
         * en la lista de visualizacion. */
        glNewList(displayList, GL_COMPILE);
        glBegin(GL_POINTS);
        for (Point p : points)
            glVertex3f(p.x, p.y, p.z);
        glEnd();
        glEndList(); // Deja de almacenar llamadas en la lista de visualizacion y compila

        /* Vertex Arrays y Vertex Buffer Objects */
        /* Crea un FloatBuffer (arreglo complejo de flotantes) con la longitud de la cantidad de puntos * 3 (porque tenemos 3
         * vertices por punto). */
        vertexArray = BufferUtils.createFloatBuffer(points.length * 3);
        // Itera los puntos y los guarda en el FloatBuffer
        for (Point p : points)
            vertexArray.put(new float[]{p.x, p.y, p.z});
        // Hace que el buffer sea legible para OpenGL (lo voltea)
        vertexArray.flip();
        // Crea el identificador para el VBO
        vertexBufferObject = glGenBuffers();
        // Vincula el VBO para su uso (en este caso, almacena informacion)
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
        // Almacena todo el contenido del FloatBuffer en el VBO
        glBufferData(GL_ARRAY_BUFFER, vertexArray, GL_STATIC_DRAW);
        // Desenlaza el VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        lastFrame = getTime();

        System.out.println("Modo de renderizado configurado a " + mode.name());

        while (!Display.isCloseRequested()) {

            render();
            input();

            Display.update();
            Display.sync(30);
        }

        // Elimina la lista de visualizacion
        glDeleteLists(displayList, 1);
        // Elimina el VBO
        glDeleteBuffers(vertexBufferObject);

        Display.destroy();
        System.exit(0);
    }

    private void setUpDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle("Renderizados");
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
        // Configura el far en 1_000_000_000_000 para ver todos los puntos (propositos de evaluacion comparativa)
        gluPerspective((float) 30, (float) Display.getWidth() / Display.getHeight(), 0.001f, 10000);
        glMatrixMode(GL_MODELVIEW);
        // Vuelve a verificar la validez del dibujo 3D
        glEnable(GL_DEPTH_TEST);
    }

    private void setUpEntities() {

        points = new Point[3_000_000];
        Random random = new Random();

        /* Modifica la variable far para adaptarse a points.length. Los puntos, no importa cuanto, ahora aparecen distribuidos
         * uniformemente a lo largo de la pantalla. */
        for (int i = 0; i < points.length; i++)
            points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(points.length / 50) - (float) points.length / 50);

    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        /* Divide el delta entre 16 para obtener un delta de 16 a 60 fps. Si es 60 fps, el delta sera 1.0f y todas las llamadas de
         * entrada seran las mismas que antes. */
        delta = getDelta() / 16f;

        // Empuja la pantalla hacia adentro a la velocidad especificada
        glTranslatef(0, 0, speed * delta);

        switch (mode) {

            case DISPLAY_LISTS:
                // Dibuja la lista de visualizacion
                glCallList(displayList);
                break;
            case VERTEX_BUFFER_OBJECT:
                // Habilita las matrices de vertices (VBO)
                glEnableClientState(GL_VERTEX_ARRAY);
                // Le dice a OpenGL que use nuestro VBO
                glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
                // Le dice a OpenGL que busque los datos en el VBO vinculado con 3 componentes (xyz) y con el tipo float
                glVertexPointer(3, GL_FLOAT, 0, 0L);
                /* Le dice a OpenGL que dibuje los datos proporcionados por el metodo de puntero como puntos. Longitud cantidad de
                 * puntos. */
                glDrawArrays(GL_POINTS, 0, points.length);
                // Desenlaza el VBO
                glBindBuffer(GL_ARRAY_BUFFER, 0);
                // Deshabilita las matrices de vertices
                glDisableClientState(GL_VERTEX_ARRAY);
                break;
            case VERTEX_ARRAY_OBJECT:
                glEnableClientState(GL_VERTEX_ARRAY);
                // Le dice a OpenGL que busque los datos en el buffer vertexArray con 3 componentes (xyz)
                glVertexPointer(3, 0, vertexArray);
                glDrawArrays(GL_POINTS, 0, points.length);
                glDisableClientState(GL_VERTEX_ARRAY);
                break;
            case IMMEDIATE:
                glBegin(GL_POINTS);
                for (Point p : points)
                    glVertex3f(p.x, p.y, p.z);
                glEnd();
                break;
        }

    }

    private void input() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) speed += 0.01f * delta;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) speed -= 0.01f * delta;
        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                speed = 0;
                glLoadIdentity();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) speed = 0;
            if (Keyboard.isKeyDown(Keyboard.KEY_1)) mode = RenderMode.VERTEX_BUFFER_OBJECT;
            if (Keyboard.isKeyDown(Keyboard.KEY_2)) mode = RenderMode.VERTEX_ARRAY_OBJECT;
            if (Keyboard.isKeyDown(Keyboard.KEY_3)) mode = RenderMode.DISPLAY_LISTS;
            if (Keyboard.isKeyDown(Keyboard.KEY_4)) mode = RenderMode.IMMEDIATE;
            System.out.println("Modo de renderizado configurado a " + mode.name());
        }
    }

    private static class Point {
        final float x;
        final float y;
        final float z;
        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static void main(String[] args) {
        new AdvancedRendering().start();
    }

}