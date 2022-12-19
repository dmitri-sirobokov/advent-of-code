package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.List;

/**
 * Day 17: Pyroclastic Flow
 */
public class Day18 extends CodeBase {


    public static long part1(List<String> input) {
        var points = readPoints(input);
        return countSurface(points, (byte) 0);
    }

    public static long part2(List<String> input) {
        var points = readPoints(input);
        fillWithWater(points);
        fillWithWater(points);
        fillWithWater(points);
        fillWithWater(points);
        fillWithWater(points);
        fillWithWater(points);
        return countSurface(points, (byte) 2);
    }

    private static void fillWithWater(byte[][][] points) {
        points[0][0][0] = 2;
        for (var x = 0; x < points.length; x++) {
            for (var y = 0; y < points[0].length; y++) {
                for (var z = 0; z < points[0][0].length; z++) {
                    if (points[x][y][z] != 0) {
                        continue;
                    }

                    if (x == 0 || points[x - 1][y][z] == 2) {
                        points[x][y][z] = 2;
                    }
                    if (x >= points.length - 1 || points[x + 1][y][z] == 2) {
                        points[x][y][z] = 2;
                    }
                    if (y == 0 || points[x][y - 1][z] == 2) {
                        points[x][y][z] = 2;
                    }
                    if (y >= points[0].length - 1 || points[x][y + 1][z] == 2) {
                        points[x][y][z] = 2;
                    }
                    if (z == 0 || points[x][y][z - 1] == 2) {
                        points[x][y][z] = 2;
                    }
                    if (z >= points[0][0].length - 1 || points[x][y][z + 1] == 2) {
                        points[x][y][z] = 2;
                    }

                }
            }
        }
    }

    private static byte[][][] readPoints(List<String> input) {
        // todo: calculated the size of the array from input
        var result = new byte[100][100][100];
        for (var line : input) {
            var lineParts = line.split(",");
            result[Integer.parseInt(lineParts[0])][Integer.parseInt(lineParts[1])][Integer.parseInt(lineParts[2])] = 1;
        }

        return result;

    }

    private static long countSurface(byte[][][] points, byte type) {
        var surface = 0L;
        for (var x = 0; x < points.length; x++) {
            for (var y = 0; y < points[0].length; y++) {
                for (var z = 0; z < points[0][0].length; z++) {
                    if (points[x][y][z] != 1) {
                        continue;
                    }

                    if (x == 0 || points[x - 1][y][z] == type) {
                        surface++;
                    }
                    if (x >= points.length - 1 || points[x + 1][y][z] == type) {
                        surface++;
                    }
                    if (y == 0 || points[x][y - 1][z] == type) {
                        surface++;
                    }
                    if (y >= points[0].length - 1 || points[x][y + 1][z] == type) {
                        surface++;
                    }
                    if (z == 0 || points[x][y][z - 1] == type) {
                        surface++;
                    }
                    if (z >= points[0][0].length - 1 || points[x][y][z + 1] == type) {
                        surface++;
                    }
                }
            }
        }
        return surface;
    }

}
