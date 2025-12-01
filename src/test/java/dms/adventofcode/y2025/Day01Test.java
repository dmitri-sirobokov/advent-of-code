package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day01_sample.txt", expected = "3")
    @TestInput(input = "y2025/day01.txt", expected = "1043")
    void part1(List<String> input, long expected) {
        var result = Day01.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day01_sample.txt", expected = "6")
    @TestInput(input = "y2025/day01.txt", expected = "5963")
    void part2(List<String> input, long expected) {
        var result = Day01.part2(input);

        assertEquals(expected, result);
    }

}