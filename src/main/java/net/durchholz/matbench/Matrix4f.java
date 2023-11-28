package net.durchholz.matbench;

public abstract sealed class Matrix4f extends Matrix permits ArrayMatrix4f, FieldMatrix4f {

    public static final int DIMENSION = 4;
    public static final int LENGTH = DIMENSION * DIMENSION;

    @Override
    public final int dimension() {
        return DIMENSION;
    }

    public static Matrix4f multiply(Matrix4f a, Matrix4f b) {
        switch (a) {
            case ArrayMatrix4f aa -> {
                if (b instanceof ArrayMatrix4f ab) return ArrayMatrix4f.multiply(aa, ab);
            }
            case FieldMatrix4f fa -> {
                if (b instanceof FieldMatrix4f fb) return FieldMatrix4f.multiply(fa, fb);
            }
        }

        throw new IllegalArgumentException("Matrices must be of same type");
    }
}
