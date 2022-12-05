package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test extends TestBase {

    @Test
    void countPaths_Part1_Sample1() throws IOException {
        var input = readResourceFile("y2021/day12_sample1.txt");
        var result = Day12.countPaths(input, false);

        assertEquals(10, result);
    }

    @Test
    void countPaths_Part1_Sample2() throws IOException {
        var input = readResourceFile("y2021/day12_sample2.txt");
        var result = Day12.countPaths(input, false);

        assertEquals(19, result);
    }

    @Test
    void countPaths_Part1_Sample3() throws IOException {
        var input = readResourceFile("y2021/day12_sample3.txt");
        var result = Day12.countPaths(input, false);

        assertEquals(226, result);
    }

    @Test
    void countPaths_Part1_Input() throws IOException {
        var input = readResourceFile("y2021/day12.txt");
        var result = Day12.countPaths(input, false);

        assertEquals(3369, result);
    }

    @Test
    void countPaths_Part2_Sample1() throws IOException {
        var input = readResourceFile("y2021/day12_sample1.txt");
        var result = Day12.countPaths(input, true);

        assertEquals(36, result);
    }

    @Test
    void countPaths_Part2_Sample2() throws IOException {
        var input = readResourceFile("y2021/day12_sample2.txt");
        var result = Day12.countPaths(input, true);

        assertEquals(103, result);
    }

    @Test
    void countPaths_Part2_Sample3() throws IOException {
        var input = readResourceFile("y2021/day12_sample3.txt");
        var result = Day12.countPaths(input, true);

        assertEquals(3509, result);
    }

    @Test
    void countPaths_Part2_Input() throws IOException {
        var input = readResourceFile("y2021/day12.txt");
        var result = Day12.countPaths(input, true);

        assertEquals(85883, result);
    }

}