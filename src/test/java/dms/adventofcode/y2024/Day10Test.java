package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day10_sample1.txt", expected = "1")
    @TestInput(input = "y2024/day10_sample2.txt", expected = "2")
    @TestInput(input = "y2024/day10_sample3.txt", expected = "4")
    @TestInput(input = "y2024/day10_sample4.txt", expected = "3")
    @TestInput(input = "y2024/day10_sample5.txt", expected = "36")
    @TestInput(input = "y2024/day10.txt", expected = "744")
    void part1(List<String> input, long expected) {
        var result = Day10.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day10_sample5.txt", expected = "81")
    @TestInput(input = "y2024/day10_sample6.txt", expected = "3")
    @TestInput(input = "y2024/day10_sample7.txt", expected = "13")
    @TestInput(input = "y2024/day10_sample8.txt", expected = "227")
    @TestInput(input = "y2024/day10.txt", expected = "1651")
    void part2(List<String> input, long expected) {
        var result = Day10.part2(input);

        assertEquals(expected, result);
    }

}