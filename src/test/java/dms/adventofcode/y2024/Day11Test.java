package dms.adventofcode.y2024;


import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test {

    @ParameterizedTest
    @TestInput(input = "y2024/day11_sample1.txt")
    void part1_sample1(List<String> input) {
        var result = Day11.part1(input, 1);
        assertEquals(7, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day11_sample2.txt")
    void part1_sample2(List<String> input) {
        var result = Day11.part1(input, 1);
        assertEquals(3, result);

        result = Day11.part1(input, 2);
        assertEquals(4, result);

        result = Day11.part1(input, 3);
        assertEquals(5, result);

        result = Day11.part1(input, 4);
        assertEquals(9, result);

        result = Day11.part1(input, 5);
        assertEquals(13, result);

        result = Day11.part1(input, 6);
        assertEquals(22, result);

        result = Day11.part1(input, 25);
        assertEquals(55312, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day11.txt")
    void part1(List<String> input) {
        var result = Day11.part1(input, 25);
        assertEquals(229043, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2024/day11.txt")
    void part2(List<String> input) {
        var result = Day11.part2(input, 75);

        assertEquals(272673043446478L, result);
    }

}