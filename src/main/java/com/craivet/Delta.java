package com.craivet;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static com.craivet.Global.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * La variable Delta representa el tiempo transcurrido desde la ultima actualizacion del cuadro. Cuanto mayor sea el Delta, menor
 * sera la velocidad de fotogramas. Cuanto menor sea el Delta, mayor sera la velocidad de fotogramas. Si la velocidad de
 * fotogramas esta limitada a 60 FPS, NUNCA deberia existir un valor Delta inferior a 16, por que lo unico que le puede pasar a
 * nuestra velocidad de fotogramas es que disminuira.
 * <p>
 * Algo de lo que me di cuenta, es que especificando la cantidad de actualizaciones por segundo desde {@code Display.sync(FPS)}
 * no haria falta determinar el delta, ya que de eso se encarga el metodo sync. La ventaja de esto es que no haria falta multiplicar
 * el delta por la velocidad.
 * <br><br>
 * Recursos: <a href="https://www.youtube.com/watch?v=C1_2XlPE6s8">What the heck is "DELTA TIME"? (Frame Independece)</a>
 * <a href="https://www.youtube.com/watch?v=pctGOMDW-HQ">TIMESTEPS and DELTA TIME</a>
 * <a href="https://www.parallelcube.com/es/2017/10/25/por-que-necesitamos-utilizar-delta-time/#:~:text=Delta%2520time%2520(%25CE%2594t)%25">¿Por que necesitamos utilizar Delta Time?</a>
 */

public class Delta {

    private static long lastFrame;

    private int x = 10, y = 10;

    /**
     * Obtiene la hora del sistema.
     * <p>
     * La resolucion del temporizador se define como (de los documentos LWJGL) "el numero de tics que... el temporizador hace en
     * un segundo".
     * <p>
     * Divide el valor actual del temporizador en tics (getTime) por la resolucion del temporizador para obtener el tiempo en
     * segundos. Para obtener el tiempo en milisegundos, lo multiplica por 1000.
     * <p>
     * Sys.getTime se creo para usos como LWJGL con aspectos como la precision y el rendimiento en mente. System.currentTimeMillis,
     * sin embargo, no lo fue.
     */
    private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    private static double getDelta() {
        long currentTime = getTime();
        // Diferencia entre el tiempo actual y el ultimo fotograma
        double delta = (double) (currentTime - lastFrame);
        lastFrame = getTime(); // Estable el tiempo actual despues de calcular el delta
        return delta;
    }

    private void start() {

        setUpDisplay();
        setUpOpenGL();

        // En la primera vuelta del loop se calcula el tiempo del ultimo fotograma ya que nunca se inicializa
        lastFrame = getTime();

        while (!Display.isCloseRequested()) {

            render();

            int delta = (int) getDelta();

            System.out.println("Delta value: " + delta);

            /* ¿Se puede actualizar el movimiento del frame sin el Delta?
             * Si se puede, pero cuando haya una caida o subida de FPS, el movimiento no sera el mismo, es decir si disminuyen los
             * FPS, la velocidad disminuira, y si aumentan, la velocidad sera mas rapida.
             * Al contrario de usar el tiempo delta, el frame llegara al destino en el mismo tiempo, idependientemente de si hay una
             * caida (frames con saltos) o subida de FPS. */
            float speed = 0.1f; // Velocidad relentizada a 10 veces (0.1)
            x += (int) (delta * speed); // Velocidad horizontal
            y += (int) (delta * speed); // Velocidad vertical

            // Dibuja una caja en las coordenadas xy del primer vertice y en xy del segundo vertice de la esquina
            glRecti(x, y, x + 30, y + 30); // Esto es exactamente igual al modo inmediato

            Display.update();

            /* Aunque se cambie la cantidad de fotogramas, la velocidad del rectangulo siempre sera la misma, siempre y cuando se
             * use el Delta.
             *
             * NOTA: Al pasarle menos FPS usando el Delta, la velocidad del frame no cumple el mismo tiempo que con FPS mas altos,
             * ¿Por que?. */
            Display.sync(FPS);

        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Delta");
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
        glOrtho(0, 640, 0, 480, 1, -1); // esq inf izquierda
        glMatrixMode(GL_MODELVIEW);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void main(String[] args) {
        new Delta().start();
    }

}