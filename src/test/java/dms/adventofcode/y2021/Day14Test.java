package dms.adventofcode.y2021;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {

    @ParameterizedTest
    @TestInput(input = "y2021/day14_sample.txt", expected = "1588")
    @TestInput(input = "y2021/day14.txt", expected = "2891")
    void part1(List<String> input, int expected) {
        var result = Day14.part1(input);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day14_sample.txt", expected = "2188189693529")
    @TestInput(input = "y2021/day14.txt", expected = "4607749009683")
    void part2(List<String> input, long expected) {
        var result = Day14.part2(input);
        assertEquals(expected, result);
    }
}