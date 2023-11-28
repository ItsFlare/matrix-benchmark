package net.durchholz.matbench;

public final class FieldMatrix3f extends Matrix3f {

    private float _00, _01, _02, _10, _11, _12, _20, _21, _22;

    public FieldMatrix3f() {

    }

    public FieldMatrix3f(float _00, float _01, float _02, float _10, float _11, float _12, float _20, float _21, float _22) {
        this._00 = _00;
        this._01 = _01;
        this._02 = _02;

        this._10 = _10;
        this._11 = _11;
        this._12 = _12;

        this._20 = _20;
        this._21 = _21;
        this._22 = _22;
    }

    public FieldMatrix3f(float _00, float _11, float _22) {
        this._00 = _00;
        this._11 = _11;
        this._22 = _22;
    }

    public FieldMatrix3f(float diagonal) {
        this(diagonal, diagonal, diagonal);
    }

    public FieldMatrix3f(FieldMatrix3f matrix) {
        this._00 = matrix._00;
        this._01 = matrix._01;
        this._02 = matrix._02;

        this._10 = matrix._10;
        this._11 = matrix._11;
        this._12 = matrix._12;

        this._20 = matrix._20;
        this._21 = matrix._21;
        this._22 = matrix._22;
    }

    @Override
    public float get(int row, int column) {
        return switch (index(row, column)) {

            case 0 ->  _00;
            case 1 ->  _01;
            case 2 ->  _02;

            case 3 ->  _10;
            case 4 ->  _11;
            case 5 ->  _12;

            case 6 ->  _20;
            case 7 ->  _21;
            case 8 -> _22;

            default -> throw new IllegalStateException("(Row %d | Column %d) is out of bounds".formatted(row, column));
        };
    }

    @Override
    public void set(int row, int column, float value) {
        switch (index(row, column)) {

            case 0 ->  _00 = value;
            case 1 ->  _01 = value;
            case 2 ->  _02 = value;

            case 3 ->  _10 = value;
            case 4 ->  _11 = value;
            case 5 ->  _12 = value;

            case 6 ->  _20 = value;
            case 7 ->  _21 = value;
            case 8 -> _22 = value;

            default -> throw new IllegalStateException("(Row %d | Column %d) is out of bounds".formatted(row, column));
        }
    }

    @Override
    public void scale(float factor) {
        _00 *= factor;
        _01 *= factor;
        _02 *= factor;

        _10 *= factor;
        _11 *= factor;
        _12 *= factor;

        _20 *= factor;
        _21 *= factor;
        _22 *= factor;
    }

    @Override
    public float determinant() {
        return _00 * (_11 * _22 - _12 * _21)
                + _01 * (_12 * _20 - _10 * _22)
                + _02 * (_10 * _21 - _11 * _20);
    }

    @Override
    public void invert() {
        float determinant = determinant();
        if (determinant == 0) throw new ArithmeticException("Not invertible");

        //Construct cofactors for adjugate
        float t00 =  new FieldMatrix2f(_11, _12, _21, _22).determinant();
        float t10 = -new FieldMatrix2f(_01, _02, _21, _22).determinant();
        float t20 =  new FieldMatrix2f(_01, _02, _11, _12).determinant();
        float t01 = -new FieldMatrix2f(_10, _12, _20, _22).determinant();
        float t11 =  new FieldMatrix2f(_00, _02, _20, _22).determinant();
        float t21 = -new FieldMatrix2f(_00, _02, _10, _12).determinant();
        float t02 =  new FieldMatrix2f(_10, _11, _20, _21).determinant();
        float t12 = -new FieldMatrix2f(_00, _01, _20, _21).determinant();
        float t22 =  new FieldMatrix2f(_00, _01, _10, _11).determinant();

        //Reciprocal for cramer's rule
        determinant = 1f / determinant;

        _00 = t00 * determinant;
        _01 = t10 * determinant;
        _02 = t20 * determinant;
        _10 = t01 * determinant;
        _11 = t11 * determinant;
        _12 = t21 * determinant;
        _20 = t02 * determinant;
        _21 = t12 * determinant;
        _22 = t22 * determinant;
    }

    @Override
    public void transpose() {
        float f;

        f = _10;
        _10 = _01;
        _01 =  f;

        f = _20;
        _20 = _02;
        _02 =  f;

        f = _21;
        _21 = _12;
        _12 =  f;
    }

    @Override
    public float[] array() {
        return new float[] {_00, _01, _02, _10, _11, _12, _20, _21, _22};
    }

    @Override
    public FieldMatrix3f copy() {
        return new FieldMatrix3f(this);
    }

    @Override
    public String toString() {
        return String.format("""
                        %f %f %f
                        %f %f %f
                        %f %f %f
                        """,
                _00, _01, _02,
                _10, _11, _12,
                _20, _21, _22
        );
    }


    public static FieldMatrix3f multiply(FieldMatrix3f a, FieldMatrix3f b) {
        final FieldMatrix3f m = new FieldMatrix3f();

        m._00 = a._00 * b._00 + a._01 * b._10 + a._02 * b._20;
        m._01 = a._00 * b._01 + a._01 * b._11 + a._02 * b._21;
        m._02 = a._00 * b._02 + a._01 * b._12 + a._02 * b._22;

        m._10 = a._10 * b._00 + a._11 * b._10 + a._12 * b._20;
        m._11 = a._10 * b._01 + a._11 * b._11 + a._12 * b._21;
        m._12 = a._10 * b._02 + a._11 * b._12 + a._12 * b._22;

        m._20 = a._20 * b._00 + a._21 * b._10 + a._22 * b._20;
        m._21 = a._20 * b._01 + a._21 * b._11 + a._22 * b._21;
        m._22 = a._20 * b._02 + a._21 * b._12 + a._22 * b._22;

        return m;
    }

    private static int index(int row, int column) {
        return row * 3 + column;
    }

}
