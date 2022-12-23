package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day23Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day23_sample1.txt", expected = "25")
    @TestInput(input = "y2022/day23_sample2.txt", expected = "110")
    @TestInput(input = "y2022/day23.txt", expected = "3762")
    void part1(List<String> input, long expected) {
        var result = Day23.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day23_sample1.txt", expected = "4")
    @TestInput(input = "y2022/day23_sample2.txt", expected = "20")
    @TestInput(input = "y2022/day23.txt", expected = "997")
    void part2(List<String> input, long expected) {
        var result = Day23.part2(input);

        assertEquals(expected, result);
    }

}