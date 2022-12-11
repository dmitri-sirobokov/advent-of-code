package dms.adventofcode;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class TestBase {

    public static List<String> readResourceFile(String resourcePath) throws IOException {
        try (var resourceStream = TestBase.class.getClassLoader().getResourceAsStream(resourcePath)) {
            return readLines(IOUtils.toString(Objects.requireNonNull(resourceStream), StandardCharsets.UTF_8));
        }
    }

    private static List<String> readLines(String input) throws IOException {
        try (var br = new BufferedReader(new StringReader(input))) {
            return br.lines().toList();
        }
    }
}
