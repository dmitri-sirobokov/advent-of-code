package dms.adventofcode.y2021;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class Day13 {

    private static final boolean shouldPrintResult = false;

    public static int countVisibleDots(List<String> input, boolean justFirstFold) {
        var dots = readDots(input);
        var foldInstructions = readFoldInstructions(input);

        for (var foldInstruction : foldInstructions) {
            if (foldInstruction.isX) {
                dots = splitArrayX(dots, foldInstruction.position);
            } else {
                dots = foldArrayY(dots, foldInstruction.position);
            }

            if (shouldPrintResult) printResult(dots);

            if (justFirstFold) {
                break;
            }
        }

        var result = 0;
        for (var y = 0; y < dots.length; y++) {
            for (var x = 0; x < dots[y].length; x++) {
                if (dots[y][x]) result++;
            }
        }

        return result;
    }

    private static void printResult(boolean[][] dots) {
        for (var y = 0; y < dots.length; y++) {
            for (var x = 0; x < dots[y].length; x++) {
                System.out.print(dots[y][x] ? '#' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean[][] mergeFolds(boolean[][][] folds) {
        var result = new boolean[folds[0].length][folds[0][0].length];
        for (var k = 0; k < 2; k++) {
            for (var y = 0; y < result.length; y++) {
                for (var x = 0; x < result[y].length; x++) {
                    if (y < folds[k].length && (x < folds[k][y].length))
                        result[y][x] |= folds[k][y][x];
                }
            }
        }
        return result;
    }

    private static boolean[][] foldArrayY(boolean[][] array, int pos) {
        var result = new boolean[pos][array[0].length];
        for (var y = 0; y < pos; y++) {
            result[y] = array[y];
        }
        var t = 2 * pos - array.length + 1;
        for (var y = 0; y < pos; y++) {
            if (y + t < result.length) {
                for (var x = 0; x < result[0].length; x++) {
                    result[y + t][x] |= array[array.length - y - 1][x];
                }
            }
        }
        return result;
    }

    private static boolean[][] splitArrayX(boolean[][] array, int pos) {
        var result = new boolean[array.length][pos];
        var t = 2 * pos - array[0].length + 1;
        for (var y = 0; y < array.length; y++) {
            for (var x = 0; x < pos; x++) {
                result[y][x] = array[y][x];
                result[y][x + t] |= array[y][array[y].length - x - 1];
            }
        }
        return result;
    }

    private static void reverseArrayX(boolean[][] array) {
        for (var y = 0; y < array.length; y++) {
            ArrayUtils.reverse(array[y]);
        }
    }

    private static boolean[][] readDots(List<String> input) {
        var points = new ArrayList<Point>();
        for (var line : input) {
            if (line.isBlank()) {
                break;
            }

            var lineParts = line.split(",");
            var point = new Point(Integer.parseInt(lineParts[0]), Integer.parseInt(lineParts[1]));
            points.add(point);
        }

        var xMax = points.stream().mapToInt(p -> p.x).max().getAsInt();
        var yMax = points.stream().mapToInt(p -> p.y).max().getAsInt();
        var result = new boolean[yMax + 1][xMax + 1];
        points.forEach(p -> result[p.y][p.x] = true);
        return result;
    }

    private static List<FoldInstruction> readFoldInstructions(List<String> input) {
        var foldInstructions = new ArrayList<FoldInstruction>();
        var firstLineIndex = input.indexOf("") + 1;
        for (var i = firstLineIndex; i < input.size(); i++) {
            var line = input.get(i);
            var isX = line.charAt(11) == 'x';
            var foldPosition = Integer.parseInt(line.substring(13));
            foldInstructions.add(new FoldInstruction(foldPosition, isX));
        }
        return foldInstructions;
    }

    private record Point(int x, int y) { }

    private record FoldInstruction(int position, boolean isX) { }
}
