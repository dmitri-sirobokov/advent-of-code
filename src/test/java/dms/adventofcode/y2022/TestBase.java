package dms.adventofcode.y2022;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestBase {

    public List<String> readResourceFile(String resourcePath) throws IOException {
        try (var resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            return readLines(IOUtils.toString(resourceStream, StandardCharsets.UTF_8));
        }
    }

    private static List<String> readLines(String input) throws IOException {
        try (var br = new BufferedReader(new StringReader(input))) {
            return br.lines().toList();
        }
    }
}
