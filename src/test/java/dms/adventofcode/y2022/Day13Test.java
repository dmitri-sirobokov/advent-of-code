package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day13_sample.txt", expected = "13")
    @TestInput(input = "y2022/day13.txt", expected = "5605")
    void part1(List<String> input, long expected) {
        var result = Day13.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day13_sample.txt", expected = "140")
    @TestInput(input = "y2022/day13.txt", expected = "24969")
    void part2(List<String> input, long expected) {
        var result = Day13.part2(input);

        assertEquals(expected, result);
    }

}