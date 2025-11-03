package dms.adventofcode.y2024;


import dms.adventofcode.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Day15: Warehouse Woes
 *
 */
public class Day15 {

    private enum ObstacleType {
        BOX,
        WALL
    }

    private static class Obstacle {
        private Vector pos;
        private final ObstacleType type;
        private final long width;

        Obstacle(ObstacleType type, Vector pos, long width) {
            this.type = type;
            this.pos = pos;
            this.width = width;
        }
    }

    private static class Warehouse {
        private final Obstacle[][] map;
        private final List<Obstacle> obstacles;
        private final List<Vector> directions;
        private Vector currentPosition;

        Warehouse(List<String> lines, long obstacleWidth) {
            this.obstacles = new ArrayList<>();
            this.directions = new ArrayList<>();
            this.map = new Obstacle[lines.size()][(int)obstacleWidth * lines.getFirst().length()];

            var y = 0;
            for (y = 0; y < lines.size(); y++) {
                var line = lines.get(y);
                if (line.trim().isBlank()) break;

                for (int x = 0; x < line.length(); x++) {
                    var ch =  line.charAt(x);
                    var obstacle  = switch (ch) {
                        case '#' -> new Obstacle(ObstacleType.WALL, new Vector(obstacleWidth * x, y), obstacleWidth);
                        case 'O' -> new Obstacle(ObstacleType.BOX,  new Vector(obstacleWidth * x, y), obstacleWidth);
                        default -> null;
                    };
                    for (var i = 0; i < obstacleWidth; i++) {
                        this.map[y][(int)obstacleWidth * x + i] = obstacle;
                    }
                    if (obstacle != null) {
                        for (var i = 0; i < obstacleWidth; i++) {
                            this.map[obstacle.pos.intY()][obstacle.pos.intX() + i] = obstacle;
                        }
                        this.obstacles.add(obstacle);
                    }
                    if (ch == '@') {
                        this.currentPosition = new Vector(obstacleWidth * x, y);
                    }
                }
            }

            y++;
            for (; y < lines.size(); y++) {
                var line = lines.get(y);
                for (int x = 0; x < line.length(); x++) {
                    var ch =  line.charAt(x);
                    var direction = switch (ch) {
                        case '>' -> new Vector(1, 0);
                        case '<' -> new Vector(-1, 0);
                        case '^' -> new Vector(0, -1);
                        case 'v' -> new Vector(0, 1);
                        default -> null;
                    };
                    this.directions.add(direction);
                }
            }

        }

        private boolean canPush(Vector position, Vector direction, Set<Obstacle> obstaclesToPush) {
            var obstacle = map[position.intY()][position.intX()];
            if (obstacle == null) return true;
            if (obstacle.type == ObstacleType.WALL) return false;
            obstaclesToPush.add(obstacle);

            var positionsToPush = new ArrayList<Vector>();
            if (direction.y() != 0) {
                for (var i = 0; i < obstacle.width; i++) {
                    positionsToPush.add(obstacle.pos.add(new Vector(i, direction.y())));
                }
            } else {
                positionsToPush.add(position.add(direction));
            }

            for (var positionToPush : positionsToPush) {
                if (!canPush(positionToPush, direction, obstaclesToPush)) return false;
            }
            return true;
        }

        private void push(Set<Obstacle> obstaclesToPush, Vector direction) {
            for (Obstacle obstacle : obstaclesToPush) {
                for (var i = 0; i < obstacle.width; i++) {
                    map[obstacle.pos.intY()][obstacle.pos.intX() + i] = null;
                }
            }

            for (Obstacle obstacle : obstaclesToPush) {
                obstacle.pos = obstacle.pos.add(direction);
                for (var i = 0; i < obstacle.width; i++) {
                    map[obstacle.pos.intY()][obstacle.pos.intX() + i] = obstacle;
                }
            }
            currentPosition = currentPosition.add(direction);
        }

        public void print() {
            System.out.println();
            for (var y = 0; y < map.length; y++) {
                for (var x = 0; x < map[0].length; x++) {
                    if (currentPosition.x() == x && currentPosition.y() == y) {
                        System.out.print("@");
                        continue;
                    }
                    if (map[y][x] == null) System.out.print(".");
                    if (map[y][x] != null) {
                        if (map[y][x].type == ObstacleType.BOX) {
                            if (map[y][x].pos.x()  == x) {
                                System.out.print("[");
                            } else  {
                                System.out.print("]");
                            }

                        } else if (map[y][x].type == ObstacleType.WALL) {
                            System.out.print("#");
                        }
                    }
                }
                System.out.println();
            }
        }

        public void run() {
            for (var direction : directions) {
                var obstaclesToPush = new HashSet<Obstacle>();
                var nextPosition = currentPosition.add(direction);
                if (canPush(nextPosition, direction, obstaclesToPush)) {
                    push(obstaclesToPush, direction);
                }
            }
        }
    }

    public static long part1(List<String> input) {
        var warehouse = new Warehouse(input, 1);
        warehouse.run();

        return warehouse.obstacles
                .stream()
                .filter(obstacle -> obstacle.type == ObstacleType.BOX)
                .mapToLong(box -> box.pos.x() + 100L * box.pos.y())
                .sum();
    }

    public static long part2(List<String> input) {
        var warehouse = new Warehouse(input, 2);
        warehouse.run();
//        System.out.println("======= Warehouse after =======");
//        warehouse.print();
        return warehouse.obstacles
                .stream()
                .filter(obstacle -> obstacle.type == ObstacleType.BOX)
                .mapToLong(box -> box.pos.x() + 100L * box.pos.y())
                .sum();
    }
}
