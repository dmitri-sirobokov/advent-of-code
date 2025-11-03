package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day15_sample1.txt", expected = "2028")
    @TestInput(input = "y2024/day15.txt", expected = "1490942")
    void part1(List<String> input, long expected) {
        var result = Day15.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day15_sample3.txt", expected = "9021")
    @TestInput(input = "y2024/day15.txt", expected = "1519202")
    void part2(List<String> input, long expected) {
        var result = Day15.part2(input);

        assertEquals(expected, result);
    }

}