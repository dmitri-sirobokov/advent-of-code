package dms.adventofcode.y2022;

import java.util.List;

public class Day04 {

    public static long countFullyOverlappedSectionsPart1(List<String> input) {
        return input.stream()
                .map(l -> Pair.parse(l))
                .filter(pair -> pair.isFullyOverlapped())
                .count();
    }

    public static long countHasOverlappedSectionsPart2(List<String> input) {
        return input.stream()
                .map(l -> Pair.parse(l))
                .filter(pair -> pair.hasOverlap())
                .count();
    }

    private static class Range {
        private int min;
        private int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public static Range parse(String range) {
            var rangeParts = range.split("-");
            return new Range(Integer.parseInt(rangeParts[0]), Integer.parseInt(rangeParts[1]));
        }

        public boolean fullyContains(Range other) {
            return this.min <= other.min && this.max >= other.max;
        }

        public boolean hasOverlap(Range other) {
            return this.min >= other.min && this.min <= other.max ||
                    this.max >= other.min && this.max <= other.max ||
                    other.min >= this.min && other.min <= this.max ||
                    other.max >= this.min && other.max <= this.max;
        }
    }

    private static class Pair {
        private Range elf1;
        private Range elf2;

        public Pair(Range elf1, Range elf2) {
            this.elf1 = elf1;
            this.elf2 = elf2;
        }

        public static Pair parse(String value) {
            var ranges = value.split(",");
            return new Pair(Range.parse(ranges[0]), Range.parse(ranges[1]));
        }

        public boolean isFullyOverlapped() {
            return elf1.fullyContains(elf2) || elf2.fullyContains(elf1);
        }

        public boolean hasOverlap() {
            return elf1.hasOverlap(elf2);
        }
    }
}
