package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day03_sample.txt", expected = "357")
    @TestInput(input = "y2025/day03.txt", expected = "17332")
    void part1(List<String> input, long expected) {
        var result = Day03.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day03_sample.txt", expected = "3121910778619")
    @TestInput(input = "y2025/day03.txt", expected = "172516781546707")
    void part2(List<String> input, long expected) {
        var result = Day03.part2(input);

        assertEquals(expected, result);
    }

}