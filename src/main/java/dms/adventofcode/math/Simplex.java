package dms.adventofcode.math;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Full simplex solver that utilizes Dual Phase 1 and 2, and Primal Phase 1 and 2 LP (Linear Programming) methods.
 * Solves min (c t), s.t. (M t) >= d, t >= 0, d >= 0.
 * Returns fractional solution as double[]
 */
public class Simplex {
    private final boolean debug = false;
    private int fractionIdx;

    public enum SimplexResultType {FEASIBLE, INFEASIBLE}
    public record Constraint(int[] x, int b, boolean geq) { }
    public record SimplexResult(double[] solution, SimplexResultType resultType, int fractionIdx) {
        public boolean integral() {
            return fractionIdx < 0;
        }
    }

    private final List<Constraint> constraints;
    private final int n;
    private final int rhsCol;
    private int objRow;
    // list of variables that has a related artificial variable, indices to constraints.
    private final List<Integer> artificialVars;
    private final int slackVars;
    private final double[][] tableau; // simplex tableau
    private int totalVars;
    private final int[] basis;
    private final double[] solution;
    private SimplexResultType resultType;
    private final int[] objective;

    // used to sort out the best row or col candidates
    private record ValueIndex(int idx, double value) { }

    public static Simplex createFromMatrix(int[][] M, int[] d, int[] objective) {
        var constraints = new ArrayList<Constraint>();
        for (var i = 0; i < M.length; i++) {
            var constraint = new Constraint(Arrays.stream(M[i]).map(x -> -x).toArray(), d[i], false);
            constraints.add(constraint);
        }
       return new Simplex(constraints, objective);
    }

    public boolean isIntegral() {
        return fractionIdx < 0;
    }

    /**
     * M t >= d, objective c
     */
    public Simplex(List<Constraint> constraints, int[] objective) {
        // todo: get max number of vars (x) from constraints during initializing and save it to global n.
        if (debug) printConstraints(constraints);
        this.n = constraints.getFirst().x.length;
        this.slackVars = constraints.size();
        // potentially we can have as many artificial variables as the number of constraints,
        // so we reserve this space in tableau
        this.basis = new int[n + slackVars + constraints.size()];
        // last column = RHS, last 2 rows are reserved for objectives (artificials and standard)
        this.tableau = new double[constraints.size() + 2][this.basis.length + 1];
        this.rhsCol = this.basis.length;
        this.objRow = constraints.size();
        this.artificialVars = new ArrayList<>();
        this.totalVars = n + slackVars; // will be increased later with artificial vars
        this.solution = new double[n];
        this.objective = objective;
        this.constraints = constraints;

        buildTableau();
        solve();
    }

    private void printConstraints(List<Constraint> constraints) {
        System.out.println("Constraints:");
        for (Constraint constraint : constraints) {
            for (var j = 0; j < constraint.x.length; j++) {
                System.out.print(constraint.x[j] + "*" + "x" + (j + 1));
                if (j != constraint.x.length - 1)
                    System.out.print(" + ");
            }
            var op = constraint.geq ? " >= " : " <= ";
            System.out.println(op + constraint.b);
        }
    }

    public SimplexResult solution() {
        return new SimplexResult(solution, resultType, fractionIdx);
    }

    private void buildTableau() {
        // Convert to standard simplex form, this ensures that all variables are positive, and no inequalities, and have basis.
        // Different cases to consider:
        //   1. <= constraint, b >= 0: Convert to +x + slack = b, slack is the basis
        //   2. <= constraint, b <  0: Convert to -x - slack + artificial = b, artificial is the basis
        //   3. >= constraint, b >= 0: Convert to +x - slack + artificial = b, artificial is the basis
        //   4. >= constraint, b <  0: Convert to -x + slack = b, slack is the basis
        Arrays.fill(basis, -1);
        for (var i = 0; i < constraints.size(); i++) {
            var constraint = constraints.get(i);

            var b = constraint.b >= 0 ? constraint.b : -constraint.b;

            // x
            for (var j = 0; j < constraint.x.length; j++) {
                var x = constraint.b >= 0 ? constraint.x[j] : -constraint.x[j];
                tableau[i][j]= x;
            }

            // slack
            var slack = constraint.b < 0 && constraint.geq || constraint.b >= 0 && !constraint.geq ? 1 : -1;
            tableau[i][n + i] = slack;
            if (slack > 0) basis[n + i] = i;

            // artificial
            if (slack < 0) {
                tableau[i][n + constraints.size() + artificialVars.size()] = 1;
                basis[n + constraints.size() + artificialVars.size()] = i;
                artificialVars.add(i);
                totalVars++;
            }

            // b (rhs)
            tableau[i][rhsCol] = b;
        }

        // objective row(s)
        //   - objRow points to current objective row
        //   - main objective, is one but last row in tableau
        //   - artificial objective, last row in tableau

        // main objective
        for (var j = 0; j < objective.length; j++) tableau[objRow][j] = objective[j];
        for (Constraint constraint : constraints)
            tableau[objRow][rhsCol] += constraint.b;

        // artificial objective
        if (!artificialVars.isEmpty()) objRow++;
        for (Integer constraintIdx : artificialVars) {
            // objectives columns for non-basis columns
            for (var j = 0; j < totalVars; j++)
                if (basis[j] < 0)
                    tableau[objRow][j] -= tableau[constraintIdx][j];

            // rhs objective = sum of rhs in artificial rows, negative sign
            tableau[objRow][rhsCol] -= tableau[constraintIdx][rhsCol];
        }

    }

    private void solve() {
        if (debug) System.out.println("Initial tableau:");
        printTableau();

        if (!artificialVars.isEmpty()) {
            if (debug) System.out.println("Phase 1 of simplex to remove " + artificialVars.size() + " artificial variables");
            primalFeasiblePhase1();
        }

        if (!SimplexResultType.INFEASIBLE.equals(resultType) && !isDualFeasible() && isPrimalFeasible()) {
            primalFeasiblePhase2();
        }

        // Phase 2 Dual
        if (!SimplexResultType.INFEASIBLE.equals(resultType) && isDualFeasible() && !isPrimalFeasible()) {
            dualFeasiblePhase2();
        }

        if (!SimplexResultType.INFEASIBLE.equals(resultType) && isDualFeasible() && isPrimalFeasible()) {
            for (var i = 0; i < basis.length; i++) {
                var basisIdx = basis[i];
                if (basisIdx >= 0 && i < n) solution[i] = tableau[basisIdx][rhsCol];
                checkIntegrality();
            }
        } else {
            resultType = SimplexResultType.INFEASIBLE;
        }
    }

    private boolean isPrimalFeasible() {
        boolean allBPositive = true;
        for (var i = 0; i < constraints.size(); i++)
            if (tableau[i][rhsCol] < 0) {
                allBPositive = false;
                break;
            }
        return allBPositive;
    }

    private boolean isDualFeasible() {
        boolean allCPositive = true;
        for (var j = 0; j < totalVars; j++)
            if (tableau[objRow][j] < 0) {
                allCPositive = false;
                break;
            }
        return allCPositive;
    }

    private void primalFeasiblePhase1() {
        // this phase is used to focus on removing artificial variables

        // if all values in new objective row are positive, and objective itself is also positive,
        // then we can't improve anymore and there is no feasible solution.
        if (isDualFeasible()) {
            // todo: investigate if we can come to this conclusion before building tableau.
            resultType = SimplexResultType.INFEASIBLE;
            return;
        }

        // now we have primal feasible tableau, we can run dual simplex phase 2
        primalFeasiblePhase2();

        // did we reach objective sum(artificials)=0 ?
        if (Math.abs(tableau[objRow][rhsCol]) > 1e-9) {
            resultType = SimplexResultType.INFEASIBLE;
            return;
        }

        // todo: do we still have artificials in basis?
        // if remaining basis is degenerate, then remove it, but copy first all non basis columns to new objective
        for (var a_col = n + slackVars; a_col < n + slackVars + artificialVars.size(); a_col++) {
            if (basis[a_col] >= 0) {
                var a_row = basis[a_col];

                for (var j = 0; j < totalVars; j++) {
                    if (basis[j] < 0) {
                        tableau[objRow-1][j] -= tableau[a_row][j];
                    }
                }
            }
        }

        totalVars -= artificialVars.size();
        artificialVars.clear();
        objRow--;
    }

    private void dualFeasiblePhase1() {
        // fix negative reduced costs (c0 row)

        while (true) {

            // find most negative reduced cost, Bland’s rule also works
            var minC = 0.0;
            int pivotRow = -1, pivotCol = -1;
            var pivotCols = new ArrayList<ValueIndex>();
            for (var j = 0; j < totalVars; j++)
                if (tableau[constraints.size()][j] < 0) {
                    pivotCols.add(new ValueIndex(j, tableau[constraints.size()][j]));
                }
            pivotCols.sort(Comparator.comparingDouble(ValueIndex::value));

            // Find a row to use for the repair, != 0 and preferably < 0
            var pivotRows = new ArrayList<ValueIndex>();
            for (var colCandidate : pivotCols) {
                for (var i = 0; i < constraints.size(); i++) {
                    if (tableau[i][totalVars] < 0 && tableau[i][colCandidate.idx] != 0) {
                        pivotRows.add(new ValueIndex(i, tableau[i][colCandidate.idx]));
                    }
                }

                pivotRows.sort(Comparator.comparingDouble(ValueIndex::value));
                if (!pivotRows.isEmpty()) {
                    pivotRow = pivotRows.getFirst().idx;
                    pivotCol = colCandidate.idx;
                    break;
                }
            }

            if (pivotRow < 0 || pivotCol < 0) return;
            pivot(pivotRow, pivotCol);
        }
    }

    private void dualFeasiblePhase2() {
        // dual pivot rule, fix negative b

        while (true) {
            // Pick the leaving row: row with most negative b
            var minB = 0.0;
            int pivotRow = -1, pivotCol = -1;
            for (var i = 0; i < constraints.size(); i++)
                if (tableau[i][totalVars] < minB) {
                    minB = tableau[i][totalVars];
                    pivotRow = i;
                }
            if (pivotRow < 0) break;
            var minRatio = 1e8;
            for (var j = 0; j < totalVars; j++) {
                if (basis[j] < 0 && tableau[pivotRow][j] < 0 && tableau[constraints.size()][j] > 0) {
                    var ratio = tableau[tableau.length - 1][j] / -tableau[pivotRow][j];
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        pivotCol = j;
                    }
                }
            }

            if (pivotCol < 0) {
                resultType = SimplexResultType.INFEASIBLE;
                return;
            }
            pivot(pivotRow, pivotCol);
        }
    }

    private void primalFeasiblePhase2() {
        while (true) {
            var pivotCol = -1;
            var minZ = 0.0;
            for (var j = 0; j < totalVars; j++) {
                if (tableau[objRow][j] < minZ) {
                    minZ = tableau[objRow][j];
                    pivotCol = j;
                }
            }
            if (pivotCol == -1) break; // optimal

            var pivotRow = findBestPivotRow(pivotCol);
            pivot(pivotRow, pivotCol);
        }
    }

    private int findBestPivotRow(int pivotCol) {
        var pivotRow = -1;
        var minRatio = Double.POSITIVE_INFINITY;
        for (var i = 0; i < constraints.size(); i++) {
            if (tableau[i][pivotCol] > 1e-10) {
                var ratio = tableau[i][rhsCol] / tableau[i][pivotCol];
                if (ratio < minRatio) {
                    minRatio = ratio;
                    pivotRow = i;
                }
            }
        }
        if (pivotRow == -1) throw new ArithmeticException("Unbounded LP");
        return pivotRow;
    }

    private int rowToVar(int row) {
        for (var i = 0; i < totalVars; i++) if (basis[i] == row) { return i; }
        throw new IllegalArgumentException("No basic variable is found in row " + row);
    }

    private void pivot(int row, int col) {
        assert basis[col] < 0; // entering column must be non basis

        var oldVar = rowToVar(row);
        basis[oldVar] = -1;
        basis[col] = row;

        var pivotVal = tableau[row][col];
        for (var j = 0; j < tableau[row].length; j++) tableau[row][j] /= pivotVal;
        for (var i = 0; i < tableau.length; i++) {
            if (i == row) continue;
            var f = tableau[i][col] / tableau[row][col];
            for (var j = 0; j < tableau[row].length; j++) tableau[i][j] -= f * tableau[row][j];
        }
    }

    private void printTableau() {
        if (debug) SimplexTableauPrinter.printTableau(tableau, basis, n, slackVars, artificialVars, totalVars);
    }

    // print utility to debug simplex tableau
    private final static class SimplexTableauPrinter {

        private SimplexTableauPrinter() {
        }

        /**
         * Pretty-prints a simplex tableau given constraints A (m x n), RHS b (m), and objective c (n),
         * and current objective value c0
         */
        public static void printTableau(double[][] A, int[] basis, int vars, int slackVars, List<Integer> artificialVars, int totalVars) {
            int m = A.length - 1;
            if (m == 0) throw new IllegalArgumentException("A must have at least one row.");
            int n = A[0].length - m - 1;
            // Labels
            String[] varLabels = new String[totalVars + 1];
            for (int j = 0; j < totalVars; j++) {
                varLabels[j] = getVarLabel(vars, slackVars, artificialVars, j);
            }
            varLabels[totalVars] = "b";
            String[] rowLabels = new String[m + 1]; // last row 'c0' (objective), after constraints c1..cm
            rowLabels[m - 1] = "z";
            rowLabels[m] = "w";
            for (int i = 0; i < totalVars; i++)
                if (basis[i] >= 0) rowLabels[basis[i]] = getVarLabel(vars, slackVars, artificialVars, i);

            // Prepare string matrix: (m+1) rows by (totalVars+1) columns (variables + RHS b)
            String[][] cells = new String[m + 1][totalVars + 1];

            // constraints A|b
            for (int i = 0; i <= m; i++) {
                for (int j = 0; j <= totalVars; j++) cells[i][j] = fmt(j < totalVars ? A[i][j] : A[i][A[i].length - 1]);
            }

            // Compute column widths (including header labels)
            int leftLabelWidth = maxWidth(rowLabels);
            int[] colWidths = new int[totalVars + 1];
            for (int j = 0; j <= totalVars; j++)
                colWidths[j] = Math.max(varLabels[j].length(), maxWidth(columnStrings(cells, j)));

            // Print
            // Header line: row-label padding + variable headers + RHS 'b'
            printDivider(leftLabelWidth, colWidths);
            System.out.print(pad("", leftLabelWidth) + " | ");
            for (int j = 0; j < totalVars; j++) {
                System.out.print(pad(varLabels[j], colWidths[j]) + " ");
            }
            System.out.print("| " + pad("b", colWidths[n]));
            System.out.println();
            printDivider(leftLabelWidth, colWidths);

            // Data rows: objective 'c' then constraints 'c1..cm'
            for (int r = 0; r < m + 1; r++) {
                System.out.print(pad(rowLabels[r], leftLabelWidth) + " | ");
                for (int j = 0; j < totalVars; j++) {
                    System.out.print(pad(cells[r][j], colWidths[j], true) + " ");
                }
                System.out.print("| " + pad(cells[r][totalVars], colWidths[n], true));
                System.out.println();

                // objective line for c0
                if (r == m - 2) printDivider(leftLabelWidth, colWidths);

            }
        }

        private static String getVarLabel(int vars, int slackVars, List<Integer> artificialVars, int j) {
            if (j < vars)
                return "x" + (j + 1);
            if (j < vars + slackVars)
                return "s" + (j + 1 - vars);
            if (j < vars + slackVars + artificialVars.size())
                return "a" + (j + 1 - vars - slackVars);
            return "";
        }

        /* ---------- helpers ---------- */

        private static String fmt(double v) {
            // Compact numeric formatting: integer if close to integer, else with 4 decimals
            if (Math.abs(v) < 1e-12) return "0";
            double r = Math.rint(v); // nearest integer
            if (Math.abs(v - r) < 1e-12) return String.format("%.0f", r);
            return String.format("%.4f", v);
        }

        private static String pad(String s, int width) {
            return pad(s, width, false);
        }

        private static String pad(String s, int width, boolean leftPad) {
            if (s == null) s = "";
            if (s.length() >= width) return s;
            char[] spaces = new char[width - s.length()];
            Arrays.fill(spaces, ' ');
            return leftPad ? new String(spaces) + s : s + new String(spaces);
        }

        private static int maxWidth(String[] arr) {
            int w = 0;
            for (String s : arr) w = Math.max(w, s == null ? 0 : s.length());
            return w;
        }

        private static String[] columnStrings(String[][] cells, int col) {
            String[] out = new String[cells.length];
            for (int i = 0; i < cells.length; i++) out[i] = cells[i][col];
            return out;
        }

        private static void printDivider(int leftLabelWidth, int[] colWidths) {
            StringBuilder sb = new StringBuilder();
            sb.append(repeat('-', leftLabelWidth)).append("-+-");
            for (int j = 0; j < colWidths.length - 1; j++) {
                sb.append(repeat('-', colWidths[j])).append("-");
            }
            sb.append("+-").append(repeat('-', colWidths[colWidths.length - 1]));
            System.out.println(sb);
        }

        private static String repeat(char ch, int times) {
            char[] arr = new char[Math.max(0, times)];
            Arrays.fill(arr, ch);
            return new String(arr);
        }

    }

    private void checkIntegrality() {
        if (resultType == SimplexResultType.INFEASIBLE) return;
        for (var i = 0; i < solution.length; i++) {
            var val = solution[i];
            var fraction = Math.abs(val - Math.round(val));
            if (fraction >= 1e-6) {
                fractionIdx = i;
                return;
            }
        }
        fractionIdx = -1;
    }


}
