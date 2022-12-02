package dms.adventofcode.y2021;

import java.util.List;

public class Day03 {

    public static long calcPower(List<String> input) {
        var numberOfBits = input.get(0).length();
        var gammaRateCounters = new long[numberOfBits];

        for (int i = 0; i < numberOfBits; i++) {
            gammaRateCounters[i] = countBits(input, i);
        }
        var gammaValue = 0L;
        var epsilonValue = 0L;
        for (var i = 0; i < numberOfBits; i++) {
            var gammaBit = 2 * gammaRateCounters[i] >= input.size() ? 1 : 0;
            var epsilonBit = 2 * gammaRateCounters[i] < input.size() ? 1 : 0;
            gammaValue = 2 * gammaValue + gammaBit;
            epsilonValue = 2 * epsilonValue + epsilonBit;
        }

        return gammaValue * epsilonValue;
    }

    public static long calcLifeSupportRating(List<String> input) {
        var numberOfBits = input.get(0).length();
        var oxygenFilter = "";
        var oxygenFilteredInput = input;
        var co2Filter = "";
        var co2FilteredInput = input;
        for (var i = 0; i < numberOfBits; i++) {
            if (oxygenFilteredInput.size() > 1) {
                oxygenFilter += 2 * countBits(oxygenFilteredInput, i) >= oxygenFilteredInput.size() ? "1" : "0";
                oxygenFilteredInput = filterInput(oxygenFilteredInput, oxygenFilter);
            }

            if (co2FilteredInput.size() > 1) {
                co2Filter += 2 * countBits(co2FilteredInput, i) < co2FilteredInput.size() ? "1" : "0";
                co2FilteredInput = filterInput(co2FilteredInput, co2Filter);
            }
        }

        var oxygenRate = Long.parseLong(oxygenFilteredInput.get(0), 2);
        var co2Rate = Long.parseLong(co2FilteredInput.get(0), 2);

        return oxygenRate * co2Rate;
    }

    private static List<String> filterInput(List<String> input, String startsWith) {
        return input.stream().filter(line -> line.startsWith(startsWith)).toList();
    }

    private static int countBits(List<String> input, int position) {
        var count = 0;
        for (var line : input) {
            if (line.charAt(position) == '1') {
                count++;
            }
        }
        return count;
    }

}
