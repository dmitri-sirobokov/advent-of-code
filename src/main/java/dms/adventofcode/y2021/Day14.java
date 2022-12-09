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

    private static long calcSteps(List<String> input, int steps) {
        var codes = readCodes(input);
        String polymer = readPolymer(input);
        var codeCounts = new long[256];
        var pairCounts = new HashMap<CodePair, Long>();

        // increment character counts
        for (var i = 0; i < polymer.length(); i++) {
            codeCounts[polymer.charAt(i)]++;
        }

        // increment pair counts
        for (var i = 0; i < polymer.length() - 1; i++) {
            var codePair = new CodePair(polymer.charAt(i), polymer.charAt(i + 1));
            pairCounts.putIfAbsent(codePair, 0L);
            pairCounts.compute(codePair, (key,value) -> value + 1);
        }

        for (var step = 0; step < steps; step++) {

            var newPairCounts = new HashMap<CodePair, Long>();
            for (var pairEntrySet : pairCounts.entrySet()) {
                if (pairEntrySet.getValue() > 0) {
                    var insertChar = codes.get(pairEntrySet.getKey());
                    codeCounts[insertChar] = codeCounts[insertChar] + pairEntrySet.getValue();
                    var pair1 = new CodePair(pairEntrySet.getKey().first, insertChar);
                    var pair2 = new CodePair(insertChar, pairEntrySet.getKey().second);
                    newPairCounts.putIfAbsent(pairEntrySet.getKey(), 0L);
                    newPairCounts.putIfAbsent(pair1, 0L);
                    newPairCounts.putIfAbsent(pair2, 0L);
                    newPairCounts.compute(pair1, (key, value) -> value + pairEntrySet.getValue());
                    newPairCounts.compute(pair2, (key, value) -> value + pairEntrySet.getValue());
                }
            }
            pairCounts = newPairCounts;
        }

        return Arrays.stream(codeCounts).max().orElse(0) - Arrays.stream(codeCounts).filter(c -> c != 0).min().orElse(0);
    }

    private static String readPolymer(List<String> input) {
        return input.get(0);
    }

    private static Map<CodePair, Character> readCodes(List<String> input) {
        var result = new HashMap<CodePair, Character>();
        for (var i = 2; i < input.size(); i++) {
            var line = input.get(i);
            var lineParts = line.split(" -> ");

            result.put(new CodePair(lineParts[0].charAt(0), lineParts[0].charAt(1)),
                    lineParts[1].charAt(0));
        }
        return result;
    }

    private record CodePair (char first, char second) { }

    private static class Polymer {

        public Polymer(CharSequence initialChain) {

        }
    }
}
