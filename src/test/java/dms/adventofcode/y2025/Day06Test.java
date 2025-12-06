package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day06_sample.txt", expected = "4277556")
    @TestInput(input = "y2025/day06.txt", expected = "4583860641327")
    void part1(List<String> input, long expected) {
        var result = Day06.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day06_sample.txt", expected = "3263827")
    @TestInput(input = "y2025/day06.txt", expected = "11602774058280")
    void part2(List<String> input, long expected) {
        var result = Day06.part2(input);

        assertEquals(expected, result);
    }

}