package dms.adventofcode.y2022;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class Day01Test extends TestBase {

    @Test
    void findMaxCalories_Sample1() throws IOException {
        var input = readResourceFile("y2022/day1_sample.txt");
        var result = Day01.calcTotCalories(input, 1);

        assertEquals(24000, result);
    }

    @Test
    void findMaxCalories_Sample2() throws IOException {
        var input = readResourceFile("y2022/day1_sample.txt");
        var result = Day01.calcTotCalories(input, 3);

        assertEquals(45000, result);
    }

    @Test
    void findMaxCalories_Input_Part1() throws IOException {
        var input = readResourceFile("y2022/day1.txt");
        var result = Day01.calcTotCalories(input, 1);

        assertEquals(67027, result);
    }

    @Test
    void findMaxCalories_Input_Part2() throws IOException {
        var input = readResourceFile("y2022/day1.txt");
        var result = Day01.calcTotCalories(input, 3);

        assertEquals(197291, result);
    }
}