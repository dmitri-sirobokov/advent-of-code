package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day03Test extends TestBase {

    @Test
    void calcPower_Sample() throws IOException {
        var input = readResourceFile("y2021/day3_sample.txt");
        var result = Day03.calcPower(input);

        assertEquals(198, result);
    }

    @Test
    void calcLifeSupportRating_Sample() throws IOException {
        var input = readResourceFile("y2021/day3_sample.txt");
        var result = Day03.calcLifeSupportRating(input);

        assertEquals(230, result);
    }

    @Test
    void calcPower_Input() throws IOException {
        var input = readResourceFile("y2021/day3.txt");
        var result = Day03.calcPower(input);

        assertEquals(2967914, result);
    }

    @Test
    void calcLifeSupportRating_Input() throws IOException {
        var input = readResourceFile("y2021/day3.txt");
        var result = Day03.calcLifeSupportRating(input);

        assertEquals(7041258, result);
    }
}