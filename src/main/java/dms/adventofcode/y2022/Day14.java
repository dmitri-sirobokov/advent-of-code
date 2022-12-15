package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.List;

/**
 * Day 14: Regolith Reservoir
 */
public class Day14 extends CodeBase {

    private static final boolean printOutput = false;

    public static long part1(List<String> input) {
        var grid = readInput(input, false);
        var iterations = fillSand(grid);
        return iterations - 1;
    }

    public static long part2(List<String> input) {
        var grid = readInput(input, true);
        var iterations = fillSand(grid);
        return iterations - 1;
    }

    private static long fillSand(byte[][] grid) {
        var iteration = 0;
        var state = FallingState.REST;

        while (!FallingState.BOTTOM.equals(state)) {
            iteration++;

            state = fall(grid, 500, 0);
            if (printOutput) printGrid(grid);
        }
        return iteration;
    }

    private static void printGrid(byte[][] grid) {
        var minX = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var maxY = Integer.MIN_VALUE;
        var minY = 0;
        for (var y = 0; y < grid.length; y++) {
            for (var x = 0; x < grid[0].length; x++) {
                if (grid[y][x] != 0) {
                    minX = Math.min(x, minX);
                    maxX = Math.max(x, maxX);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        for (var y = minY; y <= maxY; y++) {
            for (var x = minX; x <= maxX; x++) {
                if (grid[y][x] == 0) {
                    System.out.print('.');
                } else if (grid[y][x] == 1) {
                    System.out.print('#');
                } else if (grid[y][x] == 2) {
                    System.out.print('o');
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static FallingState fall(byte[][] grid, int x, int y) {
        if (y == 0 && grid[y][x] != 0) {
            return FallingState.BOTTOM;
        }
        if (y + 1 >= grid.length) {
            return FallingState.BOTTOM;
        }
        if (grid[y + 1][x] == 0) {
            return fall(grid, x, y + 1);
        } else if (x > 0 && grid[y + 1][x - 1] == 0) {
            return fall(grid, x - 1, y + 1);
        } else if (x + 1 < grid[y + 1].length && grid[y + 1][x + 1] == 0) {
            return fall(grid, x + 1, y + 1);
        } else {
            grid[y][x] = 2;
            return FallingState.REST;
        }
    }

    private static byte[][] readInput(List<String> lines, boolean hasBottom) {
        // todo: we can find the best size of the grid based on the input...but for now just hardcoded
        var result = new byte[600][1200];
        var maxY = Integer.MIN_VALUE;
        for (var line : lines) {
            var xPrev = -1;
            var yPrev = -1;
            var lineParts = line.split(" -> ");
            for (String linePart : lineParts) {
                var xyParts = linePart.split(",");
                var xNext = Integer.parseInt(xyParts[0]);
                var yNext = Integer.parseInt(xyParts[1]);
                maxY = Math.max(maxY, yNext);

                if (yPrev < 0) {
                    xPrev = xNext;
                    yPrev = yNext;
                }

                for (var x = xPrev; x >= xNext; x--) {
                    result[yNext][x] = 1;
                }
                for (var x = xPrev; x <= xNext; x++) {
                    result[yNext][x] = 1;
                }
                for (var y = yPrev; y >= yNext; y--) {
                    result[y][xNext] = 1;
                }
                for (var y = yPrev; y <= yNext; y++) {
                    result[y][xNext] = 1;
                }

                xPrev = xNext;
                yPrev = yNext;
            }

        }
        if (hasBottom) {
            for (var x = maxY; x < result[0].length; x++) {
                result[maxY + 2][x] = 1;
            }
        }
        return result;
    }

    private enum FallingState {
        BOTTOM,
        REST,
    }

}
