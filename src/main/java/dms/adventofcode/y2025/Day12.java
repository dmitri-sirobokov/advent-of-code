package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;
import dms.adventofcode.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Day12: Christmas Tree Farm
 *
 */
public class Day12 extends CodeBase {

    public static long part1(List<String> input) {
        var puzzleData = readPuzzleData(input);

        tryFit(puzzleData);


        return puzzleData.regions.stream().filter(r -> r.fit).count();
    }

    private final static class Shape {
        private final char[][] cells;
        private final int weight;

        Shape(char[][] cells) {
            this.cells = cells;
            // weight is the maximum volume the shape can take in the container
            var weight = 0;
            for (char[] cell : cells) {
                for (char c : cell) {
                    if (c == '#') {
                        weight++;
                    }
                }
            }
            this.weight = weight;
        }

        public Shape rotate() {
            var newSpace = new char[cells.length][cells[0].length];
            for (var x = -1; x <= 1; x++) {
                for (var y = -1; y <= 1; y++) {
                    var rotVector = new Vector(x, y);
                    var ch = this.cells[y + 1][x + 1];
                    var newVector = rotVector.rotate90deg(false);
                    newSpace[newVector.intY() + 1][newVector.intX() + 1] = ch;
                }
            }
            return new Shape(newSpace);
        }

        public Shape flip() {
            var newSpace = new char[cells.length][cells[0].length];
            for (var x = -1; x <= 1; x++) {
                for (var y = -1; y <= 1; y++) {
                    var ch = this.cells[y + 1][x + 1];
                    newSpace[y + 1][-x + 1] = ch;
                }
            }
            return new Shape(newSpace);
        }
    }

    private static class Region {
        private final Vector size;
        private final int[] requiredBoxes;
        private final char[][] cells;
        private boolean fit;

        public Region(Vector size, int[] requiredBoxes) {
            this.size = size;
            this.requiredBoxes = requiredBoxes;
            this.cells = new char[size.intY()][size.intX()];
        }
    }

    private static class CellData {
        private char ch;
    }

    private static class PuzzleData {
        // each shape can have 8 different forms, we precalculate these forms and save in this list.
        private final List<List<Shape>> shapeTemplates = new ArrayList<>();
        private final List<Region> regions = new ArrayList<>();
    }

    private static PuzzleData readPuzzleData(List<String> input) {
        PuzzleData data = new PuzzleData();

        var lineIndex = 0;

        // read shapes
        while (true) {
            var line = input.get(lineIndex);
            if (!line.endsWith(":")) break;

            var shapes = new ArrayList<Shape>();
            lineIndex++;
            var shapeSpace = new char[3][3];
            for (var y = 0; y < 3; y++) {
                line = input.get(lineIndex);
                lineIndex++;
                for (var x = 0; x < 3; x++) {
                    shapeSpace[y][x] = line.charAt(x);
                }
            }
            var shape = new Shape(shapeSpace);
            shapes.add(shape);

            // precompute all shape orientations (8 possibilities)
            for (var i = 0; i < 3; i++) {
                shape = shape.rotate();
                shapes.add(shape);
            }
            shape = shape.flip();
            shapes.add(shape);
            for (var i = 0; i < 3; i++) {
                shape = shape.rotate();
                shapes.add(shape);
            }
            data.shapeTemplates.add(shapes);
            lineIndex++;
        }

        // read regions
        while (lineIndex < input.size()) {
            var parts = input.get(lineIndex).split(":");
            var sizeParts = parts[0].split("x");
            var len = Integer.parseInt(sizeParts[0]);
            var height = Integer.parseInt(sizeParts[1]);
            var reqParts = parts[1].trim().split(" ");
            var requiredBoxes = new int[reqParts.length];
            for (var i = 0; i < reqParts.length; i++) {
                requiredBoxes[i] = Integer.parseInt(reqParts[i]);
            }
            data.regions.add(new Region(new Vector(len, height), requiredBoxes));
            lineIndex++;
        }
        return data;
    }

    private static void tryFit(PuzzleData puzzleData) {
        for (Region region : puzzleData.regions) {
            var remainingBoxes = Arrays.copyOf(region.requiredBoxes, region.requiredBoxes.length);
            var remainingVolume = region.size.intX() * region.size.intY();
            region.fit = dfs(puzzleData, region.cells, remainingBoxes, remainingVolume);
        }
    }

    private static boolean dfs(PuzzleData puzzleData, char[][] regionCells, int[] remainingBoxes, int remainingVolume) {
        var totalRemaing = Arrays.stream(remainingBoxes).sum();
        if (totalRemaing * 9 < remainingVolume) {
            return true;
        }

        var requiredVolume = 0;
        for (var i = 0; i < remainingBoxes.length; i++) {
            requiredVolume += remainingBoxes[i] * puzzleData.shapeTemplates.get(i).getFirst().weight;
        }

        if (requiredVolume > remainingVolume) {
            return false;
        }

        // almost nothing left here after above checks, do we really need dfs to search further?

        var newRegionCells = copyArray2D(regionCells);
        var newRemainingBoxes = Arrays.copyOf(remainingBoxes, remainingBoxes.length);
        var found = true;

        while (found && totalRemaing > 0) {
            var newRemainingVolume = new int[] { remainingVolume } ;
            found = tryFit(puzzleData.shapeTemplates, newRegionCells, newRemainingBoxes, newRemainingVolume);
            remainingVolume = newRemainingVolume[0];

            if (found) {

                if (!dfs(puzzleData, newRegionCells, newRemainingBoxes, remainingVolume)) {
                    return false;
                }
            }

            totalRemaing = Arrays.stream(newRemainingBoxes).sum();

        }
        if (found) {
            System.arraycopy(newRemainingBoxes, 0, remainingBoxes, 0, remainingBoxes.length);
            for (var i = 0; i < regionCells.length; i++) {
                System.arraycopy(newRegionCells[i], 0, regionCells[i], 0, regionCells[i].length);
            }
        }
        return totalRemaing == 0;
    }

    private static boolean tryFit(List<List<Shape>> shapes, char[][] regionCells, int[] remainingBoxes, int[] remainingVolume) {
        for (var boxIdx = 0; boxIdx < remainingBoxes.length; boxIdx++) {
            if (remainingBoxes[boxIdx] > 0) {
                for (var shape : shapes.get(boxIdx)) {
                    if (tryFit(regionCells, shape.cells)) {
                        remainingBoxes[boxIdx]--;
                        remainingVolume[0] -= shape.weight;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean tryFit(char[][] regionSpace, char[][] boxSpace) {
        for (var y  = 0; y < regionSpace.length; y++) {
            for (var x = 0; x < regionSpace[0].length; x++) {
                if (tryFit(regionSpace, boxSpace, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean tryFit(char[][] regionSpace, char[][] boxSpace, int x, int y) {
        for (var boxX = 0; boxX < boxSpace.length; boxX++) {
            for (var boxY = 0; boxY < boxSpace[0].length; boxY++) {
                var shapeCh = boxSpace[boxY][boxX];
                if (shapeCh == '#') {
                    var destX = x + boxX;
                    var destY = y + boxY;
                    if (destX >= regionSpace[0].length || destY >= regionSpace.length) {
                        return false;
                    }
                    var regionCh = regionSpace[y + boxY][x + boxX];
                    if (regionCh == '#') {
                        return false;
                    }
                }
            }
        }
        for (var boxX = 0; boxX < boxSpace.length; boxX++) {
            for (var boxY = 0; boxY < boxSpace[0].length; boxY++) {
                var shapeCh = boxSpace[boxY][boxX];
                if (shapeCh == '#') {
                    regionSpace[y + boxY][x + boxX] = shapeCh;
                }
            }
        }
        return true;
    }

    private static char[][] copyArray2D(char[][] source) {
        var copy = new char[source.length][];
        for (int y = 0; y < source.length; y++) {
            copy[y] = Arrays.copyOf(source[y], source[y].length);
        }
        return copy;
    }

}
