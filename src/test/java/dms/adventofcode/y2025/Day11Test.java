package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day11_sample.txt", expected = "5")
    @TestInput(input = "y2025/day11.txt", expected = "749")
    void part1(List<String> input, long expected) {
        var result = Day11.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day11_sample2.txt", expected = "2")
    @TestInput(input = "y2025/day11.txt", expected = "420257875695750")
    void part2(List<String> input, long expected) {
        var result = Day11.part2(input);

        assertEquals(expected, result);
    }

}