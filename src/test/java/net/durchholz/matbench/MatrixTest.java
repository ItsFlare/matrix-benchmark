package net.durchholz.matbench;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    public static final float FLOAT_PRECISION = 1e-6f;

    @ParameterizedTest
    @MethodSource("parameters")
    public void set(Matrix matrix) {
        for (int i = 0; i < matrix.dimension(); i++) {
            for (int j = 0; j < matrix.dimension(); j++) {
                float value = ThreadLocalRandom.current().nextFloat(1);

                matrix.set(i, j, value);

                assertEquals(value, matrix.get(i, j), FLOAT_PRECISION);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void scale(Matrix matrix) {
        var copy = matrix.copy();

        matrix.scale(2);

        for (int i = 0; i < matrix.dimension(); i++) {
            for (int j = 0; j < matrix.dimension(); j++) {
                assertEquals(copy.get(i, j) * 2, matrix.get(i, j), FLOAT_PRECISION);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void transpose(Matrix matrix) {
        var copy = matrix.copy();

        matrix.transpose();

        for (int i = 0; i < matrix.dimension(); i++) {
            for (int j = 0; j < matrix.dimension(); j++) {
                assertEquals(copy.get(j, i), matrix.get(i, j));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void invert(Matrix matrix) {
        var copy = matrix.copy();

        matrix.invert();

        assertEquals(1 / copy.determinant(), matrix.determinant(), FLOAT_PRECISION);

        matrix.invert();

        for (int i = 0; i < matrix.dimension(); i++) {
            for (int j = 0; j < matrix.dimension(); j++) {
                assertEquals(copy.get(i, j), matrix.get(i, j), FLOAT_PRECISION);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void square(Matrix matrix) {
        var square = Matrix.multiply(matrix, matrix);

        assertEquals(Math.pow(matrix.determinant(), 2), square.determinant(), FLOAT_PRECISION);

        var expected = matrix.copy();

        //Zero
        for (int i = 0; i < matrix.dimension(); i++) {
            for (int j = 0; j < matrix.dimension(); j++) {
                expected.set(i, j, 0);
            }
        }

        //Square
        for (int i = 0; i < matrix.dimension(); i++) {
            for (int k = 0; k < matrix.dimension(); k++) {
                for (int j = 0; j < matrix.dimension(); j++) {
                    expected.set(i, k, expected.get(i, k) + matrix.get(i, j) * matrix.get(j, k));
                }
            }
        }

        //Check
        for (int i = 0; i < matrix.dimension(); i++) {
            for (int j = 0; j < matrix.dimension(); j++) {
                assertEquals(expected.get(i, j), square.get(i, j), FLOAT_PRECISION);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void array(Matrix matrix) {
        var array = matrix.array();

        for (int i = 0; i < matrix.dimension(); i++) {
            for (int j = 0; j < matrix.dimension(); j++) {
                assertEquals(matrix.get(i, j), array[linearize(i, j, matrix.dimension())]);
            }
        }
    }

    private static List<Matrix> parameters() {
        return List.of(
                randomize(new ArrayMatrix2f()),
                randomize(new FieldMatrix2f()),
                randomize(new ArrayMatrix3f()),
                randomize(new FieldMatrix3f()),
                randomize(new ArrayMatrix4f()),
                randomize(new FieldMatrix4f())
        );
    }

    private static Matrix randomize(Matrix matrix) {
        do {
            for (int i = 0; i < matrix.dimension(); i++) {
                for (int j = 0; j < matrix.dimension(); j++) {
                    matrix.set(i, j, ThreadLocalRandom.current().nextFloat(2) - 1f);
                }
            }

            //Keep randomizing until matrix is invertible and determinant is not far off 1 to avoid precision errors
        } while (!range(0.5f, 2f, Math.abs(matrix.determinant())));

        return matrix;
    }

    private static boolean range(float min, float max, float value) {
        return value >= min && value <= max;
    }

    private static int linearize(int row, int column, int dimension) {
        return dimension * row + column;
    }
}