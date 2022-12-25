package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import dms.adventofcode.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Day 16: Proboscidea Volcanium
 */
public class Day16 extends CodeBase {


    public static long part1(List<String> input) {

        var graph = readValves(input);

        var path = new ArrayList<Valve>();
        var openValves = new HashMap<Valve, Integer>();
        path.add(graph.startValve);
        var newPressure = calcPressureReleased(graph, 0, path, openValves);
        printPath(path, newPressure);
        return newPressure;
    }

    public static long part2(List<String> input) {
        var graph = readValves(input);

        var openValves = new HashMap<Valve, Integer>();
        var pathMe = new ArrayList<Valve>();
        pathMe.add(graph.startValve);
        var pathElephant = new ArrayList<Valve>();
        pathElephant.add(graph.startValve);
        var newPressure = calcPressureReleased2(graph, 0, 0, pathMe, pathElephant, openValves);
        printPath(pathMe, newPressure);
        printPath(pathElephant, newPressure);
        return newPressure;
    }


    private static void printPath(List<Valve> path, int pressureReleased) {
        var string = new StringBuilder();
        string.append(String.join("->", path.stream().map(valve -> valve.name).toList()));
        string.append("=");

        string.append(pressureReleased);

        System.out.println(string);
    }

    /**
     * Part1: Calculate total pressure release given the opening sequence of the valves and within a time we have.
     */
    private static int calcPressureReleased(ValveGraph graph, int minutesElapsed, List<Valve> path, Map<Valve, Integer> openValves) {
        var timeLimit = 30;
        var maximumPressure = 0;
        var currentValve = path.get(path.size() - 1);
        if (minutesElapsed >= timeLimit || path.size() == graph.valvesToOpen.size() + 1) {
            var pressureReleased = 0;

            for (var openValveKeyValue : openValves.entrySet()) {
                var minutesOpened = Math.max(timeLimit - openValveKeyValue.getValue(), 0);
                pressureReleased += openValveKeyValue.getKey().rate * minutesOpened;
            }
           return pressureReleased;
        } else {
            for (var nextValve : graph.valvesToOpen) {
                if (!openValves.containsKey(nextValve)) {
                    int travelTime = getTravelTimeCached(graph, currentValve, nextValve);
                    var newMinutesElapsed = minutesElapsed + travelTime + 1;

                    var newOpenValves = new HashMap<>(openValves);
                    newOpenValves.put(nextValve, newMinutesElapsed);

                    var newPath = new ArrayList<>(path);
                    newPath.add(nextValve);

                    maximumPressure = Math.max(maximumPressure, calcPressureReleased(graph, newMinutesElapsed, newPath, newOpenValves));
                }
            }
        }
        return maximumPressure;
    }

    private static int getTravelTimeCached(ValveGraph graph, Valve currentValve, Valve nextValve) {
        var shortestPathKey1 = currentValve.name + nextValve.name;
        var shortestPathKey2 = nextValve.name + currentValve.name;
        int travelTime;
        if (graph.shortestPathsCache.containsKey(shortestPathKey1)) {
            travelTime = graph.shortestPathsCache.get(shortestPathKey1);
        } else if (graph.shortestPathsCache.containsKey(shortestPathKey2)) {
            travelTime = graph.shortestPathsCache.get(shortestPathKey2);
        } else {
            travelTime = graph.findShortestPath(currentValve, nextValve).size() - 1;
            graph.shortestPathsCache.put(currentValve.name + nextValve.name, travelTime);
        }
        return travelTime;
    }

    /**
     * Part2: Calculate total pressure release given the opening sequence of the valves and within a time we have.
     */
    private static int calcPressureReleased2(ValveGraph graph, int minutesElapsedMe, int minutesElapsedElephant, List<Valve> pathMe, List<Valve> pathElephant, Map<Valve, Integer> openValves) {
        var timeLimit = 26;
        var maximumPressure = 0;
        var currentValveMe = pathMe.get(pathMe.size() - 1);
        var currentValveElephant = pathElephant.get(pathMe.size() - 1);
        var maxRemainingTime = Math.max(0, timeLimit - Math.min(minutesElapsedMe, minutesElapsedElephant));
        if (maxRemainingTime == 0 || pathMe.size() + pathElephant.size() >= graph.valvesToOpen.size() + 2) {
            var pressureReleased = 0;

            for (var openValveKeyValue : openValves.entrySet()) {
                var minutesOpened = Math.max(timeLimit - openValveKeyValue.getValue(), 0);
                pressureReleased += openValveKeyValue.getKey().rate * minutesOpened;
            }
            return pressureReleased;
        } else {

            var maxRemainingPressure = 0;
            for (var i = 0; i < graph.valvesToOpen.size(); i++) {

            }


            for (var i = 0; i < graph.valvesToOpen.size(); i++) {
                var nextValveMe = graph.valvesToOpen.get(i);
                var travelTimeMe = getTravelTimeCached(graph, currentValveMe, nextValveMe);
                var newMinutesElapsedMe = minutesElapsedMe + travelTimeMe + 1;
                if (!openValves.containsKey(nextValveMe)) {
                    for (var j = 0; j < graph.valvesToOpen.size(); j++) {
                        var nextValveElephant = graph.valvesToOpen.get(j);
                        if (!openValves.containsKey(nextValveElephant)) {

                            var travelTimeElephant = getTravelTimeCached(graph, currentValveElephant, nextValveElephant);
                            var newMinutesElapsedElephant = minutesElapsedElephant + travelTimeElephant + 1;

                            var newOpenValves = new HashMap<>(openValves);
                            if (newMinutesElapsedMe <= timeLimit) {
                                newOpenValves.put(nextValveMe, newMinutesElapsedMe);
                            }
                            if (newMinutesElapsedElephant <= timeLimit) {
                                newOpenValves.put(nextValveElephant, newMinutesElapsedElephant);
                            }

                            var newPathMe = new ArrayList<>(pathMe);
                            newPathMe.add(nextValveMe);

                            var newPathElephant = new ArrayList<>(pathElephant);
                            newPathElephant.add(nextValveElephant);

                            maximumPressure = Math.max(maximumPressure, calcPressureReleased2(graph, newMinutesElapsedMe, newMinutesElapsedElephant, newPathMe, newPathElephant, newOpenValves));
                            if (newPathMe.size() < 4 && newPathElephant.size() < 4) {
                                System.out.println("== New Path for Me and Elephant ==");
                                printPath(newPathMe, maximumPressure);
                                printPath(newPathElephant, maximumPressure);
                                System.out.println();
                            }
                        }
                    }
                }
            }
        }
        return maximumPressure;
    }

    private static ValveGraph readValves(List<String> input) {
        var graph = new ValveGraph();
        var leadsToTempMap = new HashMap<String, String[]>();

        for (var line : input) {
            var lineParts = line.split(";");
            var part1Parts = lineParts[0].split(" ");
            var valveName = part1Parts[1];
            var valveRate = Integer.parseInt(part1Parts[4].substring(5));
            var lineParts2 = lineParts[1].split(" ");
            var targetValveNames = new String[lineParts2.length - 5];

            for (var i = 5; i < lineParts2.length; i++) {
                targetValveNames[i - 5] = lineParts2[i].replace(",", "");
            }
            var valve = new Valve(valveName, valveRate);
            valve.waitTime = 0;
            graph.valvesToOpen.add(valve);
            graph.map.put(valve.name, valve);
            leadsToTempMap.put(valveName, targetValveNames);
        }

        for (var sourceValve : graph.valvesToOpen) {
            var leadsTo = leadsToTempMap.get(sourceValve.name);
            for (var targetValveName : leadsTo) {
                var targetValve = graph.map.get(targetValveName);
                sourceValve.neighbours.add(targetValve);
            }
        }

        for (var sourceValve : graph.valvesToOpen) {
            for (var targetValve : sourceValve.neighbours) {
                // initially we spend 1 minute to open the valve, and one minute to move to the next valve
                // unless the valve is already open or damaged
                graph.addEdge(sourceValve, targetValve, 1);
            }
        }

        graph.startValve = graph.map.get("AA");
        graph.valvesToOpen.removeIf(v -> v.rate == 0);

        return graph;
    }

    /**
     * Directional graph. We set the weight for each edges to 1 and that will be number of steps (minutes) that costs
     * to get from source node to the next one.
     * That way the shortest paths will represent the total time in minutes to get from one node to another.
     */
    private static class ValveGraph extends Graph<Valve> {

        // the list of all valves in the order of their openings,
        // we are going to sort this list to produce maximum release of pressure.
        public final List<Valve> valvesToOpen = new ArrayList<>();

        // Dictionary of valves, to quickly find a valve by name.
        public final HashMap<String, Valve> map = new HashMap<>();

        public final HashMap<String, Integer> shortestPathsCache = new HashMap<>();
        public Valve startValve;

        public ValveGraph() {
            super((a, b) -> a.waitTime + 1); // valve opening time + time to go to another valve
        }
    }

    private static class Valve {
        private final String name;
        private final int rate;

        private final List<Valve> neighbours = new ArrayList<>();

        private int pressureReleased;

        private int waitTime;

        // to save a time (minutes from start) when the valve is open.
        private int timeOpen;

        public Valve(String name, int rate) {
            this.name = name;
            this.rate = rate;
        }
    }

}
