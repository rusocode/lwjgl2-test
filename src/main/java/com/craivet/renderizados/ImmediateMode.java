package com.craivet.renderizados;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

/**
 * Renderiza un triangulo de color usando el modo inmediato.
 * <p>
 * Actualmente el modo inmediato esta obsoleto, y la razon por la que no es optimo, es que la tarjeta grafica esta
 * vinculada directamente con el flujo de su programa. El controlador no puede decirle a la GPU que comience a
 * renderizar antes de glEnd, porque no sabe cuando terminara de enviar datos.
 * <br><br>
 * Recursos
 * <br>
 * <a href="https://stackoverflow.com/questions/6733934/what-does-immediate-mode-mean-in-opengl">¿Que significa el modo inmediato en OpenGL?</a>
 */

public class ImmediateMode {

	private final int WIDTH = 640;
	private final int HEITGH = 480;

	private void start() throws LWJGLException {

		setUpDisplay();
		setUpOpenGL();

		while (!Display.isCloseRequested()) {
			render();
			Display.update();
			Display.sync(60);
		}

		Display.destroy();
	}

	private void setUpDisplay() {
		try {
			Display.setTitle("Immediate Mode Demo");
			Display.setDisplayMode(new DisplayMode(WIDTH, HEITGH));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	private void setUpOpenGL() {
		/* OpenGL usa varias matrices de 4 x 4 para transformaciones; GL_MODELVIEW, GL_PROJECTION, GL_TEXTURE y GL_COLOR.
		 * Tanto los datos geometricos como los de imagen son transformados por estas matrices antes del proceso de
		 * rasterizacion de OpenGL. */
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		// El metodo glOrtho multiplica la matriz actual con una matriz ortografica
		glOrtho(1, -1, 1, -1, 1, -1); // Ejes ortogonales
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	private void render() {

		glClear(GL_COLOR_BUFFER_BIT);

		glBegin(GL_TRIANGLES);
		glColor3f(1, 0, 0);
		glVertex2f(-0.5f, -0.5f);
		glColor3f(0, 1, 0);
		glVertex2f(0.5f, -0.5f);
		glColor3f(0, 0, 1);
		glVertex2f(0.5f, 0.5f);
		glEnd();

	}

	public static void main(String[] args) {
		try {
			new ImmediateMode().start();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

}
