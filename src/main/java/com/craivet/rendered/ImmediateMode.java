package com.craivet.rendered;

import javax.swing.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import static com.craivet.DisplayTest.*;

/**
 * Render a color triangle using immediate mode.
 * <p>
 * Currently the immediate mode is obsolete, and the reason it is not optimal, is that the graphics card is linked directly to the
 * flow of your program. The driver can't tell the GPU to start rendering before glEnd, because it doesn't know when it will
 * finish sending data.
 * <br><br>
 * Resources:
 * <a href="https://stackoverflow.com/questions/6733934/what-does-immediate-mode-mean-in-opengl">What does "immediate mode" mean in OpenGL?</a>
 */

public class ImmediateMode {

    /**
     * Start the application.
     */
    private void start() {
        setUpDisplay();
        setUpOpenGL();
        while (!Display.isCloseRequested()) {
            render();
            Display.update();
            Display.sync(FPS);
        }
        Display.destroy();
    }

    /**
     * Render.
     */
    private void render() {
        // Clear the 2D content of the window (clear the screen)
        glClear(GL_COLOR_BUFFER_BIT);

        /* Start drawing a triangle by specifying the 3 vertices (2 floating value points indicating the x and y coordinates) with
         * their respective colors. The glBegin method accepts a single argument that specifies how the vertices are interpreted.
         * Methods glBegin and glEnd delimit the vertices that define a primitive or a group of similar primitives. The number
         * after 'glColor' and 'glVertex' indicates the number of components (xyzw/rgba). The character after the number
         * indicates the type of argument: d=Double, f=Float, i=Integer, b=Signed Byte, ub=Unsigned Byte. The number 2 of glVertex
         * tells you how many components the vertex will have (x and y), being a 2D model in this case. If a vertex is sent to
         * OpenGL, it binds the current color state to the vertex and draws it accordingly. Color values for doubles and floats
         * range from 0.0 to 1.0 and for unsigned bytes range from 0 to 255. */
        glBegin(GL_TRIANGLES);
        // All future calls to vertices will be taken as points of a quadrilateral until glEnd is called
        glColor3f(1, 0, 0);
        glVertex2f(-0.5f, -0.5f);
        glColor3f(0, 1, 0);
        glVertex2f(0.5f, -0.5f);
        glColor3f(0, 0, 1);
        glVertex2f(0.5f, 0.5f);
        // Stop rendering when finished sending vertex data to OpenGL
        glEnd();

    }

    /**
     * Set up the display.
     */
    private void setUpDisplay() {
        try {
            Display.setTitle("Immediate Mode");
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
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
        /* OpenGL uses several 4 x 4 matrices for transformations; GL_MODELVIEW, GL_PROJECTION, GL_TEXTURE and GL_COLOR. Both
         * geometric and image data are transformed by these matrices before the OpenGL rasterization process. */
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        // The glOrtho method multiplies the current matrix with an orthographic matrix
        glOrtho(1, -1, 1, -1, 1, -1); // Orthogonal axes
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    public static void main(String[] args) {
        new ImmediateMode().start();
    }

}
