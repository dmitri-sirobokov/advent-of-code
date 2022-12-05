package dms.adventofcode.y2021;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {

    public static int countVisibleDots(List<String> input) {
        var dots = readDots(input);
        var foldInstructions = readFoldInstructions(input);

        for (var foldInstruction : foldInstructions) {
            boolean[][][] folds;
            if (foldInstruction.isX) {
                folds = splitArrayX(dots, foldInstruction.position);
                reverseArrayX(folds[0]);
            } else {
                folds = splitArrayY(dots, foldInstruction.position);
                ArrayUtils.reverse(folds[0]);
            }

            dots = mergeFolds(folds);
        }

        return 0;
    }

    private static boolean[][] mergeFolds(boolean[][][] folds) {
        var result = new boolean[Math.max(folds[0].length, folds[1].length)][Math.max(folds[0][0].length, folds[1][0].length)];
        for (var y = 0; y < result.length; y++) {
            for (var x = 0; x < result[y].length; x++) {
                result[y][x] = folds[0][y][x] | folds[1][y][x];
            }
        }
        return result;
    }

    private static boolean[][][] splitArrayY(boolean[][] array, int pos) {
        var result = new boolean[2][][];
        result[0] = Arrays.copyOf(array, pos);
        result[1] = new boolean[array.length - pos - 1][array[0].length];
        System.arraycopy(array, pos + 1, result[1], 0, array.length - pos - 1);
        return result;
    }

    private static boolean[][][] splitArrayX(boolean[][] array, int pos) {
        var result = new boolean[2][][];
        result[0] = new boolean[array.length][array[0].length - pos - 1];
        result[1] = new boolean[array.length][pos];
        for (var y = 0; y < array.length; y++) {
            System.arraycopy(array[y], pos + 1, result[0], 0, array.length - pos - 1);
            System.arraycopy(array[y], 0, result[1], 0, pos);
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
