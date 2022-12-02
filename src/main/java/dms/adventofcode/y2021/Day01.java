package dms.adventofcode.y2021;

import java.util.ArrayList;
import java.util.List;

public class Day01 {

    public static long countIncreases(List<String> input, int windowSize) {
        var measurements = input.stream().mapToInt(Integer::parseInt).toArray();
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
