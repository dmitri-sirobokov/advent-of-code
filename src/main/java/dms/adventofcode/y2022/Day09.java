package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day09 extends CodeBase {

    public static long part1(List<String> input) {
        return countTailMovements(input, 2);
    }

    public static long part2(List<String> input) {
        return countTailMovements(input, 10);
    }


    private static long countTailMovements(List<String> input, int knotCount) {
        var motions = readMotions(input);
        var grid = new int[1][1];
        grid[0][0] = 1;
        var knots = new Position[knotCount];
        for (var i = 0; i < knotCount; i++) {
            knots[i] = new Position(0, 0);
        }

        for (var motion : motions) {
            for (var step = 0; step < motion.distance; step++) {
                var head = knots[0];
                switch (motion.direction) {
                    case UP -> head.y--;
                    case DOWN -> head.y++;
                    case LEFT -> head.x--;
                    case RIGHT -> head.x++;
                }

                grid = enlageGridIfNeeded(grid, knots, head);

                for (var knotIndex = 1; knotIndex < knots.length; knotIndex++) {
                    followTheHead(knots[knotIndex - 1], knots[knotIndex]);
                }

                grid[knots[knots.length - 1].y][knots[knots.length - 1].x]++;
            }
        }

        return Arrays.stream(grid)
                .flatMapToInt(Arrays::stream)
                .filter(visitsCount -> visitsCount > 0)
                .count();
    }

    private static void followTheHead(Position head, Position knot) {
        if (knot.x < head.x - 1) {
            knot.x ++;
            if (knot.y < head.y) {
                knot.y++;
            } else if (knot.y > head.y) {
                knot.y--;
            }
        }
        if (knot.x > head.x + 1) {
            knot.x --;
            if (knot.y < head.y) {
                knot.y++;
            } else if (knot.y > head.y) {
                knot.y--;
            }
        }
        if (knot.y > head.y + 1) {
            knot.y--;
            if (knot.x < head.x) {
                knot.x++;
            } else if (knot.x > head.x) {
                knot.x--;
            }
        }
        if (knot.y < head.y - 1) {
            knot.y++;
            if (knot.x < head.x) {
                knot.x++;
            } else if (knot.x > head.x) {
                knot.x--;
            }
        }
    }

    private static int[][] enlageGridIfNeeded(int[][] grid, Position[] knots, Position head) {
        if (head.x < 0) {
            return enlargeGridX(grid, knots, head.x);
        }

        if (head.x >= grid[0].length) {
            return enlargeGridX(grid, knots, head.x - grid[0].length + 1);
        }

        if (head.y < 0) {
            return enlargeGridY(grid, knots, head.y);
        }

        if (head.y >= grid.length) {
            return enlargeGridY(grid, knots, head.y - grid.length + 1);
        }
        return grid;
    }

    private static int[][] enlargeGridY(int[][] grid, Position[] knots, int increase) {
        if (increase < 0) {
            var prevLength = grid.length;
            grid = Arrays.copyOf(grid, grid.length - increase);
            for (var y = prevLength; y < grid.length; y++) {
                grid[y] = new int[grid[0].length];
            }
            ArrayUtils.shift(grid, -increase);
            for (var knot : knots) {
                knot.y -= increase;
            }
        }

        if (increase > 0) {
            var prevLength = grid.length;
            grid = Arrays.copyOf(grid, grid.length + increase);
            for (var y = prevLength; y < grid.length; y++) {
                grid[y] = new int[grid[0].length];
            }
        }
        return grid;
    }

    private static int[][] enlargeGridX(int[][] grid, Position[] knots, int increase) {
        if (increase > 0) {
            for (var y = 0; y < grid.length; y++) {
                grid[y] = Arrays.copyOf(grid[y], grid[y].length + increase);
            }
        }
        if (increase < 0) {
            for (var y = 0; y < grid.length; y++) {
                grid[y] = Arrays.copyOf(grid[y], grid[y].length - increase);
                ArrayUtils.shift(grid[y], -increase);
            }
            for (var knot : knots) {
                knot.x -= increase;
            }
        }
        return grid;
    }

    private static List<Motion> readMotions(List<String> input) {
        var result = new ArrayList<Motion>();
        for (var line : input) {
            var lineParts = line.split(" ");

            Direction direction = switch (lineParts[0]) {
                case "U" -> Direction.UP;
                case "D" -> Direction.DOWN;
                case "L" -> Direction.LEFT;
                case "R" -> Direction.RIGHT;
                default -> throw new RuntimeException("Invalid Direction value in '" + line + "'");
            };
            result.add(new Motion(direction, Integer.parseInt(lineParts[1])));
        }
        return result;
    }

    private record Motion(Direction direction, int distance) { }

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private static class Position {

        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
