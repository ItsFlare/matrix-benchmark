package net.durchholz.matbench;

public abstract sealed class Matrix3f extends Matrix permits ArrayMatrix3f, FieldMatrix3f {

    public static final int DIMENSION = 3;
    public static final int LENGTH = DIMENSION * DIMENSION;

    @Override
    public final int dimension() {
        return DIMENSION;
    }

    public static Matrix3f multiply(Matrix3f a, Matrix3f b) {
        switch (a) {
            case ArrayMatrix3f aa -> {
                if (b instanceof ArrayMatrix3f ab) return ArrayMatrix3f.multiply(aa, ab);
            }
            case FieldMatrix3f fa -> {
                if (b instanceof FieldMatrix3f fb) return FieldMatrix3f.multiply(fa, fb);
            }
        }

        throw new IllegalArgumentException("Matrices must be of same type");
    }
}
