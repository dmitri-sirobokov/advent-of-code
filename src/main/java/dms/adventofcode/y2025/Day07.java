package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;
import dms.adventofcode.Vector;

import java.util.List;

/**
 * Day07:
 *
 */
public class Day07 extends CodeBase {

    public static long part1(List<String> input) {
        var map = readCharMatrix(input);
        var start = findStartPos(map);
        var beams = new boolean[map.length][map[0].length];
        return countBeamSplits(map, start, beams);
    }

   public static long part2(List<String> input) {
       var map = readCharMatrix(input);
       var start = findStartPos(map);
       var timelines = new long[map.length][map[0].length];
       return countTimelines(map, start, timelines) + 1;
    }

    private static Vector findStartPos(char[][] map) {
        for (var row = 0; row < map.length; row++) {
            for (var col = 0; col < map[row].length; col++) {
                if (map[row][col] == 'S') {
                    return new Vector(col, row);
                }
            }
        }
        return new Vector(0, 0);
    }

    private static long countBeamSplits(char[][] map, Vector pos, boolean[][] beams) {
        if (pos.y() >= map.length || pos.x() >= map[0].length || pos.x() < 0 ) {
            return 0;
        }

        if (beams[pos.intY()][pos.intX()]) {
            return 0;
        }

        beams[pos.intY()][pos.intX()] = true;

        if (map[pos.intY()][pos.intX()] == '^') {
            var splitsL = countBeamSplits(map, new Vector(pos.x() - 1, pos.y() + 1), beams);
            var splitsR = countBeamSplits(map, new Vector(pos.x() + 1, pos.y() + 1), beams);
            return splitsL + splitsR + 1;
        } else {
            return countBeamSplits(map, new Vector(pos.x(), pos.y() + 1), beams);
        }
    }

    private static long countTimelines(char[][] map, Vector pos, long[][] timelines) {
        if (pos.y() >= map.length || pos.x() >= map[0].length || pos.x() < 0 ) {
            return 0;
        }

        if (timelines[pos.intY()][pos.intX()] != 0) {
            return timelines[pos.intY()][pos.intX()];
        }

        if (map[pos.intY()][pos.intX()] == '^') {
            var newTimeLines1 = countTimelines(map, new Vector(pos.x() - 1, pos.y() + 1), timelines);
            var newTimeLines2 = countTimelines(map, new Vector(pos.x() + 1, pos.y() + 1), timelines);
            timelines[pos.intY()][pos.intX()] = newTimeLines1 + newTimeLines2 + 1;

        } else {
            var newTimelines = countTimelines(map, new Vector(pos.x(), pos.y() + 1), timelines);
            timelines[pos.intY()][pos.intX()] = newTimelines;
        }
        return timelines[pos.intY()][pos.intX()];
    }

}
