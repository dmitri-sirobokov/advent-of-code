package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;

import java.util.Arrays;
import java.util.List;

public class Day11 extends CodeBase {

    public static int countFlashes(List<String> input) {

        var octopusMap = readMatrix(input);
        var flashes = 0;
        for (var iteration = 0; iteration < 100; iteration++) {
            flashes += iteration(octopusMap);
        }
        return flashes;
    }

    public static int countIterations(List<String> input) {

        var octopusMap = readMatrix(input);
        var iterations = 1;

        // iterate until all octopuses flash in the same iteration
        while (iteration(octopusMap) < (long) octopusMap.length * octopusMap[0].length) {
            iterations++;
        }

        return iterations;
    }

    /** Perform one operation step and return number of flashes
     *
     */
    private static long iteration(int[][] octopusMap) {
        for (int y = 0; y < octopusMap.length; y++) {
            for (var x = 0; x < octopusMap[y].length; x++) {
                octopusMap[y][x]++;
            }
        }

        // any octopus with energy level greater than 9 will flash, increasing energy level of adjacent octopuses
        for (int y = 0; y < octopusMap.length; y++) {
            for (var x = 0; x < octopusMap[y].length; x++) {
                if (octopusMap[y][x] > 9) {
                    flash(octopusMap, x, y);
                }
            }
        }

        // count number of flashes
        return Arrays.stream(octopusMap)
                .flatMapToInt(Arrays::stream)
                .filter(energy -> energy == 0)
                .count();
    }

    private static void flash(int[][] octopusMap, int x, int y) {
        // can only flash once
        if (octopusMap[y][x] == 0) {
            return;
        }

        // no energy left after flash
        octopusMap[y][x] = 0;

        // increase the energy of adjacent octopuses
        charge(octopusMap, x - 1, y - 1);
        charge(octopusMap, x - 1, y);
        charge(octopusMap, x - 1, y + 1);
        charge(octopusMap, x, y + 1);
        charge(octopusMap, x + 1, y + 1);
        charge(octopusMap, x + 1, y);
        charge(octopusMap, x + 1, y - 1);
        charge(octopusMap, x, y - 1);
    }

    private static void charge(int[][] octopusMap, int x, int y) {
        if (y < 0 || y >= octopusMap.length || x < 0 || x >= octopusMap[y].length) {
            return;
        }

        // if flashed, no charge anymore
        if (octopusMap[y][x] == 0) {
            return;
        }

        octopusMap[y][x]++;

        if (octopusMap[y][x] > 9) {
            flash(octopusMap, x, y);
        }
    }

}
