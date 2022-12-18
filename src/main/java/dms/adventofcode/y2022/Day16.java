package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import dms.adventofcode.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Day 16: Proboscidea Volcanium
 */
public class Day16 extends CodeBase {


    public static long part1(List<String> input) {

        var graph = readValves(input);

        var maxTime = 30;
        var bestPath = new ArrayList<>(graph.valves);
        var maxPressure = calcPressureReleased(graph, maxTime);
        printPath(graph, maxTime);

        // we try all paths through all combinations (permutations) of possible open valves
        // normally, if we test all permutations, then we run to performance issues, as number of permutations
        // is very large f(n!), where n is number of valves that can be open,
        // but we can skip all smaller permutations at the right of the path when we know that they won't add up to max pressure
        var permutationsIndex = new int[graph.valves.size()];
        var valveNames = graph.valves.stream().map(v -> v.name).toList();

        while (permutationsIndex[0] == 0) {
            var permutList = new ArrayList<>(valveNames);
            // next permutations
            var newValveList = new ArrayList<Valve>();
            for (var permutationIndex = 0; permutationIndex < permutationsIndex.length; permutationIndex++) {
                var name = permutList.remove(permutationsIndex[permutationIndex]);
                var valve = graph.map.get(name);
                newValveList.add(valve);
            }

            graph.valves.clear();
            graph.valves.addAll(newValveList);

            var newPressure = calcPressureReleased(graph, maxTime);
            if (newPressure > maxPressure) {
                maxPressure = newPressure;
                bestPath.clear();
                bestPath.addAll(graph.valves);
                printPath(graph, maxTime);
            }

            // approximate next permutation
            var approximated = false;
            var currentPressure = 0;
            for (var i = 1; i < graph.valves.size(); i++) {
                var prevValve = graph.valves.get(i - 1);
                var remainingTime = maxTime - prevValve.timeOpen - 2;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                currentPressure += prevValve.rate * remainingTime;
                var maxRemainingPressure = 0;
                for (var j = i; j < graph.valves.size(); j++) {
                    var remainingValve = graph.valves.get(j);
                    maxRemainingPressure += remainingTime * remainingValve.rate;
                }
                if (maxRemainingPressure + currentPressure < maxPressure) {
                    permutationsIndex[i]++;
                    for (var j = i + 1; j < permutationsIndex.length; j++) {
                        permutationsIndex[j] = 0;
                    }
                    approximated = true;
                    break;
                }
            }
            if (!approximated) {
                permutationsIndex[permutationsIndex.length - 1]++;
            }
            for (var permutationIndex = permutationsIndex.length - 1; permutationIndex >= 0; permutationIndex--) {
                if (permutationsIndex[permutationIndex] > permutationsIndex.length - permutationIndex - 1) {
                    permutationsIndex[permutationIndex] = 0;
                    permutationsIndex[permutationIndex - 1]++;
                }
            }

        }

        graph.valves.clear();
        graph.valves.addAll(bestPath);
        var result =  calcPressureReleased(graph, maxTime);
        printPath(graph, maxTime);
        return result;
    }

    private static void calculatePathLength(ValveGraph graph, int maxTime) {
        for (var i = 1; i < graph.valves.size(); i++) {
            graph.valves.forEach(valve -> valve.waitTime = 0);
            var sourceValve = graph.valves.get(i - 1);
            var targetValve = graph.valves.get(i);
            sourceValve.waitTime = 1;
            graph.valves.get(0).waitTime = 0;
            var paths = graph.findShortestPath(sourceValve);
            var distance = paths.weights().get(targetValve);
            if (distance != null) {
                targetValve.timeOpen = sourceValve.timeOpen + distance;
                if (targetValve.timeOpen > maxTime) {
                    targetValve.timeOpen = maxTime;
                }
            }
        }
    }

    private static void printPath(ValveGraph graph, int maxTime) {
        var string = new StringBuilder();
        string.append(String.join("->", graph.valves.stream().map(valve -> valve.name).toList()));
        string.append("=");

        // print minutes
        string.append(String.join(",", graph.valves.stream().map(valve -> Integer.valueOf(valve.timeOpen + 1).toString()).toList()));

        string.append("=");
        string.append(countPressureReleased(graph, maxTime));

        System.out.println(string);
    }

    /**
     * Calculate total pressure release given the opening sequence of the valves and within a time we have.
     */
    private static int calcPressureReleased(ValveGraph graph, int maxTime) {
        calculatePathLength(graph, maxTime);
        return countPressureReleased(graph, maxTime);
    }

    private static int countPressureReleased(ValveGraph graph, int maxTime) {
        var result = 0;
        for (var valve : graph.valves) {
            var totTimeOpen = maxTime - valve.timeOpen - 1;
            if (totTimeOpen < 0) {
                totTimeOpen = 0;
            }
            result += valve.rate * totTimeOpen;
        }
        return result;
    }

    //    private static List<List<Valve>> visitAllPaths(Valve start) {
//        var path = new ArrayList<Valve>();
//        path.add(start);
//
//            for (var target: start.edges) {
//                var listvisitAllPaths(target);
//            }
//
//        return result;
//    }
//
    public static long part2(List<String> input) {
        return 0;
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
            graph.valves.add(valve);
            graph.map.put(valve.name, valve);
            leadsToTempMap.put(valveName, targetValveNames);
        }

        for (var sourceValve : graph.valves) {
            var leadsTo = leadsToTempMap.get(sourceValve.name);
            for (var targetValveName : leadsTo) {
                var targetValve = graph.map.get(targetValveName);
                sourceValve.edges.add(targetValve);
            }
        }

        for (var sourceValve : graph.valves) {
            for (var targetValve : sourceValve.edges) {
                // initially we spend 1 minute to open the valve, and one minute to move to the next valve
                // unless the valve is already open or damaged
                graph.addEdge(sourceValve, targetValve, 1);
            }
        }

        var startValve = graph.valves.get(0);
        graph.valves.removeIf(v -> v.rate == 0);
        graph.valves.add(0, startValve);

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
        public List<Valve> valves = new ArrayList<>();

        // Dictionary of valves, to quickly find a valve by name.
        public HashMap<String, Valve> map = new HashMap<>();

        public ValveGraph() {
            super((a, b) -> a.waitTime + 1); // valve opening time + time to go to another valve
        }
    }

    private static class Valve {
        private final String name;
        private final int rate;

        private final List<Valve> edges = new ArrayList<>();

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
