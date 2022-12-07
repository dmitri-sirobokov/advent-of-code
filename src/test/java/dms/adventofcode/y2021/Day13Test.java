package dms.adventofcode.y2021;

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test {

    @ParameterizedTest
    @TestInput(input = "y2021/day13_sample.txt", expected = "17")
    @TestInput(input = "y2021/day13.txt", expected = "827")
    void countVisibleDots_Part1(List<String> input, int expected) throws IOException {
        var result = Day13.countVisibleDots(input, true);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "y2021/day13_sample.txt", expected = "16")
    @TestInput(input = "y2021/day13.txt", expected = "104")
    void countVisibleDots_Part2(List<String> input, int expected) throws IOException {
        var result = Day13.countVisibleDots(input, false);

        // Result value corresponds to the code: EAHKRECP, enable print results to show it in console
        assertEquals(expected, result);
    }


}