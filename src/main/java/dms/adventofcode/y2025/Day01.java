package dms.adventofcode.y2025;


import java.util.List;

/**
 * Day01: Secret Entrance
 *
 */
public class Day01 {

    private static int[] readDeltas(List<String> lines) {
        return lines.stream().mapToInt(line -> {
            var dir = line.substring(0, 1);
            var delta  = Integer.parseInt(line.substring(1));
            return dir.equals("L") ? -delta : delta;
        }).toArray();
    }

    public static long part1(List<String> input) {
        var pos = 50L;
        var crossings = 0L;
        var deltas = readDeltas(input);
        for (var delta : deltas) {
            var next = pos + delta;
            if (next % 100 == 0) {
                crossings++;
            }
            pos = next;
        }
        return crossings;
    }

    public static long part2(List<String> input) {
        var pos = 50L;
        var crossings = 0L;
        var deltas = readDeltas(input);
        for (var delta : deltas) {
            var next = pos + delta;
            var prevBucket = Math.floorDiv(pos, 100);
            var nextBucket = Math.floorDiv(next, 100);
            crossings += Math.abs(nextBucket - prevBucket);
            // do not count starting 0-position as crossing when moving to lower number
            if (pos % 100 == 0 && delta < 0) {
                crossings--;
            }
            // count as crossing when reaching 0 position and moving to lower number
            if (next % 100 == 0 && delta < 0) {
                crossings++;
            }
            pos = next;
        }
        return crossings;
    }
}
