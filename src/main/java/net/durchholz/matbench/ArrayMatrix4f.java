package net.durchholz.matbench;

import java.util.Arrays;

/**
 * Row-major array-backed mutable 4x4 matrix
 */
public final class ArrayMatrix4f extends Matrix4f {


    public final float[] values;

    public ArrayMatrix4f(float[] values) {
        this.values = values;
    }

    public ArrayMatrix4f() {
        this(new float[LENGTH]);
    }

    public ArrayMatrix4f(float _00, float _01, float _02, float _03,
                         float _10, float _11, float _12, float _13,
                         float _20, float _21, float _22, float _23,
                         float _30, float _31, float _32, float _33) {
        this(new float[]{_00, _01, _02, _03, _10, _11, _12, _13, _20, _21, _22, _23, _30, _31, _32, _33});
    }

    public ArrayMatrix4f(float _00, float _11, float _22, float _33) {
        this(new float[]{_00, 0, 0, 0, 0, _11, 0, 0, 0, 0, _22, 0, 0, 0, 0, _33});
    }

    public ArrayMatrix4f(float diagonal) {
        this(diagonal, diagonal, diagonal, diagonal);
    }

    public ArrayMatrix4f(ArrayMatrix4f matrix) {
        this(Arrays.copyOf(matrix.values, LENGTH));
    }

    @Override
    public float get(int row, int column) {
        return values[index(row, column)];
    }

    @Override
    public void set(int row, int column, float value) {
        values[index(row, column)] = value;
    }

    @Override
    public void scale(float factor) {
        for (int i = 0; i < values.length; i++) {
            values[i] *= factor;
        }
    }

    @Override
    public void invert() {
        float determinant = determinant();
        if (determinant == 0) throw new ArithmeticException("Not invertible");

        //Construct cofactors for adjugate
        float t00 =  new ArrayMatrix3f(values[5], values[6], values[7], values[9], values[10], values[11], values[13], values[14], values[15]).determinant();
        float t10 = -new ArrayMatrix3f(values[1], values[2], values[3], values[9], values[10], values[11], values[13], values[14], values[15]).determinant();
        float t20 =  new ArrayMatrix3f(values[1], values[2], values[3], values[5], values[6],  values[7],  values[13], values[14], values[15]).determinant();
        float t30 = -new ArrayMatrix3f(values[1], values[2], values[3], values[5], values[6],  values[7],  values[9],  values[10], values[11]).determinant();
        float t01 = -new ArrayMatrix3f(values[4], values[6], values[7], values[8], values[10], values[11], values[12], values[14], values[15]).determinant();
        float t11 =  new ArrayMatrix3f(values[0], values[2], values[3], values[8], values[10], values[11], values[12], values[14], values[15]).determinant();
        float t21 = -new ArrayMatrix3f(values[0], values[2], values[3], values[4], values[6],  values[7],  values[12], values[14], values[15]).determinant();
        float t31 =  new ArrayMatrix3f(values[0], values[2], values[3], values[4], values[6],  values[7],  values[8],  values[10], values[11]).determinant();
        float t02 =  new ArrayMatrix3f(values[4], values[5], values[7], values[8], values[9],  values[11], values[12], values[13], values[15]).determinant();
        float t12 = -new ArrayMatrix3f(values[0], values[1], values[3], values[8], values[9],  values[11], values[12], values[13], values[15]).determinant();
        float t22 =  new ArrayMatrix3f(values[0], values[1], values[3], values[4], values[5],  values[7],  values[12], values[13], values[15]).determinant();
        float t32 = -new ArrayMatrix3f(values[0], values[1], values[3], values[4], values[5],  values[7],  values[8],  values[9],  values[11]).determinant();
        float t03 = -new ArrayMatrix3f(values[4], values[5], values[6], values[8], values[9],  values[10], values[12], values[13], values[14]).determinant();
        float t13 =  new ArrayMatrix3f(values[0], values[1], values[2], values[8], values[9],  values[10], values[12], values[13], values[14]).determinant();
        float t23 = -new ArrayMatrix3f(values[0], values[1], values[2], values[4], values[5],  values[6],  values[12], values[13], values[14]).determinant();
        float t33 =  new ArrayMatrix3f(values[0], values[1], values[2], values[4], values[5],  values[6],  values[8],  values[9],  values[10]).determinant();

        //Reciprocal for cramer's rule
        determinant = 1f / determinant;

        values[0]  = t00 * determinant;
        values[1]  = t10 * determinant;
        values[2]  = t20 * determinant;
        values[3]  = t30 * determinant;
        values[4]  = t01 * determinant;
        values[5]  = t11 * determinant;
        values[6]  = t21 * determinant;
        values[7]  = t31 * determinant;
        values[8]  = t02 * determinant;
        values[9]  = t12 * determinant;
        values[10] = t22 * determinant;
        values[11] = t32 * determinant;
        values[12] = t03 * determinant;
        values[13] = t13 * determinant;
        values[14] = t23 * determinant;
        values[15] = t33 * determinant;
    }

    @Override
    public void transpose() {
        float f;

        f = values[4];
        values[4] = values[1];
        values[1] =  f;

        f = values[8];
        values[8] = values[2];
        values[2] =  f;

        f = values[12];
        values[12] = values[3];
        values[3] =  f;

        f = values[9];
        values[9] = values[6];
        values[6] =  f;

        f = values[13];
        values[13] = values[7];
        values[7] =  f;

        f = values[14];
        values[14] = values[11];
        values[11] =  f;
    }

    @Override
    public float determinant() {
        float f;
        f  = values[0] * (values[5] * values[10] * values[15] + values[6] * values[11] * values[13] + values[7] * values[9] * values[14] - values[7] * values[10] * values[13] - values[5] * values[11] * values[14] - values[6] * values[9] * values[15]);
        f -= values[1] * (values[4] * values[10] * values[15] + values[6] * values[11] * values[12] + values[7] * values[8] * values[14] - values[7] * values[10] * values[12] - values[4] * values[11] * values[14] - values[6] * values[8] * values[15]);
        f += values[2] * (values[4] * values[9]  * values[15] + values[5] * values[11] * values[12] + values[7] * values[8] * values[13] - values[7] * values[9]  * values[12] - values[4] * values[11] * values[13] - values[5] * values[8] * values[15]);
        f -= values[3] * (values[4] * values[9]  * values[14] + values[5] * values[10] * values[12] + values[6] * values[8] * values[13] - values[6] * values[9]  * values[12] - values[4] * values[10] * values[13] - values[5] * values[8] * values[14]);
        return f;
    }

    @Override
    public float[] array() {
        return values;
    }

    @Override
    public ArrayMatrix4f copy() {
        return new ArrayMatrix4f(this);
    }

    @Override
    public String toString() {
        return String.format("""
                        %f %f %f %f
                        %f %f %f %f
                        %f %f %f %f
                        %f %f %f %f
                        """,
                values[0],  values[1],  values[2],  values[3],
                values[4],  values[5],  values[6],  values[7],
                values[8],  values[9],  values[10], values[11],
                values[12], values[13], values[14], values[15]
        );
    }

    public static ArrayMatrix4f multiply(ArrayMatrix4f a, ArrayMatrix4f b) {
        final ArrayMatrix4f m = new ArrayMatrix4f();

        m.values[0]  = a.values[0]  * b.values[0] + a.values[1]  * b.values[4] + a.values[2]  * b.values[8]  + a.values[3]  * b.values[12];
        m.values[1]  = a.values[0]  * b.values[1] + a.values[1]  * b.values[5] + a.values[2]  * b.values[9]  + a.values[3]  * b.values[13];
        m.values[2]  = a.values[0]  * b.values[2] + a.values[1]  * b.values[6] + a.values[2]  * b.values[10] + a.values[3]  * b.values[14];
        m.values[3]  = a.values[0]  * b.values[3] + a.values[1]  * b.values[7] + a.values[2]  * b.values[11] + a.values[3]  * b.values[15];

        m.values[4]  = a.values[4]  * b.values[0] + a.values[5]  * b.values[4] + a.values[6]  * b.values[8]  + a.values[7]  * b.values[12];
        m.values[5]  = a.values[4]  * b.values[1] + a.values[5]  * b.values[5] + a.values[6]  * b.values[9]  + a.values[7]  * b.values[13];
        m.values[6]  = a.values[4]  * b.values[2] + a.values[5]  * b.values[6] + a.values[6]  * b.values[10] + a.values[7]  * b.values[14];
        m.values[7]  = a.values[4]  * b.values[3] + a.values[5]  * b.values[7] + a.values[6]  * b.values[11] + a.values[7]  * b.values[15];

        m.values[8]  = a.values[8]  * b.values[0] + a.values[9]  * b.values[4] + a.values[10] * b.values[8]  + a.values[11] * b.values[12];
        m.values[9]  = a.values[8]  * b.values[1] + a.values[9]  * b.values[5] + a.values[10] * b.values[9]  + a.values[11] * b.values[13];
        m.values[10] = a.values[8]  * b.values[2] + a.values[9]  * b.values[6] + a.values[10] * b.values[10] + a.values[11] * b.values[14];
        m.values[11] = a.values[8]  * b.values[3] + a.values[9]  * b.values[7] + a.values[10] * b.values[11] + a.values[11] * b.values[15];

        m.values[12] = a.values[12] * b.values[0] + a.values[13] * b.values[4] + a.values[14] * b.values[8]  + a.values[15] * b.values[12];
        m.values[13] = a.values[12] * b.values[1] + a.values[13] * b.values[5] + a.values[14] * b.values[9]  + a.values[15] * b.values[13];
        m.values[14] = a.values[12] * b.values[2] + a.values[13] * b.values[6] + a.values[14] * b.values[10] + a.values[15] * b.values[14];
        m.values[15] = a.values[12] * b.values[3] + a.values[13] * b.values[7] + a.values[14] * b.values[11] + a.values[15] * b.values[15];

        return m;
    }

    private static int index(int row, int column) {
        return (row << 2) + column;
    }

}
