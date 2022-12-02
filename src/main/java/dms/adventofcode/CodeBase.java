package dms.adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class CodeBase {
    public static List<String> readLines(String input) throws IOException {
        try (var br = new BufferedReader(new StringReader(input))) {
            return br.lines().toList();
        }
    }
}
