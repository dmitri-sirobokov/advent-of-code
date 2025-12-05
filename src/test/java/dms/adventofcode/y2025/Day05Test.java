package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day05_sample.txt", expected = "3")
    @TestInput(input = "y2025/day05.txt", expected = "567")
    void part1(List<String> input, long expected) {
        var result = Day05.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day05_sample.txt", expected = "14")
    @TestInput(input = "y2025/day05.txt", expected = "354149806372909")
    void part2(List<String> input, long expected) {
        var result = Day05.part2(input);

        assertEquals(expected, result);
    }

}