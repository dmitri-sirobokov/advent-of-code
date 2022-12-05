package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("Under construction")
class Day13Test extends TestBase {

    @Test
    void countVisibleDots_Sample() throws IOException {
        var input = readResourceFile("y2021/day13_sample.txt");
        var result = Day13.countVisibleDots(input);

        assertEquals(17, result);
    }

    @Test
    void countVisibleDots_Input() throws IOException {
        var input = readResourceFile("y2021/day13.txt");
        var result = Day13.countVisibleDots(input);

        assertEquals(17, result);
    }
}