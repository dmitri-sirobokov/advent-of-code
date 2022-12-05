package dms.adventofcode.y2022;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class Day05Test extends TestBase {

    @Test
    void rearrangePart1_Sample() throws IOException {
        var input = readResourceFile("y2022/day5_sample.txt");
        var result = Day05.rearrange(input, Day05.CraneType.CRATE_MOVER_9000);

        assertEquals("CMZ", result);
    }

    @Test
    void rearrangePart1_Input() throws IOException {
        var input = readResourceFile("y2022/day5.txt");
        var result = Day05.rearrange(input, Day05.CraneType.CRATE_MOVER_9000);

        assertEquals("BWNCQRMDB", result);
    }

    @Test
    void rearrangePart2_Sample() throws IOException {
        var input = readResourceFile("y2022/day5_sample.txt");
        var result = Day05.rearrange(input, Day05.CraneType.CRATE_MOVER_9001);

        assertEquals("MCD", result);
    }

    @Test
    void rearrangePart2_Input() throws IOException {
        var input = readResourceFile("y2022/day5.txt");
        var result = Day05.rearrange(input, Day05.CraneType.CRATE_MOVER_9001);

        assertEquals("NHWZCBNBF", result);
    }
}