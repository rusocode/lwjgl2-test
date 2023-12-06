package com.craivet;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

import static com.craivet.DisplayTest.*;

/**
 * Vertex: A point in 2D or 3D space.
 * <br>
 * Primitive: A simple form consisting of one or more vertices.
 * <br>
 * Model: Object in world space/scene and the view is the camera.
 * <br>
 * <a href="https://github.com/mattdesl/lwjgl-basics/wiki/Display">Display</a>
 */

public class DisplayTest2 {

    public static final boolean VSYNC = true;
    public static final boolean FULLSCREEN = false;

    /**
     * Start the application.
     */
    public void start() {

        setUpDisplay();
        setUpOpenGL();

        // Call this method before running the application to set the initial size
        resize();

        while (!Display.isCloseRequested()) {
            // If the game size was changed, update the projection
            if (Display.wasResized()) resize();
            render();
            Display.update();
            Display.sync(FPS);
        }

        Display.destroy();

    }

    /**
     * Set up the display.
     */
    private void setUpDisplay() {
        try {
            Display.setTitle("Display Test 2");
            Display.setResizable(true);
            Display.setVSyncEnabled(VSYNC);
            Display.setFullscreen(FULLSCREEN);
            Display.create();
        } catch (LWJGLException e) {
            JOptionPane.showMessageDialog(null, "Error", e.getMessage(), JOptionPane.ERROR_MESSAGE);
            Display.destroy();
            System.exit(1);
        }
    }

    /**
     * Set up OpenGL.
     */
    private void setUpOpenGL() {

        // 2D games generally do not require depth testing
        // glDisable(GL_DEPTH_TEST);

        // Enable the combination
        // glEnable(GL_BLEND);
        // glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        /* Enter the state required to modify the projection. Note that unlike Java2D, the vertex coordinate system does not have
         * to be equal to the window coordinate space. Invoking glOrtho creates a 2D vertex coordinate system like this:
         * Upper-Left: (0,0) Upper-Right: (WIDTH,0)
         * Bottom-Left: (0,HEIGHT) Bottom-Right: (WIDTH,HEIGHT)
         * If you skip invoking the glOrtho method, the default 2D projection coordinate space will be like this:
         * Upper-Left: (-1,+1) Upper-Right: (+1,+1)
         * Bottom-Left: (-1,-1) Bottom-Right: (+1,-1) */

        /* A computer monitor is a 2D surface. A 3D scene rendered by OpenGL must be projected onto the computer screen as a 2D
         * image. The GL_PROJECTION matrix is used for this projection transformation.
         * http://www.songho.ca/opengl/gl_projectionmatrix.html */
        glMatrixMode(GL_PROJECTION);
        // glLoadIdentity(); // Resets any previous projection matrix

        // Parameters for glOrtho()
        int left = 0; // x min
        int right = WIDTH; // x max
        int top = HEIGHT; // y max
        int bottom = 0; // y min
        int near = 1; // z min, if this is close to -1 times, then a negative input means positive z
        int far = -1; // z max (also negative)
        glOrtho(left, right, bottom, top, near, far);

        glMatrixMode(GL_MODELVIEW);
        // Sets light to transparent black
        // glClearColor(0f, 0f, 0f, 0f);
    }

    private void render() {

        glClear(GL_COLOR_BUFFER_BIT);

        // Since this primitive requires four vertices, we will have to call glVertex four times
        glBegin(GL_QUADS);
        glColor3f(1.0f, 0.0f, 0.0f); // Green
        glVertex2i(0, 0);
        glColor3b((byte) 0, (byte) 127, (byte) 0); // Red
        glVertex2d(640.0, 0.0);
        glColor3ub((byte) 255, (byte) 255, (byte) 255); // White
        glVertex2f(640.0f, 480.0f);
        glColor3d(0.0d, 0.0d, 1.0d); // Blue
        glVertex2i(0, 480);
        glEnd();

    }

    /**
     * Change the size of the display.
     */
    private void resize() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        // Update the projection matrix here...
    }

    public static void main(String[] args) {
        new DisplayTest2().start();
    }

}
