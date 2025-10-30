package dms.adventofcode.y2024;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day02_sample.txt", expected = "2")
    @TestInput(input = "y2024/day02.txt", expected = "660")
    void part1(List<String> input, long expected) {
        var result = Day02.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day02_sample.txt", expected = "4")
    @TestInput(input = "y2024/day02.txt", expected = "689")
    void part2(List<String> input, long expected) {
        var result = Day02.part2(input);

        assertEquals(expected, result);
    }

}