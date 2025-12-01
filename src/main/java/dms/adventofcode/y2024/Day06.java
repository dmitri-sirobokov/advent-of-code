package dms.adventofcode.y2024;


import dms.adventofcode.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Day06: Guard Gallivant
 *
 */
public class Day06 {

    private static class MapCellState {
        private boolean isObstruction;
        private final Set<Vector> visitedDirections = new HashSet<>();
    }

    private enum GuardRunState {
        // Guard is walking inside the room, watching
        RUNNING,
        // Guard came into infinite loop
        LOOP,
        // Guard exit the map.
        EXIT_MAP,

    }

    private static class Map {
        private final MapCellState[][] cells;
        private Vector guardLocation;
        private Vector guardDirection;
        private int visitCount;

        public Map(List<String> input) {
            cells = new MapCellState[input.size()][];
            guardLocation = new Vector(0, 0);
            guardDirection = new Vector(1, 0);
            for (int i = 0; i < input.size(); i++) {
                var line = input.get(i);
                cells[i] = new MapCellState[line.length()];
                for (var j = 0; j < line.length(); j++) {
                    cells[i][j] = new MapCellState();
                    var ch =  line.charAt(j);
                    switch (ch) {
                        case '^', '>', 'v', 'V', '<':
                            guardLocation = new Vector(i, j);
                            guardDirection = switch (ch) {
                                case '^' -> new Vector(0, -1);
                                case '>' -> new Vector(1, 0);
                                case 'V','v' -> new Vector(0, 1);
                                case '<' -> new Vector(-1, 0);
                                default -> throw new IllegalArgumentException(Character.toString(ch));
                            };
                            this.cells[i][j].visitedDirections.add(this.guardDirection);
                            break;
                        case '#':
                            cells[i][j].isObstruction = true;
                            break;
                    }
                }
            }
        }

        private GuardRunState moveGuard() {
            var nextGuardLocation = getNextGuardLocation();
            if (nextGuardLocation.x() < 0 || nextGuardLocation.x() >= cells[0].length) {
                return GuardRunState.EXIT_MAP;
            }
            if (nextGuardLocation.y() < 0 || nextGuardLocation.y() >= cells.length) {
                return GuardRunState.EXIT_MAP;
            }
            var nextGuardCell = cells[nextGuardLocation.intY()][nextGuardLocation.intX()];
            if (nextGuardCell.isObstruction) {

                this.guardDirection = this.guardDirection.rotate90deg(true);
            } else {
                this.guardLocation = nextGuardLocation;
                if (nextGuardCell.visitedDirections.contains(this.guardDirection)) {
                    return GuardRunState.LOOP;
                }
                nextGuardCell.visitedDirections.add(this.guardDirection);
            }
            return GuardRunState.RUNNING;
        }

        private Vector getNextGuardLocation() {
            return guardLocation.add(guardDirection);
        }

        public GuardRunState runGuard() {
            var guardState = GuardRunState.RUNNING;
            while (guardState == GuardRunState.RUNNING) {
                guardState = moveGuard();
            }

            for (MapCellState[] cell : cells) {
                for (MapCellState mapCellState : cell) {
                    if (!mapCellState.visitedDirections.isEmpty()) {
                        visitCount++;
                    }
                }
            }
            return guardState;
        }

    }

    public static long part1(List<String> input) {
        var map = new Map(input);
        map.runGuard();
        return map.visitCount;
    }

    public static long part2(List<String> input) {
        var map = new Map(input);
        map.runGuard();

        // get all visited locations and try to loop the guard by setting an obstruction
        var visitedLocations = new ArrayList<Vector>();
        for  (var i = 0; i < map.cells.length; i++) {
            for (var j = 0; j < map.cells[i].length; j++) {
                if (!map.cells[i][j].visitedDirections.isEmpty()) {
                    visitedLocations.add(new Vector(i, j));
                }
            }
        }

        var loopCounts = 0;
        for (var visitedLocation : visitedLocations) {
            map = new Map(input);
            // can't put an obstacle right next in front of the guard, without him noticing it.
            if (visitedLocation.equals(map.guardLocation) || visitedLocation.equals(map.getNextGuardLocation())) {
                continue;
            }
            map.cells[visitedLocation.intY()][visitedLocation.intX()].isObstruction = true;
            if (map.runGuard() == GuardRunState.LOOP) {
                loopCounts++;
            }
        }
        return loopCounts;
    }
}
