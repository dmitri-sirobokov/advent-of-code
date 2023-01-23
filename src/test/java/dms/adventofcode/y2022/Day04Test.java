package dms.adventofcode.y2022;

import dms.adventofcode.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day04Test extends TestBase {

    @Test
    void part1_Sample() throws IOException {
        var input = readResourceFile("y2022/day4_sample.txt");
        var result = Day04.part1(input);

        assertEquals(2, result);
    }

    @Test
    void part1_Input() throws IOException {
        var input = readResourceFile("y2022/day4.txt");
        var result = Day04.part1(input);

        assertEquals(511, result);
    }

    @Test
    void part2_Sample() throws IOException {
        var input = readResourceFile("y2022/day4_sample.txt");
        var result = Day04.part2(input);

        assertEquals(4, result);
    }

    @Test
    void part2_Input() throws IOException {
        var input = readResourceFile("y2022/day4.txt");
        var result = Day04.part2(input);

        assertEquals(821, result);
    }
}