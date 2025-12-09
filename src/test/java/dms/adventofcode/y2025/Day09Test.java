package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day09Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day09_sample.txt", expected = "50")
    @TestInput(input = "y2025/day09.txt", expected = "4776487744")
    void part1(List<String> input, long expected) {
        var result = Day09.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day09_sample.txt", expected = "24")
    @TestInput(input = "y2025/day09.txt", expected = "1560299548")
    void part2(List<String> input, long expected) {
        var result = Day09.part2(input);

        assertEquals(expected, result);
    }

}