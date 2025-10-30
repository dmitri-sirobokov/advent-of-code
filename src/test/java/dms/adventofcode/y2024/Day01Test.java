package dms.adventofcode.y2024;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day01Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day01_sample.txt", expected = "11")
    @TestInput(input = "y2024/day01.txt", expected = "3714264")
    void part1(List<String> input, long expected) {
        var result = Day01.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day01_sample.txt", expected = "31")
    @TestInput(input = "y2024/day01.txt", expected = "18805872")
    void part2(List<String> input, long expected) {
        var result = Day01.part2(input);

        assertEquals(expected, result);
    }

}