package dms.adventofcode;

import dms.adventofcode.math.MatrixInt;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CodeBase {

    public CodeBase() { }

    protected static List<String> readResourceFile(String resourcePath) throws IOException {
        try (var resourceStream = CodeBase.class.getClassLoader().getResourceAsStream(resourcePath)) {
            return readLines(IOUtils.toString(Objects.requireNonNull(resourceStream), StandardCharsets.UTF_8));
        }
    }

    private static List<String> readLines(String input) throws IOException {
        try (var br = new BufferedReader(new StringReader(input))) {
            return br.lines().toList();
        }
    }


    /** Read text lines into a matrix with numeric values where each value represents a digit from 0 to 9.
     * Example input:
     * <pre>
     * 11111
     * 19991
     * 19191
     * 19991
     * 11111
     * </pre>
     * @param input - Text lines to parse into matrix.
     * @return - returns an array that represents matrix.
     */
    protected static int[][] readMatrix(List<String> input) {
        var result = new int[input.size()][input.getFirst().length()];
        for (var y = 0; y < input.size(); y++) {
            var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                result[y][x] = line.charAt(x) - '0';
            }
        }

        return result;
    }

    /** Read text lines into a matrix with char values where each value represents a digit from 0 to 9.
     * Example input:
     * <pre>
     * AAAAA
     * BCCCB
     * EDEDE
     * AACAA
     * BBBBB
     * </pre>
     * @param input - Text lines to parse into matrix.
     * @return - returns an array that represents matrix.
     */
    protected static char[][] readCharMatrix(List<String> input) {
        var result = new char[input.size()][input.getFirst().length()];
        for (var y = 0; y < input.size(); y++) {
            var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                result[y][x] = line.charAt(x);
            }
        }

        return result;
    }

    protected static void forEach(int[][] matrix, MatrixForEachConsumer action) {
        forEach(matrix[0].length, matrix.length, action);
    }

    protected static <T> void forEach(T[][] matrix, MatrixForEachConsumer action) {
        forEach(matrix[0].length, matrix.length, action);
    }

    protected static void forEach(int rangeX, int rangeY, MatrixForEachConsumer action) {
        for (var y = 0; y < rangeY; y++) {
            for (var x = 0; x < rangeX; x++) {
                action.apply(x, y);
            }
        }
    }

    protected static <T> void forEachAdjacent(T[][] matrix, MatrixForEachAdjacentConsumer<T> action) {
        for (var y = 0; y < matrix.length; y++) {
            for (var x = 0; x < matrix[y].length; x++) {
                var dx = new int[]{-1, 0, 0, 1};
                var dy = new int[]{0, -1, 1, 0};
                for (var k = 0; k < 4; k++) {
                    var adjX = x + dx[k];
                    var adjY = y + dy[k];
                    if (adjX >= 0 && adjX < matrix[0].length && adjY >= 0 && adjY < matrix.length) {
                        action.apply(matrix[y][x], matrix[adjY][adjX]);
                    }
                }
            }
        }
    }

    protected static String arrayToString(boolean[][] array) {
        var result = new StringBuilder();
        for (var row : array) {
            for (var cell : row) {
                result.append(cell ? '#' : '.');
            }
            result.append("\n");
        }
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    protected static <T> T[][] mapArray(int[][] array, MapArrayFunctionXY<T, Integer> func) {
        var resultUntyped = new Object[array.length][array[0].length];
        for (var y = 0; y < array.length; y++) {
            for (var x = 0; x < array[y].length; x++) {
                resultUntyped[y][x] = func.apply(x, y, array[y][x]);
            }
        }
        var result = (T[][])Array.newInstance(resultUntyped[0][0].getClass(), resultUntyped.length, resultUntyped[0].length);
        for (var y = 0; y < array.length; y++) {
            for (var x = 0; x < array[y].length; x++) {
                result[y][x] = (T)resultUntyped[y][x];
            }
        }
        return result;
    }

    protected static void printMatrix(String name, MatrixInt m) {
        if (StringUtils.isNotBlank(name)) {
            System.out.print(name + " = ");
        }
        System.out.printf(m.toString());
    }

    protected static void printMatrix(String name, int[][] A) {
        if (StringUtils.isNotBlank(name)) {
            System.out.print(name + " = ");
        }
        System.out.println("{");
        for (var i = 0; i < A.length; i++) {
            System.out.print("    { ");
            for (var j = 0; j < A[0].length; j++) {
                System.out.print(A[i][j]);
                if (j != A[0].length - 1) System.out.print(", ");
            }
            System.out.print(" }");
            if (i != A.length - 1) System.out.print(",");
            System.out.println();
        }
        System.out.println("}");
    }

    protected static void printMatrix(char[][] matrix) {
        for (char[] chars : matrix) {
            for (char ch : chars) {
                System.out.print(ch);
            }
            System.out.println();
        }
    }

    protected static <T> T[][] mapArray(int[][] array, MapArrayFunction<T, Integer> func) {
        return mapArray(array, (x, y, value) -> func.apply(value));
    }

    /**
     * Merge overlapped ranges.
     */
    protected static List<Range> mergeRanges(List<Range> ranges) {
        List<Range> sorted = new ArrayList<>(ranges);
        sorted.sort(Comparator.comparingLong(Range::start).thenComparingLong(Range::end));

        List<Range> merged = new ArrayList<>();
        long curStart = sorted.getFirst().start();
        long curEnd   = sorted.getFirst().end();

        for (int i = 1; i < sorted.size(); i++) {
            Range next = sorted.get(i);
            boolean shouldMerge = next.start() <= curEnd + 1L;

            if (shouldMerge) {
                if (next.end() > curEnd) curEnd = next.end();
            } else {
                merged.add(new Range(curStart, curEnd));
                curStart = next.start();
                curEnd   = next.end();
            }
        }
        merged.add(new Range(curStart, curEnd));
        return merged;
    }


    public interface MatrixForEachConsumer {
        void apply(int x, int y);
    }

    public interface MatrixForEachAdjacentConsumer<T> {
        void apply(T element, T adjacent);
    }

    public interface MapArrayFunction<R, V> {
        R apply(V value);
    }

    public interface MapArrayFunctionXY<R, V> {
        R apply(int x, int y, V value);
    }
}
