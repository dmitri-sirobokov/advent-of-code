package dms.adventofcode.y2025;

import dms.adventofcode.CodeBase;
import dms.adventofcode.math.LinearSolverInt;
import dms.adventofcode.math.MatrixInt;
import dms.adventofcode.math.Simplex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Day10: Factory
 *
 */
public class Day10 extends CodeBase {

    public static long part1(List<String> input) {
        var machines = readMachines(input);

        for (var machine : machines) {
            var combinations = (1 << machine.buttons.n) - 1;
            var minPressCountLights = Integer.MAX_VALUE;
            for (var combination = 1; combination <= combinations; combination++) {
                var combinationBits = combination;
                var buttonsIndex = 0;
                int[] lights = new int[machine.lights.length];
                var pressCount = 0;
                while (combinationBits > 0) {
                    if (combinationBits % 2 == 1) {
                        for (var i = 0; i < machine.buttons.m; i++) {
                            lights[i] ^= machine.buttons.data[i][buttonsIndex];
                        }
                        pressCount++;
                    }
                    buttonsIndex++;
                    combinationBits /= 2;
                }

                if (Arrays.equals(lights, machine.lights)) {
                    if (pressCount < minPressCountLights) {
                        minPressCountLights = pressCount;
                    }
                }
            }

            machine.pressCount = minPressCountLights;
        }

        return machines.stream().mapToLong(machine -> machine.pressCount).sum();
    }

    public static long part2(List<String> input) {
        var lpSolver = new SimplexSolver();

        var machines = readMachines(input);
        System.out.println("Total number of machines: " + machines.size());
        var t_count_stats = new int[10];

        var id = 0;
        for (var machine : machines) {
            id++;
            var linSolver = new LinearSolverInt(machine.buttons, machine.jolts);
            t_count_stats[linSolver.free.m]++;

            var x0Sum = sum(linSolver.x0);
            // calculate free variables' reduced costs
            var reducedCosts = new int[linSolver.free.m];
            for (var i = 0; i < linSolver.free.m; i++) {
                reducedCosts[i] = sum(linSolver.free.data[i]);
            }

            // if no free variables = x0 - is already at optimum solution
            // or if reduced cost is 0 with one variable - we can't improve
            if (linSolver.free.m == 0 || linSolver.free.m == 1 && reducedCosts[0] == 0) {
                machine.pressCount = x0Sum;
                machine.buttonsPressed = linSolver.x0;
            }

            if (linSolver.free.m == 1) {
                // just one parameter, always a trivial solution
                // look the minimum range where one of the x is 0
                int lb = Integer.MIN_VALUE, ub = Integer.MAX_VALUE;
                var free_0 = linSolver.free.data[0];
                for (var j = 0; j < linSolver.x0().length; j++) {
                    if (free_0[j] > 0) lb = Math.max(lb, -Math.floorDiv(linSolver.x0[j], free_0[j]));
                    if (free_0[j] < 0) ub = Math.min(ub, Math.floorDiv(-linSolver.x0[j],  free_0[j]));
                    if (free_0[j] == 0 && linSolver.x0[j] < 0) throw new ArithmeticException("No feasible solution?");
                }

                if (lb > ub) throw new ArithmeticException("No feasible solution?");
                var t = reducedCosts[0] < 0 ? ub : lb;
                var x = linSolver.solve(new int[] { t });
                machine.pressCount = sum(x);
                machine.buttonsPressed = x;
                machine.parametricSolution = new int[] { t };
                assert machine.pressCount >= 0;
            }

            // not vary trivial cases for null-space > 1, we use branch-and-bound algorithm
            if (linSolver.free.m > 1) {
                brunchAndBounds(machine, linSolver, lpSolver);
            }

//            System.out.print("id=" + id + ", ");
//            System.out.print("x=" + Arrays.toString(machine.buttonsPressed) + ", ");
//            System.out.print("sum=" + machine.pressCount + ", ");
//            System.out.println("t=" + Arrays.toString(machine.parametricSolution));

        }

        System.out.println("t_count_stats=" + Arrays.toString(t_count_stats));

        return machines.stream().mapToLong(machine -> machine.pressCount).sum();
    }

    public static abstract class LPSolver {
        abstract Simplex.SimplexResult solve(List<Simplex.Constraint> constraints, int[] objective);
    }

    // search node for Branch-And-Bounds DFS
    private static class Node {
        public final List<Bound> bounds;
        public final LinearSolverInt solver;
        public final MatrixInt free;

        public Node(LinearSolverInt solver, List<Bound> bounds) {
            this.solver = solver;
            this.bounds = bounds;
            this.free = solver.getFree().transpose();
        }

        // branch on bounds
        public Node branch(int varIdx, int lb, int ub) {
            var newBounds = new ArrayList<>(this.bounds);
            newBounds.set(varIdx, new Bound(lb, ub));
            return new Node(this.solver, newBounds);
        }

        // branch on fractional value
        public List<Node> branch(int varIdx, double val) {
            if (varIdx < 0 || varIdx >= bounds.size()) return Collections.emptyList();
            var bound = bounds.get(varIdx);
            if (val == bound.lb || val == bound.ub) return List.of(this);
            if (bound.lb > bound.ub) return Collections.emptyList();
            if (val < bound.lb || val > bound.ub) return Collections.emptyList();
            Node left, right;
            if (val > 0) {
                left = branch(varIdx, bound.lb, (int) val);
                right = branch(varIdx, (int) val + 1, bound.ub);
            } else if (val < 0) {
                left = branch(varIdx, bound.lb, (int) val - 1);
                right = branch(varIdx, (int) val, bound.ub);
            } else {
                left = branch(varIdx, bound.lb, - 1);
                right = branch(varIdx, 0, bound.ub);
            }
            return List.of(left, right);
        }

        public List<Node> branchPosAndNegBounds() {
            // split positive and negative ranges
            var result = new ArrayList<Node>();
            for (var i = 0; i < bounds.size(); i++) {
                var bound = bounds.get(i);
                if (bound.oppositeSigns()) {
                    var branch = branch(i, 0);
                    result.addAll(branch);
                }
            }
            return result;

        }
    }

    // lb - lower bound, ub - upper bound
    private record Bound(int lb, int ub) {

        // return true if only one of the bounds is negative.
        public boolean oppositeSigns() {
            return (lb ^ ub) < 0;
        }
    }

    private static void brunchAndBounds(Machine machine, LinearSolverInt solver, SimplexSolver lpSolver) {
        var bestSolution = Integer.MAX_VALUE;

        var bounds = new ArrayList<Bound>();
        for (var i = 0; i < solver.free.m; i++)
            bounds.add(new Bound(Integer.MIN_VALUE, Integer.MAX_VALUE));
        var rootNode = new Node(solver, bounds);

        var stack = new Stack<Node>();
        stack.push(rootNode);

        while (!stack.isEmpty()) {
            var node = stack.pop();

            // split positive and negative bounds
            var branches = node.branchPosAndNegBounds();
            stack.addAll(branches);
            if (!branches.isEmpty()) continue;

            var lpSolution = solveLp(node, lpSolver);
            if (lpSolution.resultType() == Simplex.SimplexResultType.INFEASIBLE) {
                continue;
            }

            // branch on fractional solutions
            if (!lpSolution.integral()) {
                var i = lpSolution.fractionIdx();
                var value = lpSolution.solution()[lpSolution.fractionIdx()];
                var branch = node.branch(i, value);
                stack.addAll(branch);
                if (!branch.isEmpty()) continue;
            }

            // if not branched, we have an integer solution
            var t = Arrays.stream(lpSolution.solution()).mapToInt(v -> (int) Math.round(v)).toArray();
            var x = solver.solve(t);
            var sumX = sum(x);
            if (sumX < bestSolution) {
                machine.pressCount = sumX;
                machine.buttonsPressed = x;
                machine.parametricSolution = t;
                bestSolution = sumX;
            }
        }
    }

    private static Simplex.SimplexResult solveLp(Node node, LPSolver lpSolver) {
        var free = node.free;

        // cost of free vars c = 1T * free, is a sum of columns in free vectors. That is what we are minimizing.
        var objectives = MatrixInt.one(free.m).transpose().mul(free).data[0];

        // constraints for base solution -Nt <= x0, that guarantees each x_i >= 0
        var x = free.mul(-1);
        var constraints = new ArrayList<Simplex.Constraint>();

        // constraint for bounds
        for (var j = 0; j < node.bounds.size(); j++) {
            var bounds = node.bounds.get(j);

            // substitute t with (t-) = -t for negative ranges (assumed lb and ub are always same sign)
            if (bounds.lb < 0) {
                for (var i = 0; i < x.m; i++) x.data()[i][j] = -x.data()[i][j];
                objectives[j] = -objectives[j];
            }

            var lb_abs = bounds.lb < 0 ? abs(bounds.ub) : bounds.lb;
            var ub_abs = bounds.ub < 0 ? abs(bounds.lb) : bounds.ub;

            if (lb_abs != Integer.MAX_VALUE && lb_abs != 0) {
                var lbConstrArr = new int[node.free.n];
                lbConstrArr[j] = 1;
                constraints.add(new Simplex.Constraint(lbConstrArr, lb_abs, true));

            }
            if (ub_abs != Integer.MAX_VALUE) {
                var ubConstrArr= new int[node.free.n];
                ubConstrArr[j] = 1;
                constraints.add(new Simplex.Constraint(ubConstrArr, ub_abs, false));
            }
        }

        for (var i = 0; i < x.m; i++) {
            var constraint = new Simplex.Constraint(x.data()[i], node.solver.x0()[i], false);
            constraints.add(constraint);
        }

        var lpSolution = lpSolver.solve(constraints, objectives);
        if (lpSolution.resultType() != Simplex.SimplexResultType.INFEASIBLE) {
            var solution = lpSolution.solution();
            // substitute back t = -(t-)
            for (var i = 0; i < solution.length; i++)
                if (node.bounds.get(i).lb < 0)
                    solution[i] = -solution[i];
        }
        return lpSolution;
    }

    // saturate on Integer.MIN_VALUE
    private static int abs(int value) {
        if (value >= 0) return value;
        if (value == Integer.MIN_VALUE) return Integer.MAX_VALUE;
        return -value;
    }

    private static class Machine {
        private final int[] lights;
        // a matrix of wiring's for each button to a jolt counters.
        private final MatrixInt buttons;
        private final int[] jolts;
        public int[] buttonsPressed;
        public int[] parametricSolution;
        private long pressCount;

        Machine(int[] lights, MatrixInt buttons, int[] jolts) {
            this.lights = lights;
            this.buttons = buttons;
            this.jolts = jolts;
        }
    }

    private static List<Machine> readMachines(List<String> input) {
        var machines = new ArrayList<Machine>();
        for (var line : input) {
            var parts = line.split(" ");
            var lightsStr = parts[0].substring(1, parts[0].length() - 1);
            var lights = new int[lightsStr.length()];
            var lighCount = 0;
            for (var ch : lightsStr.chars().toArray()) {
                if (ch == '#') lights[lighCount] = 1;
                lighCount++;
            }

            var joltsStr = parts[parts.length - 1];
            var joltsStr2 = joltsStr.substring(1, joltsStr.length() - 1);
            var joltsStr3 = joltsStr2.split(",");
            var jolts = Arrays.stream(joltsStr3).mapToInt(Integer::parseInt).toArray();

            var buttons = new MatrixInt(new int[jolts.length][parts.length - 2]);
            for (var i = 1; i < parts.length - 1; i++) {
                var buttonsStr = parts[i];
                var buttonsStr2 = buttonsStr.substring(1, buttonsStr.length() - 1);
                var buttonsStr3 = buttonsStr2.split(",");
                var joltRefs = Arrays.stream(buttonsStr3).mapToInt(Integer::parseInt).toArray();
                for (var jolt : joltRefs)
                    buttons.data[jolt][i - 1] = 1;
            }

            var machine = new Machine(lights, buttons, jolts);
            machines.add(machine);


        }

        return machines;
    }

    private static int sum(int[] values) {
        var result = 0;
        for (int value : values) result += value;
        return result;
    }

    private static class SimplexSolver extends LPSolver {
        @Override
        Simplex.SimplexResult solve(List<Simplex.Constraint> constraints, int[] objectives) {
            var simplex = new Simplex(constraints, objectives);
            return simplex.solution();
        }
    }

}
