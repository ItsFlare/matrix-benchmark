package net.durchholz.matbench;

public final class FieldMatrix2f extends Matrix2f {

    private float _00, _01, _10, _11;

    public FieldMatrix2f() {

    }

    public FieldMatrix2f(float _00, float _01, float _10, float _11) {
        this._00 = _00;
        this._01 = _01;

        this._10 = _10;
        this._11 = _11;
    }

    public FieldMatrix2f(float _00, float _11) {
        this._00 = _00;
        this._11 = _11;
    }

    public FieldMatrix2f(float diagonal) {
        this(diagonal, diagonal);
    }

    public FieldMatrix2f(FieldMatrix2f matrix) {
        this._00 = matrix._00;
        this._01 = matrix._01;

        this._10 = matrix._10;
        this._11 = matrix._11;
    }

    @Override
    public float get(int row, int column) {
        return switch (index(row, column)) {

            case 0 ->  _00;
            case 1 ->  _01;

            case 2 ->  _10;
            case 3 ->  _11;

            default -> throw new IllegalStateException("(Row %d | Column %d) is out of bounds".formatted(row, column));
        };
    }

    @Override
    public void set(int row, int column, float value) {
        switch (index(row, column)) {

            case 0 ->  _00 = value;
            case 1 ->  _01 = value;

            case 2 ->  _10 = value;
            case 3 ->  _11 = value;

            default -> throw new IllegalStateException("(Row %d | Column %d) is out of bounds".formatted(row, column));
        }
    }

    @Override
    public void scale(float factor) {
        _00 *= factor;
        _01 *= factor;

        _10 *= factor;
        _11 *= factor;
    }

    @Override
    public float determinant() {
        return _00 * _11 - _01 * _10;
    }

    @Override
    public void invert() {
        float determinant = determinant();
        if (determinant == 0) throw new ArithmeticException("Not invertible");

        determinant = 1f / determinant;

        float t00 =  _11 * determinant;
        float t01 = -_10 * determinant;
        float t10 = -_01 * determinant;
        float t11 =  _00 * determinant;

        _00 = t00;
        _01 = t01;
        _10 = t10;
        _11 = t11;
    }

    @Override
    public void transpose() {
        float f;

        f = _10;
        _10 = _01;
        _01 =  f;
    }

    @Override
    public float[] array() {
        return new float[] {_00, _01, _10, _11};
    }

    @Override
    public FieldMatrix2f copy() {
        return new FieldMatrix2f(this);
    }

    @Override
    public String toString() {
        return String.format("""
                        %f %f
                        %f %f
                        """,
                _00, _01,
                _10, _11
        );
    }

    public static FieldMatrix2f multiply(FieldMatrix2f a, FieldMatrix2f b) {
        final FieldMatrix2f m = new FieldMatrix2f();

        m._00 = a._00 * b._00 + a._01 * b._10;
        m._01 = a._00 * b._01 + a._01 * b._11;

        m._10 = a._10 * b._00 + a._11 * b._10;
        m._11 = a._10 * b._01 + a._11 * b._11;

        return m;
    }

    private static int index(int row, int column) {
        return (row << 1) + column;
    }
}
