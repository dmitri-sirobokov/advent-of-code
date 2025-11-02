package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day12_sample1.txt", expected = "140")
    @TestInput(input = "y2024/day12_sample2.txt", expected = "772")
    @TestInput(input = "y2024/day12_sample3.txt", expected = "1930")
    @TestInput(input = "y2024/day12.txt", expected = "1494342")
    void part1(List<String> input, long expected) {
        var result = Day12.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day12_sample1.txt", expected = "80")
    @TestInput(input = "y2024/day12.txt", expected = "893676")
    void part2(List<String> input, long expected) {
        var result = Day12.part2(input);

        assertEquals(expected, result);
    }

}