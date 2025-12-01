package dms.adventofcode.y2025;


import java.util.List;

/**
 * Day01: Secret Entrance
 *
 */
public class Day01 {

    private static int[] readNumbers(List<String> lines) {
        return lines.stream().mapToInt(line -> {
            var dir = line.substring(0, 1);
            var num  = Integer.parseInt(line.substring(1));
            return dir.equals("L") ? -num : num;
        }).toArray();
    }

    public static long part1(List<String> input) {
        var current = 50;
        var result = 0;
        var numbers = readNumbers(input);
        for (var num : numbers) {
            current += num;
            current = current % 100;
            if (current < 0) {
                current += 100;
            }
            if (current == 0) {
                result++;
            }
        }
        return result;
    }

    public static long part2(List<String> input) {
        var current = 50;
        var result = 0;
        var numbers = readNumbers(input);
        for (var num : numbers) {
            var newCurrent = current + num;
            if (newCurrent == 0) {
                result++;
            } else if (newCurrent < 0) {
                result -= newCurrent / 100;
            } else {
                result += newCurrent / 100;
            }
            // if starting position was already zero then do not count it.
            if (current != 0 && newCurrent < 0) {
                result++;
            }


            newCurrent = newCurrent % 100;
            if (newCurrent < 0) {
                newCurrent += 100;
            }
            current = newCurrent;
        }
        return result;
    }
}
