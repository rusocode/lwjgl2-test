package com.craivet;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import javax.swing.*;

import static com.craivet.Global.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Si intentaramos usar debugTexture del tutorial de Textures para renderizar todos nuestros mosaicos y sprites, es probable que
 * nos encontremos rapidamente con problemas de rendimiento. Esto se debe a que solo estamos enviando un sprite a la vez a la GPU.
 * Lo que necesitamos es "agrupar" muchos sprites en la misma llamada de dibujo; para esto usamos un SpriteBatch.
 * <p>
 * Un sprite no es mas que un conjunto de vertices que componen una forma rectangular. Cada vertice contiene una serie de atributos
 * que lo definen, como por ejemplo:
 * <p>
 * Position(x, y) - Donde se encuentra el vertice en la pantalla.
 * <p>
 * TexCoord(s, t) - Que region de nuestra textura queremos representar.
 * <p>
 * Color(r, g, b, a) - El color del vertice, utilizado para especificar el tinte o la transparencia.
 * <br><br>
 * <h2>Explicacion detallada del SpriteBatch</h2>
 * Primero comenzamos el lote {@code begin()}, simplemente le decimos que esta en "modo de dibujo". Luego, se llama a {@code spriteBatch.draw(...)}
 * que empuja la informacion del vertice del sprite (posicion, texcoord, color) en una pila muy grande. Los vertices no se pasan a
 * la GPU hasta que ocurre una de las siguientes situaciones:
 * <ul>
 * <li>El lote se ve obligado a procesarse con {@code end()} u otra llamada que vacia el lote, como {@code flush()}.
 * <li>El usuario intenta dibujar un objeto que usa una textura diferente a la anterior. El lote debe lavarse y enlazar la nueva
 * textura antes de que podamos continuar.
 * <li>Hemos alcanzado la capacidad de nuestra pila, por lo que necesitamos vaciarla para comenzar de nuevo.
 * </ul>
 * Esta es la idea basica detras de un batcher de sprites. Como puede ver, el uso de muchas texturas dara lugar a muchas llamadas
 * de dibujo (ya que el lote debera limpiarse para cada nueva textura). Es por eso que siempre se recomienda un atlas de texturas
 * (tambien conocido como hoja de sprites); nos permite renderizar muchos sprites en una sola llamada de dibujo.
 * <br><br>
 * <h2>TextureRegion</h2>
 * Para obtener el mejor rendimiento, deberiamos usar un atlas de texturas y dibujar regiones (tambien conocidas como subimagenes)
 * para crear los sprites de nuestro juego. Para esto tenemos una clase de utilidad, TextureRegion. Nos permite especificar en
 * pixeles la posicion superior izquierda (x, y) y el tamanio (ancho, alto) de nuestra subimagen.
 * <p>
 * Podemos obtener una TextureRegion del mosaico con lo siguiente:
 * <pre>{@code
 * region = new TextureRegion(64, 64, 64, 64);
 * }</pre>
 * <p>
 * Como puede ver, la utilidad TextureRegion nos permite obtener subimagenes sin preocuparnos por calcular las coordenadas de la
 * textura. Luego podemos renderizar el mosaico individual con nuestro lote de sprites asi:
 * <pre>{@code
 * spriteBatch.draw(region, x, y);
 * }</pre>
 * <br>
 * <h3>Color del vertice</h3>
 * Podemos cambiar el tinte y la transparencia de nuestros sprites ajustando el color del lote, tambien conocido como <b>color de
 * vertice</b>.
 * <p>
 * El RGB resultante se multiplica por el color de la textura; asi que si usamos una textura blanca (1, 1, 1, 1) y especificamos
 * un color de vertice de (1, 0, 0, 1), el resultado seria rojo. El componente Alfa del color nos permite ajustar la opacidad de
 * los sprites renderizados en la pantalla.
 */

public class SpriteBatching {

    // SpriteBatch spriteBatch;

    private void start() {

        setUpDisplay();
        setUpOpenGL();

        while (!Display.isCloseRequested()) {
            if (Display.wasResized()) resize(WIDTH, HEIGHT);
            render();
            Display.update();
            Display.sync(FPS);
        }

        Display.destroy();
    }

    private void setUpDisplay() {
        try {
            Display.setTitle("Sprite Batching");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setResizable(true);
            Display.create();
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy();
            System.exit(1);
        }
    }

    public void setUpOpenGL() {
        // Crea un solo lote que usaremos en toda nuestra aplicacion
        // spriteBatch = new SpriteBatch();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // Restablece cualquier matriz de proyeccion anterior
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    public void render() {
        // Preparar el lote para renderizar
        // spriteBatch.begin();

        // Dibuja todos los sprites
        // spriteBatch.draw(mySprite1, x, y);
        // spriteBatch.draw(mySprite2, x, y);

        // Finaliza el lote, vaciar los datos a la GPU
        // spriteBatch.end();
    }

    private void resize(int width, int height) {
        // Notificar al sprite batch cada vez que se cambie el tamanio la pantalla
        // spriteBatch.resize(width, height);
    }

    public static void main(String[] args) {
        new SpriteBatching().start();
    }

}