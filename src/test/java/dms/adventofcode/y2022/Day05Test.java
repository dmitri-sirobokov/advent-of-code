package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test {

    @ParameterizedTest
    @TestInput(input = "y2022/day5_sample.txt", expected = "CMZ")
    @TestInput(input = "y2022/day5.txt", expected = "BWNCQRMDB")
    void rearrangePart1(List<String> input, String expected) {
        var result = Day05.rearrange(input, Day05.CraneType.CRATE_MOVER_9000);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day5_sample.txt", expected = "MCD")
    @TestInput(input = "y2022/day5.txt", expected = "NHWZCBNBF")
    void rearrangePart2(List<String> input, String expected) {
        var result = Day05.rearrange(input, Day05.CraneType.CRATE_MOVER_9001);

        assertEquals(expected, result);
    }
}