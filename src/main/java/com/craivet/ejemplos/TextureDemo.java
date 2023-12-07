package com.craivet.ejemplos;

import java.io.*;
import java.util.Objects;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

/**
 * <h2>Sistema de Coordenadas en LWJGL</h2>
 * En LWJGL (y en muchos otros sistemas graficos), el origen del sistema de coordenadas se encuentra en la esquina superior
 * izquierda de la pantalla o area de visualizacion. Esto significa que la coordenada (0, 0) corresponde a la esquina superior
 * izquierda de la ventana, y las coordenadas aumentan hacia abajo y hacia la derecha.
 * <p>
 * En un sistema de coordenadas tipico, el eje x se extiende horizontalmente de izquierda a derecha, y el eje y se extiende
 * verticalmente de arriba hacia abajo. Por lo tanto, en LWJGL, el eje x positivo se extiende hacia la derecha desde el origen, y
 * el eje y positivo se extiende hacia abajo desde el origen.
 * <br><br>
 * <h2>Proyeccion Ortografica</h2>
 * La proyeccion ortografica es un tipo de transformacion utilizada en graficos por computadora y en el campo de la vision
 * tridimensional para mapear un objeto tridimensional en un plano bidimensional sin tener en cuenta la perspectiva. En otras
 * palabras, la proyeccion ortografica representa los objetos con proporciones iguales, independientemente de su distancia desde
 * la camara. Esta tecnica es diferente de la proyeccion perspectiva, que simula la forma en que los objetos se ven en la vida
 * real, donde los objetos mas cercanos parecen mas grandes que los objetos mas lejanos.
 * <p>
 * En la proyeccion ortografica, los rayos de luz que viajan desde los vertices del objeto hacia la camara son paralelos. Este
 * enfoque simplificado es util en situaciones donde no se necesita o no se desea un efecto de perspectiva, como en graficos 2D o
 * en ciertos estilos de juegos.
 * <p>
 * En OpenGL, una proyeccion ortografica se logra comunmente utilizando la funcion glOrtho(). Al configurar adecuadamente los
 * parametros de glOrtho(), puedes especificar el volumen de vision ortografica y proyectar los objetos en un espacio
 * bidimensional sin tener en cuenta la profundidad. Este tipo de proyeccion es util en aplicaciones como interfaces de usuario,
 * mapas 2D y otros casos donde la percepcion de la profundidad no es crítica.
 * <p>
 * En resumen, la proyeccion ortografica es un metodo de proyeccion que simplifica la representacion de objetos tridimensionales
 * al proyectarlos en un plano bidimensional con proporciones iguales, sin tener en cuenta la perspectiva.
 * <p>
 * La llamada a <b><i>glOrtho(left, right, bottom, top, zNear, zFar)</i></b> en OpenGL, como mencionado anteriormente, establece
 * una matriz de proyeccion ortografica. Esta configuracion en particular define un volumen de vision ortografica en el espacio
 * tridimensional. La interpretacion detallada de los parametros es la siguiente:
 * <p>
 * <b>left y right:</b> Establecen las coordenadas x del plano izquierdo y derecho del volumen de vision.
 * <p>
 * <b>bottom y top:</b> Establecen las coordenadas y del plano inferior y superior del volumen de vision.
 * <p>
 * <b>zNear y zFar:</b> Representan las distancias del plano cercano y lejano respecto a la camara.
 * <p>
 * Dado que glOrtho define una proyeccion ortografica, todos los objetos dentro de este volumen de vision ortografica seran
 * proyectados en la pantalla sin efectos de perspectiva. La llamada glOrtho() configura una matriz de proyeccion ortografica
 * adecuada para un sistema de coordenadas en el que la esquina superior izquierda de la pantalla es el origen (0,0) y las
 * coordenadas x aumentan hacia la derecha, mientras que las coordenadas y aumentan hacia abajo.
 * <br><br>
 * <h2>Matriz</h2>
 * Una matriz se refiere a un objeto que representa una matriz matematica. En el contexto de graficos 3D y OpenGL, las matrices se
 * utilizan para realizar transformaciones en los objetos tridimensionales, como rotaciones, traslaciones y escalas.
 * <br><br>
 * <h3>Modos de matrices</h3>
 * Los modos de matrices en OpenGL se utilizan para especificar sobre que matriz se realizaran las operaciones subsiguientes.
 * Cada uno de estos modos se refiere a un tipo específico de matriz y su funcion en el pipeline grafico.
 * <ul>
 * <li>GL_MODELVIEW: Este modo se utiliza para realizar operaciones de transformacion de modelo y vista. Es el modo predeterminado
 * al iniciar una aplicacion OpenGL.
 * <li>GL_PROJECTION: Este modo se utiliza para realizar operaciones de transformacion de proyeccion, que afectan la forma en que
 * se proyectan los objetos en la pantalla.
 * <li>GL_TEXTURE: Este modo se utiliza para realizar operaciones de transformacion de textura. Es menos común y se utiliza cuando
 * trabajas con mapeo de texturas.
 * </ul>
 * Para representar texturas en 2D en OpenGL con LWJGL, se utiliza comunmente el modo de matriz GL_PROJECTION. La matriz de
 * proyeccion se encarga de definir como se proyectan las coordenadas 3D en el plano de la pantalla. En el caso de graficos 2D, no
 * se necesita la componente de perspectiva, y se utiliza una proyeccion ortografica.
 */

public class TextureDemo {

    private Texture img;

    public void start() {

        setUpDisplay();
        setUpOpenGL();

        img = loadTexture();

        while (!Display.isCloseRequested()) {
            render();
            Display.update();
            Display.sync(FPS);
        }

        Display.destroy();

    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Texture Demo");
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
        glOrtho(0, 640, 480, 0, 1, -1); // Si utilizo la esquina inferior izquierda la textura se voltea
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        // Habilita el texturizado 2D
        glEnable(GL_TEXTURE_2D);
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);

        // Enlaza la textura antes de renderizarla
        // img.bind(); // TODO No parece necesario

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2i(10, 10);
        glTexCoord2f(1, 0);
        glVertex2i(10 + 50, 10);
        glTexCoord2f(1, 1);
        glVertex2i(10 + 50, 10 + 50);
        glTexCoord2f(0, 1);
        glVertex2i(10, 10 + 50);
        glEnd();
    }

    private Texture loadTexture() {
        try {
            return TextureLoader.getTexture("PNG", Objects.requireNonNull(TextureDemo.class.getClassLoader().getResourceAsStream("textures/" + "air" + ".png")));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de I/O", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static void main(String[] args) {
        new TextureDemo().start();
    }

}
