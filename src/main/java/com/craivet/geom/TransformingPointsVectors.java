package com.craivet.geom;

/**
 * <h2>Puntos transformadores</h2>
 * Aunque la traslacion es el operador lineal mas sencillo que se puede aplicar a un punto, no se ha mencionado con frecuencia en
 * el capitulo anterior. Para que la traslacion funcione con la teoria de la multiplicacion de matrices, es necesario realizar
 * cambios en la estructura del punto.
 * <p>
 * Como se menciono en los dos ultimos capitulos, la multiplicacion de matrices solo funciona si las dos matrices involucradas
 * tienen un tamaño compatible, es decir, si tienen un tamaño m x p y p x n. Empecemos con una matriz identidad de 3x3. Sabemos
 * que un punto multiplicado por esta matriz mantiene sus coordenadas sin cambios. Veamos que cambios necesitamos hacer en esa
 * matriz para manejar la traslacion. La traslacion de un punto no es mas que sumar un numero a cada una de sus coordenadas (estos
 * numeros pueden ser positivos o negativos). Por ejemplo, si queremos mover el punto (1, 1, 1) a las coordenadas (2, 3, 4),
 * necesitamos sumar los valores 1, 2 y 3, respectivamente, a cada una de las coordenadas x, y, y z del punto. Es muy sencillo. A
 * partir de ahora, observaremos puntos y vectores como una matriz de tamaño 1x3.
 * <pre>{@code
 * P❜.x = P.x + Tx
 * P❜.y = P.y + Ty
 * P❜.z = P.z + Tz
 * }</pre>
 * Ahora volvamos al codigo que transforma un punto usando una matriz:
 * <pre>{@code
 * P❜.x = P.x * M00 + P.y * M10 + P.z * M20
 * P❜.y = P.x * M01 + P.y * M11 + P.z * M21
 * P❜.x = P.x * M02 + P.y * M12 + P.z * M22
 * }</pre>
 * ¿Que necesitamos para extender la matriz de rotacion para manejar tambien la traduccion? Necesitamos tener un cuarto termino a
 * la derecha para codificar la traduccion. Algo como esto:
 * <pre>{@code
 * P❜.x = P.x * M00 + P.y * M10 + P.z * M20 + Tx
 * P❜.y = P.x * M01 + P.y * M11 + P.z * M21 + Ty
 * P❜.x = P.x * M02 + P.y * M12 + P.z * M22 + Tz
 * }</pre>
 * Recuerde que queremos desarrollar una matriz que codifique escala, rotacion y traduccion. Entonces, necesitamos hacer que Tx,
 * Ty y Tz encajen dentro del codigo de la multiplicacion de la matriz de puntos (y almacenar estos valores en algun lugar de la
 * matriz). Mire la primera linea por ahora. Tenga en cuenta que para calcular x', solo utilizamos los coeficientes de la primera
 * columna de la matriz. Entonces, si la columna tuviera cuatro coeficientes en lugar de tres, Tx seria M30 . Se puede hacer el
 * mismo razonamiento con Ty y Tz. Entonces obtendriamos lo siguiente:
 * <pre>{@code
 * P❜.x = P.x * M00 + P.y * M10 + P.z * M20 + M30
 * P❜.y = P.x * M01 + P.y * M11 + P.z * M21 + M31
 * P❜.x = P.x * M02 + P.y * M12 + P.z * M22 + M32
 * }</pre>
 * Sin embargo, esto supone que nuestra matriz ahora tiene el tamaño 4x3 y no 3x3. Esto esta bien. Dijimos que las matrices podian
 * tener cualquier tamaño. Sin embargo, sabemos que la multiplicacion de matrices solo puede ser valida si sus tamaños son
 * compatibles. Pero ahora intentamos multiplicar un punto como una matriz de 1x3 por una matriz de 4x3, y la teoria nos dice que
 * esto es imposible. ¿Que haremos? La solucion es sencilla. Agregaremos una columna adicional al punto para convertirlo en una
 * matriz de 1x4 y estableceremos el cuarto coeficiente de este punto en 1. Nuestro punto ahora se ve asi (x, y, z, 1). En graficos
 * por computadora, se llama <b>punto homogeneo</b> (o punto con <b>coordenadas homogeneas</b>). Con tal punto, podemos codificar
 * facilmente la traduccion en nuestra matriz. Vea como magicamente encaja en su lugar en el siguiente codigo:
 * <pre>{@code
 * P❜.x = P.x * M00 + P.y * M10 + P.z * M20 + 1 * M30
 * P❜.y = P.x * M01 + P.y * M11 + P.z * M21 + 1 * M31
 * P❜.x = P.x * M02 + P.y * M12 + P.z * M22 + 1 * M32
 * }</pre>
 * Esta es la teoria. Para codificar traslacion, escala y rotacion en una matriz, necesitamos tratar con puntos que tengan
 * coordenadas homogeneas. Pero como el cuarto valor es siempre 1, no lo definimos explicitamente en el codigo. En cambio, solo
 * definimos x, y y z y asumimos que hay un cuarto valor. El codigo de matriz de puntos ahora tiene este aspecto:
 * <pre>{@code
 * P❜.x = P.x * M00 + P.y * M10 + P.z * M20 * M30
 * P❜.y = P.x * M01 + P.y * M11 + P.z * M21 * M31
 * P❜.x = P.x * M02 + P.y * M12 + P.z * M22 * M32
 * }</pre>
 * Nuestra matriz es ahora una matriz de 4x3. Pasemos de una matriz de 4x3 a nuestra matriz final de 4x4, la forma mas comunmente
 * utilizada del CG. Las cuartas columnas desempeñan un papel en la proyeccion en perspectiva y para algunos otros tipos de
 * transformaciones que no son muy comunes (como la transformacion de corte), pero generalmente se establecen en (0, 0, 0, 1). ¿Que
 * sucede cuando el coeficiente de esta columna tiene valores diferentes a los predeterminados (dijimos que es poco comun pero
 * sucede a veces)? Antes de responder a esta pregunta, primero debemos aprender algunas cosas mas sobre los puntos homogeneos.
 * <br><br>
 * <h2>El truco de los puntos homogeneos</h2>
 * Se presenta un punto como un punto homogeneo para permitir la multiplicacion por matrices [4x4]. Aunque en el codigo esto se
 * hace implicitamente con w siempre igual a 1, la clase Point C++ solo define el tipo de punto con tres flotantes (x, y, z). En
 * una multiplicacion de un punto homogeneo por una matriz [4x4], la coordenada w del punto transformado se obtiene multiplicando
 * las coordenadas del punto por los coeficientes de la cuarta columna de la matriz. Normalmente, esta columna es (0, 0, 0, 1), lo
 * que hace que w' sea 1, y las coordenadas transformadas x', y', y z' son utilizables directamente. Sin embargo, en matrices de
 * proyeccion, la cuarta columna puede variar, y en estos casos excepcionales, el resultado para w' puede ser diferente de 1. Para
 * utilizar el punto como un punto cartesiano, es necesario normalizar w' dividiendolo por si mismo, lo que implica dividir las
 * otras coordenadas (x', y', z') por w'. En pseudocodigo, da algo como esto:
 * <pre>{@code
 * P❜.x = P.x * M00 + P.y * M10 + P.z * M20 + M30;
 * P❜.y = P.x * M01 + P.y * M11 + P.z * M21 + M31;
 * P❜.z = P.x * M02 + P.y * M12 + P.z * M22 + M32;
 * w❜   = P.x * M03 + P.y * M13 + P.z * M23 + M33;
 * if (w❜ != 1 && w❜ != 0) {
 *     P❜.x /= w❜, P❜.y /= w❜, P❜.z /= w❜;
 * }
 * }</pre>
 * Al transformar un punto con matrices, no es necesario declarar explicitamente la coordenada w' en el tipo de punto. Se asume
 * que el punto es cartesiano y se representa como un punto homogeneo con una coordenada w' implicita siempre igual a 1. No
 * obstante, si la matriz de transformacion es de proyeccion, la coordenada resultante w' podria ser diferente de 1. En este
 * caso, es necesario normalizar las coordenadas de P' para que vuelva a ser 1. En general, no es común preocuparse por las
 * coordenadas homogeneas, excepto cuando se multiplican por matrices de proyeccion en perspectiva. Sin embargo, en el trazado de
 * rayos, que no utiliza este tipo de matriz, es poco probable que encuentre este problema.
 * <br><br>
 * <h2>Transformar vectores</h2>
 * Los vectores son mas simples de transformar que los puntos, ya que representan la direccion sin tener una posicion definida en
 * el espacio. Dado que la posicion de los vectores no tiene sentido, no es necesario traducirlos. Nos interesa principalmente la
 * direccion y, ocasionalmente, su longitud para resolver problemas geometricos o de sombreado. Aunque los vectores se pueden
 * transformar de manera similar a los puntos, podemos eliminar la parte del codigo responsable de la traduccion. El codigo
 * utilizado para transformar vectores se presenta para su comparacion con el codigo de transformacion de puntos.
 * <pre>{@code
 * V❜.x = V.x * M00 + V.y * M10 + V.z * M20;
 * V❜.y = V.x * M01 + V.y * M11 + V.z * M21;
 * V❜.z = V.x * M02 + V.y * M12 + V.z * M22;
 * }</pre>
 * <h2>Resumen</h2>
 * Este capitulo nos enseña por que utilizamos matrices [4x4] en lugar de [3x3]. los coeficientes C30, C31 y C32 mantener los
 * valores de traduccion. Ahora que la matriz tiene un tamaño [4x4], necesitamos ampliar el tamaño del punto agregando una
 * coordenada adicional. Podemos hacer esto tratando implicitamente los puntos como puntos homogeneos, pero para continuar
 * usandolos en un sistema de coordenadas cartesiano (como puntos cartesianos), debemos asegurarnos de que w, esta cuarta
 * coordenada, siempre este establecida en 1. La mayoria de las veces, las matrices que usamos para transformar un punto tendran
 * su cuarta columna establecida en (0, 0, 0, 1), y con estas matrices, el valor de w' siempre debe ser 1. Sin embargo, en casos
 * especiales (matriz de proyeccion, corte transformar), el valor de w' puede ser diferente de 1, en cuyo caso necesitaras
 * normalizarlo (dividimos w' por si mismo), lo que requiere dividir tambien las otras coordenadas transformadas x', y' y z' por w'.
 */

public class TransformingPointsVectors {

}
