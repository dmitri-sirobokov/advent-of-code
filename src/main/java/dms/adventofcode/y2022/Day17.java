package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Day 17: Pyroclastic Flow
 */
public class Day17 extends CodeBase {


    public static long part1(List<String> input) throws IOException {
        Chamber chamber = simulateChamber(input, 2022);
        return chamber.getHeight();
    }

    public static long part2(List<String> input) throws IOException {
        Chamber chamber = simulateChamber(input, 1000000000000L);
        return chamber.getHeight();
    }

    private static Chamber simulateChamber(List<String> input, long count) throws IOException {
        var rocksEngine = readRocks();
        var jetsEngine = readJets(input);
        var chamber = new Chamber(rocksEngine, jetsEngine, 7);
        HashMap<String, Integer> heightCache = new HashMap<>();
        var height1 = 0L;
        var height2 = 0L;
        var iter1 = 0L;
        var iter2 = 0L;
        for (var i = 0L; i < count; i++) {

            var rock = rocksEngine.getNext();
            var jetHash = jetsEngine.counter % jetsEngine.jets.length;
            var rockHash = rock.id;
            var heightsHash = chamber.calcHeightsHash();

            var rockAndJetHash = "[" + rockHash + "," + jetHash + "]";
            var hash = heightsHash + rockAndJetHash;
            heightCache.putIfAbsent(hash, 0);
            var hashCounter = heightCache.compute(hash, (k, v) -> v + 1);
            if ((hashCounter >= 2 && hashCounter <= 5) && rockHash == 1) {
                if (height1 == 0 && hashCounter == 2) {
                    height1 = chamber.getHeight();
                    iter1 = rocksEngine.counter;
                }
                if (height2 == 0 && hashCounter == 3) {
                    height2 = chamber.getHeight();
                    iter2 = rocksEngine.counter;
                    var modulo = iter2 - iter1;
                    var times = count / modulo - 3;

                    i += times * modulo;
                    chamber.bufferHeight += times * (height2 - height1);
                }
            }

            chamber.addRock(rock);
            while (chamber.process()) {
            }
        }
        return chamber;
    }


    private static RocksEngine readRocks() throws IOException {
        var rocks = new ArrayList<Rock>();
        var shapes_input = readResourceFile("y2022/day17_shapes.txt");
        var shapeBuffer = new boolean[4][4];
        var xMax = 0;
        var yMax = 0;
        for (var i = 0; i < shapes_input.size(); i++) {
            var line = shapes_input.get(i);
            for (var j = 0; j < line.length(); j++) {
                var ch = line.charAt(j);
                shapeBuffer[yMax][j] = ch == '#';
                if (shapeBuffer[yMax][j])
                    xMax = Math.max(xMax, line.length());
            }
            yMax++;
            var endOfShape = i == shapes_input.size() - 1 || shapes_input.get(i + 1).isBlank();
            if (endOfShape) {
                var finalShape = new boolean[yMax][xMax];
                var rock = new Rock(finalShape, rocks.size() + 1);
                for (var y = 0; y < yMax; y++) {
                    for (var x = 0; x < xMax; x++) {
                        finalShape[y][x] = shapeBuffer[yMax - y - 1][x];
                    }
                }
                rocks.add(rock);
                xMax = 0;
                yMax = 0;
            }

            if (i == shapes_input.size() - 1) {
                break;
            }

            if (shapes_input.get(i + 1).isBlank()) {
                i++;
            }

        }
        return new RocksEngine(rocks);
    }

    private static JetEngine readJets(List<String> input) {
        return new JetEngine(input.get(0));
    }


    private static class JetEngine {
        private final JetDirection[] jets;
        private long counter;

        public JetEngine(String jets) {
            this.jets = new JetDirection[jets.length()];
            for (var i = 0; i < jets.length(); i++) {
                this.jets[i] = jets.charAt(i) == '>' ? JetDirection.RIGHT : JetDirection.LEFT;
            }
        }

        public JetDirection getNext() {
            var result = this.jets[(int) (counter % this.jets.length)];
            counter++;
            return result;
        }
    }

    private static class RocksEngine {
        private final List<Rock> rocks;
        public long counter;

        public RocksEngine(List<Rock> rocks) {
            this.rocks = rocks;
        }

        public Rock getNext() {
            var rock = this.rocks.remove(0);
            this.rocks.add(rock);
            this.counter++;
            return rock;
        }
    }

    private enum JetDirection {
        LEFT,
        RIGHT
    }

    private static class Rock {
        private final boolean[][] shape;
        private final int width;
        private final int height;
        private int id;

        public Rock(boolean[][] shape, int id) {
            this.shape = shape;
            this.width = shape[0].length;
            this.height = shape.length;
            this.id = id;
        }
    }

    private static class Chamber {
        private final int[][] jetCache;
        private long height;

        public final int width;
        private Rock currentRock;

        private int currentRockX;
        private long currentRockY;

        private final RocksEngine rocksEngine;
        private final JetEngine jetsEngine;
        private boolean[][] buffer;
        private long bufferHeight;
        private long jetHash;

        private long rockHash;
        private String heightsHash;

        public Chamber(RocksEngine rocksEngine, JetEngine jetsEngine, int width) {
            this.rocksEngine = rocksEngine;
            this.jetsEngine = jetsEngine;
            this.width = width;
            this.buffer = new boolean[1000000][width];
            this.jetCache = new int[jetsEngine.jets.length][rocksEngine.rocks.size()];
        }

        private String calcHash() {
            var rockAndJetHash = "[" + rockHash + "," + jetHash + "]";
            return heightsHash + rockAndJetHash;
        }

        public void addRock(Rock rock) {
            this.currentRock = rock;

            currentRockX = 2;
            currentRockY = height;
            processJet();
            processJet();
            processJet();

        }

        /**
         * Process one step, and return true, if there was something to process.
         */
        public boolean process() {
            if (currentRock == null) {
                return false;
            }

            processJet();

            if (canPlaceRockAtPosition(currentRock, currentRockX, currentRockY - 1)) {
                currentRockY--;
                return true;
            } else {
                for (var rockY = 0; rockY < currentRock.height; rockY++) {
                    for (var rockX = 0; rockX < currentRock.width; rockX++) {
                        buffer[(int) (currentRockY + rockY)][currentRockX + rockX] |= currentRock.shape[rockY][rockX];
                    }
                }

                height = Math.max(height, currentRockY + currentRock.height);
                var heights = calcHeights();
                var maxHeight = Arrays.stream(heights).max().orElse(0);
                var reduce = height - maxHeight;
                if (reduce > 100000) {
                    for (var y = 0; y < 100000; y++) {
                        Arrays.fill(buffer[y], false);
                    }
                    ArrayUtils.shift(buffer, -100000);
                    height -= 100000;
                    bufferHeight = 100000;
                }
                currentRock = null;
            }
            return false;
        }

        public long getHeight() {
            return bufferHeight + height;
        }

        private void processJet() {
            var jetDirection = this.jetsEngine.getNext();
            if (jetDirection == JetDirection.LEFT) {
                if (canPlaceRockAtPosition(currentRock, currentRockX - 1, currentRockY)) {
                    currentRockX--;
                }
            }
            if (jetDirection == JetDirection.RIGHT) {
                if (canPlaceRockAtPosition(currentRock, currentRockX + 1, currentRockY)) {
                    currentRockX++;
                }
            }
        }

        public void print(PrintStream printStream) {
            print(printStream, height);
        }

        private String calcHeightsHash() {
            long[] heights = calcHeights();
            return Arrays.stream(heights).mapToObj(v -> String.format("%3d", v)).reduce((a, b) -> a + "," + b).orElse("");
        }

        private long[] calcHeights() {
            var heights = new long[width];
            for (var x = 0; x < width; x++) {
                for (var y = height - 1; y >= 0; y--) {
                    if (buffer[(int) y][x]) {
                        heights[x] = height - y - 1;
                        break;
                    }
                }
            }
            return heights;
        }

        public void print(PrintStream printStream, long top) {
            var printHeight = height;
            if (currentRock != null) {
                printHeight = Math.max(currentRockY + currentRock.height, printHeight);
            }

            if (top < printHeight) {
                top = printHeight;
            }
            for (var y = printHeight - 1; y >= printHeight - top; y--) {
                printStream.print('|');
                for (var x = 0; x < width; x++) {
                    var bufferTest = buffer[(int) y][x];
                    var rockTest = currentRock != null
                            && y - currentRockY >= 0
                            && y - currentRockY < currentRock.height
                            && x - currentRockX >= 0
                            && x - currentRockX < currentRock.width
                            && currentRock.shape[(int) (y - currentRockY)][x - currentRockX];
                    if (rockTest) {
                        printStream.print('@');
                    } else if (bufferTest) {
                        printStream.print('#');
                    } else {
                        printStream.print('.');
                    }
                }
                printStream.println('|');
            }
            if (printHeight == top) {
                printStream.println('+' + StringUtils.repeat('-', width) + '+');
            }
        }

        private boolean canPlaceRockAtPosition(Rock rock, int x, long y) {
            if (x < 0 || x + rock.width > width || y < 0) {
                return false;
            }
            for (var rockY = 0; rockY < rock.height; rockY++) {
                for (var rockX = 0; rockX < rock.width; rockX++) {
                    if (rock.shape[rockY][rockX] && buffer[(int) (y + rockY)][x + rockX]) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
