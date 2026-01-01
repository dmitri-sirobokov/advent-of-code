package dms.adventofcode.math;

import dms.adventofcode.CodeBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class SimplexTest extends CodeBase {

    @Test
    void solve_01() {
        // optimal solution x0=[1, 5, 0, 1, 3, 0], sum=10, t={1,0}
        var A = new MatrixInt(LinearSolverIntTest.TestData.A1).transpose();
        var b = LinearSolverIntTest.TestData.b1;
        var solution = calcSimplex(A, b);
        Assertions.assertArrayEquals(new double[] { 1, 0 }, solution, 0.0001 );
    }

    @Test
    void solve_02() {
        // optimal solution x0={ 5, 0, 5, 1 }, sum 11, t={1}
        var A = new MatrixInt(LinearSolverIntTest.TestData.A2).transpose();
        var b = LinearSolverIntTest.TestData.b2;
        var solution = calcSimplex(A, b);
        Assertions.assertArrayEquals(new double[] { 1 }, solution, 0.0001 );
    }

    @Test
    void solve_03() {
        var A = new MatrixInt(LinearSolverIntTest.TestData.A3).transpose();
        var b = LinearSolverIntTest.TestData.b3;
        var solution = calcSimplex(A, b);
        Assertions.assertArrayEquals(new double[] { 19.4, 4.8 }, solution, 0.0001 );
    }

    @Test
    void solve_04() {
        var A = new MatrixInt(LinearSolverIntTest.TestData.A4).transpose();
        var b = LinearSolverIntTest.TestData.b4;
        var solution = calcSimplex(A, b);
        Assertions.assertArrayEquals(new double[] { -2.0 }, solution, 0.0001 );
    }

    @Test
    void solve_05() {
        var A = new MatrixInt(LinearSolverIntTest.TestData.A5).transpose();
        var b = LinearSolverIntTest.TestData.b5;
        var solution = calcSimplex(A, b);
        Assertions.assertArrayEquals(new double[] { 9.63 }, solution, 0.01 );
    }

    @Test
    void solve_01_bounds() {
        var A = new MatrixInt(LinearSolverIntTest.TestData.A1).transpose();
        var b = LinearSolverIntTest.TestData.b1;

        var solver = new LinearSolverInt(A, b);
        var base = solver.solve(new int[]{-5, 0});
        var free = solver.getFree().transpose();

        // create constraints
        var constraints = new ArrayList<Simplex.Constraint>();
        for (var i = 0; i < free.m; i++) {
            var constraint = new Simplex.Constraint(Arrays.copyOf(free.mul(-1).data()[i], free.n), base[i], false);
            constraints.add(constraint);
        }

        constraints.add(new Simplex.Constraint(new int[] { 1, 0 }, 10, true));
        // t1 upper bound test
        var simplex = new Simplex(constraints, new int[] { 1, 0 });
        // should be bigger or equal to 12, because of constraint above, in this case 12.0 exact.
        Assertions.assertArrayEquals(new double[] { 10, 3 }, simplex.solution().solution(), 0.0001);
    }

    @Test
    public void solve_88() {
        // objective = [-2, 3], and b is derived from solution t=[0,0]
        // bounds t1 >= 0, t2 >= 0]
        var constraints = new ArrayList<Simplex.Constraint>();
        constraints.add(new Simplex.Constraint(new int[] { -1, 1 }, -1, false));
        constraints.add(new Simplex.Constraint(new int[] { 2, 1 }, 46, false));
        constraints.add(new Simplex.Constraint(new int[] { 1, 4 }, 222, false));
        constraints.add(new Simplex.Constraint(new int[] { -2, -3 }, -46, false));
        constraints.add(new Simplex.Constraint(new int[] { 1, -3 }, -1, false));
        constraints.add(new Simplex.Constraint(new int[] { -1, -1 }, -4, false));
        constraints.add(new Simplex.Constraint(new int[] { -1, -1 }, -13, false));
        constraints.add(new Simplex.Constraint(new int[] { 0, 7 }, 74, false));
        constraints.add(new Simplex.Constraint(new int[] { 1, -3 }, -9, false));
        constraints.add(new Simplex.Constraint(new int[] { -1, 0 }, 0, false));
        constraints.add(new Simplex.Constraint(new int[] { 3, -4 }, -6, false));
        constraints.add(new Simplex.Constraint(new int[] { 0, -1 }, 0, false));

        var objective = new int[] { -2, 3 };

        var simplex = new Simplex(constraints, objective);
        Assertions.assertArrayEquals(new double[] { 10.0, 9.0 }, simplex.solution().solution(), 0.01);
    }


    private static double[] calcSimplex(MatrixInt A, int[] b) {
        // original objective = minimize costs of params x, size n = number of buttons.
        // some results from SNF decomposition of A1 * x = b1
        var lin = new LinearSolverInt(A, b);
        var x0 = lin.x0();

        // initially each free variables can be both positive and negative, we start with 2 nodes t+ and t-
        // initial bounds are (t+)-(t-)=[0...INF]
        var free = lin.getFree();
        var M = new int[free.n][free.m];

        //t+
        for (var i = 0; i < free.m; i++)
            for (var j = 0; j < free.n; j++) {
                M[j][i] = free.data[i][j];
            }

        int z1 = Integer.MAX_VALUE, z2 = Integer.MAX_VALUE;
        int[] t1, t2;
        double[] td, t1d = null, t2d = null;
        var simplex = Simplex.createFromMatrix(M, x0, freeCost(new MatrixInt(M)));
        var solution = simplex.solution();
        if (Simplex.SimplexResultType.INFEASIBLE != solution.resultType()) {
            t1 = Arrays.stream(solution.solution()).map(Math::round).mapToInt(v -> (int) v).toArray();
            var b1 = new MatrixInt(M).mul(new MatrixInt(t1).transpose()).add(new MatrixInt(x0).transpose()).transpose();
            z1 = Arrays.stream(b1.data()[0]).sum();
            t1d = solution.solution();
        }

        // t-
        for (var i = 0; i < free.m; i++)
            for (var j = 0; j < free.n; j++) {
                M[j][i] = -free.data[i][j];
            }

        simplex = Simplex.createFromMatrix(M, x0, freeCost(new MatrixInt(M)));
        solution = simplex.solution();
        if (solution.resultType() != Simplex.SimplexResultType.INFEASIBLE) {
            t2 = Arrays.stream(solution.solution()).map(Math::round).mapToInt(v -> (int) v).toArray();
            var b2 = new MatrixInt(M).mul(new MatrixInt(t2).transpose()).add(new MatrixInt(x0).transpose()).transpose();
            z2 = Arrays.stream(b2.data()[0]).sum();
            t2d = Arrays.stream(solution.solution()).map(v -> -v).toArray();
        }

        if (z1 < z2) {
            td = t1d;
        } else {
            td = t2d;
        }
        System.out.println("t=" + Arrays.toString(td));

        return td;
    }

    private static int[] freeCost(MatrixInt free) {
        return MatrixInt.one(free.m).transpose().mul(free).data()[0];
    }


}
