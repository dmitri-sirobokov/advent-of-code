package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.List;

/**
 * Day 22: Monkey Map
 */
public class Day22 extends CodeBase {

    public static long part1(List<String> input) {
        var mapData = readMapData(input);
        var pos = move(mapData, new Position(0, 0, 0), 0);

        for (var i = 0; i < mapData.instructions.length(); i++) {
            if (Character.isDigit(mapData.instructions.charAt(i))) {
                var i1 = i + 1;
                while (i1 < mapData.instructions.length() && Character.isDigit(mapData.instructions.charAt(i1))) {
                    i1++;
                }
                var steps = Integer.parseInt(mapData.instructions.substring(i, i1));
                pos = move(mapData, pos, steps);
                i = i1 - 1;
            } else {
                var dir = pos.dir;
                if (mapData.instructions.charAt(i) == 'R') {
                    dir++;
                    if (dir > 3) {
                        dir = 0;
                    }
                } else {
                    dir--;
                    if (dir < 0) {
                        dir = 3;
                    }
                }
                pos = new Position(pos.x, pos.y, dir);
            }
        }

        return 1000 * (pos.y + 1) + 4 * (pos.x + 1) + pos.dir;
    }

    public static long part2(List<String> input) {
        return 0;
    }

    private static Position move(MapData mapData, Position pos, int steps) {
        var xStep = pos.dir == 0 ? 1 : pos.dir == 2 ? -1 : 0;
        var yStep = pos.dir == 1 ? 1 : pos.dir == 3 ? -1 : 0;
        var x = pos.x;
        var y = pos.y;
        while (mapData.cube.map[y][x] == ' ') {
            x = x + xStep;
            y = y + yStep;
        }
        for (var i = 0; i < steps; i++) {
            var prevx = x;
            var prevy = y;
            x = (x + xStep) % mapData.cube.map[0].length;
            y = (y + yStep) % mapData.cube.map.length;
            if (x < 0) {
                x = mapData.cube.map[0].length + x;
            }
            if (y < 0) {
                y = mapData.cube.map.length + y;
            }
            while (mapData.cube.map[y][x] == ' ') {
                x = (x + xStep) % mapData.cube.map[0].length;
                y = (y + yStep) % mapData.cube.map.length;
                if (x < 0) {
                    x = mapData.cube.map[0].length + x;
                }
                if (y < 0) {
                    y = mapData.cube.map.length + y;
                }
            }
            if (mapData.cube.map[y][x] == '#') {
                x = prevx;
                y = prevy;
                break;
            }
        }
        return new Position(x, y, pos.dir);
    }

    private static MapData readMapData(List<String> input) {
        var maxY = input.indexOf("");
        var maxX = input.stream().mapToInt(l -> l.length()).max().orElse(0);

        var map = new char[maxY][maxX];
        for (var y = 0; y < maxY; y++) {
            var line = input.get(y);
            for (var x = 0; x < maxX; x++) {
                map[y][x] = x < line.length() ? line.charAt(x) : ' ';
            }
        }

        var cube = new Cube(map);
        return new MapData(cube, input.get(maxY + 1));

    }

    private static class MapData {
        // 6 faces of the cube
        private final Cube cube;
        private final String instructions;

        public MapData(Cube cube, String instructions) {
            this.instructions = instructions;
            this.cube = cube;

        }
    }

    private record Position(int x, int y, int dir) {
    }

    private static class Face {
        private int x;
        private int y;
    }

    private static class Cube {
        private final char[][] map;
        private int size = 0;

        private int x;
        private int y;

        private Face top = new Face();
        private Face bottom = new Face();
        private Face left = new Face();
        private Face right = new Face();
        private Face front = new Face();
        private Face back = new Face();

        public Cube(char[][] map) {
            this.map = map;
            var countElements = 0;
            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[y].length; x++) {
                    if (map[y][x] != ' ') {
                        countElements++;
                    }
                }
            }

            while ((size + 1) * (size + 1) <= (countElements / 6)) {
                size++;
            }

            // starting point
            var x0 = 0;
            for (var x = 0; x < map[0].length; x++) {
                if (map[0][x] != ' ') {
                    x0 = x;
                    break;
                }
            }

        }

    }
}
