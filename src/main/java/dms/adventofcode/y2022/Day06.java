package dms.adventofcode.y2022;

import java.util.List;

public class Day06 {

    public static int findMarkerIndex(List<String> input) {
        var line = input.get(0);
        return findMarkerIndex(line, 4);
    }

    public static int findMessageIndex(List<String> input) {
        var line = input.get(0);
        Integer i = findMarkerIndex(line, 14);
        if (i != null) return i;
        return -1;
    }

    private static int findMarkerIndex(String line, int len) {
        for (var i = 0; i < line.length() - len; i++) {
            if (isMarker(line, i, len)) {
                return i + len;
            }
        }
        return -1;
    }

    private static boolean isMarker(String line, int pos, int len) {
        if (pos + len > line.length()) {
            return false;
        }
        for (var i = pos; i < pos + len - 1; i++) {
            for (var j = i + 1; j < pos + len; j++) {
                if (line.charAt(i) == line.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }

}
