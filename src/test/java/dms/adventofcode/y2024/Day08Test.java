package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day08_sample1.txt", expected = "2")
    @TestInput(input = "y2024/day08_sample2.txt", expected = "4")
    @TestInput(input = "y2024/day08_sample3.txt", expected = "14")
    @TestInput(input = "y2024/day08.txt", expected = "332")
    void part1(List<String> input, long expected) {
        var result = Day08.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day08.txt", expected = "1174")
    void part2(List<String> input, long expected) {
        var result = Day08.part2(input);

        assertEquals(expected, result);
    }

}