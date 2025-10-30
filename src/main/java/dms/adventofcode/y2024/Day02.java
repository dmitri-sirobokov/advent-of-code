package dms.adventofcode.y2024;


import java.util.ArrayList;
import java.util.List;

/**
 * Day 02:
 *
 */
public class Day02 {

    private static List<List<Integer>> readReports(List<String> input) {
        var result = new ArrayList<List<Integer>>();
        for (var line : input) {
            var row = new ArrayList<Integer>();
            var parts = line.split("\\s+");
            for  (var part : parts) {
                row.add(Integer.parseInt(part));
            }
            result.add(row);
        }
        return result;
    }

    private static boolean isSafeReport(List<Integer> report, boolean ascending) {
        var reportCopy = new ArrayList<>(report);
        for (var i = 1; i < reportCopy.size(); i++) {
            var l0 = reportCopy.get(i - 1);
            var l1 = reportCopy.get(i);

            var dif =  ascending ? l1 - l0 : l0 - l1;
            if (dif <= 0 || dif > 3) {
                return false;
            }
        }
        return true;
    }

    private static int countSafeReports(List<String> input, boolean  allowOneError) {
        var reports = readReports(input);
        var result = 0;
        for (var report : reports) {
            for (var i = -1; i < report.size(); i++) {
                var reportCopy = new ArrayList<>(report);
                if (i >= 0) {
                    reportCopy.remove(i);
                }
                if (isSafeReport(reportCopy, true) || isSafeReport(reportCopy, false)) {
                    result++;
                    break;
                }
                if (!allowOneError) {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Count how many safe reports are in the input.
     */
    public static long part1(List<String> input) {
        return countSafeReports(input, false);
    }

    public static long part2(List<String> input) {
        return countSafeReports(input, true);
    }
}
