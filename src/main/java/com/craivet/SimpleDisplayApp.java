package com.craivet;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static com.craivet.Global.*;

/**
 * La clase Display proporciona metodos para crear, mostrar y administrar la ventana de la aplicacion. Algunas de las operaciones
 * tipicas que puede realizar con la clase Display incluyen crear una ventana con un contexto OpenGL, manipular eventos de entrada
 * del usuario y actualizar el contenido de la ventana.
 * <p>
 * Un monitor de computadora es una superficie 2D. Una escena 3D renderizada por OpenGL debe proyectarse en la pantalla de
 * la computadora como una imagen 2D. La matriz GL_PROJECTION se utiliza para esta transformacion de proyeccion:
 * <br><br>
 * {@code glMatrixMode(GL_PROJECTION);}
 * <br><br>
 * <h3>Modo inmediato</h3>
 * La razon por la que el modo inmediato no es optimo es que la tarjeta grafica esta vinculada directamente con el flujo de su
 * programa. El controlador no puede decirle a la GPU que comience a renderizar antes de glEnd, porque no sabe cuando terminara de
 * enviar datos y tambien necesita transferir esos datos (lo que solo puede hacer despues de glEnd).
 * <p>
 * En contraste con eso, si usas, por ejemplo, un objeto de buffer de vertice, llenas un buffer con datos y lo entregas a OpenGL.
 * Su proceso ya no posee estos datos y, por lo tanto, ya no puede modificarlos. El conductor puede confiar en este hecho y puede
 * (incluso especulativamente) cargar los datos siempre que el autobus este libre.
 * <br><br>
 * Recursos: <a href="https://github.com/mattdesl/lwjgl-basics/wiki/Display">Display</a>
 * <a href="http://www.songho.ca/opengl/gl_projectionmatrix.html">Projection Matrix</a>
 * <a href="https://stackoverflow.com/questions/2571402/how-to-use-glortho-in-opengl">How to use glOrtho() in OpenGL?</a>
 * <a href="https://www.youtube.com/watch?v=cvcAjgMUPUA">How Rendering Graphics Works in Games!</a>
 * <a href="https://stackoverflow.com/questions/6733934/what-does-immediate-mode-mean-in-opengl/36166310#36166310">What does "immediate mode" mean in OpenGL?</a>
 */

public class SimpleDisplayApp {

    /**
     * Inicia la aplicacion.
     */
    private void start() {

        setUpDisplay();
        setUpOpenGL();

        // Actualiza y renderiza los fotogramas mientras la pantalla no esta cerrada
        while (!Display.isCloseRequested()) {
            // Si se cambio el tamaño de la ventana, actualiza la proyeccion
            if (Display.wasResized()) resize();
            render();
            // Voltea los buffers y sincroniza a 60 FPS
            Display.update();
            Display.sync(FPS); // Sincroniza la pantalla a 60 fps (16.67 milisegundos)
        }

        dispose();
        Display.destroy();
    }

    /**
     * Configura la pantalla.
     */
    private void setUpDisplay() {
        try {
            Display.setTitle("Display Test");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            // Display.setFullscreen(true);
            Display.setResizable(true);
            /* Vsync solo funciona en modo de pantalla completa, ya que las aplicaciones con ventana no tienen acceso directo ni
             * control sobre la pantalla y el sistema operativo maneja cualquier cambio de buffer. Sin embargo, Vsync puede actuar
             * como un limitador de velocidad de fotogramas en el modo de ventana. */
            Display.setVSyncEnabled(true); // Elimina el desgarro de la pantalla
            Display.create(); // Crea una pantalla con el modo de visualizacion y el titulo especificados
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy();
            System.exit(1);
        }
    }

    /**
     * Configura el contexto OpenGL.
     */
    private void setUpOpenGL() {

        // Habilita la combinacion
        // glEnable(GL_BLEND);
        // glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        /* OpenGL utiliza varias matrices de 4 x 4 para transformaciones; GL_MODELVIEW, GL_PROJECTION, GL_TEXTURE y GL_COLOR.
         * Estas matrices transforman tanto los datos geometricos como los de imagen antes del proceso de rasterizacion OpenGL.
         * Configura una presentacion ortografica donde (0, 0) es la esquina superior izquierda y (WIDTH, HEIGHT) en la esquina
         * inferior derecha. La matriz de proyeccion controla la perspectiva aplicada a las primitivas; se utiliza de forma
         * similar a modelview. */
        glMatrixMode(GL_PROJECTION); // Lente

        // glLoadIdentity(); // Restablece cualquier matriz de proyeccion anterior

        /* Ingrese el estado requerido para modificar la proyeccion. Tenga en cuenta que, a diferencia de Java2D, el sistema de
         * coordenadas del vertice no tiene que ser igual al espacio de coordenadas de la ventana. La invocacion a glOrtho crea
         * un sistema de coordenadas de vertice 2D como este:
         * upper left = (0, 0)
         * upper right = (WIDTH, 0)
         * bottom left = (0, HEIGHT)
         * bottom right = (WIDTH, HEIGHT)
         * Si omite la invocacion del metodo glOrtho, el espacio de coordenadas de proyeccion 2D predeterminado sera asi:
         * upper left = (-1, 1)
         * upper right = (1, 1)
         * bottom left = (-1, -1)
         * bottom right = (1, -1) */

        // El metodo glOrtho multiplica la matriz actual por una matriz ortografica
        glOrtho(0, WIDTH, 0, HEIGHT, 1, -1); // left = xmin, right = xmax, bottom = ymin, top = ymax, near = zmin, far = zmax

        // Se establece la matriz modelview, que controla la posicion de la camara respecto a las primitivas que renderizamos
        glMatrixMode(GL_MODELVIEW); // Camara

        // Establece claro a negro transparente
        // glClearColor(0f, 0f, 0f, 0f);

        // ... Inicializar los recursos aqui ...
    }

    /**
     * Renderiza la ventana.
     */
    private void render() {
        // Borra el contenido 2D de la ventana (limpia la pantalla)
        glClear(GL_COLOR_BUFFER_BIT);

        /* Este metodo acepta un solo argumento que especifica como se interpretan los vertices. Como esta primitiva requiere
         * cuatro vertices, tendremos que llamar a glVertex cuatro veces. Los metodos glBegin y glEnd delimitan los vertices que
         * definen una primitiva o un grupo de primitivas similares (vertices de punto, linea y poligono). */
        glBegin(GL_QUADS);

        /* El metodo glColor() establece el color actual (a todas las llamadas posteriores a glVertex se les asignara este color)
         * y el numero despues de glColor/glVertex indica la cantidad de componentes (xyzw o rgba). El caracter despues del numero
         * indica el tipo de argumento. Siendo d=Double, f=Float, i=Integer, b=Signed Byte, ub=Unsigned Byte. El numero 2 de
         * glVertex le indica cuantos componentes tendra el vertice (x e y), siendo un modelo 2D en este caso. Si se envia un
         * vertice a OpenGL, vincula el estado del color actual al vertice y lo dibuja en consecuencia. Los valores de color para
         * dobles y flotantes varian de 0.0 a 1.0 y para bytes sin firmar varian de 0 a 255. */
        glColor3f(1.0f, 0.0f, 0.0f); // Green
        glVertex2i(0, 0);
        glColor3b((byte) 0, (byte) 127, (byte) 0); // Red
        glVertex2d(640.0, 0.0);
        glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
        glVertex2f(640.0f, 480.0f);
        glColor3d(0.0d, 0.0d, 1.0d); // Blue
        glVertex2i(0, 480);

        // Cuando haya terminado de enviar datos de vertices a OpenGL, puede dejar de renderizar escribiendo
        glEnd();

    }

    /**
     * Cambia el tamaño de la ventana.
     */
    private void resize() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        // Actualice la matriz de proyeccion aqui...
    }

    // Elimina los recursos
    private void dispose() {
        // ... Deshacerse de las texturas, etc. ...
    }

    public static void main(String[] args) {
        new SimpleDisplayApp().start();
    }

}
