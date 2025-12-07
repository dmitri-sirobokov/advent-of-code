package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day07_sample.txt", expected = "21")
    @TestInput(input = "y2025/day07.txt", expected = "1543")
    void part1(List<String> input, long expected) {
        var result = Day07.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day07_sample.txt", expected = "40")
    @TestInput(input = "y2025/day07.txt", expected = "3223365367809")
    void part2(List<String> input, long expected) {
        var result = Day07.part2(input);

        assertEquals(expected, result);
    }

}