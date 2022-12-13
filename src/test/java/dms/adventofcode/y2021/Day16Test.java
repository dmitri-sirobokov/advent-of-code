package dms.adventofcode.y2021;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Day 16: Packet Decoder
 */
class Day16Test {

    @ParameterizedTest
    @TestInput(input = "y2021/day16_sample.txt", expected = "1")
    @TestInput(input = "y2021/day16.txt", expected = "1")
    void part1(List<String> input, long expected) throws IOException {
        var result = Day16.part1(input);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day16_sample.txt", expected = "1")
    @TestInput(input = "y2021/day16.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day16.part2(input);
        assertEquals(expected, result);
    }
}