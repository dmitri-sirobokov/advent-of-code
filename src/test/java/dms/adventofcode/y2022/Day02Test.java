package dms.adventofcode.y2022;

import dms.adventofcode.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day02Test extends TestBase {

    @Test
    void calcScore_Sample() throws IOException {
        var input = readResourceFile("y2022/day2_sample.txt");
        var result = Day02.calcScore(input);
        assertEquals(12, result);
    }

    @Test
    void calcScore_Input() throws IOException {
        var input = readResourceFile("y2022/day2.txt");
        var result = Day02.calcScore(input);
        assertEquals(9541, result);
    }
}