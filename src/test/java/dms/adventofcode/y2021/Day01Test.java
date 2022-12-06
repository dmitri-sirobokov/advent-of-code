package dms.adventofcode.y2021;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {

    @ParameterizedTest
    @TestInput(input = "y2021/day1_sample.txt", expected = "7")
    @TestInput(input = "y2021/day1.txt", expected = "1121")
    void countIncreases_Part1(List<String> input, int expected) {
        var result = Day01.countIncreases(input, 1);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day1_sample.txt", expected = "5")
    @TestInput(input = "y2021/day1.txt", expected = "1065")
    void countIncreases_Part2(List<String> input, int expected) {
        var result = Day01.countIncreases(input, 3);

        assertEquals(expected, result);
    }
}