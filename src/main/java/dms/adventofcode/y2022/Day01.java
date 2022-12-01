package dms.adventofcode.y2022;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Day01 {

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

    private static ArrayList<Integer> readCalories(String input) throws IOException {
        var resultList = new ArrayList<Integer>();
        try (var br = new BufferedReader(new StringReader(input))) {
            var totalCalories = 0;
            var line = "";
            do {
                line = br.readLine();
                if (StringUtils.isEmpty(line)) {
                    resultList.add(totalCalories);
                    totalCalories = 0;
                } else {
                    var foodCal = Integer.parseInt(line);
                    totalCalories += foodCal;
                }

            } while (line != null);
            return resultList;
        }

    }
}
