package dms.adventofcode.y2022;

import dms.adventofcode.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day04Test extends TestBase {

    @Test
    void countFullyOverlappedSectionsPart1_Sample() throws IOException {
        var input = readResourceFile("y2022/day4_sample.txt");
        var result = Day04.countFullyOverlappedSectionsPart1(input);

        assertEquals(2, result);
    }

    @Test
    void countFullyOverlappedSectionsPart1_Input() throws IOException {
        var input = readResourceFile("y2022/day4.txt");
        var result = Day04.countFullyOverlappedSectionsPart1(input);

        assertEquals(511, result);
    }

    @Test
    void countHasOverlappedSectionsPart2_Sample() throws IOException {
        var input = readResourceFile("y2022/day4_sample.txt");
        var result = Day04.countHasOverlappedSectionsPart2(input);

        assertEquals(4, result);
    }

    @Test
    void countHasOverlappedSectionsPart2_Input() throws IOException {
        var input = readResourceFile("y2022/day4.txt");
        var result = Day04.countHasOverlappedSectionsPart2(input);

        assertEquals(821, result);
    }
}