package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day01 extends CodeBase {

    public static long countIncreases(String input, int windowSize) throws IOException {
        var lines = readLines(input);
        var measurements = lines.stream().mapToInt(Integer::parseInt).toArray();
        var deviations = calcDeviations(measurements, windowSize);
        return deviations.stream().filter(i -> i > 0).count();
    }

    private static List<Integer> calcDeviations(int[] measurements, int windowSize) {
        var resultList = new ArrayList<Integer>();
        for (var i = windowSize; i < measurements.length; i++) {
            resultList.add(measurements[i] - measurements[i - windowSize]);
        }
        return resultList;
    }
}
