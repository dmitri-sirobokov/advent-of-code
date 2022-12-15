package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Day 15: Beacon Exclusion Zone
 */
public class Day15 extends CodeBase {


    public static long part1(List<String> input, Position searchAreaMin, Position searchAreaMax) {
        return countNotPossiblePositions(input, searchAreaMin, searchAreaMax);
    }

    public static long part2(List<String> input, Position searchAreaMin, Position searchAreaMax) {
        return findBeaconFrequency(input, searchAreaMin, searchAreaMax);
    }

    private static long countNotPossiblePositions(List<String> input, Position searchAreaMin, Position searchAreaMax) {
        var sensors = readSensors(input);
        var minX = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var minY = Integer.MAX_VALUE;
        var maxY = Integer.MIN_VALUE;

        for (var sensor : sensors) {
            int dist = Math.abs(sensor.sensorPosition.x - sensor.beaconPosition.x) + Math.abs(sensor.sensorPosition.y - sensor.beaconPosition.y);
            minX = Math.min(minX, sensor.sensorPosition.x - dist);
            minY = Math.min(minY, sensor.sensorPosition.y - dist);
            maxX = Math.max(maxX, sensor.sensorPosition.x + dist);
            maxY = Math.max(maxY, sensor.sensorPosition.y + dist);
        }

        minX = Math.max(minX, searchAreaMin.x);
        minY = Math.max(minY, searchAreaMin.y);
        maxX = Math.min(maxX, searchAreaMax.x);
        maxY = Math.min(maxY, searchAreaMax.y);

       var count = countPositionsWhereBeaconCanNotBePresent(sensors, minX, minY, maxX - minX, maxY - minY);

        return count;
    }

    private static long countPositionsWhereBeaconCanNotBePresent(List<Sensor> sensors, int x, int y, int width, int height) {
        // get number of known beacons in the area
        var beaconSet = new HashSet<Position>();
        for (var sensor : sensors) {
            if (sensor.beaconPosition.x >= x && sensor.beaconPosition.x <= x + width && sensor.beaconPosition.y >= y && sensor.beaconPosition.y <= y + height) {
                beaconSet.add(sensor.beaconPosition);
            }
        }
        for (var sensor : sensors) {
            if (sensor.areaFullyCovered(x, y, width, height)) {
                return (width + 1) * (height + 1) - beaconSet.size();
            }
        }
        var fullyUncovered = true;
        for (var sensor : sensors) {
            if (!sensor.areaFullyUnCovered(x, y, width, height)) {
                fullyUncovered = false;
                break;
            }
        }
        if (fullyUncovered) {
            return 0;
        }
        if (width == 0 && height == 0) {
            return 0;
        }
        var count = countPositionsWhereBeaconCanNotBePresent(sensors, x, y, width / 2, height / 2);
        if (width > 0) {
            count += countPositionsWhereBeaconCanNotBePresent(sensors, x + width / 2 + 1, y, width - width / 2 - 1, height / 2);
        }
        if (height > 0)  {
            count += countPositionsWhereBeaconCanNotBePresent(sensors, x, y + height / 2 + 1, width / 2, height - height / 2 - 1);
        }
        if (width > 0 && height > 0) {
            count += countPositionsWhereBeaconCanNotBePresent(sensors, x + width / 2 + 1, y + height / 2 + 1, width - width / 2 - 1, height - height / 2 - 1);
        }
        return count;
    }

    private static long findBeaconFrequency(List<String> input, Position searchAreaMin, Position searchAreaMax) {
        var sensors = readSensors(input);
        var minX = Integer.MAX_VALUE;
        var maxX = Integer.MIN_VALUE;
        var minY = Integer.MAX_VALUE;
        var maxY = Integer.MIN_VALUE;

        for (var sensor : sensors) {
            int dist = Math.abs(sensor.sensorPosition.x - sensor.beaconPosition.x) + Math.abs(sensor.sensorPosition.y - sensor.beaconPosition.y);
            minX = Math.min(minX, sensor.sensorPosition.x - dist);
            minY = Math.min(minY, sensor.sensorPosition.y - dist);
            maxX = Math.max(maxX, sensor.sensorPosition.x + dist);
            maxY = Math.max(maxY, sensor.sensorPosition.y + dist);
        }

        minX = Math.max(minX, searchAreaMin.x);
        minY = Math.max(minY, searchAreaMin.y);
        maxX = Math.min(maxX, searchAreaMax.x);
        maxY = Math.min(maxY, searchAreaMax.y);

        var position = findMissingBeacon(sensors, minX, minY, maxX - minX, maxY - minY);

        return (long)position.x * 4000000 + position.y;
    }

    private static Position findMissingBeacon(List<Sensor> sensors, int x, int y, int width, int height) {
        for (var sensor : sensors) {
            if (sensor.areaFullyCovered(x, y, width, height)) {
                return null;
            }
        }
        if (width == 0 && height == 0) {
            return new Position(x, y);
        }
        var position = findMissingBeacon(sensors, x, y, width / 2, height / 2);
        if (width > 0 && position == null) {
            position = findMissingBeacon(sensors, x + width / 2 + 1, y, width - width / 2 - 1, height / 2);
        }
        if (height > 0 && position == null)  {
            position = findMissingBeacon(sensors, x, y + height / 2 + 1, width / 2, height - height / 2 - 1);
        }
        if (width > 0 && height > 0 && position == null) {
            position = findMissingBeacon(sensors, x + width / 2 + 1, y + height / 2 + 1, width - width / 2 - 1, height - height / 2 - 1);
        }
        return position;
    }

    private static List<Sensor> readSensors(List<String> input) {
        var sensors = new ArrayList<Sensor>();
        for (var line : input) {
            var xyParts = line.replaceAll("[^-?\\d]+", " ").trim().split(" ");
            var xySensor = new Position(Integer.parseInt(xyParts[0]), Integer.parseInt(xyParts[1]));
            var xyBeacon = new Position(Integer.parseInt(xyParts[2]), Integer.parseInt(xyParts[3]));
            var sensor = new Sensor(xySensor, xyBeacon);
            sensors.add(sensor);
        }
        return sensors;
    }

    private record Sensor(Position sensorPosition, Position beaconPosition) {

        // test if the area specified by x,y, width and height is fully covered by the sensor
        public boolean areaFullyCovered(int x, int y, int width, int height) {
            // if all 4 corners of the area are inside the sensor radius, then the whole area is inside the sensor radius
            var x1 = x + width;
            var y1 = y + height;
            var dist1 = Math.abs(x - sensorPosition.x) + Math.abs(y - sensorPosition.y);
            var dist2 = Math.abs(x1 - sensorPosition.x) + Math.abs(y - sensorPosition.y);
            var dist3 = Math.abs(x1 - sensorPosition.x) + Math.abs(y1 - sensorPosition.y);
            var dist4 = Math.abs(x - sensorPosition.x) + Math.abs(y1 - sensorPosition.y);
            var sensorRadius = Math.abs(sensorPosition.x - beaconPosition.x) + Math.abs(sensorPosition.y - beaconPosition.y);
            return dist1 <= sensorRadius && dist2 <= sensorRadius && dist3 <= sensorRadius && dist4 <= sensorRadius;
        }

        public boolean areaFullyUnCovered(int x, int y, int width, int height) {
            var x1 = x + width;
            var y1 = y + height;
            var dist1 = Math.abs(y - sensorPosition.y);
            var dist2 = Math.abs(x - sensorPosition.x);
            var dist3 = Math.abs(y - sensorPosition.y + height);
            var dist4 = Math.abs(x - sensorPosition.x + width);
            var sensorRadius = Math.abs(sensorPosition.x - beaconPosition.x) + Math.abs(sensorPosition.y - beaconPosition.y);
            return dist1 > sensorRadius && dist2 > sensorRadius && dist3 > sensorRadius && dist4 > sensorRadius;
        }
    }

    private static class Area {
        private Position topLeft;
        private int width;
        private int height;

        private boolean covered;
    }

    // todo: do not expose as public, or make it global and share for every test
    public record Position(int x, int y) { }

}
