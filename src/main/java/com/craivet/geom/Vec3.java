package com.craivet.geom;

/**
 * <h2>Vector</h2>
 * En matematicas y fisica, un vector es un objeto geometrico que tiene magnitud y direccion. Es comunmente representado por una
 * flecha en un espacio tridimensional, donde la longitud de la flecha indica la magnitud del vector y la direccion de la flecha
 * indica la direccion del vector. Los vectores son herramientas fundamentales en algebra lineal y calculo, y se utilizan en una
 * variedad de campos, incluyendo fisica, informatica, ingenieria y estadisticas.
 * <p>
 * Aqui hay algunas caracteristicas clave de los vectores:
 * <ol>
 * <li>
 * <b>Magnitud</b>: La magnitud de un vector representa su longitud o tamaño. Puede ser cualquier numero real no negativo y se
 * denota por |v| o v. En el espacio tridimensional, la magnitud de un vector v = (x, y, z) se calcula como la raiz cuadrada de la
 * suma de los cuadrados de sus componentes: |v| = √(x² + y² + z²).
 * </li>
 * <li>
 * <b>Direccion</b>: La direccion de un vector indica hacia donde se extiende la flecha. Se especifica utilizando angulos o
 * coordenadas direcionales.
 * </li>
 * <li>
 * <b>Representacion grafica</b>: En una representacion grafica, un vector se muestra como una flecha con un punto de inicio y un
 * punto final. La longitud de la flecha representa la magnitud del vector, y la orientacion de la flecha representa la direccion.
 * </li>
 * <li>
 * <b>Componentes</b>: Un vector en el espacio tridimensional tiene tres componentes: una para la direccion x, otra para la
 * direccion y y otra para la direccion z. Se denotan como (x, y, z).
 * </li>
 * <li>
 * <b>Operaciones vectoriales</b>: Los vectores admiten operaciones como la suma, la resta, la multiplicacion por un escalar y el
 * producto punto. Estas operaciones tienen significados geometricos y algebraicos.
 * </li>
 * </ol>
 * En programacion, los vectores tambien se utilizan para representar puntos en el espacio tridimensional y realizar operaciones
 * matematicas. Los lenguajes de programacion como C++, Java y Python proporcionan estructuras de datos para trabajar con vectores,
 * y las bibliotecas matematicas suelen incluir funciones para operaciones vectoriales.
 * <br><br>
 * <h2>Transformaciones</h2>
 * En geometria, las transformaciones son operaciones que modifican la posicion, el tamaño o la orientacion de figuras geometricas
 * en el plano o en el espacio. Las transformaciones geometricas se aplican comunmente a puntos o vectores en un espacio
 * tridimensional para cambiar su ubicacion o apariencia. Estas transformaciones permiten cambiar la apariencia de un objeto
 * geometrico sin cambiar su esencia o propiedades fundamentales. Las transformaciones geometricas son herramientas importantes en
 * matematicas y graficos computacionales, y se clasifican comunmente en tres tipos principales: traslacion, rotacion y escalado.
 * <ol>
 * <li>
 * <b>Traslacion</b>: Es una transformacion que desplaza un objeto geometrico en una direccion especifica. Todos los puntos del
 * objeto se mueven la misma distancia y en la misma direccion. En el plano cartesiano bidimensional, la traslacion se expresa
 * como (x′,y′)=(x+Δx,y+Δy), donde (x,y) son las coordenadas originales, y (Δx,Δy) son las cantidades de desplazamiento en las
 * direcciones x e y, respectivamente.
 * </li>
 * <li>
 * <b>Rotacion</b>: Es una transformacion que gira un objeto alrededor de un punto fijo llamado el centro de rotacion. La rotacion
 * puede ser en sentido horario o antihorario y se mide en grados o radianes. En el plano bidimensional, una rotacion alrededor
 * del origen se expresa como (x′,y′)=(xcosθ−ysinθ,xsinθ+ycosθ), donde (x,y) son las coordenadas originales, (x′,y′) son las
 * coordenadas rotadas, y θ es el angulo de rotacion.
 * </li>
 * <li>
 * <b>Escalado</b>: Es una transformacion que cambia el tamaño de un objeto geometrico en funcion de un factor de escala. Puede
 * aumentar o disminuir las dimensiones del objeto. En el plano bidimensional, el escalado se expresa como (x′,y′)=(sx⋅x,sy⋅y),
 * donde (x,y) son las coordenadas originales, y sx y sy son los factores de escala en las direcciones x e y, respectivamente.
 * </li>
 * </ol>
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
     * En matematicas, tambien encontraras el termino <b>norma</b> para definir una funcion que asigna una longitud o tamaño (o
     * distancia) a un vector. Por ejemplo, la funcion que acabamos de describir se llama <b>norma euclidiana</b>.
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
     * esta operacion se relaciona con el coseno del angulo entre los dos vectores. Puede usarse para muchas cosas, por ejemplo,
     * como prueba de ortogonalidad. Cuando dos vectores son perpendiculares entre si, el resultado del producto escalar entre
     * estos dos vectores es 0. Cuando los dos vectores apuntan en direcciones opuestas, el producto escalar devuelve -1. Cuando
     * apuntan en la misma direccion, devuelve 1.
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
