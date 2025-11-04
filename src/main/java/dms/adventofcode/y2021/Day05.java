package dms.adventofcode.y2021;

import dms.adventofcode.Vector;

import java.util.ArrayList;
import java.util.List;

public class Day05 {

    public static int countOverlapPoints(List<String> input, int threshold, boolean includeDiagonal) {
        var lines = new ArrayList<Line>();
        for (var inputLine : input) {
            var line = parseLine(inputLine);
            lines.add(line);
        }
        var maxX = 1 + lines.stream().map(l -> Math.max(l.a.x(), l.b.x())).max(Long::compareTo).orElse(0L);
        var maxY = 1 + lines.stream().map(l -> Math.max(l.a.y(), l.b.y())).max(Long::compareTo).orElse(0L);
        var intersections = new byte[(int)maxY][(int)maxX];
        for (var x = 0; x < maxX; x++) {
            for (var y = 0; y < maxY; y++) {
                for (var line : lines) {
                    // process only horizontal or verticals lines for now
                    if (includeDiagonal || line.isHorizontal() || line.isVertical()) {
                        // cross product of 2 vectors will return 0 if they are parallel in space, or x, y lies on the same line
                        var crossProd = line.crossProd(x, y);
                        if (crossProd == 0) {

                            // point should be between 'a' en 'b', we can use vector dot product math for that
                            var dotProduct = line.dotProd(x, y);
                            if (dotProduct >= 0 && dotProduct <= line.lengthSqr()) {
                                intersections[y][x]++;
                            }
                        }
                    }
                }
            }
        }

        var countOverlaps = 0;
        for (var x = 0; x < maxX; x++) {
            for (var y = 0; y < maxY; y++) {
                if (intersections[y][x] >= threshold) {
                    countOverlaps++;
                }
            }
        }
        return countOverlaps;
    }

    private static Line parseLine(String line) {
        var lineParts = line.split(" -> ");
        assert lineParts.length == 2;
        var a = Vector.parse(lineParts[0]);
        var b = Vector.parse(lineParts[1]);
        return new Line(a, b);
    }

    private record Line(Vector a, Vector b) {

        public long crossProd(int x, int y) {
                return (b.x() - a.x()) * (y - a.y()) - (b.y() - a.y()) * (x - a.x());
            }

            public long dotProd(int x, int y) {
                return (b.x() - a.x()) * (x - a.x()) + (b.y() - a.y()) * (y - a.y());
            }

            public boolean isHorizontal() {
                return a.y() == b.y();
            }

            public boolean isVertical() {
                return a.x() == b.x();
            }

            public long lengthSqr() {
                var x = b.x() - a.x();
                var y = b.y() - a.y();
                return x * x + y * y;
            }
        }
}
