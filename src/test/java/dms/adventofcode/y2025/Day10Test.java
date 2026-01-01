package dms.adventofcode.y2025;


import com.google.ortools.Loader;
import com.google.ortools.modelbuilder.LinearExpr;
import com.google.ortools.modelbuilder.ModelBuilder;
import com.google.ortools.modelbuilder.ModelSolver;
import com.google.ortools.modelbuilder.SolveStatus;
import com.google.ortools.modelbuilder.Variable;
import dms.adventofcode.TestInput;
import dms.adventofcode.math.Simplex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {

    public final static boolean USE_GOOGLE_SOLVER = false;

    @BeforeAll
    static void setUp() {
        if (USE_GOOGLE_SOLVER) Loader.loadNativeLibraries();
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day10_sample.txt", expected = "7")
    @TestInput(input = "y2025/day10.txt", expected = "404")
    void part1(List<String> input, long expected) {
        var result = Day10.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day10_sample.txt", expected = "33")
    @TestInput(input = "y2025/day10_sample_58.txt", expected = "279")
    @TestInput(input = "y2025/day10_sample_88.txt", expected = "269")
    @TestInput(input = "y2025/day10_sample_123.txt", expected = "109")
    @TestInput(input = "y2025/day10.txt", expected = "16474")
    void part2(List<String> input, long expected) {
        //var lpSolver = USE_GOOGLE_SOLVER ? new DemoLpSolver() : null;

        var result = Day10.part2(input);

        assertEquals(expected, result);
    }

    // test different solver
    static class DemoLpSolver extends Day10.LPSolver {

        @Override
        Simplex.SimplexResult solve(List<Simplex.Constraint> constraints, int[] objective) {
                ModelBuilder model = new ModelBuilder();
                var vars = new Variable[constraints.getFirst().x().length];
                for (var j = 0; j < vars.length; j++)
                    vars[j] = model.newIntVar(0, Integer.MAX_VALUE, "x" + (j + 1));

            for (Simplex.Constraint c : constraints) {
                var coefs = Arrays.stream(c.x()).mapToDouble(v -> v).toArray();
                var sumExpr = LinearExpr.weightedSum(vars, coefs);
                if (c.geq()) {
                    model.addGreaterOrEqual(sumExpr, c.b());
                } else {
                    model.addLessOrEqual(sumExpr, c.b());
                }
            }

                model.minimize(LinearExpr.sum(vars));
                var solver = new ModelSolver("glop");
                var solverResult = solver.solve(model);

                if (solverResult == SolveStatus.OPTIMAL) {
                    var doublesResult = new double[vars.length];
                    for (var i = 0; i < vars.length; i++) {
                        doublesResult[i] = solver.getValue(vars[i]);
                    }
                    return new Simplex.SimplexResult(doublesResult, Simplex.SimplexResultType.FEASIBLE, -1);
                } else {
                    return new Simplex.SimplexResult(new double[0], Simplex.SimplexResultType.INFEASIBLE, -1);
                }
            }
    }

}