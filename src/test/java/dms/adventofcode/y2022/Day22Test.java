package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day22Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day22_sample.txt", expected = "6032")
    @TestInput(input = "y2022/day22.txt", expected = "165094")
    void part1(List<String> input, long expected) {
        var result = Day22.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day22_sample.txt", expected = "5031")
    @TestInput(input = "y2022/day22.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day22.part2(input);

        assertEquals(expected, result);
    }


}