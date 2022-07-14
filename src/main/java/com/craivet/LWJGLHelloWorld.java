package com.craivet;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

public class LWJGLHelloWorld {

	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;

	public LWJGLHelloWorld() {
		try {
			// Configuracion
			Display.setTitle("LWJGL Test");
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		while (!Display.isCloseRequested()) {
			render();
		}

		Display.destroy();

	}

	/** Actualiza y sincroniza los frames. */
	private void render() {
		Display.update();
		Display.sync(60);
	}

	public static void main(String[] args) {
		new LWJGLHelloWorld();
	}
}
