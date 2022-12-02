package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day04Test extends TestBase {

    @Test
    void calcBingoScore_Sample_Part1() throws IOException {
        var input = readResourceFile("y2021/day4_sample.txt");
        var result = Day04.calcBingoScore(input, Day04.Strategy.FirstBoard);

        assertEquals(4512, result);
    }

    @Test
    void calcBingoScore_Input_Part1() throws IOException {
        var input = readResourceFile("y2021/day4.txt");
        var result = Day04.calcBingoScore(input, Day04.Strategy.FirstBoard);

        assertEquals(39984, result);
    }

    @Test
    void calcBingoScore_Sample_Part2() throws IOException {
        var input = readResourceFile("y2021/day4_sample.txt");
        var result = Day04.calcBingoScore(input, Day04.Strategy.LastBoard);

        assertEquals(1924, result);
    }

    @Test
    void calcBingoScore_Input_Part2() throws IOException {
        var input = readResourceFile("y2021/day4.txt");
        var result = Day04.calcBingoScore(input, Day04.Strategy.LastBoard);

        assertEquals(8468, result);
    }
}