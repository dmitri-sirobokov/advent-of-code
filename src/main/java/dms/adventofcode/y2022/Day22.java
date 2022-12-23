package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        } else if (sampleType == 3) {
            part2RealMapData(mapData);
        }

        var instructionCount = 0;
        for (var i = 0; i < mapData.instructions.length(); i++) {
            var instructionDisplayText = "";
            if (Character.isDigit(mapData.instructions.charAt(i))) {
                var i1 = i + 1;
                while (i1 < mapData.instructions.length() && Character.isDigit(mapData.instructions.charAt(i1))) {
                    i1++;
                }
                var steps = Integer.parseInt(mapData.instructions.substring(i, i1));
                move(mapData, steps);
                i = i1 - 1;
                instructionDisplayText = "Move " + steps;
            } else {
                var dir = mapData.cube.dir;
                if (mapData.instructions.charAt(i) == 'R') {
                    dir++;
                    if (dir > 3) {
                        dir = 0;
                    }
                    instructionDisplayText = "Rotate R";
                } else {
                    dir--;
                    if (dir < 0) {
                        dir = 3;
                    }
                    instructionDisplayText = "Rotate L";
                }
                mapData.cube.dir = dir;

            }
            instructionCount++;
            // printMap(mapData, instructionCount, instructionDisplayText);
        }

        // printMap(mapData, instructionCount, "End");

        return 1000 * (mapData.cube.y + 1) + 4 * (mapData.cube.x + 1) + mapData.cube.dir;
    }

    private static void printMap(MapData mapData, int step, String instructionName) {
        System.out.println("== After Instruction " + step + " (" + instructionName + "): ==");
        printMatrix(mapData.cube.map);
        System.out.println();
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

        mapData.cube.right.x = 3 * mapData.cube.size;
        mapData.cube.right.y = 2 * mapData.cube.size;

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

    private static void part2RealMapData(MapData mapData) {
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

        mapData.cube.front.left = new CubeEdge(0, mapData.cube.front, mapData.cube.left);
        mapData.cube.front.right = new CubeEdge(0, mapData.cube.front, mapData.cube.right);
        mapData.cube.front.top = new CubeEdge(0, mapData.cube.front, mapData.cube.top);
        mapData.cube.front.bottom = new CubeEdge(1, mapData.cube.front, mapData.cube.bottom);

        mapData.cube.bottom.left = new CubeEdge(1, mapData.cube.bottom, mapData.cube.left);
        mapData.cube.bottom.right = new CubeEdge(3, mapData.cube.bottom, mapData.cube.right);
        mapData.cube.bottom.top = new CubeEdge(3, mapData.cube.bottom, mapData.cube.front);
        mapData.cube.bottom.bottom = new CubeEdge(1, mapData.cube.bottom, mapData.cube.back);

        mapData.cube.left.left = new CubeEdge(0, mapData.cube.left, mapData.cube.front);
        mapData.cube.left.right = new CubeEdge(0, mapData.cube.left, mapData.cube.back);
        mapData.cube.left.top = new CubeEdge(0, mapData.cube.left, mapData.cube.bottom);
        mapData.cube.left.bottom = new CubeEdge(1, mapData.cube.left, mapData.cube.top);

        mapData.cube.top.left = new CubeEdge(1, mapData.cube.top, mapData.cube.front);
        mapData.cube.top.right = new CubeEdge(3, mapData.cube.top, mapData.cube.back);;
        mapData.cube.top.top = new CubeEdge(3, mapData.cube.top, mapData.cube.left);
        mapData.cube.top.bottom = new CubeEdge(1, mapData.cube.top, mapData.cube.right);

        mapData.cube.back.left = new CubeEdge(2, mapData.cube.back, mapData.cube.left);
        mapData.cube.back.right = new CubeEdge(2, mapData.cube.back, mapData.cube.right);
        mapData.cube.back.top = new CubeEdge(3, mapData.cube.back, mapData.cube.bottom);
        mapData.cube.back.bottom = new CubeEdge(2, mapData.cube.back, mapData.cube.top);

        mapData.cube.right.left = new CubeEdge(2, mapData.cube.right, mapData.cube.front);
        mapData.cube.right.right = new CubeEdge(2, mapData.cube.right, mapData.cube.back);
        mapData.cube.right.top = new CubeEdge(3, mapData.cube.right, mapData.cube.top);
        mapData.cube.right.bottom = new CubeEdge(2, mapData.cube.right, mapData.cube.bottom);
    }

    public static long part2(List<String> input, int dataType) {
        return run(input, dataType);
    }

    private static void move(MapData mapData, int steps) {
        for (var i = 0; i < steps; i++) {
            var xStep = mapData.cube.dir == 0 ? 1 : mapData.cube.dir == 2 ? -1 : 0;
            var yStep = mapData.cube.dir == 1 ? 1 : mapData.cube.dir == 3 ? -1 : 0;
            var prevx = mapData.cube.x;
            var prevy = mapData.cube.y;
            var prevDir = mapData.cube.dir;
            var prevFace = mapData.cube.currentFace;

            mapData.cube.x += xStep;
            mapData.cube.y += yStep;
            CubeEdge edge = null;
            if (mapData.cube.x >= mapData.cube.currentFace.x + mapData.cube.size) {
                edge = mapData.cube.currentFace.right;
            } else if (mapData.cube.x < mapData.cube.currentFace.x) {
                edge = mapData.cube.currentFace.left;
            } else if (mapData.cube.y >= mapData.cube.currentFace.y + mapData.cube.size) {
                edge = mapData.cube.currentFace.bottom;
            } else if (mapData.cube.y < mapData.cube.currentFace.y) {
                edge = mapData.cube.currentFace.top;
            }

            if (edge != null) {
                mapData.cube.currentFace = edge.dest;

                // transform coordinates from one cube face to another
                // todo: we should use a transform matrix for that, instead of giant switch
                var x1 = mapData.cube.x;
                var y1 = mapData.cube.y;
                switch (mapData.cube.dir) {
                    case 0: switch (edge.dir) {
                        // from right to right
                        case 0 -> {
                            x1 = edge.dest.x;
                            y1 = edge.dest.y + (mapData.cube.y - edge.source.y);
                        }
                        // from right to down
                        case 1 -> {
                            x1 = edge.dest.x + mapData.cube.size - (mapData.cube.y - edge.source.y) - 1;
                            y1 = edge.dest.y;
                        }
                        // from right to left
                        case 2 -> {
                            x1 = edge.dest.x + mapData.cube.size - 1;
                            y1 = edge.dest.y + mapData.cube.size - (mapData.cube.y - edge.source.y) - 1;
                        }
                        // from right to up
                        case 3 -> {
                            x1 = edge.dest.x + mapData.cube.y - edge.source.y;
                            y1 = edge.dest.y + mapData.cube.size - 1;
                        }

                    }
                    break;

                    case 1: switch (edge.dir) {
                        // from down to right
                        case 0 -> {
                            x1 = edge.dest.x;
                            y1 = edge.dest.y + mapData.cube.size - (mapData.cube.x - edge.source.x) - 1;
                        }
                        // from down to down
                        case 1 -> {
                            x1 = edge.dest.x + (mapData.cube.x - edge.source.x);
                            y1 = edge.dest.y;
                        }
                        // from down to left
                        case 2 -> {
                            x1 = edge.dest.x + mapData.cube.size - 1;
                            y1 = edge.dest.y + (mapData.cube.x - edge.source.x);
                        }
                        // from down to up
                        case 3 -> {
                            x1 = edge.dest.x + mapData.cube.size - (mapData.cube.x - edge.source.x) - 1;
                            y1 = edge.dest.y + mapData.cube.size - 1;
                        }
                    }
                    break;

                    case 2: switch (edge.dir) {
                        // from left to right
                        case 0 -> {
                            x1 = edge.dest.x;
                            y1 = edge.dest.y + mapData.cube.size - (mapData.cube.y - edge.source.y) - 1;
                        }
                        // from left to down
                        case 1 -> {
                            x1 = edge.dest.x + (mapData.cube.y - edge.source.y);
                            y1 = edge.dest.y;
                        }
                        // from left to left
                        case 2 -> {
                            x1 = edge.dest.x + mapData.cube.size - 1;
                            y1 = edge.dest.y + (mapData.cube.y - edge.source.y);
                        }
                        // from left to up
                        case 3 -> {
                            x1 = edge.dest.x + mapData.cube.size - (mapData.cube.y - edge.source.y) - 1;
                            y1 = edge.dest.y + mapData.cube.size - 1;
                        }
                    }
                    break;

                    case 3: switch (edge.dir) {
                        // from up to right
                        case 0 -> {
                            x1 = edge.dest.x;
                            y1 = edge.dest.y + mapData.cube.x - edge.source.x;
                        }
                        // from up to down
                        case 1 -> {
                            x1 = edge.dest.x + mapData.cube.size - (mapData.cube.x - edge.source.x) - 1;
                            y1 = edge.dest.y;
                        }
                        // from up to left
                        case 2 -> {
                            x1 = edge.dest.x + mapData.cube.size - 1;
                            y1 = edge.dest.y + mapData.cube.size - 1 - (mapData.cube.x - edge.source.x);
                        }
                        // from up to up
                        case 3 -> {
                            x1 = edge.dest.x + (mapData.cube.x - edge.source.x);
                            y1 = edge.dest.y + mapData.cube.size - 1;
                        }
                    }
                        break;
                }
                mapData.cube.dir = edge.dir;
                mapData.cube.x = x1;
                mapData.cube.y = y1;

                var key = "(" + edge.source.x + "," + edge.source.y + ")->(" + edge.dest.x + "," + edge.dest.y + "); " +
                        "; dir " + prevDir + " -> " + mapData.cube.dir;
                if (mapData.crossedEdges.add(key)) {
                    System.out.println("Crossing the edge from face " + key
                            + "; xy changed (" + prevx + "," + prevy + ") -> (" + mapData.cube.x + "," + mapData.cube.y + ")");
                }

            }

            if (mapData.cube.map[mapData.cube.y][mapData.cube.x] == '#') {
                if (edge != null) {
                    System.out.println("Crossing the edge is blocked at position (" + mapData.cube.x + "," + mapData.cube.y + "). Rolling back coordinates.");
                }
                mapData.cube.x = prevx;
                mapData.cube.y = prevy;
                mapData.cube.dir = prevDir;
                mapData.cube.currentFace = prevFace;
                break;
            }
            assert mapData.cube.map[mapData.cube.y][mapData.cube.x] != ' ';
            assert mapData.cube.map[mapData.cube.y][mapData.cube.x] != '#';
            mapData.cube.map[mapData.cube.y][mapData.cube.x] = switch (mapData.cube.dir) {
                case 0 -> '>';
                case 1 -> 'V';
                case 2 -> '<';
                case 3 -> '^';
                default -> '?';
            };
        }
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

        // for debugging purpose
        private final Set<String> crossedEdges = new HashSet<>();

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
