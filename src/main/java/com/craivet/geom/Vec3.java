package com.craivet.geom;

/**
 * Esta clase muestra las 3 formas mas basicas de inicializar un vector. Tambien calcula la longitud y lo normaliza.
 * <p>
 * Un vector puede verse como una flecha que comienza en un punto y termina en otro. El vector en si indica no solo la direccion
 * del punto B desde A sino que tambien se puede utilizar para encontrar la distancia entre A y B.
 */

public class Vec3 {

    public double x, y, z;

    public Vec3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vec3(double xx) {
        this.x = xx;
        this.y = xx;
        this.z = xx;
    }

    public Vec3(double xx, double yy, double zz) {
        this.x = xx;
        this.y = yy;
        this.z = zz;
    }

    /**
     * Normaliza el vector.
     * <p>
     * Primero, solo normalizamos el vector si su longitud es mayor que 0 (ya que esta prohibido dividir por 0). Luego calculamos
     * una variable temporal, la inversa de la longitud del vector, y multiplicamos cada coordenada del vector con este valor en
     * lugar de dividirlas por la longitud del vector.
     * <p>
     * En matematicas, tambien encontraras el termino <b>norma</b> para definir una funcion que asigna una longitud o tamaño (o distancia)
     * a un vector. Por ejemplo, la funcion que acabamos de describir se llama <b>norma euclidiana</b>.
     *
     * @return vector normalizado.
     */
    public Vec3 normalize() {
        double len = length();
        if (len > 0) {
            double invLen = 1 / len;
            x *= invLen;
            y *= invLen;
            z *= invLen;
        }
        return this;
    }

    /**
     * Calcula la longitud del vector.
     *
     * @return longitud del vector.
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

}
