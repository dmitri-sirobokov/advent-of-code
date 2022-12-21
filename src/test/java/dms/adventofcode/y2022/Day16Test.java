package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("Under construction")
class Day16Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day16_sample.txt", expected = "1651")
    @TestInput(input = "y2022/day16.txt", expected = "1940")
    void part1(List<String> input, long expected) {
        var result = Day16.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day16_sample.txt", expected = "1707")
    @TestInput(input = "y2022/day16.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day16.part2(input);

        assertEquals(expected, result);
    }


}