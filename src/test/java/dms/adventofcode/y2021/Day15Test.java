package dms.adventofcode.y2021;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Day 15: Chiton
 */
class Day15Test {

    @ParameterizedTest
    @TestInput(input = "y2021/day15_sample.txt", expected = "40")
    @TestInput(input = "y2021/day15.txt", expected = "447")
    void part1(List<String> input, long expected) {
        var result = Day15.part1(input);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day15_sample.txt", expected = "315")
    @TestInput(input = "y2021/day15.txt", expected = "2825")
    void part2(List<String> input, long expected) {
        var result = Day15.part2(input);
        assertEquals(expected, result);
    }
}