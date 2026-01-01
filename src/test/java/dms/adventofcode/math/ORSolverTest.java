package dms.adventofcode.math;

import com.google.ortools.Loader;
import com.google.ortools.modelbuilder.LinearExpr;
import com.google.ortools.modelbuilder.ModelBuilder;
import com.google.ortools.modelbuilder.LinearArgument;
import com.google.ortools.modelbuilder.ModelSolver;
import com.google.ortools.modelbuilder.SolveStatus;
import com.google.ortools.modelbuilder.Variable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

// some experiments with Google LP solvers.
@Disabled
public class ORSolverTest {

    @BeforeAll
    static void setUp() {
        Loader.loadNativeLibraries();
    }

    @Test
    void test_01() {
        var A = new MatrixInt(LinearSolverIntTest.TestData.A1).transpose();
        var b = new MatrixInt(LinearSolverIntTest.TestData.b1).transpose();
        assertObjective(A, b, 10);
    }

    @Test
    void test_02() {
        var A = new MatrixInt(LinearSolverIntTest.TestData.A2).transpose();
        var b = new MatrixInt(LinearSolverIntTest.TestData.b2).transpose();
        assertObjective(A, b, 11);
    }

    @Test
    void test_03() {
        var A = new MatrixInt(LinearSolverIntTest.TestData.A3).transpose();
        var b = new MatrixInt(LinearSolverIntTest.TestData.b3).transpose();
        assertObjective(A, b, 113.6);
    }

    void assertObjective(MatrixInt A, MatrixInt b, double expected) {
        ModelBuilder model = new ModelBuilder();
        var vars = new Variable[A.n];
        for (var j = 0; j < A.n; j++)
            vars[j] = model.newIntVar(0, Integer.MAX_VALUE, "x" + (j + 1));

        for (var i = 0; i < A.m; i++) {
            var constraint_x_row = new ArrayList<LinearArgument>();
            for (var j = 0; j < A.n; j++)
                if (A.data()[i][j] > 0) constraint_x_row.add(vars[j]);
            model.addEquality(LinearExpr.sum(constraint_x_row.toArray(new LinearArgument[0])), b.data()[i][0]);
        }

        model.minimize(LinearExpr.sum(vars));
        var solver = new ModelSolver("glop");
        Assertions.assertEquals(SolveStatus.OPTIMAL, solver.solve(model));

        var actual = solver.getObjectiveValue();
        Assertions.assertEquals(expected, actual, 0.01);

    }

}
