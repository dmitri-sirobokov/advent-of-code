package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day04_sample.txt", expected = "13")
    @TestInput(input = "y2025/day04.txt", expected = "1395")
    void part1(List<String> input, long expected) {
        var result = Day04.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day04_sample.txt", expected = "43")
    @TestInput(input = "y2025/day04.txt", expected = "8451")
    void part2(List<String> input, long expected) {
        var result = Day04.part2(input);

        assertEquals(expected, result);
    }

}