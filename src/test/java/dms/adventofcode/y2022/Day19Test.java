package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day19Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day19_sample.txt", expected = "1")
    @TestInput(input = "y2022/day19.txt", expected = "1")
    void part1(List<String> input, long expected) throws IOException {
        var result = Day19.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day19_sample.txt", expected = "1")
    @TestInput(input = "y2022/day19.txt", expected = "1")
    void part2(List<String> input, long expected) throws IOException {
        var result = Day19.part2(input);

        assertEquals(expected, result);
    }


}