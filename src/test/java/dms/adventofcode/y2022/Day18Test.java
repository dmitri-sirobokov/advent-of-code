package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day18Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day18_sample.txt", expected = "64")
    @TestInput(input = "y2022/day18.txt", expected = "4310")
    void part1(List<String> input, long expected) throws IOException {
        var result = Day18.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
   @TestInput(input = "y2022/day18_sample.txt", expected = "58")
    @TestInput(input = "y2022/day18.txt", expected = "2466")
    void part2(List<String> input, long expected) throws IOException {
        var result = Day18.part2(input);

        assertEquals(expected, result);
    }


}