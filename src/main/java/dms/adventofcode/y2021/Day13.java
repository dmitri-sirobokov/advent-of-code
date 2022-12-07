package dms.adventofcode.y2021;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day13 {

    public static int countVisibleDots(List<String> input, boolean justFirstFold) {
        var dots = readDots(input);
        var foldInstructions = readFoldInstructions(input);

        for (var foldInstruction : foldInstructions) {
            boolean[][][] folds;
            if (foldInstruction.isX) {
                folds = splitArrayX(dots, foldInstruction.position);
                reverseArrayX(folds[1]);
            } else {
                folds = splitArrayY(dots, foldInstruction.position);
                ArrayUtils.reverse(folds[1]);
            }

            dots = mergeFolds(folds);
            if (justFirstFold) {
                break;
            }
        }

        var result = 0;
        for (var y = 0; y < dots.length; y++) {
            for (var x = 0; x < dots[y].length; x++) {
                if (dots[y][x]) result++;
                System.out.print(dots[y][x] ? '#' : ' ');
            }
            System.out.println();

        }
        return result;
    }

    private static boolean[][] mergeFolds(boolean[][][] folds) {
        var result = new boolean[Math.max(folds[0].length, folds[1].length)][Math.max(folds[0][0].length, folds[1][0].length)];
        for (var k = 0; k < 2; k++) {
            for (var y = 0; y < folds[k].length; y++) {
                for (var x = 0; x < folds[k][y].length; x++) {
                    result[y][x] |= folds[k][y][x];
                }
            }
        }
        return result;
    }

    private static boolean[][][] splitArrayY(boolean[][] array, int pos) {
        var result = new boolean[2][][];
        result[0] = Arrays.copyOf(array, pos);
        result[1] = Arrays.copyOf(array, array.length - pos - 1);
        System.arraycopy(array, pos + 1, result[1], 0, array.length - pos - 1);
        return result;
    }

    private static boolean[][][] splitArrayX(boolean[][] array, int pos) {
        var result = new boolean[2][array.length][];
        result[0] = new boolean[array.length][array[0].length - pos - 1];
        result[1] = new boolean[array.length][pos];
        for (var y = 0; y < array.length; y++) {
            result[0][y] = Arrays.copyOfRange(array[y], pos + 1, array[y].length);
            result[1][y] = Arrays.copyOfRange(array[y], 0, pos);
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
