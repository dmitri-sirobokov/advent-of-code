package dms.adventofcode.y2024;


import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Day14: Restroom Redoubt
 *
 */
public class Day14 extends CodeBase {

    public record Vector(long x, long y) {
        public Vector add(Vector vector) {
            return new Vector(this.x + vector.x, this.y + vector.y);
        }

        public Vector multiply(long scalar) {
            return new Vector(this.x * scalar, this.y * scalar);
        }
    }

    private static class Robot {
        private Vector position;
        private final Vector speed;
        public Robot(Vector position, Vector speed) {
            this.position = position;
            this.speed = speed;
        }

        public void move(long ticks, Vector roomSize) {
            var newPosition = this.position.add(this.speed.multiply(ticks));
            long newX = newPosition.x % roomSize.x;
            long newY = newPosition.y % roomSize.y;
            if (newX < 0) {
                newX += roomSize.x;
            }
            if (newY < 0) {
                newY += roomSize.y;
            }
            this.position = new Vector(newX, newY);
        }
    }

    private static Vector readVector(String input) {
        var parts = input.split(",");
        return new Vector(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    private static List<Robot> readRobots(List<String> lines) {
        List<Robot> robots = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(" ");
            if (parts.length != 2) throw new RuntimeException("Input parse error");
            var location = readVector(parts[0].substring(2));
            var speed = readVector(parts[1].substring(2));
            var robot = new Robot(location, speed);
            robots.add(robot);
        }
        return robots;
    }

    private static long runRobots(List<Robot> robots, long ticks, Vector roomSize) {
        for (Robot robot : robots) {
            robot.move(ticks, roomSize);
        }

        var result1 = robots.stream()
                .filter(robot ->  (robot.position.x < (roomSize.x / 2) && (robot.position.y < (roomSize.y / 2))))
                .count();
        var result2 = robots.stream()
                .filter(robot ->  (robot.position.x >= (roomSize.x - roomSize.x / 2) && (robot.position.y >= (roomSize.y - roomSize.y / 2))))
                .count();
        var result3 = robots.stream()
                .filter(robot ->  (robot.position.x < (roomSize.x / 2) && (robot.position.y >= (roomSize.y - roomSize.y / 2))))
                .count();
        var result4 = robots.stream()
                .filter(robot ->  (robot.position.x >= (roomSize.x - roomSize.x / 2) && (robot.position.y < (roomSize.y / 2))))
                .count();
        return result1 *  result2 * result3 * result4;
    }

    private static List<String> robotsToLines(List<Robot> robots, Vector roomSize, char ch) {
        var map = new char[(int)roomSize.y][(int)roomSize.x];
        for (var row : map) {
            Arrays.fill(row, '.');
        }
        for (Robot robot : robots) {
            map[(int)robot.position.y][(int)robot.position.x] = ch;
        }
        var result = new ArrayList<String>();
        for (var row : map) {
            result.add(String.valueOf(row));
        }
        return result;
    }
    private static void printRobots(List<Robot> robots, Vector roomSize, char ch) {
        var map = new char[(int)roomSize.y][(int)roomSize.x];
        for (var row : map) {
            Arrays.fill(row, '.');
        }
        for (Robot robot : robots) {
            map[(int)robot.position.y][(int)robot.position.x] = ch;
        }
        printMatrix(map);
    }

    public static long part1(List<String> input, long ticks, Vector roomSize) {
        var robots = readRobots(input);
        return runRobots(robots, ticks, roomSize);
    }

    public static long part2(List<String> input) {
        var robots = readRobots(input);
        var roomSize = new Vector(101, 103);

        // search for "******" arrangement of robots that probably is Easter egg arrangement
        var result = 0;
         while(true) {
             result++;
            runRobots(robots, 1, roomSize);
            var lines = robotsToLines(robots, roomSize, '*');
            boolean match = lines.stream().anyMatch(line -> line.contains("*********"));
            if (match) {
                printRobots(robots, roomSize, '*');
                break;
            }
        }

        return result;
    }

}
