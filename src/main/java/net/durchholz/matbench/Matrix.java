package net.durchholz.matbench;

public abstract sealed class Matrix permits Matrix2f, Matrix3f, Matrix4f {

    public abstract float get(int row, int column);

    public abstract void set(int row, int column, float value);

    /**
     * Scales all elements of this matrix by the given factor.
     * @param factor scalar
     */
    public abstract void scale(float factor);

    public abstract float determinant();

    /**
     * Inverts this matrix if it is invertible (non-zero determinant)
     * @throws ArithmeticException if matrix is not invertible
     */
    public abstract void invert();

    public abstract void transpose();

    /**
     * Array representation in row-major order.
     * @return either the backing array or new array
     */
    public abstract float[] array();

    /**
     * Matrix dimension, the square of which equals amount of elements.
     * @return matrix dimension
     */
    public abstract int dimension();

    public abstract Matrix copy();

    public static <M extends Matrix> M multiply(M a, M b) {
        switch (a) {
            case ArrayMatrix2f aa -> {
                if (b instanceof ArrayMatrix2f ab) return (M) ArrayMatrix2f.multiply(aa, ab);
            }
            case FieldMatrix2f fa -> {
                if (b instanceof FieldMatrix2f fb) return (M) FieldMatrix2f.multiply(fa, fb);
            }
            case ArrayMatrix3f aa -> {
                if (b instanceof ArrayMatrix3f ab) return (M) ArrayMatrix3f.multiply(aa, ab);
            }
            case FieldMatrix3f fa -> {
                if (b instanceof FieldMatrix3f fb) return (M) FieldMatrix3f.multiply(fa, fb);
            }
            case ArrayMatrix4f aa -> {
                if (b instanceof ArrayMatrix4f ab) return (M) ArrayMatrix4f.multiply(aa, ab);
            }
            case FieldMatrix4f fa -> {
                if (b instanceof FieldMatrix4f fb) return (M) FieldMatrix4f.multiply(fa, fb);
            }
        }

        throw new IllegalArgumentException("Matrices must be of same type");
    }
}
