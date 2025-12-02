package dms.adventofcode.y2025;


import java.util.Arrays;
import java.util.List;

/**
 * Day02:
 *
 */
public class Day02 {
    private record Range(long start, long end) {
    }

    public static long part1(List<String> input) {
        var ranges = readRanges(input);
        return sumInvalidNumbers(ranges, 2);
    }

    public static long part2(List<String> input) {
        var ranges = readRanges(input);
        return sumInvalidNumbers(ranges, Integer.MAX_VALUE);

    }

    private static List<Range> readRanges(List<String> lines) {
        return lines.stream().flatMap(line -> Arrays.stream(line.split(",")).map(r ->
        {
            var parts = r.split("-");
            return new Range(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        })).toList();
    }

    private static long sumInvalidNumbers(List<Range> ranges, int maxReps) {
        var result = 0L;
        for (Range range : ranges) {
            for (var n = range.start; n <= range.end; n++) {
                if (isInvalidNumber(n, range, maxReps)) {
                    result += n;
                }
            }
        }
        return result;
    }

    private static boolean isInvalidNumber(long n, Range range, int maxReps) {
        var str = Long.toString(n);
        for (int len = 1; len <= str.length() / 2; len++) {
            var subStr = str.substring(0, len);
            var subStr2 = subStr;

            for (var rep = 2; rep <= maxReps; rep++) {
                subStr2 += subStr;

                var l = Long.parseLong(subStr2);

                if (l == n) {
                    return true;
                }
                if (l > range.end) {
                    break;
                }
            }
        }
        return false;
    }
}
