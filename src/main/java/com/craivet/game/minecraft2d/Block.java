package com.craivet.game.minecraft2d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import com.craivet.TextureDemo;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import javax.swing.*;

import static org.lwjgl.opengl.GL11.*;

public class Block {

    private final BlockType type;
    private final float x;
    private final float y;
    private Texture texture;

    public Block(BlockType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
        try {
            // Decodifica la imagen de tipo PNG (en "location") para trabajarla como una textura
            texture = TextureLoader.getTexture("PNG", Objects.requireNonNull(TextureDemo.class.getClassLoader().getResourceAsStream(type.location)));
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "No se pudo encontrar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de I/O", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void draw() {

        glEnable(GL_TEXTURE_2D);

        // Enlaza la textura pasandole el texturizado y el ID de cada textura para que GL sepa que textura enlazar
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        // glPushMatrix() y glPopMatrix() se usan para guardar y cargar la matriz actual (pila de matrices)
        glPushMatrix();

        // Aplica un desplazamiento a todas las invocaciones posteriores del metodo glVertex
        glTranslatef(x, y, 0);

        // Es obligatorio pasarle a GL las coordenadas de texturas si se trabaja con estas
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);
        glTexCoord2f(1, 0);
        glVertex2f(World.BLOCK_SIZE, 0);
        glTexCoord2f(1, 1);
        glVertex2f(World.BLOCK_SIZE, World.BLOCK_SIZE);
        glTexCoord2f(0, 1);
        glVertex2f(0, World.BLOCK_SIZE);
        glEnd();

        glPopMatrix();
    }

    public BlockType getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}