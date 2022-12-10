package dms.adventofcode.y2021;

import dms.adventofcode.TestInput;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("Under construction")
class Day15Test {

    @ParameterizedTest
    @TestInput(input = "y2021/day15_sample.txt", expected = "40")
    @TestInput(input = "y2021/day15.txt", expected = "1")
    void part1(List<String> input, long expected) {
        var result = Day15.part1(input);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day15_sample.txt", expected = "1")
    @TestInput(input = "y2021/day15.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day15.part2(input);
        assertEquals(expected, result);
    }
}