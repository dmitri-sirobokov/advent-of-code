package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day05_sample.txt", expected = "143")
    @TestInput(input = "y2024/day05.txt", expected = "4662")
    void part1(List<String> input, long expected) {
        var result = Day05.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day05_sample.txt", expected = "123")
    @TestInput(input = "y2024/day05.txt", expected = "5900")
    void part2(List<String> input, long expected) {
        var result = Day05.part2(input);

        assertEquals(expected, result);
    }

}