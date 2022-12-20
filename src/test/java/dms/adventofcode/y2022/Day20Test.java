package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day20Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day20_sample.txt", expected = "3")
    @TestInput(input = "y2022/day20.txt", expected = "3700")
    void part1(List<String> input, long expected) {
        var result = Day20.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day20_sample.txt", expected = "1623178306")
    @TestInput(input = "y2022/day20.txt", expected = "10626948369382")
    void part2(List<String> input, long expected) {
        var result = Day20.part2(input);

        assertEquals(expected, result);
    }


}