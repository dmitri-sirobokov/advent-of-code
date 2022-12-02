package dms.adventofcode.y2022;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestBase {
    public String readResourceFile(String resourcePath) throws IOException {
        try (var resourceStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            return IOUtils.toString(resourceStream, StandardCharsets.UTF_8);
        }
    }
}
