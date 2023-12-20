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
 * <h2>Multiplicacion de matrices</h2>
 * Las matrices se pueden multiplicar entre si, y esta operacion es el nucleo del proceso de transformacion de matrices puntuales
 * o vectoriales. El resultado de la multiplicacion de matrices (el termino tecnico es producto matricial, el producto de dos
 * matrices) es otra matriz: M3 = M1 * M2
 * <p>
 * Cuando se sabe como se obtiene la multiplicacion de dos matrices, no es dificil observar que la multiplicacion de M1 por M2 da
 * un resultado diferente al de la multiplicacion de M2 por M1. De hecho, la multiplicacion de matrices no es <b>conmutativa</b>.
 * Entonces M1 * M2 da un resultado diferente que M2 * M1.
 * <br><br>
 * <h2>Multiplicacion de matriz de Puntos</h2>
 * Un punto o un vector es una secuencia de tres numeros, y por este motivo tambien se pueden escribir como una matriz de 1x3, una
 * matriz con una fila y tres columnas. A continuacion se muestra un ejemplo de un punto escrito en forma matricial:
 * <p>
 * P = [xyz]
 * <p>
 * El truco aqui es que si podemos escribir puntos y vectores como matrices [1x3], podemos multiplicarlos por otras matrices.
 * Recuerde que la matriz <b>m x p</b> se puede multiplicar por la matriz <b>p x n</b> para obtener la matriz <b>m x n</b>. Si la
 * primera matriz es un punto, podemos escribir m = 1 y p = 3. Esto implica que la matriz <b>p x n</b> es algo de la forma 3 x n
 * donde n puede ser cualquier numero mayor que 1. En teoria, una multiplicacion de a [1x3] matriz por cualquiera de las
 * siguientes matrices funcionaria: [3x1], [3x2], [3x3], [3x4], etc.
 * <p>
 * Necesitamos recordar dos cosas ahora para que lo que explicaremos tenga sentido. Primero, un punto multiplicado por una matriz
 * transforma el punto a una nueva posicion. Por tanto, el resultado de un punto multiplicado por una matriz tiene que ser un punto.
 * Si no fuera asi, no estariamos usando matrices como una forma conveniente de transformar puntos. Debemos recordar que una matriz
 * de <b>m x p</b> multiplicada por una matriz de <b>p x n</b> da una matriz de <b>m x n</b>. Si miramos nuestro punto como una
 * matriz de 1x3, necesitamos que el resultado de la multiplicacion sea otro punto, una matriz de 1x3. Por lo tanto, requiere que
 * la matriz con la que multiplicaremos el punto sea una matriz de 3x3. Multiplicar una matriz de 1x3 por una matriz de 3x3 da,
 * como se esperaba, una matriz de 1x3 que es otro punto. Asi es como se ve esta multiplicacion:
 * <pre>{@code
 *           c00 c01 c02
 * [x y z] * c10 c11 c12
 *           c20 c21 c22
 * }</pre>
 * En CG, normalmente usamos matrices de 4x4 en lugar de matrices de 3x3, y pronto explicaremos por que, pero por ahora, sigamos
 * con las matrices de 3x3 por un tiempo. Para finalizar esta seccion del capitulo, escribiremos un pseudocodigo para mostrar como
 * podemos multiplicar un punto P (o un vector) en su forma matricial a una matriz de 3x3 para obtener un punto recien
 * transformado Pt. Si necesita un repaso sobre la multiplicacion de matrices, lea el capitulo anterior. Recuerde que para cada
 * coeficiente de la nueva matriz, debe multiplicar cada coeficiente de la fila actual de la matriz del lado izquierdo con su
 * coeficiente "equivalente" de la columna actual de la matriz del lado derecho y sumar los productos resultantes. En pseudocodigo,
 * da algo como esto:
 * <pre>{@code
 * // multiplica los coeficientes de la fila 1 con los coeficientes de la columna 1
 * Ptransformed.x = P.x * c00 + P.y * c10 + P.z * c20
 * // multiplica los coeficientes de la fila 1 con los coeficientes de la columna 2
 * Ptransformed.y = P.x * c01 + P.y * c11 + P.z * c21
 * // multiplica los coeficientes de la fila 1 con los coeficientes de la columna 3
 * Ptransformed.z = P.x * c02 + P.y * c12 + P.z * c22
 * }</pre>
 * <br>
 * <h2>La matriz de Identidad</h2>
 * La matriz identidad o matriz unitaria es una matriz cuadrada cuyos coeficientes son todos 0 excepto los coeficientes a lo largo
 * de la diagonal, que se establecen en 1.
 * <p>
 * El resultado de P multiplicado por la matriz identidad es P.
 * <br><br>
 * <h2>La matriz de Escala</h2>
 * Si observa el codigo de multiplicacion de matriz de puntos, puede ver que las coordenadas del punto P se multiplican
 * respectivamente por los coeficientes R00 para x, R11 para y y R22 por z. Cuando estos coeficientes se establecen en 1 (y todos
 * los demas coeficientes de la matriz se establecen en 0), obtenemos la matriz identidad. Sin embargo, cuando estos coeficientes
 * (a lo largo de la diagonal) son diferentes de 1 (ya sean menores o mayores que 1), actúan como un multiplicador en las
 * coordenadas del punto (en otras palabras, las coordenadas del punto aumentan o disminuyen en cierta cantidad). Si
 * recuerdas lo que dijimos en el capitulo sobre sistemas de coordenadas, multiplicar las coordenadas de un punto por algunos
 * números reales da como resultado escalar las coordenadas del punto.
 * <p>
 * Donde los números reales Sx, Sy y Sz son los factores de escala.
 * <pre>{@code
 * // Multiplicando P por la matriz de escala
 * Ptransformed.x = P.x * Sx + P.y * 0  + P.z * 0  = P.x * Sx
 * Ptransformed.y = P.x * 0  + P.y * Sy + P.z * 0  = P.y * Sy
 * Ptransformed.z = P.x * 0  + P.y * 0  + P.z * Sz = P.z * Sz
 * }</pre>
 * Por ejemplo, imagine un punto P con coordenadas (1, 2, 3). Entonces, si fijamos los coeficientes de la matriz de escala con
 * Sx = 1, Sy = 2 y Sz = 3, P multiplicado por esta matriz da otro punto cuyas coordenadas son (1, 4, 9).
 * <p>
 * Tenga en cuenta que si cualquiera de los coeficientes de escala en la matriz es negativo, entonces la coordenada del punto para
 * el eje correspondiente se invertira (se reflejara en el otro lado del eje).
 * <br><br>
 * <h2>La matriz de Rotacion</h2>
 * <a href="https://www.scratchapixel.com/lessons/mathematics-physics-for-computer-graphics/geometry/how-does-matrix-work-part-1.html">Mejor explicado aca</a>
 * <br><br>
 * <h2>Relacion entre matrices y sistemas de coordenadas cartesianas</h2>
 * La idea clave para comprender las matrices es que cada fila de la matriz representa un eje (o las bases) de un sistema de
 * coordenadas. Esto es fundamental porque mas adelante se aprendera como crear matrices para transformar puntos y vectores de un
 * sistema de coordenadas a otro (cambio de base). Esto se logra simplemente reemplazando las filas de la matriz con las
 * coordenadas de cada eje del sistema de coordenadas al que deseas transformar tus vectores o puntos.
 * <p>
 * Las matrices son menos misteriosas cuando se entiende que son una forma de almacenar las coordenadas de un sistema de
 * coordenadas, donde las <b>filas de la matriz son los ejes de este sistema de coordenadas</b>, a veces llamado <b>matriz de orientacion</b>.
 * <br><br>
 * <h2>Matrices Ortogonales</h2>
 * En algebra lineal, las matrices descritas en este capitulo y el anterior (las matrices de rotacion) se denominan <b>matrices
 * ortogonales</b>. Una matriz ortogonal es cuadrada, con entradas reales, cuyas columnas y filas son <b>vectores unitarios ortogonales</b>.
 * Cada fila de la matriz representa un eje de un sistema de coordenadas cartesianas, y si la matriz es una matriz de rotacion o
 * el resultado de varias multiplicadas entre si, cada fila representa un eje de longitud unitaria. Las matrices ortogonales se
 * pueden visualizar como un sistema de coordenadas cartesianas alineado inicialmente con el sistema de coordenadas mundial y
 * rotado alrededor de un eje especifico o un eje aleatorio. Una propiedad clave de las matrices ortogonales, particularmente util
 * en GC, es que la <b>traspuesta</b> de una matriz ortogonal es igual a su <b>inversa</b>.
 * <br><br>
 * <h2>Transformaciones Afines</h2>
 * Las <b>transformaciones afines</b>, a veces llamadas transformaciones de matriz, son un termino tecnico mas preciso para describir las
 * transformaciones obtenidas mediante el uso de los tipos de matrices que hemos descrito hasta ahora. En resumen, una
 * transformacion afin es aquella que conserva lineas rectas. Por ejemplo, las matrices de traslacion, rotacion y corte son todas
 * transformaciones afines, al igual que sus combinaciones. El otro tipo de transformacion estudiado en GC se llama <b>transformacion
 * proyectiva</b> (la proyeccion perspectiva es una transformacion proyectiva). Como podrias haber deducido, tales
 * transformaciones no necesariamente conservan la paralelismo entre lineas.
 * <br><br>
 * <h2>Resumen</h2>
 * En este capitulo (y en el anterior), aprendiste como crear matrices de rotacion. Ademas, te proporcionamos una forma de
 * visualizar que es una matriz: cada fila de la matriz representa un eje de un sistema de coordenadas cartesianas. La orientacion
 * (rotacion), tamaño (escala) y posicion (traslacion) de este sistema de coordenadas representan la transformacion que se
 * aplicara a los puntos cuando se multipliquen por esta matriz. La idea principal es que los puntos se definen inicialmente en un
 * sistema de coordenadas particular (llamemoslo A). Supongamos que un punto esta unido a un sistema de coordenadas local B (la
 * matriz) y movemos, rotamos y trasladamos ese sistema de coordenadas local (es decir, la matriz). En este caso, las coordenadas
 * del punto no cambiaran con respecto al sistema de coordenadas local B. El punto esta sujeto a la transformacion aplicada al
 * sistema de coordenadas local B (se mueve con el). Sin embargo, las coordenadas de ese punto cambiaran en el sistema de
 * coordenadas A. Al multiplicar el punto cuyas coordenadas estan expresadas en relacion con A por la matriz B, obtendremos las
 * nuevas coordenadas del punto en el sistema de coordenadas A.
 */

public class Matrix44 {

    private final double[][] m;

    public Matrix44() {
        // Inicializa los coeficientes de la matriz con los coeficientes de la matriz identidad
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


