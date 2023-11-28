package net.durchholz.matbench;

/**
 * Field-backed mutable 4x4 matrix
 */
public final class FieldMatrix4f extends Matrix4f {

    public float _00,  _01,  _02,  _03;
    public float _10,  _11,  _12,  _13;
    public float _20,  _21,  _22,  _23;
    public float _30,  _31,  _32,  _33;

    public FieldMatrix4f() {

    }

    public FieldMatrix4f(FieldMatrix4f matrix) {
        this._00 = matrix._00;
        this._01 = matrix._01;
        this._02 = matrix._02;
        this._03 = matrix._03;

        this._10 = matrix._10;
        this._11 = matrix._11;
        this._12 = matrix._12;
        this._13 = matrix._13;

        this._20 = matrix._20;
        this._21 = matrix._21;
        this._22 = matrix._22;
        this._23 = matrix._23;

        this._30 = matrix._30;
        this._31 = matrix._31;
        this._32 = matrix._32;
        this._33 = matrix._33;
    }

    public FieldMatrix4f(float _00, float _01, float _02, float _03,
                         float _10, float _11, float _12, float _13,
                         float _20, float _21, float _22, float _23,
                         float _30, float _31, float _32, float _33) {
        this._00 = _00;
        this._01 = _01;
        this._02 = _02;
        this._03 = _03;

        this._10 = _10;
        this._11 = _11;
        this._12 = _12;
        this._13 = _13;

        this._20 = _20;
        this._21 = _21;
        this._22 = _22;
        this._23 = _23;

        this._30 = _30;
        this._31 = _31;
        this._32 = _32;
        this._33 = _33;
    }

    public FieldMatrix4f(float _00, float _11, float _22, float _33) {
        this._00 = _00;
        this._11 = _11;
        this._22 = _22;
        this._33 = _33;
    }

    public FieldMatrix4f(float diagonal) {
        this(diagonal, diagonal, diagonal, diagonal);
    }

    @Override
    public float get(int row, int column) {
        return switch (index(row, column)) {

            case 0 ->  _00;
            case 1 ->  _01;
            case 2 ->  _02;
            case 3 ->  _03;            
            
            case 4 ->  _10;
            case 5 ->  _11;
            case 6 ->  _12;
            case 7 ->  _13;            
            
            case 8 ->  _20;
            case 9 ->  _21;
            case 10 -> _22;
            case 11 -> _23;    
            
            case 12 -> _30;
            case 13 -> _31;
            case 14 -> _32;
            case 15 -> _33;
            
            default -> throw new IllegalStateException("(Row %d | Column %d) is out of bounds".formatted(row, column));
        };
    }

    @Override
    public void set(int row, int column, float value) {
        switch (index(row, column)) {

            case 0 ->  _00 = value;
            case 1 ->  _01 = value;
            case 2 ->  _02 = value;
            case 3 ->  _03 = value;

            case 4 ->  _10 = value;
            case 5 ->  _11 = value;
            case 6 ->  _12 = value;
            case 7 ->  _13 = value;
            
            case 8 ->  _20 = value;
            case 9 ->  _21 = value;
            case 10 -> _22 = value;
            case 11 -> _23 = value;

            case 12 -> _30 = value;
            case 13 -> _31 = value;
            case 14 -> _32 = value;
            case 15 -> _33 = value;

            default -> throw new IllegalStateException("(Row %d | Column %d) is out of bounds".formatted(row, column));
        }
    }

    @Override
    public void scale(float factor) {
        _00 *= factor;
        _01 *= factor;
        _02 *= factor;
        _03 *= factor;

        _10 *= factor;
        _11 *= factor;
        _12 *= factor;
        _13 *= factor;

        _20 *= factor;
        _21 *= factor;
        _22 *= factor;
        _23 *= factor;

        _30 *= factor;
        _31 *= factor;
        _32 *= factor;
        _33 *= factor;
    }

    @Override
    public void invert() {
        float determinant = determinant();
        if (determinant == 0) throw new ArithmeticException("Not invertible");

        //Construct cofactors for adjugate
        float t00 =  new FieldMatrix3f(_11, _12, _13, _21, _22, _23, _31, _32, _33).determinant();
        float t10 = -new FieldMatrix3f(_01, _02, _03, _21, _22, _23, _31, _32, _33).determinant();
        float t20 =  new FieldMatrix3f(_01, _02, _03, _11, _12, _13, _31, _32, _33).determinant();
        float t30 = -new FieldMatrix3f(_01, _02, _03, _11, _12, _13, _21, _22, _23).determinant();
        float t01 = -new FieldMatrix3f(_10, _12, _13, _20, _22, _23, _30, _32, _33).determinant();
        float t11 =  new FieldMatrix3f(_00, _02, _03, _20, _22, _23, _30, _32, _33).determinant();
        float t21 = -new FieldMatrix3f(_00, _02, _03, _10, _12, _13, _30, _32, _33).determinant();
        float t31 =  new FieldMatrix3f(_00, _02, _03, _10, _12, _13, _20, _22, _23).determinant();
        float t02 =  new FieldMatrix3f(_10, _11, _13, _20, _21, _23, _30, _31, _33).determinant();
        float t12 = -new FieldMatrix3f(_00, _01, _03, _20, _21, _23, _30, _31, _33).determinant();
        float t22 =  new FieldMatrix3f(_00, _01, _03, _10, _11, _13, _30, _31, _33).determinant();
        float t32 = -new FieldMatrix3f(_00, _01, _03, _10, _11, _13, _20, _21, _23).determinant();
        float t03 = -new FieldMatrix3f(_10, _11, _12, _20, _21, _22, _30, _31, _32).determinant();
        float t13 =  new FieldMatrix3f(_00, _01, _02, _20, _21, _22, _30, _31, _32).determinant();
        float t23 = -new FieldMatrix3f(_00, _01, _02, _10, _11, _12, _30, _31, _32).determinant();
        float t33 =  new FieldMatrix3f(_00, _01, _02, _10, _11, _12, _20, _21, _22).determinant();

        //Reciprocal for cramer's rule
        determinant = 1f / determinant;

        _00 = t00 * determinant;
        _01 = t10 * determinant;
        _02 = t20 * determinant;
        _03 = t30 * determinant;
        _10 = t01 * determinant;
        _11 = t11 * determinant;
        _12 = t21 * determinant;
        _13 = t31 * determinant;
        _20 = t02 * determinant;
        _21 = t12 * determinant;
        _22 = t22 * determinant;
        _23 = t32 * determinant;
        _30 = t03 * determinant;
        _31 = t13 * determinant;
        _32 = t23 * determinant;
        _33 = t33 * determinant;
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

        f = _30;
        _30 = _03;
        _03 =  f;

        f = _21;
        _21 = _12;
        _12 =  f;

        f = _31;
        _31 = _13;
        _13 =  f;

        f = _32;
        _32 = _23;
        _23 =  f;
    }

    @Override
    public float determinant() {
        float f;
        f  = _00 * (_11 * _22 * _33 + _12 * _23 * _31 + _13 * _21 * _32 - _13 * _22 * _31 - _11 * _23 * _32 - _12 * _21 * _33);
        f -= _01 * (_10 * _22 * _33 + _12 * _23 * _30 + _13 * _20 * _32 - _13 * _22 * _30 - _10 * _23 * _32 - _12 * _20 * _33);
        f += _02 * (_10 * _21 * _33 + _11 * _23 * _30 + _13 * _20 * _31 - _13 * _21 * _30 - _10 * _23 * _31 - _11 * _20 * _33);
        f -= _03 * (_10 * _21 * _32 + _11 * _22 * _30 + _12 * _20 * _31 - _12 * _21 * _30 - _10 * _22 * _31 - _11 * _20 * _32);
        return f;
    }

    @Override
    public FieldMatrix4f copy() {
        return new FieldMatrix4f(this);
    }

    @Override
    public float[] array() {
        return new float[] { _00, _01, _02, _03, _10, _11, _12, _13, _20, _21, _22, _23, _30, _31, _32, _33 };
    }

    @Override
    public String toString() {
        return String.format("""
                        %f %f %f %f
                        %f %f %f %f
                        %f %f %f %f
                        %f %f %f %f
                        """,
                _00, _01, _02, _03,
                _10, _11, _12, _13,
                _20, _21, _22, _23,
                _30, _31, _32, _33
        );
    }

    public static FieldMatrix4f multiply(FieldMatrix4f a, FieldMatrix4f b) {
        final FieldMatrix4f m = new FieldMatrix4f();

        m._00 = a._00 * b._00 + a._01 * b._10 + a._02 * b._20 + a._03 * b._30;
        m._01 = a._00 * b._01 + a._01 * b._11 + a._02 * b._21 + a._03 * b._31;
        m._02 = a._00 * b._02 + a._01 * b._12 + a._02 * b._22 + a._03 * b._32;
        m._03 = a._00 * b._03 + a._01 * b._13 + a._02 * b._23 + a._03 * b._33;

        m._10 = a._10 * b._00 + a._11 * b._10 + a._12 * b._20 + a._13 * b._30;
        m._11 = a._10 * b._01 + a._11 * b._11 + a._12 * b._21 + a._13 * b._31;
        m._12 = a._10 * b._02 + a._11 * b._12 + a._12 * b._22 + a._13 * b._32;
        m._13 = a._10 * b._03 + a._11 * b._13 + a._12 * b._23 + a._13 * b._33;

        m._20 = a._20 * b._00 + a._21 * b._10 + a._22 * b._20 + a._23 * b._30;
        m._21 = a._20 * b._01 + a._21 * b._11 + a._22 * b._21 + a._23 * b._31;
        m._22 = a._20 * b._02 + a._21 * b._12 + a._22 * b._22 + a._23 * b._32;
        m._23 = a._20 * b._03 + a._21 * b._13 + a._22 * b._23 + a._23 * b._33;

        m._30 = a._30 * b._00 + a._31 * b._10 + a._32 * b._20 + a._33 * b._30;
        m._31 = a._30 * b._01 + a._31 * b._11 + a._32 * b._21 + a._33 * b._31;
        m._32 = a._30 * b._02 + a._31 * b._12 + a._32 * b._22 + a._33 * b._32;
        m._33 = a._30 * b._03 + a._31 * b._13 + a._32 * b._23 + a._33 * b._33;

        return m;
    }

    private static int index(int row, int column) {
        return (row << 2) + column;
    }
}
