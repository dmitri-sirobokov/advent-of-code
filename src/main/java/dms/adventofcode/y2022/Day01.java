package dms.adventofcode.y2022;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Day 1: Calorie Counting.
 * Find elf's carrying the most calories.
 */
public class Day01 {

    /**
     * @param input - input to the puzzle
     * @param top - number of elves carrying the most calories
     * @return - Total number of the calories carried by the top elf's, specified by the parameter.
     */
    public static int calcTotCalories(List<String> input, int top) {
        var calories = readCalories(input);
        calories.sort(Collections.reverseOrder());
        var sum = 0;
        for (var i = 0; i < top; i++) {
            sum += calories.get(i);
        }
        return sum;
    }

    private static List<Integer> readCalories(List<String> input) {
        var resultList = new ArrayList<Integer>();
        var totalCalories = 0;
        for (var line : input) {
            if (StringUtils.isEmpty(line)) {
                resultList.add(totalCalories);
                totalCalories = 0;
            } else {
                var foodCal = Integer.parseInt(line);
                totalCalories += foodCal;
            }
        }

        // we reach the end of the input, add the last calculated score
        resultList.add(totalCalories);
        return resultList;
    }
}
