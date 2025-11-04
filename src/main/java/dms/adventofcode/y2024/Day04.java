package dms.adventofcode.y2024;


import dms.adventofcode.Vector;

import java.util.List;

/**
 * Day04: Ceres Search
 *
 */
public class Day04 {

    private static char[][] readInput(List<String> input) {
        var colCount = input.getFirst().length();
        var rowCount = input.size();
        var result = new char[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            var row = input.get(i);
            for (int j = 0; j < colCount; j++) {
                var ch = row.charAt(j);
                result[i][j] = ch;
            }
        }
        return result;
    }

    private static int isMatch(char[][] data, int i, int j, Vector searchVector, String word) {
        var rowCount = data.length;
        var colCount = data[0].length;

        StringBuilder foundWord = new StringBuilder();
        for (var n = 0; n < word.length(); n++ ) {
            var iN = n * searchVector.x() + i;
            var jN = n * searchVector.y() + j;
            foundWord.append((iN >= 0 && iN < rowCount && jN >= 0 && jN < colCount) ? data[(int)iN][(int)jN] : "");
        }
        return word.contentEquals(foundWord) ? 1 : 0;
    }

    private static int countXMas(char[][] data) {
        var result = 0;
        var word = "XMAS";
        for (var i = 0; i < data.length; i++) {
            for (var j = 0; j < data[i].length; j++) {
                result += isMatch(data, i, j, new Vector(1, 0), word);
                result += isMatch(data, i, j, new Vector(1, 1), word);
                result += isMatch(data, i, j, new Vector(0, 1), word);
                result += isMatch(data, i, j, new Vector(-1, 1), word);
                result += isMatch(data, i, j, new Vector(-1, 0), word);
                result += isMatch(data, i, j, new Vector(-1, -1), word);
                result += isMatch(data, i, j, new Vector(0, -1), word);
                result += isMatch(data, i, j, new Vector(1, -1), word);
            }
        }
        return result;
    }

    private static int countMas(char[][] data) {
        var result = 0;
        var word = "MAS";
        for (var i = 1; i < data.length - 1; i++) {
            for (var j = 1; j < data[i].length - 1; j++) {
                if ((isMatch(data, i - 1, j - 1, new Vector(1, 1), word) > 0) &&
                        (isMatch(data, i - 1, j + 1, new Vector(1, -1), word) > 0)) {
                    result++;
                } else if ((isMatch(data, i - 1, j - 1, new Vector(1, 1), word) > 0) &&
                        (isMatch(data, i + 1, j - 1, new Vector(-1, 1), word) > 0)) {
                    result++;
                } else if ((isMatch(data, i + 1, j + 1, new Vector(-1, -1), word) > 0) &&
                        (isMatch(data, i - 1, j + 1, new Vector(1, -1), word) > 0)) {
                    result++;
                } else if ((isMatch(data, i + 1, j + 1, new Vector(-1, -1), word) > 0) &&
                        (isMatch(data, i + 1, j - 1, new Vector(-1, 1), word) > 0)) {
                    result++;
                }
            }
        }
        return result;
    }

    public static long part1(List<String> input) {
        var data = readInput(input);
        return countXMas(data);
    }

    public static long part2(List<String> input) {
        var data = readInput(input);
        return countMas(data);
    }
}
