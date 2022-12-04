package dms.adventofcode.y2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day09 {

    public static long calcRiskLevel(List<String> input) {
        int[][] levels = parseLevels(input);
        ArrayList<Position> lowestPoints = findLowestPositions(levels);

        return lowestPoints.stream().map(p -> 1 + levels[p.y][p.x])
                .reduce(0, Integer::sum);
    }

    public static int findLargestBasins(List<String> input) {
        int[][] levels = parseLevels(input);
        ArrayList<Position> lowestPoints = findLowestPositions(levels);

        boolean[][] counted = new boolean[levels.length][levels[0].length];
        return lowestPoints.stream().map(p -> calcBasinSize(p.x, p.y, 0, 0, levels, counted))
                .sorted(Collections.reverseOrder())
                .limit(3)
                .reduce((a,b) -> a * b)
                .orElse(0);
    }

    private static int calcBasinSize(int x, int y, int currentSize, int currentLevel, int[][] levels, boolean[][] counted) {
        if (y < 0 || y >= levels.length || x < 0 || x >= levels[y].length) {
            return currentSize;
        }

        if (levels[y][x] == 9 || levels[y][x] < currentLevel) {
            return currentSize;
        }
        if (counted[y][x]) {
            return currentSize;
        }
        counted[y][x] = true;
        currentSize++;
        currentLevel = levels[y][x];
        currentSize = calcBasinSize(x - 1, y, currentSize, currentLevel, levels, counted);
        currentSize = calcBasinSize(x + 1, y, currentSize, currentLevel, levels, counted);
        currentSize = calcBasinSize(x, y - 1, currentSize, currentLevel, levels, counted);
        currentSize = calcBasinSize(x, y + 1, currentSize, currentLevel, levels, counted);
        return currentSize;
    }

    private static ArrayList<Position> findLowestPositions(int[][] levels) {
        var lowestPoints = new ArrayList<Position>();
        for (var y = 0; y < levels.length; y++) {
            for (var x = 0; x < levels[y].length; x++) {
                var y1 = y - 1;
                var y2 = y + 1;
                var x1 = x - 1;
                var x2 = x + 1;

                // test all adjacent points (4 directions)
                var test1 = y - 1 < 0 || levels[y - 1][x] > levels[y][x];
                var test2= y + 1 >= levels.length || levels[y + 1][x] > levels[y][x];
                var test3= x - 1 < 0 || levels[y][x - 1] > levels[y][x];
                var test4= x + 1 >= levels[y].length || levels[y][x + 1] > levels[y][x];

                // if all adjacent points are higher, then we are at the lowest point.
                if (test1 && test2 && test3 && test4) {
                    lowestPoints.add(new Position(x, y));
                }
            }
        }
        return lowestPoints;
    }

    private static int[][] parseLevels(List<String> input) {
        var levels = new int[input.size()][input.get(0).length()];
        for (var y = 0; y < input.size(); y++) {
            var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                levels[y][x] = line.charAt(x) - '0';
            }
        }
        return levels;
    }

    private static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
