package dms.adventofcode.y2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Day 01: Historian Hysteria
 *
 */
public class Day01 {

    private static class Table {
        private List<Integer> col1 = new ArrayList<>();
        private List<Integer> col2 = new ArrayList<>();
    }

    private static Table readTable(List<String> input) {
        var result = new Table();

        for (String line : input) {
            var parts = line.split("\\s+");
            if (parts.length != 2) { continue; }
            var num1 = Integer.parseInt(parts[0]);
            var num2 = Integer.parseInt(parts[1]);
            result.col1.add(num1);
            result.col2.add(num2);
        }
        if (result.col1.size() != result.col2.size()) {
            throw new RuntimeException("Number of columns do not match");
        }

        return result;
    }

    /**
     * Calculated distance
     */
    public static long part1(List<String> input) {
        var result = readTable(input);
        result.col1.sort(Integer::compareTo);
        result.col2.sort(Integer::compareTo);
        var dist = 0;
        for (int i = 0; i < result.col1.size(); i++) {
            dist += Math.abs(result.col1.get(i) - result.col2.get(i));
        }
        return dist;
    }

    /**
     * Calculate similarity score
     */
    public static long part2(List<String> input) {
        var result = readTable(input);
        var map = new HashMap<Integer, Integer>();
        for (int i = 0; i < result.col1.size(); i++) {
            map.put(result.col1.get(i), 0);
        }
        for (int i = 0; i < result.col2.size(); i++) {
            var key = result.col2.get(i);
            var value = map.get(key);
            if (value != null) {
                value++;
                map.replace(key, value);
            }
        }
        var score = 0;
        for (int i = 0; i < result.col1.size(); i++) {
            var key = result.col1.get(i);
            var value = map.get(key);
            score += key * value;
        }
        return score;
    }
}
