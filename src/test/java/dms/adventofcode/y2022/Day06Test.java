package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day6_sample1.txt", expected = "7")
    @TestInput(input = "y2022/day6_sample2.txt", expected = "5")
    @TestInput(input = "y2022/day6_sample3.txt", expected = "6")
    @TestInput(input = "y2022/day6_sample4.txt", expected = "10")
    @TestInput(input = "y2022/day6_sample5.txt", expected = "11")
    @TestInput(input = "y2022/day6.txt", expected = "1566")
    void part1_tests(List<String> input, int expected) {
        var result = Day06.findMarkerIndex(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day6_sample1.txt", expected = "19")
    @TestInput(input = "y2022/day6_sample2.txt", expected = "23")
    @TestInput(input = "y2022/day6_sample3.txt", expected = "23")
    @TestInput(input = "y2022/day6_sample4.txt", expected = "29")
    @TestInput(input = "y2022/day6_sample5.txt", expected = "26")
    @TestInput(input = "y2022/day6.txt", expected = "2265")
    void part2_tests(List<String> input, int expected) {
        var result = Day06.findMessageIndex(input);

        assertEquals(expected, result);
    }
}