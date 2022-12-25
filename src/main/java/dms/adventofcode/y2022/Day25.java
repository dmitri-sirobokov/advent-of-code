package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.List;

/**
 * Day 25: Full of Hot Air
 */
public class Day25 extends CodeBase {

    private static char[] decToSnafuMap = new char[] { '=', '-', '0', '1', '2' };

    public static String part1(List<String> input) {

        var totalDec = 0L;
        for (var line : input) {
            totalDec += snafuToDecimal(line);
        }
        return decimalToSnafu(totalDec);
    }

    public static long part2(List<String> input) {
        return 0;
    }

    public static String decimalToSnafu(long decimal) {
        var str = new StringBuilder();
        var temp = decimal;
        while (temp != 0) {
            temp += 2;
            var mod = temp % 5;
            str.insert(0, decToSnafuMap[(int)mod]);
            temp /= 5;
        }
        return str.toString();
    }

    public static long snafuToDecimal(String snafu) {
        var result = 0L;
        for (var i = 0; i < snafu.length(); i++) {
            result *= 5;
            result += switch (snafu.charAt(i)) {
                case '=' -> -2;
                case '-' -> -1;
                case '0' -> 0;
                case '1' -> 1;
                case '2' -> 2;
                default -> throw new RuntimeException("Invalid character in snafu number '" + snafu + "' at position " + i);
            };

        }
        return result;
    }
}
