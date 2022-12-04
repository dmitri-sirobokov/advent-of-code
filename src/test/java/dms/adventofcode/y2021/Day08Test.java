package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day08Test extends TestBase {

    @Test
    void countUniqueNumberOfSegments_Sample_Part1() throws IOException {
        var input = readResourceFile("y2021/day8_sample.txt");
        var result = Day08.countUniqueNumberOfSegmentsPart1(input);

        assertEquals(26, result);
    }

    @Test
    void countUniqueNumberOfSegments_Input_Part1() throws IOException {
        var input = readResourceFile("y2021/day8.txt");
        var result = Day08.countUniqueNumberOfSegmentsPart1(input);

        assertEquals(284, result);
    }

    @Test
    void countOutputValues_Sample_Part2() throws IOException {
        var input = readResourceFile("y2021/day8_sample.txt");
        var result = Day08.countOutputValuesPart2(input);

        assertEquals(61229, result);
    }

    @Test
    void countOutputValues_Input_Part2() throws IOException {
        var input = readResourceFile("y2021/day8.txt");
        var result = Day08.countOutputValuesPart2(input);

        assertEquals(973499, result);
    }
}