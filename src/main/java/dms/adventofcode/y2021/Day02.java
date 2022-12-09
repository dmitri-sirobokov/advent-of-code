package dms.adventofcode.y2021;

import java.util.List;

public class Day02 {

    public static long calcPositionProduct(List<String> input) {
        var currentPosition = new Position();
        for (var line : input) {
            var lineParts = line.split(" ");
            if (lineParts.length != 2) {
                throw new RuntimeException(String.format("Unrecognized movement step values. Expected 2 values in '%s'", line));
            }

            var stepValue = Long.parseLong(lineParts[1].trim());
            switch (lineParts[0].trim()) {
                case "forward" -> {
                    currentPosition.x += stepValue;
                    currentPosition.z += currentPosition.aim * stepValue;
                }
                case "down" -> currentPosition.aim += stepValue;
                case "up" -> currentPosition.aim -= stepValue;
                default -> throw new RuntimeException("Unrecognized value for movement step: " + line);
            }
        }
        return currentPosition.x * currentPosition.z;
    }

    private static class Position {
        long x;
        long z;
        long aim;
    }

}
