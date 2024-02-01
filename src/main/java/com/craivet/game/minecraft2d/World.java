package com.craivet.game.minecraft2d;

public class World {

    // Textura de 32 pixeles justo para 640 y 480?
    public static final int BLOCK_SIZE = 32;

    // Calcula el cantidad de columnas y filas dependiendo del tamanio de la ventana y la textura
    public static int cols = Screen.getWidth() / BLOCK_SIZE;
    public static int rows = Screen.getHeight() / BLOCK_SIZE;

    public static int getCols() {
        return cols;
    }

    public static void setCols(int cols) {
        World.cols = cols;
    }

    public static int getRows() {
        return rows;
    }

    public static void setRows(int rows) {
        World.rows = rows;
    }

}