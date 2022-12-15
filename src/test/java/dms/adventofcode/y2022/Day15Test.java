package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day15_sample.txt", expected = "26")
    void part1_line10(List<String> input, long expected) {
        var result = Day15.part1(input, new Day15.Position(Integer.MIN_VALUE, 10), new Day15.Position(Integer.MAX_VALUE, 10));

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day15.txt", expected = "5461729") // first guess 7257165, too high
    void part1_line20000000(List<String> input, long expected) {
        var result = Day15.part1(input, new Day15.Position(Integer.MIN_VALUE, 2000000), new Day15.Position(Integer.MAX_VALUE, 2000000));

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day15_sample.txt", expected = "56000011")
    void part2_small(List<String> input, long expected) {
        var result = Day15.part2(input,new Day15.Position(0, 0), new Day15.Position(20, 20) );

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day15.txt", expected = "10621647166538") // first guess 193043530, too low
    void part2_big(List<String> input, long expected) {
        var result = Day15.part2(input,new Day15.Position(0, 0), new Day15.Position(4000000, 4000000) );

        assertEquals(expected, result);
    }

}