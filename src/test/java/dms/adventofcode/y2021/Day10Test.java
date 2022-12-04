package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test extends TestBase {

    @Test
    void errorScore_Sample() throws IOException {
        var input = readResourceFile("y2021/day10_sample.txt");
        var result = Day10.errorScore(input);

        assertEquals(26397, result);
    }

    @Test
    void errorScore_Input() throws IOException {
        var input = readResourceFile("y2021/day10.txt");
        var result = Day10.errorScore(input);

        assertEquals(344193, result);
    }

    @Test
    void repair_Sample() throws IOException {
        var input = readResourceFile("y2021/day10_sample.txt");
        var result = Day10.repairScore(input);

        assertEquals(288957, result);
    }

    @Test
    void repair_Input() throws IOException {
        var input = readResourceFile("y2021/day10.txt");
        var result = Day10.repairScore(input);

        assertEquals(3241238967L, result);
    }
}