package dms.adventofcode.math;

import java.util.Arrays;

import static dms.adventofcode.math.MathUtils.gcd;

/**
 * Solve system of equations Ax=b, and rewrite it in the parametric form: x = x0 + Vt.
 * It uses SnF decomposition under the hood to find matrix V and base solution vector x0.
 * In particular, matrix A is decomposed to the form A = U D V, which gives us U and V matrices
 * and reveals free (independent) parameters, and a basic solution x0.
 * For more information on SnF, see <a href="https://en.wikipedia.org/wiki/Smith_normal_form">Smith Normal Form Wiki</a>
 */
public class LinearSolverInt {
    private final MatrixInt A;
    private final int[] b;
    private final int m;
    private final int n;

    // internal SNF decomposition
    private final MatrixInt U;
    private final MatrixInt V;
    private final MatrixInt D;
    private MatrixInt Uinv;
    private MatrixInt Vinv;

    private final int[] freeIndices;
    private final int freeCount;
    public final MatrixInt free;

    // solution with free variables = 0
    public final int[] x0;

    // helper class to keep some temporary rational arithmetic in inverse matrix
    private record Rational(long num, long den) implements Comparable<Rational> {
        public static final Rational ZERO = new Rational(0, 1);
        public static final Rational ONE = new Rational(1, 1);
        public static Rational of(long n) { return new Rational(n, 1); }
        public static Rational of(long num, long den) {
            if (den == 0) throw new ArithmeticException("Denominator = 0");
            if (den < 0) { den = -den; num = -num; }               // normalize sign
            long g = gcd(Math.abs(num), den);                      // reduce
            num /= g; den /= g;
            return (den == 1) ? new Rational(num, 1) : new Rational(num, den);
        }
        public boolean isZero() { return num == 0; }
        public Rational add(Rational r) { return Rational.of(num * r.den + den * r.num, den * r.den); }
        public Rational sub(Rational r) { return Rational.of(num * r.den - den * r.num, den * r.den); }
        public Rational mul(Rational r) {
            // cross-cancel to limit growth
            long a = num, b = den, c = r.num, d = r.den;
            var g1 = gcd(Math.abs(a), d);
            var g2 = gcd(Math.abs(c), b);
            a /= g1; d /= g1; c /= g2; b /= g2;
            return Rational.of(a * c, b * d);
        }
        public Rational div(Rational r) {
            if (r.isZero()) throw new ArithmeticException("Division by zero");
            long a = num, b = den, c = r.num, d = r.den;
            var g1 = gcd(Math.abs(a), Math.abs(c));
            var g2 = gcd(b, d);
            a /= g1; c /= g1; b /= g2; d /= g2;
            long N = a * d, D = b * c;
            if (D < 0) { D = -D; N = -N; }
            return Rational.of(N, D);
        }
        public Rational abs() { return new Rational(Math.abs(num), den); }
        @Override public int compareTo(Rational r) { return Long.compare(num * r.den, den * r.num); }
        @Override public String toString() { return den == 1 ? Long.toString(num) : (num + "/" + den); }
    }

    public LinearSolverInt(MatrixInt A, int[] b) {
        this(A, b, false);
    }
    
    protected LinearSolverInt(MatrixInt A, int[] b, boolean computeInverse) {
        if (A.m != b.length) {
            throw new IllegalArgumentException(String.format("Rows of A(%s) must match rows of b(%s)", A.m, b.length));
        }
        this.A = A;
        this.b = Arrays.copyOf(b, b.length);
        this.m = A.m;
        this.n = A.n;

        // compute SNF decomposition
        var snf = computeSNFWithUV(this.A);
        this.U = snf[0];
        this.D = snf[1];
        this.V = snf[2];

        // compute inverses
        if (computeInverse) this.Uinv = invertUnimodular(U);
        if (computeInverse) this.Vinv = invertUnimodular(V);

        // identify free variables
        var countFree = 0;
        for (var i = 0; i < Math.min(n, m); i++)
            if (D.data[i][i] == 0) countFree++;
        for (var i = m; i < n; i++)
            countFree++;

        this.freeCount = countFree;
        this.freeIndices = new int[freeCount];
        var idx = 0;
        for (var i = 0; i < n; i++) {
            if (i >= m || D.data[i][i] == 0) freeIndices[idx++] = i;
        }

        // compute one particular solution with free variables = 0
        this.x0 = solveInternalX0();

        // compute free variable vectors
        this.free = new MatrixInt(new int[freeCount][n]);
        for (var k = 0; k < freeIndices.length; k++) {
            var j = freeIndices[k];
            for (var i = 0; i < n; i++) {
                this.free.data[k][i] = V.data[i][j]; // we could use a view hier instead copy data
            }
        }
    }

    // solve with free variables = 0
    public int[] solve() {
        return x0;
    }

    // solve with user-specified free variables
    public int[] solve(int[] free) {
        if (free.length != freeCount) {
            throw new IllegalArgumentException("Incorrect number of free variables provided");
        }
        return solveInternalX(free);
    }

    // Accessors
    public MatrixInt A() {
        return A;
    }

    public int[] b() {
        return b;
    }

    public MatrixInt U() {
        return U;
    }

    public MatrixInt Uinv() {
        return Uinv;
    }

    public MatrixInt V() {
        return V;
    }

    public MatrixInt Vinv() {
        return Vinv;
    }

    public int[] x0() {
        return x0;
    }

    public MatrixInt D() {
        return D;
    }

    public int[] freeIndices() {
        return freeIndices;
    }

    public MatrixInt getFree() {
        return free;
    }

    // ------------ Internal methods ---------------

    // find particular base vector = x0, which is used later for quick solutions in parametric form x = x0 + V(f)*f
    // that is actually solution of x assuming free variables are 0
    private int[] solveInternalX0() {
        // Step 1: c = U * b;
        var c = new long[m];
        for (var i = 0; i < m; i++) {
            var sum = 0L;
            for (var j = 0; j < m; j++)
                sum += (long)U.data[i][j] * b[j];
            c[i] = sum;
        }

        // Step 2: Solve D * y = c
        var y = new long[n];
        var freeIdxCounter = 0;
        for (var i = 0; i < n; i++) {
            if (freeIdxCounter < freeCount && i == freeIndices[freeIdxCounter])
                y[i] = 0;
            else if (i < m && D.data[i][i] != 0) {
                if (c[i] % D.data[i][i] != 0)
                    throw new ArithmeticException("No integer solution exists");
                y[i] = c[i] / D.data[i][i];
            } else y[i] = 0;
        }

        // Step 3: x = V * y
        var x = new int[n];
        for (var i = 0; i < n; i++) {
            var sum = 0L;
            for (var j = 0; j < n; j++)
                sum += V.data[i][j] * y[j];
            // check for overflow
            if (sum > Integer.MAX_VALUE || sum < Integer.MIN_VALUE)
                throw new ArithmeticException("Integer overflow in solution");
            x[i] = (int)sum;
        }
        return x;
    }

    private int[] solveInternalX(int[] freeValues) {
        var x = Arrays.copyOf(x0, x0.length);
        for (var i = 0; i < free.m; i++)
            for (var j = 0; j < x0.length; j++)
                x[j] += free.data[i][j] * freeValues[i];
        return x;
    }

    // --------------- SNF Computation ------------------
    private static MatrixInt[] computeSNFWithUV(MatrixInt A) {
        int m = A.m, n = A.n;
        var D = A.copy();
        var U = new int[m][m];
        var V = new int[n][n];
        for (var i = 0; i < m; i++) U[i][i] = 1;
        for (var i = 0; i < n; i++) V[i][i] = 1;

        // current pivot
        var p = 0;
        while (p < Math.min(m, n)) {
            // find pivot row and col, which is d minimum value in the remaining matrix.
            var minVal = Integer.MAX_VALUE;
            int minI = -1, minJ = -1;
            for (var r = p; r < m; r++) {
                for (var c = p; c < n; c++) {
                    var val = Math.abs(D.data[r][c]);
                    if (val != 0 && val < minVal) {
                        minVal = val;
                        minI = r;
                        minJ = c;
                    }
                }
            }

            if (minVal == Integer.MAX_VALUE) break;

            // swap rows and columns
            if (minI != p) swapRows(D.data, U, p, minI);
            if (minJ != p) swapCols(D.data, V, p, minJ);

            // Clearing rows and columns explained:
            // - For each element under pivot row, and for each element right to the pivot column, we keep only remainder.
            // - This is equivalent of Euclidean algorithm y = value % pivot.
            // - We keep how many times we subtracted (x = value / pivot), so we can record this operation into corresponding U and V matrix.
            // - it's allowed unimodular operation because it is translated as subtracting pivot from value multiple times.
            // - because mod operation (%) always reduce value, we guaranteed to reach 0 value at the end
            //   after repeating the whole process multiple times - clear complete.

            var d = D.data[p][p];
            var cleared = true;

            // clear column
            for (var r = p + 1; r < m; r++) {
                // how many times to subtract pivot from this row = x
                var x = D.data[r][p] / d;
                if (x == 0) continue;

                // subtract pivot row from this row x times to reduce pivot column
                for (var c = p; c < n; c++)  D.data[r][c] -= x * D.data[p][c];
                for (var c = 0; c < m; c++) U[r][c] -= x * U[p][c];
                cleared &= D.data[r][p] == 0;
            }

            // clear row
            for (var c = p + 1; c < n; c++) {
                // how many times to subtract pivot from this col = x
                var x = D.data[p][c] / d;
                if (x == 0) continue;

                // subtract pivot col from this col x times to reduce pivot row
                for (var r = p; r < m; r++) D.data[r][c] -= x * D.data[r][p];
                for (var r = 0; r < n; r++) V[r][c] -= x * V[r][p];
                cleared &= D.data[p][c] == 0;
            }

            if (cleared) p++;
        }

        // make all invariants positive
        for (var i = 0; i < Math.min(n, m); i++)
            if (D.data[i][i] < 0) invertRow(D.data, U, i);

        var result = new MatrixInt[] {
                new MatrixInt(U),
                D,
                new MatrixInt(V)
        };
        // Assert sanity check U*A*V == D
        assert sanityCheck(result[0], A, result[2], D);
        return result;
    }

    private static MatrixInt invertUnimodular(MatrixInt A) {
        final int n = A.m;

        // --- Augmented matrices L = A (as rationals), R = I (as rationals) ---
        Rational[][] L = new Rational[n][n];
        Rational[][] R = new Rational[n][n];
        for (int i = 0; i < n; i++) {
            if (A.data[i].length != n) throw new IllegalArgumentException("Matrix not square.");
            for (int j = 0; j < n; j++) L[i][j] = Rational.of(A.data[i][j]);
            for (int j = 0; j < n; j++) R[i][j] = (i == j) ? Rational.ONE : Rational.ZERO;
        }

        // --- Gauss–Jordan on (L, R) ---
        for (int col = 0; col < n; col++) {
            int pivot = findPivotRow(L, col);
            if (pivot == -1) throw new IllegalArgumentException("Matrix is singular at column " + col);

            if (pivot != col) {
                swapRows(L, pivot, col);
                swapRows(R, pivot, col);
            }

            // Normalize pivot row so L[col][col] becomes 1
            Rational pivotVal = L[col][col];
            for (int j = 0; j < n; j++) {
                L[col][j] = L[col][j].div(pivotVal);
                R[col][j] = R[col][j].div(pivotVal);
            }

            // Eliminate other rows
            for (int i = 0; i < n; i++) {
                if (i == col) continue;
                Rational factor = L[i][col];
                if (factor.isZero()) continue;
                for (int j = 0; j < n; j++) {
                    L[i][j] = L[i][j].sub(factor.mul(L[col][j]));
                    R[i][j] = R[i][j].sub(factor.mul(R[col][j]));
                }
            }
        }

        // --- Check L is identity and extract integer inverse from R ---
        int[][] inv = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Rational v = L[i][j];
                boolean ok = (i == j && v.num / v.den == 1 && v.num % v.den == 0) || (i != j && v.num == 0);
                if (!ok) throw new IllegalStateException("Left matrix not identity at (" + i + "," + j + "): " + v);
            }
            for (int j = 0; j < n; j++) {
                Rational r = R[i][j];
                if (r.num % r.den != 0) {
                    throw new IllegalArgumentException(
                            "Matrix not unimodular: inverse has non-integer entry at (" + i + "," + j + "): " + r
                    );
                }
                inv[i][j] = (int) (r.num / r.den);
            }
        }
        return new MatrixInt(inv);
    }

    // -------- allowed unimodular operation --------------

    private static void swapRows(int[][] A, int[][] U, int i, int j) {
        swapRows(A, i, j);
        swapRows(U, i, j);
    }

    private static <T> void swapRows(T[][] M, int i, int j) {
        var tmp = M[i];
        M[i] = M[j];
        M[j] = tmp;
    }

    private static void swapRows(int[][] M, int i, int j) {
        var tmp = M[i];
        M[i] = M[j];
        M[j] = tmp;
    }

    private static void swapCols(int[][] A, int[][] V, int i, int j) {
        for (var r = 0; r < A.length; r++) {
            int tmp = A[r][i];
            A[r][i] = A[r][j];
            A[r][j] = tmp;
        }
        for (var r = 0; r < V.length; r++) {
            var tmp = V[r][i];
            V[r][i] = V[r][j];
            V[r][j] = tmp;
        }
    }

    private static void invertRow(int[][] A, int[][] U, int r) {
        for (var c = 0; c < A[0].length; c++) A[r][c] *= -1;
        for (var c = 0; c < U[0].length; c++) U[r][c] *= -1;
    }

    private static int findPivotRow(Rational[][] L, int col) {
        int n = L.length;
        int pivot = -1;
        Rational best = Rational.ZERO;
        for (int r = col; r < n; r++) {
            Rational v = L[r][col];
            if (!v.isZero()) {
                var abs = v.abs();
                if (pivot == -1 || abs.compareTo(best) > 0) { pivot = r; best = abs; }
            }
        }
        return pivot;
    }


    // --------- matrix helpers ---------

    // check that UAV = D
    private static boolean sanityCheck(MatrixInt U, MatrixInt A, MatrixInt V, MatrixInt D) {
        return U.mul(A).mul(V).equals(D);
    }

    private static int[][] multiply(int[][] a, int[][] b) {
        var result = new int[a.length][b[0].length];
        for (var r = 0; r < a.length; r++)
            for (var c = 0; c < b[0].length; c++)
                for (var i = 0; i < a[0].length; i++) result[r][c] += a[r][i] * b[i][c];
        return result;
    }

}
