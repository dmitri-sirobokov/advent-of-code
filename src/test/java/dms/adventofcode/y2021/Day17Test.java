package dms.adventofcode.y2021;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {

    @ParameterizedTest
    @TestInput(input = "y2021/day17_sample.txt", expected = "45")
    @TestInput(input = "y2021/day17.txt", expected = "1")
    void part1(List<String> input, long expected) {
        var result = Day17.part1(input);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day17_sample.txt", expected = "1")
    @TestInput(input = "y2021/day17.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day17.part2(input);
        assertEquals(expected, result);
    }

}