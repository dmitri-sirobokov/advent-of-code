package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day8_sample.txt", expected = "21")
    @TestInput(input = "y2022/day8.txt", expected = "1849")
    void part1(List<String> input, int expected) {
        var result = Day08.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day8_sample.txt", expected = "8")
    @TestInput(input = "y2022/day8.txt", expected = "201600")
    void part2(List<String> input, int expected) {
        var result = Day08.part2(input);

        assertEquals(expected, result);
    }
}