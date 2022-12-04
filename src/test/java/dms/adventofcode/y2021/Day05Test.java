package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day05Test extends TestBase {

    @Test
    void countOverlaps_Sample_Part1() throws IOException {
        var input = readResourceFile("y2021/day5_sample.txt");
        var result = Day05.countOverlapPoints(input, 2, false);

        assertEquals(5, result);
    }

    @Test
    void countOverlaps_Sample_Part2() throws IOException {
        var input = readResourceFile("y2021/day5_sample.txt");
        var result = Day05.countOverlapPoints(input, 2, true);

        assertEquals(12, result);
    }

    @Test
    void countOverlaps_Input_Part1() throws IOException {
        var input = readResourceFile("y2021/day5.txt");
        var result = Day05.countOverlapPoints(input, 2, false);

        assertEquals(7414, result);
    }

    @Test
    void countOverlaps_Input_Part2() throws IOException {
        var input = readResourceFile("y2021/day5.txt");
        var result = Day05.countOverlapPoints(input, 2, true);

        assertEquals(19676, result);
    }
}