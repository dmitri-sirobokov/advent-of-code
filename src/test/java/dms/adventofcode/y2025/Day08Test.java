package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day08_sample.txt", expected = "40")
    void part1_sample(List<String> input, long expected) {
        var result = Day08.part1(input, 10);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day08.txt", expected = "97384")
    void part1(List<String> input, long expected) {
        var result = Day08.part1(input, 1000);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day08_sample.txt", expected = "25272")
    @TestInput(input = "y2025/day08.txt", expected = "9003685096")
    void part2(List<String> input, long expected) {
        var result = Day08.part2(input, 6000);

        assertEquals(expected, result);
    }

}