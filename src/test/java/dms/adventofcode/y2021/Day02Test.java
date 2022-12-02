package dms.adventofcode.y2021;

import dms.adventofcode.y2022.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day02Test extends TestBase {

    @Test
    void calcPositionProduct_Sample() throws IOException {
        var input = readResourceFile("y2021/day2_sample.txt");
        var result = Day02.calcPositionProduct(input);

        assertEquals(900, result);
    }

    @Test
    void calcPositionProduct_Input() throws IOException {
        var input = readResourceFile("y2021/day2.txt");
        var result = Day02.calcPositionProduct(input);

        assertEquals(2006917119, result);
    }
}