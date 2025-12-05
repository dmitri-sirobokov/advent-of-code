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
        var ingredients = scanIngredients(input);
        return ingredients.freshCount;
    }

    public static long part2(List<String> input) {
        var ingredients = scanIngredients(input);
        return ingredients.freshRanges.stream()
                .mapToLong(Range::length)
                .sum();
    }

    private static class Ingredients {
        public List<Range> freshRanges;
        public List<Long> availableIngredients;
        public long freshCount;
        Ingredients(List<Range> freshRanges, List<Long> availableIngredients) {
            this.freshRanges = freshRanges;
            this.availableIngredients = availableIngredients;
        }
    }

    private static Ingredients scanIngredients(List<String> input) {
        var ingredients = readIngredients(input);
        ingredients.freshRanges = mergeRanges(ingredients.freshRanges);
        ingredients.freshCount = ingredients.availableIngredients.stream()
                .filter(ingredient -> ingredients.freshRanges.stream()
                        .anyMatch(range -> ingredient >= range.start() && ingredient <= range.end()))
                .count();
        return ingredients;
    }

    private static Ingredients readIngredients(List<String> lines) {
        List<Range> freshRanges = new ArrayList<>();
        List<Long> availableIngredients = new ArrayList<>();
        var secondBlockPos = 0;
        for (var i = 0; i < lines.size(); i++) {
            var line = lines.get(i);
            if (line.isEmpty()) {
                secondBlockPos = i + 1;
                break;
            }

            var parts = line.split("-");
            var start = Long.parseLong(parts[0]);
            var end = Long.parseLong(parts[1]);
            var range = new Range(start, end);
            freshRanges.add(range);
        }

        freshRanges = mergeRanges(freshRanges);

        for (var i = secondBlockPos; i < lines.size(); i++) {
            var line = lines.get(i);
            var availableIngredient = Long.parseLong(line);
            availableIngredients.add(availableIngredient);
        }
        return new Ingredients(freshRanges, availableIngredients);
    }

}
