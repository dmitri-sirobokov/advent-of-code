package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day01Test extends TestBase {

    @Test
    void countIncreases_Sample_Part1() throws IOException {
        var input = readResourceFile("y2021/day1_sample.txt");
        var result = Day01.countIncreases(input, 1);

        assertEquals(7, result);
    }

    @Test
    void countIncreases_Input_Part1() throws IOException {
        var input = readResourceFile("y2021/day1.txt");
        var result = Day01.countIncreases(input, 1);

        assertEquals(1121, result);
    }

    @Test
    void countIncreases_Sample_Part2() throws IOException {
        var input = readResourceFile("y2021/day1_sample.txt");
        var result = Day01.countIncreases(input, 3);

        assertEquals(5, result);
    }

    @Test
    void countIncreases_Input_Part2() throws IOException {
        var input = readResourceFile("y2021/day1.txt");
        var result = Day01.countIncreases(input, 3);

        assertEquals(1065, result);
    }
}