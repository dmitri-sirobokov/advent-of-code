package dms.adventofcode.y2022;

import dms.adventofcode.TestInput;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test {

    @Test
    void decimalToSnafu() {
        assertEquals("1=11-2", Day25.decimalToSnafu(2022));
        assertEquals("1-0---0", Day25.decimalToSnafu(12345));
        assertEquals("1121-1110-1=0", Day25.decimalToSnafu(314159265));
        assertEquals("2011-=2=-1020-1===-1", Day25.decimalToSnafu(39021690220321L));
    }

    @Test
    void snafuToDecimal() {
        assertEquals(2022, Day25.snafuToDecimal("1=11-2"));
        assertEquals(12345, Day25.snafuToDecimal("1-0---0"));
        assertEquals(314159265, Day25.snafuToDecimal("1121-1110-1=0"));
        assertEquals(39021690220321L, Day25.snafuToDecimal("2011-=2=-1020-1===-1"));
    }

    @ParameterizedTest
    @TestInput(input = "y2022/day25_sample.txt", expected = "2=-1=0")
    @TestInput(input = "y2022/day25.txt", expected = "2011-=2=-1020-1===-1")
    void part1(List<String> input, String expected) {
        var result = Day25.part1(input);

        assertEquals(expected, result);
    }

    @Disabled("Under construction")
    @ParameterizedTest
    @TestInput(input = "y2022/day25_sample.txt", expected = "1")
    @TestInput(input = "y2022/day25.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = Day25.part2(input);

        assertEquals(expected, result);
    }


}