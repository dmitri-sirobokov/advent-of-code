package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Day 20: Grove Positioning System
 */
public class Day20 extends CodeBase {

    public static long part1(List<String> input) {
        return decrypt(input, 1, 1);
    }

    public static long part2(List<String> input) {
        return decrypt(input, 811589153, 10);
    }
    private static long decrypt(List<String> input, long decryptionKey, int iterations) {
        var initialList = new Number[input.size()];
        for (var i = 0; i < input.size(); i++) {
            initialList[i] = new Number(decryptionKey * Integer.parseInt(input.get(i)), i);
        }

        var resultList = Arrays.copyOf(initialList, initialList.length);

        for (var k = 0; k < iterations; k++) {
            for (Number number : initialList) {
                if (number.value == 0) {
                    continue;
                }

                // initial numbers could have duplicates, we need to find position of that match original number
                var sourceIndex = findNumber(resultList, n -> n.originalPosition == number.originalPosition);
                var sourceNumber = resultList[sourceIndex];

                var destIndex = (sourceIndex + sourceNumber.value) % (resultList.length - 1);
                if (destIndex < 0) {
                    destIndex = resultList.length + destIndex - 1;
                }

                resultList = ArrayUtils.remove(resultList, sourceIndex);
                resultList = ArrayUtils.insert((int) destIndex, resultList, sourceNumber);
            }
        }

        var n0Index = findNumber(resultList, n -> n.value == 0);
        var n1000Index = (n0Index + 1000) % (resultList.length);
        var n2000Index = (n0Index + 2000) % (resultList.length);
        var n3000Index = (n0Index + 3000) % (resultList.length);
        return resultList[n1000Index].value + resultList[n2000Index].value + resultList[n3000Index].value;
    }


    private static int findNumber(Number[] numbers, Predicate<Number> predicate) {
        int result = -1;
        for (var j = 0; j < numbers.length; j++) {
            if (predicate.test(numbers[j])) {
                result = j;
                break;
            }
        }
        return result;
    }

    private record Number(long value, int originalPosition) { }

}
