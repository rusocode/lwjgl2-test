package com.craivet;

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
 * <h2>Sistema de Coordenadas</h2>
 * En LWJGL (y en muchos otros sistemas graficos), el origen del sistema de coordenadas se encuentra en la esquina superior
 * izquierda de la pantalla o area de visualizacion. Esto significa que la coordenada (0, 0) corresponde a la esquina superior
 * izquierda de la ventana, y las coordenadas aumentan hacia abajo y hacia la derecha.
 * <p>
 * En un sistema de coordenadas tipico, el eje x se extiende horizontalmente de izquierda a derecha, y el eje y se extiende
 * verticalmente de arriba hacia abajo. Por lo tanto, en LWJGL, el eje x positivo se extiende hacia la derecha desde el origen, y
 * el eje y positivo se extiende hacia abajo desde el origen.
 * <br><br>
 * <h2>Proyeccion en perspectiva</h2>
 * Una imagen es una representacion de una escena 3D en una superficie plana, ya sea un lienzo o una pantalla. Para crear una imagen
 * que parezca real, se utiliza la tecnica de proyeccion en perspectiva, que implica extender lineas desde las esquinas del objeto
 * hacia el ojo y encontrar la interseccion de estas lineas con una superficie plana perpendicular a la linea de vision. Al conectar
 * estos puntos, se obtiene una representacion <b>alambrica</b> de la escena.
 * <p>
 * Una de las principales propiedades visuales importantes de este tipo de proyeccion es que un objeto se hace mas pequeño a medida
 * que se aleja del ojo (los bordes posteriores de una caja son mas pequeños que los bordes frontales). Este efecto se llama <b>escorzo</b>.
 * <p>
 * En la proyeccion en perspectiva el ojo se encuentra en el centro del lienzo, lo que significa que la linea de vision siempre
 * pasa por el centro de la imagen. El tamaño del lienzo es ajustable, y al representar el tronco de visualizacion o <b>frustum</b>,
 * una piramide definida por lineas desde las esquinas del lienzo hacia el ojo, se ilustra como este cambio afecta el volumen de
 * visualizacion. Modificar el tamaño del lienzo amplia o reduce este volumen, afectando asi el campo de vision de la escena
 * capturada, comparable al ajuste de la distancia focal en la fotografia.
 * <br><br>
 * <h2>Proyeccion Ortogonal</h2>
 * En OpenGL, la proyeccion ortogonal se refiere a la transformacion que se aplica a las coordenadas tridimensionales de una
 * escena para proyectarlas en un plano bidimensional de manera ortogonal. Esto significa que las lineas de proyeccion son
 * perpendiculares al plano de proyeccion. La proyeccion ortogonal es comunmente utilizada en situaciones donde se desea preservar
 * las proporciones y tamaños relativos de los objetos en la escena, independientemente de su profundidad en el espacio
 * tridimensional.
 * <p>
 * En OpenGL, hay una matriz de proyeccion que se utiliza para aplicar la proyeccion. Para establecer una proyeccion ortogonal en
 * OpenGL, generalmente se usa la funcion <b>glOrtho</b>. Esta funcion toma como parametros los limites izquierdo, derecho,
 * inferior, superior, cerca y lejano del volumen de vista ortogonal. Los objetos fuera de este volumen no seran visibles en la
 * proyeccion.
 * <p>
 * Aqui hay un ejemplo simple en el que se establece una proyeccion ortogonal en OpenGL:
 * <pre>{@code
 * glMatrixMode(GL_PROJECTION);
 * glLoadIdentity();
 * glOrtho(left, right, bottom, top, near, far);
 * glMatrixMode(GL_MODELVIEW);
 * }</pre>
 * Donde left, right, bottom, top, near, y far son los parametros que definen el volumen de vista ortogonal.
 * <p>
 * Despues de establecer la proyeccion ortogonal, los objetos en la escena se renderizaran proyectados en el plano bidimensional
 * según la configuracion de la matriz de proyeccion. Esta matriz se combina con otras transformaciones de modelo y vista para
 * finalmente generar las coordenadas de pantalla y renderizar la escena en la ventana de OpenGL.
 * <br><br>
 * <h3>Modos de matrices</h3>
 * Los modos de matrices en OpenGL se utilizan para especificar sobre que matriz se realizaran las operaciones subsiguientes.
 * Cada uno de estos modos se refiere a un tipo especifico de matriz y su funcion en el pipeline grafico.
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
        img.bind();

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
            return TextureLoader.getTexture("PNG", Objects.requireNonNull(TextureDemo.class.getClassLoader().getResourceAsStream("textures/" + "grass" + ".png")));
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
