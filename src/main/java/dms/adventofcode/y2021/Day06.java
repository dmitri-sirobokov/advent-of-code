package dms.adventofcode.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day06 {

    public static long countLanternFishes(List<String> input, int days) {
        var agesCount = new long[10];
        var inputAgesInitial = input.get(0).split(",");
        for (var agesInitial : inputAgesInitial) {
            agesCount[Integer.parseInt(agesInitial.trim())]++;
        }

        for (var i = 0; i < days; i ++) {
            for (var age = 0; age < 10; age++) {
                var ageCount = agesCount[age];
                if (age == 0) {
                    // time to reproduce, reset timer to 6 days (+1 because in the same loop will be reduced to 6)
                    agesCount[7] += ageCount;
                    // create new fishes with the timer 8 days
                    agesCount[9] = ageCount;
                } else {
                    // reduce the counter for this day
                    agesCount[age - 1] = ageCount;
                }

                // and reset this age slot.
                agesCount[age] = 0;
            }
        }
        return Arrays.stream(agesCount).sum();
    }
}
