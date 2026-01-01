package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dms.adventofcode.math.MathUtils.gcd;

/**
 * Day 24: Blizzard Basin
 */
public class Day24 extends CodeBase {

    public static long part1(List<String> input) {
        var blizzardsEngine = new BlizzardsEngine(input);

        return countSteps(blizzardsEngine, 0, 0, blizzardsEngine.sizeX - 1, blizzardsEngine.sizeY - 1, 0);
    }

    public static long part2(List<String> input) {
        var blizzardsEngine = new BlizzardsEngine(input);

        var startX = 0;
        var startY = 0;
        var endX = blizzardsEngine.sizeX - 1;
        var endY = blizzardsEngine.sizeY - 1;

        var steps1 = countSteps(blizzardsEngine, startX, startY, endX, endY, 0);
        var steps2 = countSteps(blizzardsEngine, endX, endY, startX, startY, steps1 - 1);
        return countSteps(blizzardsEngine, startX, startY, endX, endY, steps2 - 1);
    }

    private static int countSteps(BlizzardsEngine blizzardsEngine, int startX, int startY, int endX, int endY, int startingStep) {
        var step = startingStep;
        var priorities = new int[blizzardsEngine.sizeY][blizzardsEngine.sizeX];
        Arrays.stream(priorities).forEach(p -> Arrays.fill(p, Integer.MAX_VALUE));

        while (priorities[endY][endX] == Integer.MAX_VALUE) {
            step++;
            var blizzards = blizzardsEngine.renderStep(step);

            var newPriorities = new int[blizzardsEngine.sizeY][blizzardsEngine.sizeX];
            Arrays.stream(newPriorities).forEach(p -> Arrays.fill(p, Integer.MAX_VALUE));

            // start point
            if (blizzards[startY][startX] == 0) {
                newPriorities[startY][startX] = Math.min(step, priorities[startY][startX]);
            }

            for (var y = 0; y < blizzardsEngine.sizeY; y++) {
                for (var x = 0; x < blizzardsEngine.sizeX; x++) {

                    var dx = new int[]{x, x, x, x - 1, x + 1};
                    var dy = new int[]{y, y - 1, y + 1, y, y};
                    for (var i = 0; i < 5; i++) {
                        var xi = dx[i];
                        var yi = dy[i];
                        if (xi >= 0 && xi < blizzardsEngine.sizeX && yi >= 0 && yi < blizzardsEngine.sizeY && priorities[y][x] != Integer.MAX_VALUE) {
                            // relax next spot
                            newPriorities[yi][xi] = Math.min(priorities[y][x] + 1, newPriorities[yi][xi]);
                        }
                    }
                }
            }
            for (var y = 0; y < blizzardsEngine.sizeY; y++) {
                for (var x = 0; x < blizzardsEngine.sizeX; x++) {
                    if (blizzards[y][x] != 0) {
                        newPriorities[y][x] = Integer.MAX_VALUE;
                    }
                }
            }

            priorities = newPriorities;

        }
        return step + 1;
    }

    private static class Blizzard {
        private int x;
        private int y;
        private final char dir;

        public Blizzard(int x, int y, char dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }

    private static class BlizzardsEngine {
        private final List<char[][]> maps = new ArrayList<>();
        private final List<Blizzard> blizzards = new ArrayList<>();
        private final int sizeX;
        private final int sizeY;
        private final int cacheSize;

        public BlizzardsEngine(List<String> input) {
            sizeX = input.getFirst().length() - 2;
            sizeY = input.size() - 2;
            for (var y = 0; y < sizeY; y++) {
                var line = input.get(y + 1);
                for (var x = 0; x < sizeX; x++) {
                    var ch = line.charAt(x + 1);
                    if (ch != '.') {
                        blizzards.add(new Blizzard(x, y, ch));
                    }
                }
            }
            // as blizzard repeat themselves horizontally xSize times and vertically ySize times,
            // all blizzards will be in the same position after N = xSize * ySize / gcd(xSize,ySize) steps.
            // gcd is the greatest common divider for xSize and ySize.
            // N is thus a maximum number of maps we can generate and cache, so we do not need to recalculate them again.
            cacheSize = sizeX * sizeY / gcd(sizeX, sizeY);
        }

        private char[][] renderStep(int step) {
            // check cache
            step %= cacheSize;

            // render new image
            while (maps.size() <= step) {
                var map = new char[sizeY][sizeX];
                for (Blizzard blizzard : blizzards) {
                    if (Character.isDigit(map[blizzard.y][blizzard.x])) {
                        map[blizzard.y][blizzard.x]++;
                    } else if (map[blizzard.y][blizzard.x] != 0) {
                        map[blizzard.y][blizzard.x] = '2';
                    } else {
                        map[blizzard.y][blizzard.x] = blizzard.dir;
                    }
                    switch (blizzard.dir) {
                        case '<' -> blizzard.x--;
                        case '>' -> blizzard.x++;
                        case '^' -> blizzard.y--;
                        case 'v' -> blizzard.y++;
                        default -> throw new RuntimeException("Invalid direction symbol.");
                    }
                    blizzard.x %= sizeX;
                    blizzard.y %= sizeY;
                    if (blizzard.x < 0) blizzard.x = sizeX + blizzard.x;
                    if (blizzard.y < 0) blizzard.y = sizeY + blizzard.y;
                }
                maps.add(map);
            }

            return maps.get(step);
        }
    }

}
