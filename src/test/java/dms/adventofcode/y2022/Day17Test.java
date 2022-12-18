package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day17Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day17_sample.txt", expected = "3068")
    @TestInput(input = "y2022/day17.txt", expected = "3227")
    void part1(List<String> input, long expected) throws IOException {
        var result = Day17.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
   @TestInput(input = "y2022/day17_sample.txt", expected = "1514285714288")
    @TestInput(input = "y2022/day17.txt", expected = "1597714285698")
    void part2(List<String> input, long expected) throws IOException {
        var result = Day17.part2(input);

        assertEquals(expected, result);
    }


}