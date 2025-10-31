package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day09_sample.txt", expected = "1928")
    @TestInput(input = "y2024/day09.txt", expected = "6386640365805")
    void part1(List<String> input, long expected) {
        var result = Day09.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day09_sample.txt", expected = "2858")
    @TestInput(input = "y2024/day09.txt", expected = "6423258376982")
    void part2(List<String> input, long expected) {
        var result = Day09.part2(input);

        assertEquals(expected, result);
    }

}