package dms.adventofcode.y2022;

import java.util.List;

public class Day06 {

    public static int findMarkerIndex(List<String> input) {
        var line = input.get(0);
        return findMarkerIndex(line, 4);
    }

    public static int findMessageIndex(List<String> input) {
        var line = input.get(0);
        return findMarkerIndex(line, 14);
    }

    private Day06() { }

    private static int findMarkerIndex(String line, int len) {
        var markerWindow = new UniqueMarkerWindow(len);
        for (var i = 0; i < line.length(); i++) {
            if (markerWindow.add(line.charAt(i))) {
                return i + 1;
            }
        }
        return -1;
    }

    private static class UniqueMarkerWindow {
        private final char[] window;
        private final int[] counters;

        private int pos;

        private int counter;

        /**
         * @param size Size of the window
         */
        public UniqueMarkerWindow(int size) {
            window = new char[size];
            counters = new int[256];
            counters[0] = size + 1;
            counter = 1;
        }

        /** add a character to a window, and return true if the sequence in window is unique.
         * @param ch - a character to add to a window
         * @return - return true if the sequence in window is unique.
         */
        public boolean add(char ch) {
            var tailCharacterCount = --counters[window[pos]];
            if (tailCharacterCount == 1) counter--;

            window[pos] = ch;

            var headCharacterCount = ++counters[ch];
            if (headCharacterCount == 2) counter++;

            this.pos++;
            if (this.pos == window.length) this.pos = 0;

            return counter == 0;
        }
    }

}
