package dms.adventofcode.y2021;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {
    public static long part1(List<String> input) {
        return calcSteps(input, 10);
    }

    public static long part2(List<String> input) {
        return calcSteps(input, 40);
    }

    private static Map<String, Character> readCodes(List<String> input) {
        var result = new HashMap<String, Character>();
        for (var i = 2; i < input.size(); i++) {
            var line = input.get(i);
            var lineParts = line.split(" -> ");
            result.put(lineParts[0], lineParts[1].charAt(0));
        }
        return result;
    }

    private static class CodeStep {
        private byte[] charsCount;
    }

    private static long calcSteps(List<String> input, int steps) {
        var polymer = input.get(0);

        var codes = readCodes(input);
        for (var step = 0; step < steps; step++) {
            var sb = new StringBuilder();
            sb.append(polymer.charAt(0));
            for (var i = 0; i < polymer.length() - 1; i++) {
                var key = polymer.substring(i, i + 2);
                var insertCode = codes.get(key);
                if (insertCode != null) {
                    sb.append(insertCode);
                }
                sb.append(key.charAt(1));
            }
            polymer = sb.toString();
        }

        var charCounts = new int[256];
        for (var i = 0; i < polymer.length(); i++) {
            charCounts[polymer.charAt(i)]++;
        }
        return Arrays.stream(charCounts).asLongStream().max().getAsLong() - Arrays.stream(charCounts).asLongStream().filter(c -> c != 0).min().getAsLong();
    }
}
