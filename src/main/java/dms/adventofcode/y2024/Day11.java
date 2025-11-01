package dms.adventofcode.y2024;

import java.util.HashMap;
import java.util.List;

/**
 * Day11: Plutonian Pebbles
 *
 */
public class Day11 {

    private static class BlinkState {
        private final HashMap<Long, Long> stoneNumberCounts = new HashMap<>();
    }

    private static void addStoneCount(BlinkState blinkState, long stoneNumber, long stoneCount) {
        blinkState.stoneNumberCounts.compute(stoneNumber, (key, value) -> value == null ? stoneCount : value + stoneCount);
    }

    private static BlinkState blink(BlinkState state) {
        var newBlinkState = new BlinkState();
        for (var stoneNumberCount : state.stoneNumberCounts.entrySet()) {
            if (stoneNumberCount.getKey() == 0) {
                // If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
                addStoneCount(newBlinkState, 1, stoneNumberCount.getValue());
            } else {
                var stoneDigits = Long.toString(stoneNumberCount.getKey());
                if (stoneDigits.length() % 2 == 0) {
                    // If the stone is engraved with a number that has an even number of digits,
                    // it is replaced by two stones. The left half of the digits are engraved
                    // on the new left stone, and the right half of the digits are engraved on the
                    // new right stone. (The new numbers don't keep extra leading zeroes:
                    // 1000 would become stones 10 and 0.)
                    var firstHalf = Long.parseLong(stoneDigits.substring(0, stoneDigits.length() / 2));
                    var secondHalf = Long.parseLong(stoneDigits.substring(stoneDigits.length() / 2));

                    addStoneCount(newBlinkState, firstHalf, stoneNumberCount.getValue());
                    addStoneCount(newBlinkState, secondHalf, stoneNumberCount.getValue());
                } else {
                    // If none of the other rules apply, the stone is replaced by a new stone;
                    // the old stone's number multiplied by 2024 is engraved on the new stone.
                    addStoneCount(newBlinkState, stoneNumberCount.getKey() * 2024, stoneNumberCount.getValue());
                }
            }
        }

        return newBlinkState;
    }

    private static long calcTotalStonesCount(List<String> input, int blinks) {
        var blinkState = new BlinkState();
        var parts = input.getFirst().split(" ");
        for (var part : parts) {
            var stoneNumber = Long.parseLong(part);
            blinkState.stoneNumberCounts.compute(stoneNumber, (k, v) -> v == null ? 1 : v + 1);
        }

        for (var i = 0; i < blinks; i++) {
            blinkState = blink(blinkState);
        }
        return blinkState.stoneNumberCounts
                .values()
                .stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    public static long part1(List<String> input, int blinks) {
        return calcTotalStonesCount(input, blinks);
    }

    public static long part2(List<String> input, int blinks) {
        return calcTotalStonesCount(input, blinks);
    }
}
