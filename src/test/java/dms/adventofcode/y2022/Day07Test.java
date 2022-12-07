package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day07Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day7_sample.txt", expected = "95437")
    @TestInput(input = "y2022/day7.txt", expected = "1432936")
    void part1(List<String> input, int expected) {
        var result = Day07.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day7_sample.txt", expected = "24933642")
    @TestInput(input = "y2022/day7.txt", expected = "272298")
    @Disabled("Under construction")
    void part2(List<String> input, int expected) {
        var result = Day07.part2(input);

        assertEquals(expected, result);
    }
}