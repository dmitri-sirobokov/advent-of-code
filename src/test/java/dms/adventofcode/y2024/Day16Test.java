package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day16_sample1.txt", expected = "7036")
    @TestInput(input = "y2024/day16_sample2.txt", expected = "11048")
    @TestInput(input = "y2024/day16.txt", expected = "98484")
    void part1(List<String> input, long expected) {
        var result = Day16.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day16_sample1.txt", expected = "1")
    @TestInput(input = "y2024/day16.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day16.part2(input);

        assertEquals(expected, result);
    }

}