package dms.adventofcode.y2021;

import dms.adventofcode.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day06Test extends TestBase {

    @Test
    void countLanternFishes_Sample_Part1() throws IOException {
        var input = readResourceFile("y2021/day6_sample.txt");
        var result = Day06.countLanternFishes(input, 80);

        assertEquals(5934, result);
    }

    @Test
    void countLanternFishes_Input_Part1() throws IOException {
        var input = readResourceFile("y2021/day6.txt");
        var result = Day06.countLanternFishes(input, 80);

        assertEquals(383160, result);
    }

    @Test
    void countLanternFishes_Sample_Part2() throws IOException {
        var input = readResourceFile("y2021/day6_sample.txt");
        var result = Day06.countLanternFishes(input, 256);

        assertEquals(26984457539L, result);
    }

    @Test
    void countLanternFishes_Input_Part2() throws IOException {
        var input = readResourceFile("y2021/day6.txt");
        var result = Day06.countLanternFishes(input, 256);

        assertEquals(1721148811504L, result);
    }
}