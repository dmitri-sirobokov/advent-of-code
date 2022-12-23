package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Disabled("Under construction")
class Day22Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day22_sample.txt", expected = "6032")
    void part1_1(List<String> input, long expected) {
        var result = Day22.part1(input, 0);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day22.txt", expected = "165094")
    void part1_2(List<String> input, long expected) {
        var result = Day22.part1(input, 1);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day22_sample.txt", expected = "5031")
    void part2_1(List<String> input, long expected) {
        var result = Day22.part2(input, 2);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day22.txt", expected = "95316")
    void part2_2(List<String> input, long expected) {
        var result = Day22.part2(input, 3);

        assertEquals(expected, result);
    }


}