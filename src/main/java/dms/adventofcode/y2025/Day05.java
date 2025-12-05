package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;
import dms.adventofcode.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * Day05: Cafeteria
 *
 */
public class Day05 extends CodeBase {

    public static long part1(List<String> input) {
        var ingredients = readIngredients(input);
        return ingredients.freshCount;
    }

    public static long part2(List<String> input) {
        var ingredients = readIngredients(input);
        return ingredients.freshRanges.stream()
                .mapToLong(Range::length)
                .sum();
    }

    private record Ingredients(List<Range> freshRanges, List<Long> availableIngredients, long freshCount) { }

    private static Ingredients readIngredients(List<String> lines) {
        var firstBlockEndPos = lines.indexOf("");
        var firstList = lines.subList(0, firstBlockEndPos);
        List<Range> freshRanges = new ArrayList<>();
        for (var line : firstList) {
            var parts = line.split("-");
            var start = Long.parseLong(parts[0]);
            var end = Long.parseLong(parts[1]);
            var range = new Range(start, end);
            freshRanges.add(range);
        }

        var secondList = lines.subList(firstBlockEndPos + 1, lines.size());
        var availableIngredients = secondList.stream()
                .map(Long::parseLong)
                .toList();

        var mergedRanges = mergeRanges(freshRanges);
        var freshCount = availableIngredients.stream()
                .filter(ingredient -> mergedRanges.stream()
                        .anyMatch(range -> ingredient >= range.start() && ingredient <= range.end()))
                .count();

        return new Ingredients(mergedRanges, availableIngredients, freshCount);
    }
}
