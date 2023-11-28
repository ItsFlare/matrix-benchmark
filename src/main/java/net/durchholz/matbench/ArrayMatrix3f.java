package net.durchholz.matbench;

import java.util.Arrays;

/**
 * Row-major array-backed mutable 3x3 matrix
 */
public final class ArrayMatrix3f extends Matrix3f {

    private final float[] values;

    public ArrayMatrix3f(float[] values) {
        this.values = values;
    }

    public ArrayMatrix3f() {
        this(new float[LENGTH]);
    }

    public ArrayMatrix3f(float _00, float _01, float _02,
                         float _10, float _11, float _12,
                         float _20, float _21, float _22) {
        this(new float[]{_00, _01, _02, _10, _11, _12, _20, _21, _22});
    }

    public ArrayMatrix3f(float _00, float _11, float _22) {
        this(new float[]{_00, 0, 0, 0, _11, 0, 0, 0, _22});
    }

    public ArrayMatrix3f(float diagonal) {
        this(diagonal, diagonal, diagonal);
    }

    public ArrayMatrix3f(ArrayMatrix3f matrix) {
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
        for (int i = 0; i < LENGTH; i++) {
            values[i] *= factor;
        }
    }

    @Override
    public float determinant() {
        return values[0] * (values[4] * values[8] - values[5] * values[7])
             + values[1] * (values[5] * values[6] - values[3] * values[8])
             + values[2] * (values[3] * values[7] - values[4] * values[6]);
    }

    @Override
    public void invert() {
        float determinant = determinant();
        if (determinant == 0) throw new ArithmeticException("Not invertible");

        //Construct cofactors for adjugate
        float t00 =  new ArrayMatrix2f(values[4], values[5], values[7], values[8]).determinant();
        float t10 = -new ArrayMatrix2f(values[1], values[2], values[7], values[8]).determinant();
        float t20 =  new ArrayMatrix2f(values[1], values[2], values[4], values[5]).determinant();
        float t01 = -new ArrayMatrix2f(values[3], values[5], values[6], values[8]).determinant();
        float t11 =  new ArrayMatrix2f(values[0], values[2], values[6], values[8]).determinant();
        float t21 = -new ArrayMatrix2f(values[0], values[2], values[3], values[5]).determinant();
        float t02 =  new ArrayMatrix2f(values[3], values[4], values[6], values[7]).determinant();
        float t12 = -new ArrayMatrix2f(values[0], values[1], values[6], values[7]).determinant();
        float t22 =  new ArrayMatrix2f(values[0], values[1], values[3], values[4]).determinant();

        //Reciprocal for cramer's rule
        determinant = 1f / determinant;

        values[0] = t00 * determinant;
        values[1] = t10 * determinant;
        values[2] = t20 * determinant;
        values[3] = t01 * determinant;
        values[4] = t11 * determinant;
        values[5] = t21 * determinant;
        values[6] = t02 * determinant;
        values[7] = t12 * determinant;
        values[8] = t22 * determinant;
    }

    @Override
    public void transpose() {
        float f;

        f = values[3];
        values[3] = values[1];
        values[1] =  f;

        f = values[6];
        values[6] = values[2];
        values[2] =  f;

        f = values[7];
        values[7] = values[5];
        values[5] =  f;
    }

    @Override
    public float[] array() {
        return values;
    }


    @Override
    public ArrayMatrix3f copy() {
        return new ArrayMatrix3f(this);
    }

    @Override
    public String toString() {
        return String.format("""
                        %f %f %f
                        %f %f %f
                        %f %f %f
                        """,
                values[0], values[1], values[2],
                values[3], values[4], values[5],
                values[6], values[7], values[8]
        );
    }

    public static ArrayMatrix3f multiply(ArrayMatrix3f a, ArrayMatrix3f b) {
        final ArrayMatrix3f m = new ArrayMatrix3f();

        m.values[0]  = a.values[0] * b.values[0] + a.values[1] * b.values[3] + a.values[2] * b.values[6];
        m.values[1]  = a.values[0] * b.values[1] + a.values[1] * b.values[4] + a.values[2] * b.values[7];
        m.values[2]  = a.values[0] * b.values[2] + a.values[1] * b.values[5] + a.values[2] * b.values[8];

        m.values[3]  = a.values[3] * b.values[0] + a.values[4] * b.values[3] + a.values[5] * b.values[6];
        m.values[4]  = a.values[3] * b.values[1] + a.values[4] * b.values[4] + a.values[5] * b.values[7];
        m.values[5]  = a.values[3] * b.values[2] + a.values[4] * b.values[5] + a.values[5] * b.values[8];

        m.values[6]  = a.values[6] * b.values[0] + a.values[7] * b.values[3] + a.values[8] * b.values[6];
        m.values[7]  = a.values[6] * b.values[1] + a.values[7] * b.values[4] + a.values[8] * b.values[7];
        m.values[8]  = a.values[6] * b.values[2] + a.values[7] * b.values[5] + a.values[8] * b.values[8];

        return m;
    }

    private static int index(int row, int column) {
        return row * 3 + column;
    }

}
