package dms.adventofcode.math;

import org.junit.jupiter.api.Test;

public class MatrixPrinterTest {
    @Test
    void print_01() {
        var matrix = new MatrixInt(new int[][] {
                {0, 0, 0, 1},
                {0, 1, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 1},
                {1, 0, 1, 0},
                {1, 1, 0, 0}
        });
        MatrixPrinter.print(matrix);
    }

    @Test
    void print_md() {
        var matrix = new MatrixInt(new int[][] {
                {0, 0, 0, 1},
                {0, 1, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 1},
                {1, 0, 1, 0},
                {1, 1, 0, 0}
        });
        MatrixPrinter.print(matrix, MatrixPrinter.Format.LATEX_MD);
    }
}
