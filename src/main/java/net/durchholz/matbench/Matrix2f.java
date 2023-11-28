package net.durchholz.matbench;

public abstract sealed class Matrix2f extends Matrix permits ArrayMatrix2f, FieldMatrix2f {

    public static final int DIMENSION = 2;
    public static final int LENGTH = DIMENSION * DIMENSION;

    @Override
    public final int dimension() {
        return DIMENSION;
    }

    public static Matrix2f multiply(Matrix2f a, Matrix2f b) {
        switch (a) {
            case ArrayMatrix2f aa -> {
                if (b instanceof ArrayMatrix2f ab) return ArrayMatrix2f.multiply(aa, ab);
            }
            case FieldMatrix2f fa -> {
                if (b instanceof FieldMatrix2f fb) return FieldMatrix2f.multiply(fa, fb);
            }
        }

        throw new IllegalArgumentException("Matrices must be of same type");
    }
}
