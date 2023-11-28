package net.durchholz.matbench;

import java.util.Arrays;

public final class ArrayMatrix2f extends Matrix2f {

    private final float[] values;

    public ArrayMatrix2f(float[] values) {
        this.values = values;
    }

    public ArrayMatrix2f() {
        this(new float[LENGTH]);
    }

    public ArrayMatrix2f(float _00, float _01, float _10, float _11) {
        this(new float[]{_00, _01, _10, _11});
    }

    public ArrayMatrix2f(float _00, float _11) {
        this(new float[]{_00, 0, _11, 0});
    }

    public ArrayMatrix2f(float diagonal) {
        this(diagonal, diagonal);
    }

    public ArrayMatrix2f(ArrayMatrix2f matrix) {
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
        return (values[0] * values[3]) - (values[1] * values[2]);
    }

    @Override
    public void invert() {
        float determinant = determinant();
        if (determinant == 0) throw new ArithmeticException("Not invertible");

        determinant = 1f / determinant;

        float t00 =  values[3] * determinant;
        float t01 = -values[2] * determinant;
        float t10 = -values[1] * determinant;
        float t11 =  values[0] * determinant;

        values[0] = t00;
        values[1] = t01;
        values[2] = t10;
        values[3] = t11;
    }

    @Override
    public void transpose() {
        float f;

        f = values[2];
        values[2] = values[1];
        values[1] =  f;
    }

    @Override
    public float[] array() {
        return values;
    }

    @Override
    public ArrayMatrix2f copy() {
        return new ArrayMatrix2f(this);
    }

    @Override
    public String toString() {
        return String.format("""
                        %f %f
                        %f %f
                        """,
                values[0], values[1],
                values[2], values[3]
        );
    }

    public static ArrayMatrix2f multiply(ArrayMatrix2f a, ArrayMatrix2f b) {
        final ArrayMatrix2f m = new ArrayMatrix2f();

        m.values[0]  = a.values[0] * b.values[0] + a.values[1] * b.values[2];
        m.values[1]  = a.values[0] * b.values[1] + a.values[1] * b.values[3];

        m.values[2]  = a.values[2] * b.values[0] + a.values[3] * b.values[2];
        m.values[3]  = a.values[2] * b.values[1] + a.values[3] * b.values[3];

        return m;
    }

    private static int index(int row, int column) {
        return (row << 1) + column;
    }

}
