package dms.adventofcode.math;

import java.util.Arrays;

public class MatrixInt {
    public final int[][] data;
    public final int m;
    public final int n;

    // internal size of the data
    private final int data_n;
    private final int data_m;

    public MatrixInt(int[][] data) {
        this.data = data;
        this.m = data.length;
        this.n = data.length == 0 ? 0 : data[0].length;
        this.data_m = m;
        this.data_n = n;
    }

    public MatrixInt(int[] data) {
        this(new int[][] {data});
    }

    public static MatrixInt one(int m) {
        var result = new int[m][1];
        for (var i = 0; i < m; i++) result[i][0] = 1;
        return new MatrixInt(result);
    }

    public int[][] data() {
        return this.data;
    }

    public MatrixInt copy() {
        var dest = new int[data_m][data_n];
        for (var i = 0; i < data_m; i++) {
            System.arraycopy(data[i], 0, dest[i], 0, data_n);
        }
        return new MatrixInt(dest);
    }

    public MatrixInt mul(MatrixInt B) {
        var r = new int[m][B.n];
        for (var i = 0; i < m; i++)
            for (var j = 0; j < B.n; j++)
                for (var k = 0; k < n; k++)
                    r[i][j] += data[i][k] * B.data[k][j];

        return new MatrixInt(r);
    }

    // scalar multiplication
    public MatrixInt mul(int scalar) {
        var r = new int[m][n];
        for (var i = 0; i < m; i++)
            for (var j = 0; j < n; j++)
                r[i][j] += data[i][j] * scalar;
        return new MatrixInt(r);
    }

    public MatrixInt add(MatrixInt a) {
        var r = new int[m][n];
        for (var i = 0; i < m; i++)
            for (var j = 0; j < n; j++)
                r[i][j] = data[i][j] + a.data[i][j];
        return new MatrixInt(r);
    }

    public MatrixInt transpose() {
        var result = new int[n][m];
        for (var i = 0; i < m; i++)
            for (var j = 0; j < n; j++)
                result[j][i] = data[i][j];
        return new MatrixInt(result);
    }

    public MatrixInt addRows(MatrixInt rows) {
        assert n == rows.n;
        var result = new int[m + rows.m][n];
        for (var i = 0; i < m; i++)
            System.arraycopy(data[i], 0, result[i], 0, n);
        for (var i = 0; i < rows.m; i++)
            System.arraycopy(rows.data[i], 0, result[m + i], 0, n);
        return new MatrixInt(result);
    }

    @Override
    public String toString() {
        var result = new StringBuilder();
        result.append("{\n");
        for (var i = 0; i < m; i++) {
            result.append("    { ");
            for (var j = 0; j < n; j++) {
                result.append(data[i][j]);
                if (j != n - 1) result.append(", ");
            }
            result.append(" }");
            if (i != m - 1) result.append(",");
            result.append("\n");
        }
        result.append("}\n");

        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MatrixInt b)) return false;
        var a = this;
        if (a.m != b.m || a.n != b.n) return false;
        for (var r = 0; r < a.m; r++)
            if (!Arrays.equals(a.data[r], b.data[r])) return false;
        return true;
    }
}
