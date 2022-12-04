package dms.adventofcode.y2021;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Day08 {
    public static int countUniqueNumberOfSegmentsPart1(List<String> input) {
        var count = 0;
        for (var line : input) {
            var patternParts = line.split("\\|");
            // var patternInput = patternParts[0];
            var patternOutput = patternParts[1];
            var outputDigitPatterns = patternOutput.split(" ");
            count += Arrays.stream(outputDigitPatterns)
                    .filter(p -> p.length() == 2 || p.length() == 3 || p.length() == 4 || p.length() == 7)
                    .count();
        }
        return count;
    }

    public static int countOutputValuesPart2(List<String> input) {
        var count = 0;
        for (var line : input) {
            var patternParts = line.split(" \\| ");
            var patternInput = patternParts[0];
            var patternDigits = Arrays.asList(patternInput.split(" "));

            var digitPatterns = new String[10];

            // easy part - get unique patterns first that resolve just to a single digit
            digitPatterns[1] = patternDigits.stream().filter(p -> p.length() == 2).findFirst().get();
            digitPatterns[4] = patternDigits.stream().filter(p -> p.length() == 4).findFirst().get();
            digitPatterns[7] = patternDigits.stream().filter(p -> p.length() == 3).findFirst().get();
            digitPatterns[8] = patternDigits.stream().filter(p -> p.length() == 7).findFirst().get();

            // Segment A can be calculated by subtracting 7-pattern from 1-pattern
            var segmentA = turnOff(digitPatterns[7], digitPatterns[1]);

            var segmentBD = turnOff(digitPatterns[4], digitPatterns[1]);

            // removing either D segment from 8 should produce number 0, there is only one combination possible,
            // removing B should produce no match
            var digit0Candidate1 = turnOff(digitPatterns[8], segmentBD.substring(0, 1));
            var digit0Candidate2 = turnOff(digitPatterns[8], segmentBD.substring(1, 2));
            digitPatterns[0] = patternDigits.stream().filter(p -> p.length() == 6)
                    .filter(p -> match(p, digit0Candidate1) || match(p, digit0Candidate2))
                    .findAny().get();

            var segmentD = turnOff(digitPatterns[8], digitPatterns[0]);
            var segmentB = turnOff(segmentBD, segmentD);

            // Number 9 resolution: combine segments from number 4 and segment A.
            // Remove those segment from all number-9 candidates, if there is a single remaining segment,
            // than it is a missing piece of number 9, it is also a G-segment.
            var segmentG = patternDigits.stream().filter(p -> p.length() == 6)
                    .map(p -> turnOff(p, digitPatterns[4] + segmentA))
                    .filter(p -> p.length() == 1)
                    .findAny()
                    .get();
            digitPatterns[9] = digitPatterns[4] + segmentA + segmentG;
            digitPatterns[3] = turnOff(digitPatterns[9], segmentB);

            // there is only one possible candidate for number 5 with BD-segment
            digitPatterns[5] = patternDigits.stream().filter(p -> p.length() == 5)
                    .filter(p -> turnOff(segmentBD, p).length() == 0)
                    .findAny()
                    .get();
            digitPatterns[2] = patternDigits.stream().filter(p -> p.length() == 5)
                    .filter(p -> !match(p, digitPatterns[3]) && !match(p, digitPatterns[5]))
                    .findAny()
                    .get();

            digitPatterns[6] = patternDigits.stream().filter(p -> p.length() == 6)
                    .filter(p -> !match(p, digitPatterns[0]) && !match(p, digitPatterns[9]))
                    .findAny()
                    .get();

            var patternOutput = patternParts[1];
            var patternOutputDigits = patternOutput.split(" ");
            var resultStr = Arrays.stream(patternOutputDigits).map(p -> resolveDigit(digitPatterns, p))
                    .map(i -> i.toString())
                    .collect(Collectors.joining());
            var resultInt = Integer.parseInt(resultStr);
            count += resultInt;

        }
        return count;
    }

    private static String turnOn(String current, String onSegments) {
        for (var i = 0; i < onSegments.length(); i++) {
            var onSegment = onSegments.substring(i, i + 1);
            if (!current.contains(onSegment)) {
                current += onSegment;
            }
        }
        return current;
    }

    private static String turnOff(String current, String onSegments) {
        for (var i = 0; i < onSegments.length(); i++) {
            var onSegment = onSegments.substring(i, i + 1);
            if (current.contains(onSegment)) {
                current = current.replaceAll(onSegment, "");
            }
        }
        return current;
    }

    private static boolean match(String pattern1, String pattern2) {
        return pattern1.length() == pattern2.length() && turnOff(pattern1, pattern2).isEmpty();
    }

    private static int resolveDigit(String[] patterns, String segments) {
        for (var i = 0; i < patterns.length; i++) {
            if (match(patterns[i], segments)) {
                return i;
            }
        }
        return -1;
    }
}
