package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day14_sample.txt", expected = "12")
    void part1_sample(List<String> input, long expected) {
        var result = Day14.part1(input, 100, new Day14.Vector(11, 7));

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day14.txt", expected = "216772608")
    void part1(List<String> input, long expected) {
        var result = Day14.part1(input, 100, new Day14.Vector(101, 103));

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day14.txt", expected = "6888")
    void part2(List<String> input, long expected) {
        var result = Day14.part2(input);

        assertEquals(expected, result);
    }

}