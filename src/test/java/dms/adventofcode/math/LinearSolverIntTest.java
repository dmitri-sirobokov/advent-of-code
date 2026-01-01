package dms.adventofcode.math;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinearSolverIntTest {

    // test data for input Ax = b
    public static final class TestData {
        // Advent of code 2025, Day 10 sample
        public static final int[][] A1 = new int[][]{
                {0, 0, 0, 1},
                {0, 1, 0, 1},
                {0, 0, 1, 0},
                {0, 0, 1, 1},
                {1, 0, 1, 0},
                {1, 1, 0, 0}
        };
        public static final int[] b1 = new int[]{ 3, 5, 4, 7 };

        // Advent of code 2025, Day 10 sample
        // possible solutions { 5, 0, 5, 1}, min sum 11
        public static final int[][] A2 = new int[][]{
                {1, 1, 1, 1, 1, 0},
                {1, 0, 0, 1, 1, 0},
                {1, 1, 1, 0, 1, 1},
                {0, 1, 1, 0, 0, 0}

        };
        public static final int[] b2 = new int[]{ 10,11,11,5,10,5 };

        // Advent of code 2025, Day 10 sample
        // Optimal solutions (sum = 114), t = { 19, 4 }
        // [4, 16, 11, 2, 15, 11, 19, 0, 32, 4]
        // [4, 16, 10, 1, 15, 11, 19, 1, 32, 5]
        // [4, 16, 9, 0, 15, 11, 19, 2, 32, 6]
        public static final int[][] A3 = new int[][]{
                { 1, 1, 1, 1, 1, 1, 0, 0},
                { 1, 0, 0, 0, 1, 1, 0, 1},
                { 0, 1, 0, 0, 0, 1, 0, 1},
                { 0, 0, 0, 1, 1, 0, 1, 0},
                { 0, 0, 1, 0, 0, 0, 1, 1},
                { 1, 0, 0, 0, 0, 1, 0, 1},
                { 0, 0, 1, 1, 1, 0, 0, 1},
                { 0, 0, 0, 0, 0, 1, 1, 1},
                { 0, 1, 0, 1, 1, 1, 1, 0},
                { 0, 1, 0, 1, 1, 0, 0, 0}
        };
        public static final int[] b3 = new int[] { 31, 51, 38, 61, 77, 74, 49, 72 };

        // Advent of code 2025, Day 10 sample
        // optimal solution: { 16, 18, 13, 155, 5, 15, 0, 16, 16, 17, 8 }
        public static final int[][] A4 = new int[][]{
                { 1, 0, 1, 0, 1, 1, 0, 1, 1, 1 },
                { 0, 0, 0, 0, 1, 0, 1, 0, 0, 0 },
                { 1, 0, 0, 0, 0, 0, 1, 0, 0, 0 },
                { 0, 1, 1, 1, 0, 1, 0, 0, 1, 1 },
                { 1, 0, 0, 1, 1, 1, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 1, 0, 0, 1 },
                { 1, 1, 1, 0, 1, 1, 1, 0, 1, 0 },
                { 0, 1, 0, 1, 0, 1, 0, 1, 1, 1 },
                { 0, 1, 0, 1, 0, 0, 1, 1, 1, 0 },
                { 0, 0, 1, 0, 0, 1, 1, 0, 1, 1 },
                { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1 }
        };
        public static final int[] b4 = new int[]{ 42, 210, 196, 207, 47, 232, 92, 48, 233, 227 };

        // input Advent of code 2025, Day 10 sample
        public static final int[][] A5 = new int[][] {
                { 0, 0, 1, 0, 1, 0, 1, 1},
                { 0, 1, 0, 0, 0, 0, 1, 0},
                { 0, 0, 1, 1, 0, 1, 0, 0},
                { 0, 0, 0, 1, 1, 1, 0, 0},
                { 0, 0, 0, 1, 0, 1, 0, 1},
                { 1, 1, 1, 1, 1, 0, 1, 0},
                { 1, 1, 1, 0, 1, 1, 0, 1},
                { 1, 0, 0, 0, 0, 1, 1, 0},
                { 1, 0, 0, 0, 0, 0, 0, 0}
        };
        public static final int[] b5 = new int[] { 38, 25, 29, 19, 26, 40, 9, 31 };
    }

    @Test
    public void solve_01() {
        var a = new MatrixInt(TestData.A1).transpose();
        int[] b = TestData.b1;

        // one of the solutions is { 1,3,0,3,1,2 }
        var solver = new LinearSolverInt(a, b);
        var freeCount = solver.freeIndices().length;
        assertEquals(2, freeCount);
        var x = solver.solve();
        assertArrayEquals(new int[] { 0,5,-1,2,3,0 }, x);
        //MatrixPrinter.print(new MatrixInt(x).transpose(), MatrixPrinter.Format.LATEX_MD);
        MatrixPrinter.print(new MatrixInt(solver.solve(new int[] { 1, 0 })).transpose(), MatrixPrinter.Format.LATEX_MD);
    }

    @Test
    public void solve_02() {
        var a = new MatrixInt(TestData.A2).transpose();
        int[] b = TestData.b2;

        // optimal solutions { 5, 0, 5, 1}, min sum 11
        var solver = new LinearSolverInt(a, b);
        assertEquals(1, solver.freeIndices().length);
        var x = solver.solve();
        assertArrayEquals(new int[] { 6,-1, 5, 0 } , x);
    }

    @Test
    public void solve_03() {
        var a = new MatrixInt(TestData.A3).transpose();
        int[] b = TestData.b3;

        var solver = new LinearSolverInt(a, b);
        var result = solver.solve();
        validateResult(result, a, b);

        // optimum solution (sum x = 114, with free vars = { 19, 4 })
        result = solver.solve(new int[] { 19, 4 });
        validateResult(result, a, b);

    }

    @Test
    public void solve_04() {
        var a = new MatrixInt(TestData.A4).transpose();
        int[] b = TestData.b4;

        var solver = new LinearSolverInt(a, b);
        var x = solver.solve();
        validateResult(x, a, b);
        assertArrayEquals(new int[] { 14, 24, 19, 159, 3, 11, 2, 22, 12, 17, 4 }, x);

    }

    @Test
    public void solve_05() {
        var a = new MatrixInt(TestData.A5).transpose();
        int[] b = TestData.b5;

        var solver = new LinearSolverInt(a, b);
        var result = solver.solve();
        validateResult(result, a, b);

    }

    @Test
    public void test_88() {
        // input derived from 'y2025/day10_sample_88.txt' of Advent Of code puzzle
        var A = new MatrixInt(new int[][]{
                {0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1},
                {0, 1, 0, 1, 1, 0, 1, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0},
                {1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1},
                {1, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0},
                {1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
                {1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1},
                {1, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1},
                {1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1}
        });

        var b = new MatrixInt(new int[] { 52, 60, 212, 241, 50, 32, 21, 221, 51, 92 }).transpose();

        var solver = new LinearSolverInt(A, b.transpose().data()[0]);

        // verify Ax = b, s.t. different t
        verify_t(solver, new int[] { 0, 0 });
        verify_t(solver, new int[] { 10, 9 });
        verify_t(solver, new int[] { 11, 10 });
    }

    // verify solution in the form x = x0 + Nt is a parametric solution space of the as Ax=b
    private static void verify_t(LinearSolverInt solver, int[] t) {
        var x = solver.solve(t);
        var ax = solver.A().mul(new MatrixInt(x).transpose());
        var b = solver.b();
        assertArrayEquals(b, ax.transpose().data()[0]);
    }

    // --------- Helper methods ---------------

    private static void describeLinearSolution(int[] solution) {
        System.out.print(Arrays.toString(solution));
        System.out.println(" sum(" + Arrays.stream(solution).sum() + ")");
    }

    // validate Ax = b
    private static void validateResult(int[] x, MatrixInt A, int[] b) {
        for (var i = 0; i < A.m; i ++) {
            var sum = 0;
            for (var j = 0; j < A.n; j ++)
                sum += A.data[i][j] * x[j];
            assertEquals(b[i], sum, "Ax != b");
        }
    }


}
