package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day07_sample.txt", expected = "3749")
    @TestInput(input = "y2024/day07.txt", expected = "2437272016585")
    void part1(List<String> input, long expected) {
        var result = Day07.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day07_sample.txt", expected = "11387")
    @TestInput(input = "y2024/day07.txt", expected = "162987117690649")
    void part2(List<String> input, long expected) {
        var result = Day07.part2(input);

        assertEquals(expected, result);
    }

}