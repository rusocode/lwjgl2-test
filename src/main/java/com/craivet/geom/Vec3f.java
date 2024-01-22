package com.craivet.geom;

/**
 * <h2>Introduccion a las matrices: ¡facilitan las transformaciones!</h2>
 * Las matrices desempeñan un papel fundamental en el proceso de graficos y vera que se utilizan con regularidad en el codigo de
 * las aplicaciones 3D.
 * <p>
 * En la clase Vec3 se menciono que era posible trasladar o rotar puntos usando operadores lineales. Por ejemplo, demostramos
 * que podiamos traducir un punto agregando algunos valores a sus coordenadas. Tambien demostramos que era posible
 * rotar un vector usando funciones trigonometricas. Ahora, en resumen (y esto no es una definicion matematica de lo que son las
 * matrices), una matriz combina todas estas transformaciones (escala, rotacion, traslacion) en una sola estructura. Multiplicar
 * un punto o un vector por esta estructura (la matriz) nos da un punto o vector transformado. Combinar estas transformaciones
 * significa cualquier combinacion de las siguientes transformaciones lineales: escala, rotacion y traslacion. Por ejemplo,
 * podemos crear una matriz que rotara un punto 90 grados alrededor del eje x, lo escalara en 2 a lo largo del eje z (la escala
 * aplicada al punto es (1, 1, 2)) y luego traducirlo por (-2, 3, 1). Podriamos hacer esto realizando una sucesion de
 * transformaciones lineales en un punto, pero esto potencialmente significaria escribir mucho codigo:
 * <pre>{@code
 *      Vec3f translate(Vec3f P, Vec3f translateValue) { ... }
 *      Vec3f scale(Vec3f P, Vec3f scaleValue) { ... }
 *      Vec3f rotate(Vec3f P, Vec3f axis, float angle) { ... }
 *      ...
 *      Vec3f P = Vec3f(1, 1, 1);
 *      Vec3f translateVal(-1, 2, 4);
 *      Vec3f scaleVal(1, 1, 2);
 *      Vec3f axis(1, 0, 0);
 *      float angle = 90;
 *      Vec3f Pt;
 *      Pt = translate(P, translateVal):    // Translada P
 *      Pt = scale(Pt, scaleVal);           // Luego escala el resultado
 *      Pt = rotateValue(Pt, axis, angle);  // Finalmente rota el punto
 * }</pre>
 * Este codigo podria ser mas compacto. Pero si usamos una matriz, podemos escribir:
 * <pre>{@code
 *      Matrix4f M(...);  // Establese la matriz para traslacion, rotacion y escala
 *      Vec3f P = Vec3f(1, 1, 1);
 *      Vec3f Ptranformed = P * M;  // Hacer todo a la vez, traducir, rotar, escalar
 * }</pre>
 */

public class Vec3f {
}

