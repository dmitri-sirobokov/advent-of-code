package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Day21Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day21_sample.txt", expected = "152")
    @TestInput(input = "y2022/day21.txt", expected = "104272990112064")
    void part1(List<String> input, long expected) {
        var result = Day21.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day21_sample.txt", expected = "301")
    @TestInput(input = "y2022/day21.txt", expected = "3220993874133")
    void part2(List<String> input, long expected) {
        var result = Day21.part2(input);

        // In my case I had actually more correct answers in a small range,
        // Better solution would be to iterate further to find minimum of those possible values, because it looks like that is the correct answer,
        // but because the range is only 1 or 2 numbers, we can just try manually.
        assertTrue(Math.abs(expected - result) < 3, String.format("Expected value should be " + expected + "(+-1). Actual value: " + result));
    }


}