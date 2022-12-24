package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day24Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day24_sample1.txt", expected = "10")
    @TestInput(input = "y2022/day24_sample2.txt", expected = "18")
    @TestInput(input = "y2022/day24.txt", expected = "247")
    void part1(List<String> input, long expected) {
        var result = Day24.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day24_sample1.txt", expected = "1")
    @TestInput(input = "y2022/day24_sample2.txt", expected = "1")
    @TestInput(input = "y2022/day24.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day24.part2(input);

        assertEquals(expected, result);
    }


}