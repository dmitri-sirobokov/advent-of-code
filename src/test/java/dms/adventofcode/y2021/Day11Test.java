package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day11Test extends TestBase {

    @Test
    void countFlashes_Sample() throws IOException {
        var input = readResourceFile("y2021/day11_sample.txt");
        var result = Day11.countFlashes(input);

        assertEquals(1656, result);
    }

    @Test
    void countFlashes_Input() throws IOException {
        var input = readResourceFile("y2021/day11.txt");
        var result = Day11.countFlashes(input);

        assertEquals(1723, result);
    }

    @Test
    void countIterations_Sample() throws IOException {
        var input = readResourceFile("y2021/day11_sample.txt");
        var result = Day11.countIterations(input);

        assertEquals(195, result);
    }

    @Test
    void countIterations_Input() throws IOException {
        var input = readResourceFile("y2021/day11.txt");
        var result = Day11.countIterations(input);

        assertEquals(327, result);
    }
}