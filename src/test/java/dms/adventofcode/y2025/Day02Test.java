package dms.adventofcode.y2025;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day02Test {

    @ParameterizedTest
    @TestInput(input = "y2025/day02_sample.txt", expected = "1227775554")
    @TestInput(input = "y2025/day02.txt", expected = "22062284697")
    void part1(List<String> input, long expected) {
        var result = Day02.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2025/day02_sample.txt", expected = "4174379265")
    @TestInput(input = "y2025/day02.txt", expected = "46666175279")
    void part2(List<String> input, long expected) {
        var result = Day02.part2(input);

        assertEquals(expected, result);
    }

}