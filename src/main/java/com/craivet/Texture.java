package com.craivet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import de.matthiasmann.twl.utils.PNGDecoder;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

/**
 * Una imagen es simplemente una matriz de colores renderizada en dos dimensiones construida por pixeles individuales. Hay varias
 * formas de almacenar una imagen, para este ejemplo se usa el formato RGBA que ocupa 4 bytes por pixel. RGBA se refiere a los
 * canales rojo (red), verde (green), azul (blue) y transparente (alpha).
 * <p>
 * Dado que una matriz de bytes puede volverse muy grande, generalmente se usan compresiones como PNG o JPEG para disminuir el
 * tamaño del archivo final y distribuir la imagen en la red.
 * <p>
 * En OpenGL, se usan texturas para almacenar datos de imagenes. Las texturas OpenGL no solo almacenan datos de imagenes; son
 * simplemente matrices flotantes almacenadas en la GPU, ej. util para el mapeo de sombras y otras tecnicas avanzadas.
 * <p>
 * Los pasos basicos para convertir una imagen en textura son los siguientes:
 * <ol>
 * <li>Decodificar el formato de la imagen en bytes a RGBA.
 * <li>Obtener una nueva identificacion de la textura.
 * <li>Enlazar esa textura.
 * <li>Configurar los parametros de la textura.
 * <li>Subir los bytes RGBA a OpenGL.
 * </ol>
 * En general, la mayoria de las computadoras modernas permiten texturas de al menos 4096x4096, pero si quieres estar realmente
 * seguro, puedes limitarte a 2048x2048. Si cree que va a trabajar con controladores antiguos o limitadores (o Android, iOS,
 * WebGL), es posible que desee limitarse a 1024x1024.
 * <p>
 * Para renderizar un objeto, necesitamos darle a OpenGL cuatro vertices (en geometria, un vertice es el punto donde se encuentran
 * dos o mas elementos unidimensionales). Como puede ver, terminamos con un cuadrilatero 2D. Cada vertice tiene una serie de
 * atributos, que incluyen <b>posicion</b> (x, y) y <b>coordenadas de textura</b> (s, t). Las coordenadas de textura se definen en
 * el espacio tangente, generalmente entre 0.0 y 1.0. Estos le dicen a OpenGL donde tomar muestras de nuestros datos de textura.
 * <p>
 * Esto depende de que nuestro sistema de coordenadas tenga un origen en la parte superior izquierda. Algunas bibliotecas,
 * como LibGDX, utilizaran el origen inferior izquierdo, por lo que los valores de posicion y coordenadas de textura pueden estar
 * en un orden diferente.
 * <p>
 * ¿Que sucede si usamos valores de coordenadas de textura menores que 0.0 o mayores que 1.0? Aqui es donde entra en juego el modo
 * de envoltura. Le decimos a OpenGL como manejar valores fuera de las coordenadas de textura. Los dos modos mas comunes son
 * GL_CLAMP_TO_EDGE, que simplemente muestrea el color del borde, y GL_REPEAT, que conducira a un patron repetido. Por ejemplo, el
 * uso de 2.0 y GL_REPEAT hara que la imagen se repita dos veces dentro del ancho y alto que especificamos.
 * <br><br>
 * Resources:
 * <a href="https://github.com/mattdesl/lwjgl-basics/wiki/Textures">Textures</a>
 * <a href="https://es.wikipedia.org/wiki/Dimensi%C3%B3n">Dimension</a>
 */

public class Texture {

    public final int target = GL_TEXTURE_2D;
    public final int id;
    public final int width;
    public final int height;

    public Texture(URL url) throws IOException {
        /* Para los juegos de estilo pixel-art, generalmente la constante GL_NEAREST es adecuada para este caso, ya que conduce a
         * una escala de borde duro sin desenfoque. */
        this(url, GL_NEAREST);
    }

    public Texture(URL url, int filter) throws IOException {
        this(url, filter, GL_CLAMP_TO_EDGE);
    }

    public Texture(URL url, int filter, int wrap) throws IOException {
        // Abre una conexion de la URL especificada y devuelve un InputStream para leer desde esa conexion
        try (InputStream input = url.openStream()) {

            /* OpenGL no sabe nada sobre GIF, PNG, JPEG, etc; solo comprende bytes y flotantes. Asi que necesitamos decodificar
             * nuestra imagen PNG en un ByteBuffer. */

            // Inicializa el decodificador
            PNGDecoder decoder = new PNGDecoder(input);

            // Almacena las dimensiones de la imagen
            width = decoder.getWidth();
            height = decoder.getHeight();

            // Estamos usando el formato RGBA, es decir, 4 componentes o "bytes por pixel"
            final int bpp = 4;

            // Crea un buffer que contendra los bytes totales de la imagen
            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);

            // 1) Decodifica la imagen de tipo PNG en el buffer de bytes en formato RGBA
            decoder.decode(buffer, width * bpp, PNGDecoder.Format.RGBA); // Nos basaremos en el ancho para renderizarlo como una imagen bidimensional

            // Voltea el buffer en "modo lectura" para OpenGL
            buffer.flip();

            // 2) Habilita el texturizado y genera una identificacion unica para que GL sepa que textura enlazar
            glEnable(target);
            id = glGenTextures();

            // 3) Enlaza la textura
            glBindTexture(target, id);

            /* Usa una alineacion de 1 para estar seguro. Esto le dice a OpenGL como descomprimir los bytes RGBA que
             * especificaremos. */
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1); // Configura el modo de desempaquetar

            // 4) Configura los parametros
            // Configurar el filtrado, es decir, como OpenGL interpolara los pixeles al escalar hacia arriba o hacia abajo
            glTexParameteri(target, GL_TEXTURE_MIN_FILTER, filter);
            glTexParameteri(target, GL_TEXTURE_MAG_FILTER, filter);
            // Configura el modo de ajuste, es decir, como OpenGL manejara los pixeles fuera del rango esperado
            glTexParameteri(target, GL_TEXTURE_WRAP_S, wrap);
            glTexParameteri(target, GL_TEXTURE_WRAP_T, wrap);

            // 5) Sube los bytes RGBA a OpenGL
            glTexImage2D(target, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            // Puede consultar el ancho y alto maximo de textura con lo siguiente
            // int maxSize = glGetInteger(GL_MAX_TEXTURE_SIZE);

        }
    }

}