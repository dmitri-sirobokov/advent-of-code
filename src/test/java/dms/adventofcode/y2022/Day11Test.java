package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day11_sample.txt", expected = "10605")
    @TestInput(input = "y2022/day11.txt", expected = "316888")
    void part1(List<String> input, long expected) {
        var result = Day11.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day11_sample.txt", expected = "2713310158")
    @TestInput(input = "y2022/day11.txt", expected = "35270398814")
    void part2(List<String> input, long expected) {
        var result = Day11.part2(input);

        assertEquals(expected, result);
    }

}