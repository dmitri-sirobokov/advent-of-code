package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.List;

/**
 * Day 22: Monkey Map
 */
public class Day22 extends CodeBase {

    public static long part1(List<String> input, int sampleType) {
        return run(input, sampleType);
    }

    public static long run(List<String> input, int sampleType) {
        var mapData = readMapData(input);
        if (sampleType == 0) {
            part1SampleMapData(mapData);
        } else if (sampleType == 1) {
            part1RealMapData(mapData);
        } else if (sampleType == 2) {
            part2SampleMapData(mapData);
        }

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
                mapData.cube.dir = pos.dir;
            }
        }

        return 1000 * (pos.y + 1) + 4 * (pos.x + 1) + pos.dir;
    }

    private static void part1SampleMapData(MapData mapData) {
        mapData.cube.front.x = 2 * mapData.cube.size;
        mapData.cube.front.y = 0;

        mapData.cube.bottom.x = 2 * mapData.cube.size;
        mapData.cube.bottom.y = mapData.cube.size;

        mapData.cube.left.x = mapData.cube.size;
        mapData.cube.left.y = mapData.cube.size;

        mapData.cube.top.x = 0;
        mapData.cube.top.y = mapData.cube.size;

        mapData.cube.back.x = 2 * mapData.cube.size;
        mapData.cube.back.y = 2 * mapData.cube.size;

        mapData.cube.right.x = mapData.cube.size;
        mapData.cube.right.y = 3 * mapData.cube.size;

        mapData.cube.front.left = new CubeEdge(2, mapData.cube.front, mapData.cube.front);
        mapData.cube.front.right = new CubeEdge(0, mapData.cube.front, mapData.cube.front);
        mapData.cube.front.top =  new CubeEdge(3, mapData.cube.front, mapData.cube.back);
        mapData.cube.front.bottom = new CubeEdge(1, mapData.cube.front, mapData.cube.bottom);

        mapData.cube.bottom.left = new CubeEdge(2, mapData.cube.bottom, mapData.cube.left);
        mapData.cube.bottom.right = new CubeEdge(0, mapData.cube.bottom, mapData.cube.top);
        mapData.cube.bottom.top = new CubeEdge(3, mapData.cube.bottom, mapData.cube.front);
        mapData.cube.bottom.bottom = new CubeEdge(1, mapData.cube.bottom, mapData.cube.back);

        mapData.cube.left.left = new CubeEdge(2, mapData.cube.left, mapData.cube.top);
        mapData.cube.left.right = new CubeEdge(0, mapData.cube.left, mapData.cube.bottom);
        mapData.cube.left.top = new CubeEdge(3, mapData.cube.left, mapData.cube.left);
        mapData.cube.left.bottom = new CubeEdge(1, mapData.cube.left, mapData.cube.left);

        mapData.cube.top.left = new CubeEdge(2, mapData.cube.top, mapData.cube.bottom);
        mapData.cube.top.right = new CubeEdge(0, mapData.cube.top, mapData.cube.left);
        mapData.cube.top.top = new CubeEdge(3, mapData.cube.top, mapData.cube.top);
        mapData.cube.top.bottom = new CubeEdge(1, mapData.cube.top, mapData.cube.top);

        mapData.cube.back.left = new CubeEdge(2, mapData.cube.back, mapData.cube.right);
        mapData.cube.back.right = new CubeEdge(0, mapData.cube.back, mapData.cube.right);
        mapData.cube.back.top = new CubeEdge(3, mapData.cube.back, mapData.cube.bottom);
        mapData.cube.back.bottom = new CubeEdge(1, mapData.cube.back, mapData.cube.front);

        mapData.cube.right.left = new CubeEdge(2, mapData.cube.right, mapData.cube.bottom);
        mapData.cube.right.right = new CubeEdge(0, mapData.cube.right, mapData.cube.bottom);
        mapData.cube.right.top = new CubeEdge(3, mapData.cube.right, mapData.cube.right);
        mapData.cube.right.bottom = new CubeEdge(1, mapData.cube.right, mapData.cube.right);
    }

    private static void part2SampleMapData(MapData mapData) {
        mapData.cube.front.x = 2 * mapData.cube.size;
        mapData.cube.front.y = 0;

        mapData.cube.bottom.x = 2 * mapData.cube.size;
        mapData.cube.bottom.y = mapData.cube.size;

        mapData.cube.left.x = mapData.cube.size;
        mapData.cube.left.y = mapData.cube.size;

        mapData.cube.top.x = 0;
        mapData.cube.top.y = mapData.cube.size;

        mapData.cube.back.x = 2 * mapData.cube.size;
        mapData.cube.back.y = 2 * mapData.cube.size;

        mapData.cube.right.x = mapData.cube.size;
        mapData.cube.right.y = 3 * mapData.cube.size;

        mapData.cube.front.left = new CubeEdge(1, mapData.cube.front, mapData.cube.left);
        mapData.cube.front.right = new CubeEdge(0, mapData.cube.front, mapData.cube.right);
        mapData.cube.front.top =  new CubeEdge(1, mapData.cube.front, mapData.cube.top);
        mapData.cube.front.bottom = new CubeEdge(1, mapData.cube.front, mapData.cube.bottom);

        mapData.cube.bottom.left = new CubeEdge(2, mapData.cube.bottom, mapData.cube.left);
        mapData.cube.bottom.right = new CubeEdge(1, mapData.cube.bottom, mapData.cube.right);
        mapData.cube.bottom.top = new CubeEdge(3, mapData.cube.bottom, mapData.cube.front);
        mapData.cube.bottom.bottom = new CubeEdge(1, mapData.cube.bottom, mapData.cube.back);

        mapData.cube.left.left = new CubeEdge(2, mapData.cube.left, mapData.cube.top);
        mapData.cube.left.right = new CubeEdge(0, mapData.cube.left, mapData.cube.bottom);
        mapData.cube.left.top = new CubeEdge(0, mapData.cube.left, mapData.cube.front);
        mapData.cube.left.bottom = new CubeEdge(0, mapData.cube.left, mapData.cube.back);

        mapData.cube.top.left = new CubeEdge(0, mapData.cube.top, mapData.cube.right);
        mapData.cube.top.right = new CubeEdge(0, mapData.cube.top, mapData.cube.left);
        mapData.cube.top.top = new CubeEdge(1, mapData.cube.top, mapData.cube.front);
        mapData.cube.top.bottom = new CubeEdge(3, mapData.cube.top, mapData.cube.bottom);

        mapData.cube.back.left = new CubeEdge(2, mapData.cube.back, mapData.cube.left);
        mapData.cube.back.right = new CubeEdge(0, mapData.cube.back, mapData.cube.right);
        mapData.cube.back.top = new CubeEdge(3, mapData.cube.back, mapData.cube.bottom);
        mapData.cube.back.bottom = new CubeEdge(3, mapData.cube.back, mapData.cube.top);

        mapData.cube.right.left = new CubeEdge(2, mapData.cube.right, mapData.cube.back);
        mapData.cube.right.right = new CubeEdge(0, mapData.cube.right, mapData.cube.front);
        mapData.cube.right.top = new CubeEdge(3, mapData.cube.right, mapData.cube.bottom);
        mapData.cube.right.bottom = new CubeEdge(1, mapData.cube.right, mapData.cube.top);
    }

    private static void part1RealMapData(MapData mapData) {
        mapData.cube.front.x = 1 * mapData.cube.size;
        mapData.cube.front.y = 0;

        mapData.cube.bottom.x = 1 * mapData.cube.size;
        mapData.cube.bottom.y = mapData.cube.size;

        mapData.cube.left.x = 0;
        mapData.cube.left.y = 2 * mapData.cube.size;

        mapData.cube.top.x = 0;
        mapData.cube.top.y = 3 * mapData.cube.size;

        mapData.cube.back.x = mapData.cube.size;
        mapData.cube.back.y = 2 * mapData.cube.size;

        mapData.cube.right.x = 2 * mapData.cube.size;
        mapData.cube.right.y = 0;

        mapData.cube.front.left = new CubeEdge(2, mapData.cube.front, mapData.cube.right);
        mapData.cube.front.right = new CubeEdge(0, mapData.cube.front, mapData.cube.right);
        mapData.cube.front.top = new CubeEdge(3, mapData.cube.front, mapData.cube.back);
        mapData.cube.front.bottom = new CubeEdge(1, mapData.cube.front, mapData.cube.bottom);

        mapData.cube.bottom.left = new CubeEdge(2, mapData.cube.bottom, mapData.cube.bottom);
        mapData.cube.bottom.right = new CubeEdge(0, mapData.cube.bottom, mapData.cube.bottom);
        mapData.cube.bottom.top = new CubeEdge(3, mapData.cube.bottom, mapData.cube.front);
        mapData.cube.bottom.bottom = new CubeEdge(1, mapData.cube.bottom, mapData.cube.back);

        mapData.cube.left.left = new CubeEdge(2, mapData.cube.left, mapData.cube.back);
        mapData.cube.left.right = new CubeEdge(0, mapData.cube.left, mapData.cube.back);
        mapData.cube.left.top = new CubeEdge(3, mapData.cube.left, mapData.cube.top);
        mapData.cube.left.bottom = new CubeEdge(1, mapData.cube.left, mapData.cube.top);

        mapData.cube.top.left = new CubeEdge(2, mapData.cube.top, mapData.cube.top);
        mapData.cube.top.right = new CubeEdge(0, mapData.cube.top, mapData.cube.top);;
        mapData.cube.top.top = new CubeEdge(3, mapData.cube.top, mapData.cube.left);
        mapData.cube.top.bottom = new CubeEdge(1, mapData.cube.top, mapData.cube.left);

        mapData.cube.back.left = new CubeEdge(2, mapData.cube.back, mapData.cube.left);
        mapData.cube.back.right = new CubeEdge(0, mapData.cube.back, mapData.cube.left);
        mapData.cube.back.top = new CubeEdge(3, mapData.cube.back, mapData.cube.bottom);
        mapData.cube.back.bottom = new CubeEdge(1, mapData.cube.back, mapData.cube.front);

        mapData.cube.right.left = new CubeEdge(2, mapData.cube.right, mapData.cube.front);
        mapData.cube.right.right = new CubeEdge(0, mapData.cube.right, mapData.cube.front);
        mapData.cube.right.top = new CubeEdge(3, mapData.cube.right, mapData.cube.right);
        mapData.cube.right.bottom = new CubeEdge(1, mapData.cube.right, mapData.cube.right);
    }

    public static long part2(List<String> input, int dataType) {
        return run(input, dataType);
    }

    private static Position move(MapData mapData, Position pos, int steps) {
        var xStep = pos.dir == 0 ? 1 : pos.dir == 2 ? -1 : 0;
        var yStep = pos.dir == 1 ? 1 : pos.dir == 3 ? -1 : 0;
        var x = pos.x;
        var y = pos.y;
        var dir = pos.dir;

        while (mapData.cube.map[y][x] == ' ') {
            x = x + xStep;
            y = y + yStep;
        }

        for (var i = 0; i < steps; i++) {
            var prevx = x;
            var prevy = y;
            var prevDir = pos.dir;
            var prevFace = mapData.cube.currentFace;

            mapData.cube.x += xStep;
            mapData.cube.y += yStep;
            if (mapData.cube.x >= mapData.cube.currentFace.x + mapData.cube.size) {
                mapData.cube.dir = mapData.cube.currentFace.right.dir;
                mapData.cube.currentFace = mapData.cube.currentFace.right.dest;
                mapData.cube.x = mapData.cube.currentFace.x;
            } else if (mapData.cube.x < mapData.cube.currentFace.x) {
                mapData.cube.dir = mapData.cube.currentFace.left.dir;
                mapData.cube.currentFace = mapData.cube.currentFace.left.dest;
                mapData.cube.x = mapData.cube.currentFace.x + mapData.cube.size - 1;
            } else if (mapData.cube.y >= mapData.cube.currentFace.y + mapData.cube.size) {
                mapData.cube.dir = mapData.cube.currentFace.bottom.dir;
                mapData.cube.currentFace = mapData.cube.currentFace.bottom.dest;
                mapData.cube.y = mapData.cube.currentFace.y;
            } else if (mapData.cube.y < mapData.cube.currentFace.y) {
                mapData.cube.dir = mapData.cube.currentFace.top.dir;
                mapData.cube.currentFace = mapData.cube.currentFace.top.dest;
                mapData.cube.y = mapData.cube.currentFace.y + mapData.cube.size - 1;
            }

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

            assert mapData.cube.x == x;
            assert mapData.cube.y == y;
            assert mapData.cube.dir == dir;

            if (mapData.cube.map[mapData.cube.y][mapData.cube.x] == '#') {
                mapData.cube.x = prevx;
                mapData.cube.y = prevy;
                mapData.cube.dir = prevDir;
                mapData.cube.currentFace = prevFace;

            }
            if (mapData.cube.map[y][x] == '#') {
                x = prevx;
                y = prevy;
                dir = prevDir;
                break;
            }
        }
        return new Position(x, y, dir);
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
        private CubeEdge left;
        private CubeEdge right;
        private CubeEdge top;
        private CubeEdge bottom;
    }

    private static class Cube {
        private final char[][] map;
        public int dir;
        private int size = 0;

        private int x;
        private int y;

        private Face top = new Face();
        private Face bottom = new Face();
        private Face left = new Face();
        private Face right = new Face();
        private Face front = new Face();
        private Face back = new Face();
        private Face currentFace = front;

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

            while (map[0][x] == ' ') {
                x++;
            }

        }

    }

    private record CubeEdge(int dir, Face source, Face dest) { }
}
