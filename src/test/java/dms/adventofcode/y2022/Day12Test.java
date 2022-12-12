package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day12_sample.txt", expected = "31")
    @TestInput(input = "y2022/day12.txt", expected = "370")
    void part1(List<String> input, long expected) {
        var result = Day12.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day12_sample.txt", expected = "29")
    @TestInput(input = "y2022/day12.txt", expected = "363")
    void part2(List<String> input, long expected) {
        var result = Day12.part2(input);

        assertEquals(expected, result);
    }

}