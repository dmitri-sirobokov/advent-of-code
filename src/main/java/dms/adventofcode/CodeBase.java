package dms.adventofcode;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
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
        var result = new int[input.size()][input.get(0).length()];
        for (var y = 0; y < input.size(); y++) {
            var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                result[y][x] = line.charAt(x) - '0';
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

    protected static <T> T[][] mapArray(int[][] array, MapArrayFunction<T, Integer> func) {
        return mapArray(array, (x, y, value) -> func.apply(value));
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
