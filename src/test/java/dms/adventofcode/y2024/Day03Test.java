package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day03_sample1.txt", expected = "161")
    @TestInput(input = "y2024/day03.txt", expected = "189527826")
    void part1(List<String> input, long expected) {
        var result = Day03.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day03_sample2.txt", expected = "48")
    @TestInput(input = "y2024/day03.txt", expected = "63013756")
    void part2(List<String> input, long expected) {
        var result = Day03.part2(input);

        assertEquals(expected, result);
    }

}