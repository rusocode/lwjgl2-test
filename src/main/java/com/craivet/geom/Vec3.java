package com.craivet.geom;

/**
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
        // double len = dot(new Vec3(3.6, 5, 6));
        if (len > 0) {
            double invLen = 1 / len;
            x *= invLen;
            y *= invLen;
            z *= invLen;
        }
        return this;
    }

    /**
     * Calcula la longitud (distancia) del vector.
     *
     * @return longitud del vector.
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Calcula el producto escalar entre dos vectores.
     * <p>
     * El producto escalar entre dos vectores es una operacion esencial y comun en cualquier aplicacion 3D porque el resultado de
     * esta operacion se relaciona con el coseno del angulo entre los dos vectores.
     * <p>
     * El producto escalar es una operacion esencial en 3D. Puede usarse para muchas cosas, por ejemplo, como prueba de
     * ortogonalidad. Cuando dos vectores son perpendiculares entre si, el resultado del producto escalar entre estos dos vectores
     * es 0. Cuando los dos vectores apuntan en direcciones opuestas, el producto escalar devuelve -1. Cuando apuntan en la misma
     * direccion, devuelve 1.
     *
     * @param v el otro vector.
     * @return la suma del producto escalar entre dos vectores.
     */
    public double dot(Vec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * El producto vectorial tambien es una operacion sobre dos vectores, pero a diferencia del producto escalar, que devuelve un
     * numero, el producto vectorial devuelve un vector. La particularidad de esta operacion es que el vector resultante del
     * producto vectorial es perpendicular a los otros dos.
     * <p>
     * En matematicas, el resultado de un producto vectorial se llama <b>pseudovector</b>. El orden del vector en la operacion de
     * producto cruzado es esencial cuando las <b>normales de superficie</b> se calculan a partir de la tangente y la bitangente
     * en el punto donde se calcula la normal.
     */
    public Vec3 cross(Vec3 v) {
        return new Vec3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
    }

    /**
     * Suma dos vectores.
     *
     * @param v el otro vector.
     * @return un punto.
     */
    public Vec3 add(Vec3 v) {
        return new Vec3(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Resta dos vectores.
     *
     * @param v el otro vector.
     * @return un vector.
     */
    public Vec3 subtract(Vec3 v) {
        return new Vec3(x - v.x, y - v.y, z - v.z);
    }

    public Vec3 multiply(double r) {
        return new Vec3(x * r, y * r, z * r);
    }

}
