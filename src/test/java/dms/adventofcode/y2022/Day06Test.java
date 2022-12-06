package dms.adventofcode.y2022;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day06Test extends TestBase {

    // TODO: Parameterized Test
    @Test
    void part1_sample1() throws IOException {
        var input = readResourceFile("y2022/day6_sample1.txt");
        var result = Day06.findMarkerIndex(input);

        assertEquals(7, result);
    }

    @Test
    void part1_sample2() throws IOException {
        var input = readResourceFile("y2022/day6_sample2.txt");
        var result = Day06.findMarkerIndex(input);

        assertEquals(5, result);
    }

    @Test
    void part1_sample3() throws IOException {
        var input = readResourceFile("y2022/day6_sample3.txt");
        var result = Day06.findMarkerIndex(input);

        assertEquals(6, result);
    }

    @Test
    void part1_sample4() throws IOException {
        var input = readResourceFile("y2022/day6_sample4.txt");
        var result = Day06.findMarkerIndex(input);

        assertEquals(10, result);
    }

    @Test
    void part1_sample5() throws IOException {
        var input = readResourceFile("y2022/day6_sample5.txt");
        var result = Day06.findMarkerIndex(input);

        assertEquals(11, result);
    }

    @Test
    void part1_input() throws IOException {
        var input = readResourceFile("y2022/day6.txt");
        var result = Day06.findMarkerIndex(input);

        assertEquals(1566, result);
    }

    @Test
    void part2_sample1() throws IOException {
        var input = readResourceFile("y2022/day6_sample1.txt");
        var result = Day06.findMessageIndex(input);

        assertEquals(19, result);
    }

    @Test
    void part2_sample2() throws IOException {
        var input = readResourceFile("y2022/day6_sample2.txt");
        var result = Day06.findMessageIndex(input);

        assertEquals(23, result);
    }

    @Test
    void part2_sample3() throws IOException {
        var input = readResourceFile("y2022/day6_sample3.txt");
        var result = Day06.findMessageIndex(input);

        assertEquals(23, result);
    }

    @Test
    void part2_sample4() throws IOException {
        var input = readResourceFile("y2022/day6_sample4.txt");
        var result = Day06.findMessageIndex(input);

        assertEquals(29, result);
    }

    @Test
    void part2_sample5() throws IOException {
        var input = readResourceFile("y2022/day6_sample5.txt");
        var result = Day06.findMessageIndex(input);

        assertEquals(26, result);
    }

    @Test
    void part2_input() throws IOException {
        var input = readResourceFile("y2022/day6.txt");
        var result = Day06.findMessageIndex(input);

        assertEquals(2265, result);
    }
}