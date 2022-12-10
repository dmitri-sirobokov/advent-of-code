package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;

import java.util.*;

public class Day15 extends CodeBase {

    public static long part1(List<String> input) {
        var risks = readMatrix(input);
        var visitMask = new boolean[risks.length][risks[0].length];

        var pathInfo = findBestPathTo(new Position(0, 0), new Position(risks[0].length - 1, risks.length - 1),
                risks, visitMask, Integer.MAX_VALUE);
        return pathInfo.risk;
    }

    private static PathInfo findBestPathTo(Position from, Position to,
                                           int[][] risks, boolean[][] visitMask,
                                           int minRisk) {
        PathInfo result = new PathInfo();

        if (from.x < 0 || from.x >= risks[0].length || from.y < 0 || from.y >= risks.length || visitMask[from.y][from.x]) {
            return null;
        }

        result.positions.add(from);
        result.risk += risks[from.y][from.x];

        if (from.equals(to)) {
            // we reach the end, return the path
            return result;
        }

        visitMask[from.y][from.x] = true;
        var path1 = findBestPathTo(new Position(from.x + 1, from.y), to, risks, visitMask, minRisk);
        var path2 = findBestPathTo(new Position(from.x - 1, from.y), to, risks, visitMask, minRisk);
        var path3 = findBestPathTo(new Position(from.x, from.y - 1), to, risks, visitMask, minRisk);
        var path4 = findBestPathTo(new Position(from.x, from.y + 1), to, risks, visitMask, minRisk);
        visitMask[from.y][from.x] = false;

        var bestPath = Arrays.asList(path1, path2, path3, path4).stream()
                .filter(Objects::nonNull).min(Comparator.comparingInt(a -> a.risk))
                .orElse(null);

        if (bestPath != null) {
            result.positions.addAll(bestPath.positions);
            result.risk += bestPath.risk;
        } else {
            result = null;
        }

        return result;
    }

    public static long part2(List<String> input) {
        return 0;
    }

    private static class PathInfo {
        private final List<Position> positions = new ArrayList<>();
        private int risk;
    }

    private record Position (int x, int y) { }

}
