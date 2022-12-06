package dms.adventofcode.y2022;

import dms.adventofcode.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day03Test extends TestBase {

    @Test
    void rucksackReorganizationPrioritySum_Sample() throws IOException {
        var input = readResourceFile("y2022/day3_sample.txt");
        var result = Day03.rucksackReorganizationPrioritySum(input);

        assertEquals(157, result);
    }

    @Test
    void rucksackReorganizationPrioritySum_Input() throws IOException {
        var input = readResourceFile("y2022/day3.txt");
        var result = Day03.rucksackReorganizationPrioritySum(input);

        assertEquals(8085, result);
    }

    @Test
    void groupPrioritySum_Sample() throws IOException {
        var input = readResourceFile("y2022/day3_sample.txt");
        var result = Day03.groupPrioritySum(input);

        assertEquals(70, result);
    }

    @Test
    void groupPrioritySum_Input() throws IOException {
        var input = readResourceFile("y2022/day3.txt");
        var result = Day03.groupPrioritySum(input);

        assertEquals(2515, result);
    }
}