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
    @TestInput(input = "y2021/day16_sample1.txt", expected = "6")
    @TestInput(input = "y2021/day16_sample2.txt", expected = "9")
    @TestInput(input = "y2021/day16_sample3.txt", expected = "14")
    @TestInput(input = "y2021/day16_sample4.txt", expected = "16")
    @TestInput(input = "y2021/day16_sample5.txt", expected = "12")
    @TestInput(input = "y2021/day16_sample6.txt", expected = "23")
    @TestInput(input = "y2021/day16_sample7.txt", expected = "31")
    @TestInput(input = "y2021/day16.txt", expected = "938")
    void part1(List<String> input, long expected) throws IOException {
        var result = Day16.part1(input);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day16_sample8.txt", expected = "3")
    @TestInput(input = "y2021/day16_sample9.txt", expected = "54")
    @TestInput(input = "y2021/day16_sample10.txt", expected = "7")
    @TestInput(input = "y2021/day16_sample11.txt", expected = "9")
    @TestInput(input = "y2021/day16_sample12.txt", expected = "1")
    @TestInput(input = "y2021/day16_sample13.txt", expected = "0")
    @TestInput(input = "y2021/day16_sample14.txt", expected = "0")
    @TestInput(input = "y2021/day16_sample15.txt", expected = "1")
    @TestInput(input = "y2021/day16.txt", expected = "1495959086337")
    void part2(List<String> input, long expected) {
        var result = Day16.part2(input);
        assertEquals(expected, result);
    }
}