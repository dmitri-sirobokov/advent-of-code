package dms.adventofcode;

import java.util.List;

public class CodeBase {

    protected CodeBase() { }

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
}
