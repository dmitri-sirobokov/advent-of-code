package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;

import java.util.List;

/**
 * Day03: Lobby
 *
 */
public class Day03 extends CodeBase {

    private static long searchMax(int maxN, int[] bank) {
        var max = 0L;
        var savedPos = 0;
        for (var i = 0; i < maxN; i++) {
            max *= 10;
            var digitMax = 0;
            for (var pos = savedPos; pos < bank.length - maxN + i + 1; pos++) {
                if (digitMax < bank[pos]) {
                    digitMax = bank[pos];
                    savedPos = pos;
                }
            }
            max += digitMax;
            savedPos++;
        }
        return max;
    }

    private static long calcBatteries(List<String> input, int maxN) {
        var batteries = readMatrix(input);
        var result = 0L;
        for (int[] bank : batteries) {
            var maxResult = searchMax(maxN, bank);

            result += maxResult;
        }
        return result;

    }

    public static long part1(List<String> input) {
        return calcBatteries(input, 2);
    }

    public static long part2(List<String> input) {
        return calcBatteries(input, 12);
    }
}
