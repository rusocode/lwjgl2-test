package com.craivet.minecraft2d;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

import com.craivet.ejemplos.TextureDemo;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class Block {

    private BlockType type;
    private float x;
    private float y;
    private Texture texture;

    public Block(BlockType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
        try {
            // Decodifica la imagen de tipo PNG (en "location") para trabajarla como una textura
            // this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(type.location)));
            this.texture = TextureLoader.getTexture("PNG", Objects.requireNonNull(TextureDemo.class.getClassLoader().getResourceAsStream(type.location)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Dibuja la textura
    public void draw() {

        // Habilita el texturizado 2D
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

}