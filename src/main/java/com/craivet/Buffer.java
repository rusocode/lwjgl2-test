package com.craivet;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

/**
 * Un buffer es simplemente un bloque de memoria que contiene algunos datos. Para nuestros propositos, puede considerarlo como una
 * serie de elementos. Sin embargo, en lugar de acceso aleatorio (por ejemplo, matriz[i]), los buffers leen y escriben datos
 * relativos a su posicion actual.
 * <br><br>
 * Recursos:
 * <a href="http://tutorials.jenkov.com/java-nio/buffers.html">buffers</a>
 */

public class Buffer {

    public static void main(String[] args) {

        // Crea un buffer de cuatro bytes (LWJGL incluye utilidades para crear facilmente buffers)
        ByteBuffer buffer = BufferUtils.createByteBuffer(4);

        // Metodo de "posicion relativa", que coloca el byte y luego mueve la posicion hacia adelante
        buffer.put((byte) 4);
        buffer.put((byte) 3);
        buffer.put((byte) 45);
        buffer.put((byte) 127);

        // Voltea la posicion para restablecer la posicion relativa a cero (cambia el buffer del modo escritura a lectura)
        buffer.flip();

        // Itera el buffer
        for (int i = 0; i < buffer.limit(); i++)
            System.out.println(buffer.get());

        // Despues de leer todos los datos, limpia el buffer para poder escribir nuevamente sobre el
        buffer.clear();

        // Para entender lo que esta sucediendo, comparemoslo con una matriz de Java

        // Crea un array de 4 elementos (tamanio fijo)
        byte[] array = new byte[4];

        // Inicializa la posicion en 0
        int position = 0;

        // Asigna el valor al array e incrementa la posicion ("put" relativo)
        array[position++] = 4;
        array[position++] = 3;
        array[position++] = 45;
        array[position++] = 127;

        // "Voltea" la posicion/limite
        int limit = position;
        position = 0;

        for (int i = 0; i < limit; i++)
            System.out.println(array[position++]); // Usando un "get" relativo, la posicion aumenta cada vez

        /* . . . */

        /* Entonces, ¿Como se relaciona esto con LWJGL y OpenGL? Hay dos formas comunes en las que usara buffers: escribiendo
         * datos en GL (es decir, cargando datos de textura en la GPU) o leyendo datos de GL (es decir, leyendo datos de textura
         * de la GPU u obteniendo un cierto valor del controlador). */

        // Digamos que estamos creando una textura RGBA azul 1x1, nuestra configuracion del buffer se veria asi
        int width = 1; // 1 pixel de ancho
        int height = 1; // 1 pixel de alto
        final int bpp = 4; // 4 bytes por pixel (RGBA) https://en.wikipedia.org/wiki/RGBA_color_model

        buffer = BufferUtils.createByteBuffer(width * height * bpp);

        // Coloca los bytes Red, Green, Blue y Alpha en el buffer
        buffer.put((byte) 0x00).put((byte) 0x00).put((byte) 0xFF).put((byte) 0xFF); // Pixel azul

        // Esto debe hacerse antes de que GL pueda leerlo
        buffer.flip();

        // Ejemplo de envio de datos a GL...

        // glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        // Ejemplo de como obtener datos de GL...

        // Crea un buffer con 16 elementos
        IntBuffer intBuffer = BufferUtils.createIntBuffer(16);

        /* Esto llamara al meotodo put() relativo con el tamanio maximo de texturas, luego continua "put" ceros hasta alcanzar
         * la capacidad del buffer y por ultimo lo voltea. */
        // glGetInteger(GL_MAX_TEXTURE_SIZE, intBuffer);

        // Dado que nuestro buffer ya esta volteado, nuestra posicion sera cero... para que podamos seguir adelante y tomar el primer elemento
        // int maxSize = buffer.get();

        /* Como se describe en los documentos, GL_MAX_TEXTURE_SIZE nos dara un valor, pero como glGetInteger puede devolver
         * hasta 16 elementos, LWJGL espera que nuestro buffer tenga al menos eso como capacidad. Siempre que sea posible, debe
         * intentar reutilizar los buffers en lugar de crear siempre nuevos.
         *
         * Tambien tenga en cuenta que LWJGL incluye metodos convenientes para glGetInteger, glGenTextures y varias otras llamadas.
         * Entonces el codigo anterior en realidad se reduciria a lo siguiente: */
        int maxSize = glGetInteger(GL_MAX_TEXTURE_SIZE);

    }
}