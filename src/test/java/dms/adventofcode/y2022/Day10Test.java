package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day10Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day10_sample.txt", expected = "13140")
    @TestInput(input = "y2022/day10.txt", expected = "14620")
    void part1(List<String> input, int expected) {
        var result = Day10.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day10.txt", expected =
            """
                    ###....##.####.###..#..#.###..####.#..#.
                    #..#....#.#....#..#.#..#.#..#.#....#..#.
                    ###.....#.###..#..#.####.#..#.###..#..#.
                    #..#....#.#....###..#..#.###..#....#..#.
                    #..#.#..#.#....#.#..#..#.#.#..#....#..#.
                    ###...##..#....#..#.#..#.#..#.#.....##..
                    """)
    void part2(List<String> input, String expected) {
        var result = Day10.part2(input);

        /* Output: "BJFRHRFU" */
        assertEquals(expected, result);
    }

}