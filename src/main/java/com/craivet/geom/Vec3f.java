package com.craivet.geom;

public class Vec3f {

    public float x, y, z;

    public Vec3f(float v, float v1, float v2) {

    }


    public static Vec3f translate(Vec3f P, Vec3f translateValue) {
        return new Vec3f(P.x + translateValue.x, P.y + translateValue.y, P.z + translateValue.z);
    }

    public static Vec3f scale(Vec3f P, Vec3f scaleValue) {
        return new Vec3f(P.x * scaleValue.x, P.y * scaleValue.y, P.z * scaleValue.z);
    }

    public static Vec3f rotate(Vec3f P, Vec3f axis, float angle) {
        // Implementa la rotación según tus necesidades específicas
        // Puedes utilizar funciones trigonométricas como sin() y cos() para la rotación
        // Aquí se asume rotación en el eje x como un ejemplo
        float cosA = (float) Math.cos(Math.toRadians(angle));
        float sinA = (float) Math.sin(Math.toRadians(angle));

        float newY = cosA * P.y - sinA * P.z;
        float newZ = sinA * P.y + cosA * P.z;

        return new Vec3f(P.x, newY, newZ);
    }

    public static void main(String[] args) {
        Vec3f P = new Vec3f(1, 1, 1);
        Vec3f translateVal = new Vec3f(-1, 2, 4);
        Vec3f scaleVal = new Vec3f(1, 1, 2);
        Vec3f axis = new Vec3f(1, 0, 0);
        float angle = 90;
        Vec3f Pt;

        Pt = translate(P, translateVal);  // Translada P
        Pt = scale(Pt, scaleVal);         // Luego escala el resultado
        Pt = rotate(Pt, axis, angle);     // Finalmente rota el punto
    }

}

