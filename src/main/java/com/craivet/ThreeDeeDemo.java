package com.craivet;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import static com.craivet.Global.*;

import java.util.Random;

/**
 * <h2>Introduccion a la programacion de graficos por computadora</h2>
 * Introduccion suave a la programacion de graficos por computadora, con el objetivo de explicar conceptos clave y sus
 * interrelaciones para crear Imagenes Generadas por Computadora (CGI). Aca se hace hincapie en la tridimensionalidad del mundo real
 * y en como la vision, un proceso principalmente bidimensional, se crea en la mente. La vision estereoscopica, habilitada al tener
 * dos ojos, permite a los humanos percibir la profundidad. Los graficos por computadora buscan replicar este proceso, creando
 * mundos artificiales que imitan la realidad. Tambien se aborda el alcance mas amplio de los graficos por computadora, incluyendo
 * la simulacion de movimientos de fluidos y cuerpos, enfatizando que el realismo es un objetivo clave.
 * <br><br>
 * <h3>Describir objetos que pueblan el mundo virtual</h3>
 * La diferencia entre un pintor que esta pintando una escena real y nosotros, que intentamos crear una imagen con una computadora,
 * radica en que primero debemos describir de alguna manera la forma (y la apariencia) de los objetos que componen la escena que
 * queremos representar en la computadora.
 * <p>
 * Para esto se introduce el concepto de un sistema de coordenadas cartesianas bidimensional y tridimensional para definir la
 * posicion de puntos en un plano o en el espacio tridimensional. La posicion de un punto se determina generalmente desde un origen,
 * que actua como referencia.
 * <p>
 * La idea fundamental es la del espacio en el que los puntos pueden definirse. La posicion de un punto se determina generalmente
 * mediante un origen, como el extremo marcado con el numero cero en una regla. Al usar dos reglas perpendiculares entre si, se
 * pueden definir las posiciones de puntos en dos dimensiones, y al agregar una tercera regla perpendicular, se pueden determinar
 * las posiciones en tres dimensiones.
 * <p>
 * Suponiendo que creamos una caja 3D, siendo este un objeto que se llama <b>escena</b> (una escena tambien incluye el concepto de camara
 * y luces), todavia necesitamos dos cosas esenciales para que el proceso sea completo e interesante. Primero, para representar la
 * caja en la memoria de la computadora, idealmente tambien necesitamos un sistema que defina como se conectan estos ocho puntos
 * para formar las caras de la caja. En CG (computer graphics), esto se llama <b>topologia</b> del objeto (un objeto tambien se llama
 * <b>modelo</b>). La topologia se refiere a como los puntos que llamamos <b>vertices</b> se conectan para formar caras (o superficies planas).
 * Estas caras tambien se llaman <b>poligonos</b>. La caja estaria formada por seis caras o seis poligonos, y los poligonos forman lo que
 * llamamos una malla poligonal o simplemente una <b>malla</b>. Lo segundo que todavia necesitamos es un sistema para crear una imagen de
 * ese cuadro. Esto requiere proyectar las esquinas de la caja sobre un lienzo imaginario, un proceso que llamamos <b>proyeccion en
 * perspectiva</b>.
 * <br><br>
 * <h3>Creando una imagen de este mundo virtual</h3>
 * El proceso de proyeccion de un punto 3D en la superficie de un lienzo implica la utilizacion de una matriz especifica conocida
 * como la matriz de perspectiva. Aunque el uso de esta matriz es opcional, facilita la proyeccion. Visualmente, se puede imaginar
 * un lienzo como una superficie plana situada a cierta distancia del ojo. Para proyectar un objeto tridimensional en este lienzo,
 * se propone trazar lineas desde el ojo hasta cada una de las cuatro esquinas del lienzo, extendiendolas hacia el mundo y formando
 * asi una piramide denominada <b>viewing frustum</b>. Este frustum define un volumen en el espacio 3D, y el lienzo es un plano que
 * corta este volumen perpendicularmente a la linea de vision del ojo. Para ilustrar este concepto, se instruye al lector a colocar
 * una caja frente al lienzo, trazar lineas desde cada esquina de la caja hasta el ojo y marcar los puntos de interseccion en el
 * lienzo. Al unir estos puntos correspondientes a las aristas de la caja en el lienzo, se forma una imagen tridimensional del objeto.
 * <p>
 * Las tres reglas utilizadas para medir las coordenadas de la esquina de la caja forman lo que llamamos un sistema de coordenadas.
 * Este sistema, llamado sistema de coordenadas del <b>mundo</b> en CG, tiene un <b>origen</b> en el punto (0,0,0). Las coordenadas
 * pueden ser positivas, negativas o cero, dependiendo de su posicion relativa al origen.
 * <p>
 * Se realiza un cambio en la configuracion, moviendo el apice del frustum de vista al origen y orientando la linea de vision a lo
 * largo del eje z negativo, una configuracion comun en aplicaciones graficas. El lienzo se desplaza una unidad lejos del origen,
 * y la caja se mueve a una nueva posicion dentro del frustum. Se recalculan las coordenadas de las esquinas de la caja,
 * destacando que las coordenadas de profundidad, tambien llamadas <b>coordenadas z</b>, son negativas, las de altura (y) tambien
 * son negativas para las esquinas inferiores, y las de ancho (x) son negativas para las esquinas a la izquierda del origen.
 * <p>
 * En la figura 9 del articulo <a href="https://www.scratchapixel.com/lessons/3d-basic-rendering/get-started/gentle-introduction-to-computer-graphics-programming.html">A Gentle Introduction to Computer Graphics (Programming)</a>
 * se describe el proceso de proyeccion de puntos en un lienzo tridimensional en graficos por computadora. La figura 9 muestra un
 * lienzo de dimensiones arbitrarias, destacando que su tamaño puede ajustarse para mostrar mas o menos de la escena.
 * <p>
 * Las coordenadas proyectadas de un punto fuera del rango -1 a 1 no son visibles en el lienzo, definiendo asi el <b>espacio de pantalla</b>
 * como el area donde residen estas coordenadas proyectadas.
 * <p>
 * Para facilitar la manipulacion, se <b>normalizan</b> las coordenadas proyectadas en un rango [0,1], denominando a estas coordenadas
 * normalizadas como <b>NDC</b> (Coordenadas de Dispositivo Normalizadas). Este proceso es independiente del tamaño original del lienzo,
 * proporcionando un espacio común.
 * <p>
 * Se detalla como expresar estas coordenadas normalizadas en terminos de pixeles, multiplicandolas por las dimensiones de la
 * imagen digital, que se compone de una matriz de pixeles. Las coordenadas resultantes se denominan "raster space".
 * <br><br>
 * <h3>Conclusion</h3>
 * En conclusion, los graficos por computadora son principalmente matematicas aplicadas a un programa de computadora cuyo
 * proposito es generar una imagen (fotorreal o no) a la velocidad mas rapida posible (y con la precision de la que las
 * computadoras son capaces).
 */

public class ThreeDeeDemo {

    private Point[] points;

    // Velocidad a la que viaja la camara
    private float speed;

    private void start() {

        setUpDisplay();
        setUpOpenGL();
        setUpEntities();

        while (!Display.isCloseRequested()) {

            update();
            render();
            input();

            Display.update();
            Display.sync(FPS);

        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("3D");
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

        /* Crea una perspectiva con un angulo de 30 grados (campo de vision), relacion de aspecto de width/height, 0.001f near
         * (cerca) y 100 zFar (lejos). */
        // +x esta a la derecha
        // +y esta en la cima
        // +z es para la camara
        gluPerspective((float) 30, 640f / 480f, 0.001f, 100); // Diferencia entre el angulo y el far

        glMatrixMode(GL_MODELVIEW);

        // Se asegura de que los puntos mas cercanos a la camara se muestren delante de los puntos mas alejados
        glEnable(GL_DEPTH_TEST);
    }

    private void setUpEntities() {
        // Crea un array de Point con 1000 posiciones
        points = new Point[1000];
        Random random = new Random();

        // Crea un punto con:
        // x aleatoria entre -50 y +50
        // y aleatoria entre -50 y +50
        // z aleatoria entre 0 y -200
        for (int i = 0; i < points.length; i++)
            points[i] = new Point((random.nextFloat() - 0.5f) * 100f, (random.nextFloat() - 0.5f) * 100f, random.nextInt(200) - 200);

    }

    private void update() {
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Empuja la pantalla hacia adentro a la velocidad especificada
        glTranslatef(0, 0, speed); // update?

        glBegin(GL_POINTS);
        // Itera cada punto y lo dibuja en las coordenas aleatorias
        for (Point point : points)
            glVertex3f(point.x, point.y, point.z);
        glEnd();

    }

    private void input() {

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) speed += 0.01f; // Aumenta la velocidad
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) speed -= 0.01f; // Disminuye la velocidad

        // Iterar los eventos del teclado
        while (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) speed = 0f; // Restablece la velocidad a cero

            // Restablece la velocidad a cero y restablece la posicion
            if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                speed = 0;
                glLoadIdentity();
            }

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
        new ThreeDeeDemo().start();
    }

}
