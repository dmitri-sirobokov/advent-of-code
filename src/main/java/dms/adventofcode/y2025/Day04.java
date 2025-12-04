package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;
import dms.adventofcode.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Day04: Printing Department
 *
 */
public class Day04 extends CodeBase {

    private static boolean isPaperRoll(char[][] map, int i, int j) {
        return i >= 0 && j >= 0 && i < map.length && j < map[0].length && map[i][j] == '@';
    }

    public static long solve(List<String> input, boolean part2) {
        var chars = readCharMatrix(input);
        int[][] adjArray = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                {1, 1}, {-1, -1}, {-1, 1}, {1, -1},
        };

        var result = 0;
        while (true) {
            var removeList = new ArrayList<Vector>();
            for (var i = 0; i < chars.length; i++) {
                for (var j = 0; j < chars[i].length; j++) {
                    if (isPaperRoll(chars, i, j)) {
                        var adjCount = 0;
                        for (int[] adjPos : adjArray) {
                            if (isPaperRoll(chars, i + adjPos[0], j + adjPos[1])) {
                                adjCount++;
                            }
                        }
                        if (adjCount < 4) {
                            removeList.add(new Vector(j, i));
                            result++;
                        }
                    }
                }
            }

            for (var removeItem : removeList) {
                chars[removeItem.intY()][removeItem.intX()] = '.';
            }
            if (!part2 || removeList.isEmpty()) {
                break;
            }
            removeList.clear();
        }
        return result;
    }


    public static long part1(List<String> input) {
        return solve(input, false);
    }

    public static long part2(List<String> input) {
        return solve(input, true);
    }
}
