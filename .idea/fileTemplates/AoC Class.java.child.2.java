#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};

#end
#set ($MyYear=${PACKAGE_NAME.substring(${PACKAGE_NAME.lastIndexOf(".")}+1)})

import dms.adventofcode.TestInput;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ${NAME}Test {

    @ParameterizedTest
    @TestInput(input = "${MyYear}/${NAME.toLowerCase()}_sample.txt", expected = "1")
    @TestInput(input = "${MyYear}/${NAME.toLowerCase()}.txt", expected = "1")
    void part1(List<String> input, long expected) {
        var result = ${NAME}.part1(input);

        assertEquals(expected, result);
    }

    @ParameterizedTest
    @TestInput(input = "${MyYear}/${NAME.toLowerCase()}_sample.txt", expected = "1")
    @TestInput(input = "${MyYear}/${NAME.toLowerCase()}.txt", expected = "1")
    void part2(List<String> input, long expected) {
        var result = ${NAME}.part2(input);

        assertEquals(expected, result);
    }

}