package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01 extends CodeBase {

    /**
     * @param input - input to the puzzle
     * @param top - number of elves carrying the most calories
     * @return - Total number of the calories carried by the top elf's, specified by the parameter.
     * @throws IOException
     */
    public static int calcTotCalories(String input, int top) throws IOException {
        var calories = readCalories(input);
        calories.sort(Collections.reverseOrder());
        var sum = 0;
        for (var i = 0; i < top; i++) {
            sum += calories.get(i);
        }
        return sum;
    }

    private static List<Integer> readCalories(String input) throws IOException {
        var resultList = new ArrayList<Integer>();
        var lines = readLines(input);
        var totalCalories = 0;
        for (var line : lines) {
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
