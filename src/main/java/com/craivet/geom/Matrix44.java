package com.craivet.geom;

import java.util.Arrays;

/**
 * <h2>Matriz</h2>
 * Una matriz es una estructura matematica bidimensional compuesta por numeros, simbolos o expresiones organizadas en filas y
 * columnas. Cada elemento en una matriz se identifica por su posicion en la fila y columna correspondiente. La matriz se denota
 * comunmente por una letra mayuscula, y sus elementos individuales se denotan por letras minusculas con subindices.
 * <p>
 * Algunas caracteristicas clave de las matrices incluyen:
 * <ol>
 * <li>
 * <b>Dimension</b>: La dimension de una matriz se expresa como el numero de filas por el numero de columnas. Si una matriz tiene
 * m filas y n columnas, se dice que es una matriz de dimensiones m x n.
 * </li>
 * <li>
 * <b>Matriz cuadrada</b>: Una matriz cuadrada es aquella en la que el numero de filas es igual al numero de columnas (es decir,
 * m=n).
 * </li>
 * <li>
 * <b>Operaciones matriciales</b>: Las matrices admiten diversas operaciones matriciales, como la suma, la resta, la
 * multiplicacion por un escalar y la multiplicacion de matrices. Estas operaciones tienen reglas especificas y propiedades.
 * </li>
 * </ol>
 */

public class Matrix44 {

    private final double[][] m;

    public Matrix44() {
        // Inicializa los coeficientes de la matriz con los coeficientes de la matriz identidad creando una matriz cuadrada
        m = new double[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    public double[] getRow(int i) {
        return m[i];
    }

    public void setRow(int i, double[] row) {
        m[i] = row;
    }

    public static void main(String[] args) {
        Matrix44 matrix = new Matrix44();
        System.out.println(Arrays.toString(matrix.getRow(3))); // Accede a los coeficientes de la fila 3
        System.out.println(matrix.m[0][0]); // Accede al coeficiente del subindice 0,0
    }

}


