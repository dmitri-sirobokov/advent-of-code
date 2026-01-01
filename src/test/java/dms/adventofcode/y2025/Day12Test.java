package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day12Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day12_sample.txt", expected = "2")
    @TestInput(input = "y2025/day12.txt", expected = "463")
    void part1(List<String> input, long expected) {
        var result = Day12.part1(input);

        assertEquals(expected, result);
    }

}