package dms.adventofcode.y2021;

import dms.adventofcode.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day07Test extends TestBase {

    @Test
    void calcMinFuelConsumption_Sample_Part1() throws IOException {
        var input = readResourceFile("y2021/day7_sample.txt");
        var result = Day07.calcMinFuelConsumptionPart1(input);

        assertEquals(37, result);
    }

    @Test
    void calcMinFuelConsumption_Sample_Part2() throws IOException {
        var input = readResourceFile("y2021/day7_sample.txt");
        var result = Day07.calcMinFuelConsumptionPart2(input);

        assertEquals(168, result);
    }

    @Test
    void calcMinFuelConsumption_Input_Part1() throws IOException {
        var input = readResourceFile("y2021/day7.txt");
        var result = Day07.calcMinFuelConsumptionPart1(input);

        assertEquals(347449, result);
    }

    @Test
    void calcMinFuelConsumption_Input_Part2() throws IOException {
        var input = readResourceFile("y2021/day7.txt");
        var result = Day07.calcMinFuelConsumptionPart2(input);

        assertEquals(98039527, result);
    }

}