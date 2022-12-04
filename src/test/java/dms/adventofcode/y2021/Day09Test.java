package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day09Test extends TestBase {

    @Test
    void calcRiskLevel_Sample() throws IOException {
        var input = readResourceFile("y2021/day9_sample.txt");
        var result = Day09.calcRiskLevel(input);

        assertEquals(15, result);
    }

    @Test
    void calcRiskLevel_Input() throws IOException {
        var input = readResourceFile("y2021/day9.txt");
        var result = Day09.calcRiskLevel(input);

        assertEquals(631, result);
    }

    @Test
    void findLargestBasins_Sample() throws IOException {
        var input = readResourceFile("y2021/day9_sample.txt");
        var result = Day09.findLargestBasins(input);

        assertEquals(1134, result);
    }

    @Test
    void findLargestBasins_Input() throws IOException {
        var input = readResourceFile("y2021/day9.txt");
        var result = Day09.findLargestBasins(input);

        assertEquals(821560, result);
    }
}