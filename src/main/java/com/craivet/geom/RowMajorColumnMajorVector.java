package com.craivet.geom;

/**
 * <h2>Row Major vs Column Major Vector</h2>
 * Anteriormente en esta leccion, explicamos que los vectores (o puntos) se pueden escribir como matrices [1x3] (una fila, tres
 * columnas). Sin embargo, tambien podriamos haberlos escrito como matrices [3x1] (tres filas, una columna). Tecnicamente, estas
 * dos formas de expresar puntos y vectores como matrices son perfectamente validas, y elegir un estilo u otro es solo una
 * cuestion de convencion.
 * <pre>
 *
 * Vector escrito como matriz [1x3]: V = [x y z]
 *
 *                                       x
 * Vector escrito como matriz [3x1]: V = y
 *                                       z
 * </pre>
 * En la primera muestra ([1x3] matrix), hemos expresado nuestro vector o punto en lo que llamamos <b>orden de fila principal</b>:
 * el vector (o punto) se escribe como una fila de tres numeros. En el segundo ejemplo, decimos que los puntos o vectores se
 * escriben en <b>orden de columna principal</b>: escribimos las tres coordenadas del vector o punto verticalmente como una columna.
 * <p>
 * Recuerda que expresamos puntos y vectores como matrices para multiplicarlos por matrices de transformacion [3x3] (por
 * simplicidad, trabajaremos con matrices [3x3] en lugar de [4x4]). Tambien hemos aprendido que solo podemos multiplicar matrices
 * cuando el numero de columnas de la matriz izquierda y el numero de filas de la matriz derecha son iguales. En otras palabras,
 * las matrices [m x p] y [p x n] pueden multiplicarse entre si, pero las matrices [p x m] y [p x n] no pueden. Observa que si
 * escribimos un vector como una matriz [1x3], podemos multiplicarlo por una matriz [3x3] (asumiendo que esta matriz [3x3] esta a
 * la derecha en la multiplicacion), pero si escribimos este vector como una matriz [3x1], entonces no podemos multiplicarlo por
 * una matriz [3x3].
 * <p>
 * Para solucionar el inconveniente de multiplicar un vector como una matriz [3x1] por una matriz de transformacion [3x3], es
 * multiplicar la matriz (M) por el vector (V). En otras palabras, movemos el punto o vector hacia la derecha dentro de la
 * multiplicacion.
 * <p>
 * Tenga en cuenta que el resultado de esta operacion es un punto transformado escrito en forma de matriz [3x1]. Entonces obtenemos
 * el punto para empezar y terminamos con un punto transformado que es lo que queremos. Problema resuelto. Para resumir, cuando por
 * convencion decidimos expresar vectores o puntos en orden de fila principal ([1x3]), necesitamos poner el punto en el lado
 * izquierdo de la multiplicacion y el [3x3] en el lado derecho dentro del signo de multiplicacion. Esto se llama, en matematicas,
 * <b>left</b> o <b>pre-multiplication</b>. Si decide escribir los vectores en el orden de las columnas principales ([3x1]), la
 * matriz [3x3] debe estar en el lado izquierdo de la multiplicacion y el vector o punto en el lado derecho. Esto se llama <b>right</b>
 * o <b>post-multiplication</b>.
 * <ul>
 * <li>Row-Major order: Left o pre-multiplication = P/V * M
 * <li>Column-Major order: Right o post-multiplication = M * P/V
 * </ul>
 * Por ejemplo, sabemos como calcular el producto de dos matrices, A y B: multiplicar cada coeficiente dentro de la fila actual de
 * A por los elementos asociados dentro de la columna actual de B y sumar el resultado. Esto deberia darnos el mismo resultado ya
 * sea usando el orden principal de filas o columnas. Sin embargo, multiplicar un punto (o vector) principal de fila y columna
 * principal por la misma matriz claramente nos daria un resultado diferente. Para recuperarnos, necesitariamos <b>transponer</b>
 * la matriz [3x3] utilizada en la multiplicacion de la columna principal para asegurarnos de que x', y' y z' sean iguales.
 * <p>
 * La convencion de matriz de fila principal hace que las matrices sean mas faciles de enseñar (asi como para Maya y DirectX.
 * Tambien se definen como el estandar en las especificaciones de RenderMan). Sin embargo, algunas API 3D, como OpenGL, utilizan
 * una convencion de columnas principales.
 * <br><br>
 * <h2>Implicaciones en la codificacion: ¿Afecta el rendimiento?</h2>
 * Ahora hablaremos del impacto potencial en el rendimiento al elegir entre el orden de fila principal (row-major) y el orden de
 * columna principal (column-major) al trabajar con matrices [4x4]. El codigo de implementacion de una matriz en C++ generalmente
 * usa un arreglo bidimensional de floats (o dobles) para almacenar los 16 coeficientes de la matriz.
 * <p>
 * En el orden de fila principal, se destaca que al acceder a los elementos de la matriz en una multiplicacion de vector-matriz,
 * los elementos no se acceden secuencialmente en memoria. Esto podria degradar el rendimiento de la cache de la CPU, ya que
 * acceder a elementos de un array en un orden no secuencial no es optimo. La explicacion se relaciona con la forma en que la
 * cache funciona, buscando datos en ubicaciones cercanas en memoria.
 * <p>
 * Por otro lado, en el orden de columna principal, los coeficientes se acceden en orden secuencial, lo que aprovecha mejor el
 * mecanismo de cache de la CPU y reduce los posibles "cache misses". Sin embargo, en la practica, no se ha demostrado perdida de
 * rendimiento significativa al usar el orden de fila principal, especialmente con las optimizaciones del compilador.
 * <p>
 * Desde el punto de vista de la programacion, implementar la multiplicacion de punto o vector-matriz utilizando el orden de
 * columna principal podria ser mejor en terminos de rendimiento, pero en la practica, la eleccion entre los dos ordenes no
 * siempre impacta significativamente el rendimiento, especialmente con la optimizacion del compilador.
 * <br><br>
 * <h2>Row-major y Column-Major Order en Computacion</h2>
 * En aras de la exhaustividad, mencionemos que los terminos orden principal de fila y columna principal tambien se pueden utilizar
 * en informatica para describir como se distribuyen en la memoria los elementos de matrices multidimensionales. En orden de fila
 * principal, los elementos de una matriz multidimensional se disponen uno tras otro, de izquierda a derecha y de arriba a abajo.
 * Este es el metodo utilizado por C/C++. Por ejemplo, la matriz:
 * <pre>
 * M = 1 2 3
 *     4 5 6
 * </pre>
 * Podria escribirse en C/C++ como:
 * <pre>
 * {@code float m[2][3]={{1, 2, 3}, {4, 5, 6}}; }
 * </pre>
 * Y los elementos de esta matriz se distribuirian de forma contigua en la memoria lineal de la siguiente manera:
 * <pre>
 * {@code 1 2 3 4 5 6 }
 * </pre>
 * En el orden de columnas principales, que utilizan lenguajes como FORTRAN y MATLAB, los elementos de la matriz se almacenan en
 * la memoria de arriba a abajo y de izquierda a derecha. Usando el mismo ejemplo de matriz, los elementos de la matriz se
 * almacenarian (y se accederia a ellos) en la memoria de la siguiente manera:
 * <pre>
 * {@code 1 4 2 5 3 6 }
 * </pre>
 * La disposicion de los elementos de una matriz en la memoria es esencial, especialmente al intentar acceder a ellos mediante el
 * desplazamiento de punteros y para la optimizacion de bucles (como se explico anteriormente en este capitulo, ya que puede
 * afectar el rendimiento de la cache de la CPU). Sin embargo, al considerar solo C/C++ como nuestro lenguaje de programacion, el
 * orden column-major (aplicado a la informatica) no nos resulta relevante. Solo estamos mencionando lo que significan los
 * terminos en informatica para que seas consciente de que pueden describir dos cosas segun el contexto en el que se utilicen. En
 * matematicas, describen si tratas los vectores (o puntos) como filas de coordenadas o como columnas y en la informatica
 * representan como un lenguaje de programacion especifico almacena y accede a los elementos de las matrices multidimensionales en
 * memoria.
 * <p>
 * Detalles adicionales: OpenGL es un caso interesante en ese sentido. Cuando GL fue creado inicialmente, los desarrolladores
 * eligieron la convencion de vectores row-major. Los desarrolladores que extendieron OpenGL pensaron que deberian volver a
 * vectores column-major, lo cual hicieron. Sin embargo, por razones de compatibilidad, no quisieron cambiar el codigo para la
 * multiplicacion de puntos-matrices y decidieron cambiar en su lugar el orden en que se almacenaban los coeficientes de la matriz
 * en memoria. En otras palabras, OpenGL almacena los coeficientes en orden column-major, lo que significa que los coeficientes de
 * traslacion m03, m13 y m23 de una matriz que utiliza la convencion de vector column-major tendrian indices 13, 14 y 15 en el
 * array de float, al igual que los coeficientes de traslacion m30, m31 y m32 de una matriz que utiliza la convencion de vector row-major.
 * <br><br>
 * Fuente: <a href="https://www.scratchapixel.com/lessons/mathematics-physics-for-computer-graphics/geometry/row-major-vs-column-major-vector.html">Row Major vs Column Major Vector</a>
 */

public class RowMajorColumnMajorVector {
}
