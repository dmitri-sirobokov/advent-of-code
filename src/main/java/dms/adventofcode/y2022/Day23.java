package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Day 23: Unstable Diffusion
 */
public class Day23 extends CodeBase {

    public static long part1(List<String> input) {
        Grove grove = doItearations(input,  10);

        var minX = grove.elfs.stream().mapToInt(e -> e.x).min().orElse(0);
        var maxX = grove.elfs.stream().mapToInt(e -> e.x).max().orElse(0);
        var minY = grove.elfs.stream().mapToInt(e -> e.y).min().orElse(0);
        var maxY = grove.elfs.stream().mapToInt(e -> e.y).max().orElse(0);

        var countEmpty = 0;
        for (var y = 0; y <= maxY - minY; y++) {
            for (var x = 0; x <= maxX - minX; x++) {
                if (grove.map[minY + y][minX + x] == null) {
                    countEmpty++;
                }
            }
        }

        return countEmpty;
    }

    public static long part2(List<String> input) {
        Grove grove = doItearations(input, Integer.MAX_VALUE);

        return grove.iteration + 1;
    }

    private static Grove doItearations(List<String> input, int maxIterations) {
        var grove = readInput(input);
        for (grove.iteration = 0; grove.iteration < maxIterations; grove.iteration++) {
            // first half round - propose new positions
            grove.clearPropositions();
            for (var elf : grove.elfs) {
                elf.propose();
            }

            // next half round
            for (var elf : grove.elfs) {
                elf.move();
            }

            if (!grove.isMoved) {
                break;
            }
        }
        return grove;
    }

    // Code to print elfs positions
    //    private static void printElfs(Grove grove) {
    //        var minX = grove.elfs.stream().mapToInt(e -> e.x).min().orElse(0);
    //        var maxX = grove.elfs.stream().mapToInt(e -> e.x).max().orElse(0);
    //        var minY = grove.elfs.stream().mapToInt(e -> e.y).min().orElse(0);
    //        var maxY = grove.elfs.stream().mapToInt(e -> e.y).max().orElse(0);
    //
    //        for (var y = 0; y <= maxY - minY; y++) {
    //            System.out.print('.');
    //            for (var x = 0; x <= maxX - minX; x++) {
    //                if (grove.map[minY + y][minX + x] != null) {
    //                    System.out.print('#');
    //                } else {
    //                    System.out.print('.');
    //                }
    //            }
    //            System.out.print('.');
    //            System.out.println();
    //        }
    //        System.out.println();
    //    }

    private static Grove readInput(List<String> input) {
        var grove = new Grove();
        var o = grove.map.length / 2;
        for (var y = 0; y < input.size(); y++) {
            var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                if (line.charAt(x) == '#') {
                    var elf = new Elf(grove, x + o, y + o);
                    grove.map[elf.y][elf.x] = elf;
                    grove.elfs.add(elf);
                }
            }
        }

        return grove;
    }

    private static class Grove {
        private final List<Elf> elfs = new ArrayList<>();

        // map buffer;
        private final Elf[][] map = new Elf[400][400];;
        private final int[][] propositions = new int[400][400];
        public boolean isMoved;
        public long iteration;

        public void clearPropositions() {
            Arrays.stream(propositions).forEach(row -> Arrays.fill(row, 0));
            isMoved = false;
        }
    }

    private static class Elf {
        private int x;
        private int y;

        private List<Integer> proposes = new ArrayList<>(List.of(0, 1, 2, 3));
        private Integer lastProposition;
        private Integer firstProposition;

        private final Grove grove;

        private Elf(Grove grove, int x, int y) {
            this.grove = grove;
            this.x = x;
            this.y = y;
        }

        /**
         * This is implementation of the first half round.
         */
        public void propose() {
            lastProposition = null;
            firstProposition = proposes.get(0);
            var isNorthEmpty = grove.map[y - 1][x - 1] == null && grove.map[y - 1][x] == null && grove.map[y - 1][x + 1] == null;
            var isSouthEmpty = grove.map[y + 1][x - 1] == null && grove.map[y + 1][x] == null && grove.map[y + 1][x + 1] == null;
            var isWestEmpty = grove.map[y + 1][x - 1] == null && grove.map[y][x - 1] == null && grove.map[y - 1][x - 1] == null;
            var isEastEmpty = grove.map[y + 1][x + 1] == null && grove.map[y][x + 1] == null && grove.map[y - 1][x + 1] == null;
            if (isNorthEmpty && isSouthEmpty && isWestEmpty && isEastEmpty) {
                return;
            }
            if (!isNorthEmpty && !isSouthEmpty && !isWestEmpty && !isEastEmpty) {
                return;
            }
            for (var i = 0; i < 4; i++) {
                lastProposition = proposes.get(i);
                if (lastProposition == 0 && isNorthEmpty) {
                    grove.propositions[y - 1][x]++;
                    break;
                }
                if (lastProposition == 1 && isSouthEmpty) {
                    grove.propositions[y + 1][x]++;
                    break;
                }
                if (lastProposition == 2 && isWestEmpty) {
                    grove.propositions[y][x - 1]++;
                    break;
                }
                if (lastProposition == 3 && isEastEmpty) {
                    grove.propositions[y][x + 1]++;
                    break;
                }
            }
        }

        /**
         * This is implementation of the second half round.
         */
        public void move() {
            if (lastProposition != null) {
                var x1 = x;
                var y1 = y;
                switch (lastProposition) {
                    case 0 -> y1--;
                    case 1 -> y1++;
                    case 2 -> x1--;
                    case 3 -> x1++;
                }
                // move to new position only if no one else proposed this position
                if (grove.propositions[y1][x1] == 1) {
                    grove.map[y][x] = null;
                    grove.map[y1][x1] = this;
                    x = x1;
                    y = y1;
                    grove.isMoved = true;
                }
            }

            // first proposition should move to the end of the list
            if (firstProposition != null) {
                proposes.remove(firstProposition);
                proposes.add(firstProposition);
            }
        }
    }
}
