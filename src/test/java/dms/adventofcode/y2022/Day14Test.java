package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day14Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day14_sample.txt", expected = "24")
    @TestInput(input = "y2022/day14.txt", expected = "674")
    void part1(List<String> input, long expected) {
        var result = Day14.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day14_sample.txt", expected = "93")
    @TestInput(input = "y2022/day14.txt", expected = "24958") // 1st guess 22942
    void part2(List<String> input, long expected) {
        var result = Day14.part2(input);

        assertEquals(expected, result);
    }

}