package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;

import java.util.List;

/**
 * Day03: Lobby
 *
 */
public class Day03 extends CodeBase {

    private static class CalcResult {
        private long maxLabel;
        private final int[] maxLabels;

        CalcResult(int size) {
            this.maxLabels = new int[size];
        }

        private boolean updateMax(int n, int k, int[] labels) {
            if (k + maxLabels.length - n - 1 < labels.length && this.maxLabels[n] < labels[k]) {
                // updating one digit to higher value is per definition found higher score
                this.maxLabels[n] = labels[k];

                // reset remain digits
                for (int i = 1; i < maxLabels.length - n; i++) {
                    this.maxLabels[n + i] = labels[k + i];
                }

                // put all updated digits together
                this.maxLabel = 0;
                for (int label : maxLabels) {
                    this.maxLabel = 10 * this.maxLabel + label;
                }
                return true;
            }
            return false;
        }
    }

    private static CalcResult searchMax(int maxN, int[] bank) {
        var result = new CalcResult(maxN);
        var savedPos = 0;
        for (var i = 0; i < maxN; i++) {
            for (var pos = savedPos; pos < bank.length; pos++) {
                if (result.updateMax(i, pos, bank)) {
                    savedPos = pos;
                }
            }
            savedPos++;
        }
        return result;
    }

    private static long calcBatteries(List<String> input, int maxN) {
        var batteries = readMatrix(input);
        var result = 0L;
        for (int[] bank : batteries) {
            var maxResult = searchMax(maxN, bank);

            result += maxResult.maxLabel;
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
