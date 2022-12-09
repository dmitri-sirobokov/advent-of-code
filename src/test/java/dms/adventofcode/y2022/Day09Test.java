package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day9_sample1.txt", expected = "13")
    @TestInput(input = "y2022/day9.txt", expected = "6209")
    void part1(List<String> input, int expected) {
        var result = Day09.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day9_sample1.txt", expected = "1")
    @TestInput(input = "y2022/day9_sample2.txt", expected = "36")
    @TestInput(input = "y2022/day9.txt", expected = "2460")
    void part2(List<String> input, int expected) {
        var result = Day09.part2(input);

        assertEquals(expected, result);
    }
}