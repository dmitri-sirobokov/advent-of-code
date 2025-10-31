package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day06_sample.txt", expected = "41")
    @TestInput(input = "y2024/day06.txt", expected = "4883")
    void part1(List<String> input, long expected) {
        var result = Day06.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day06_sample.txt", expected = "6")
    @TestInput(input = "y2024/day06.txt", expected = "1655")
    void part2(List<String> input, long expected) {
        var result = Day06.part2(input);

        assertEquals(expected, result);
    }

}