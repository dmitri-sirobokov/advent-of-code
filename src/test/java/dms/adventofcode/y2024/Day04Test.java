package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day04_sample.txt", expected = "18")
    @TestInput(input = "y2024/day04.txt", expected = "2532")
    void part1(List<String> input, long expected) {
        var result = Day04.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day04_sample.txt", expected = "9")
    @TestInput(input = "y2024/day04.txt", expected = "1941")
    void part2(List<String> input, long expected) {
        var result = Day04.part2(input);

        assertEquals(expected, result);
    }

}