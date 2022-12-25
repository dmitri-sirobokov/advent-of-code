package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Day 16: Proboscidea Volcanium, Opening Valves together with elephants
 * Credits: Big hugs to Jonathan Paulson for helping me out with this one.
 */
public class Day16 extends CodeBase {

    public static long part1(List<String> input) {

        var graph = readValves(input);
        return dfs(graph, graph.startValve, 0, 30, 0);
    }

    public static long part2(List<String> input) {
        var graph = readValves(input);
        return dfs(graph, graph.startValve, 0, 26, 1);

    }

    private static int dfs(ValveGraph graph, Valve currentValve, long openValves, int remainingTime, int otherPlayers) {
        if (remainingTime == 0) {
            return otherPlayers > 0 ? dfs(graph, graph.startValve, openValves, 26, otherPlayers - 1) : 0;
        }

        // if we use only 15 possible valves in the input, then we can create an integer key, which is much faster
        var key = openValves + "-" + currentValve.id + "-" + "-" + remainingTime + "-" + otherPlayers;
        var dpValue = graph.dp.get(key);

        if (dpValue != null) {
            return dpValue;
        }
        var result = 0;
        var openValveMask = 1L << currentValve.id;
        var canOpen = (openValves & openValveMask) == 0 && currentValve.rate > 0;
        if (canOpen) {
            var newOpenValves = openValves | openValveMask;
            result = Math.max(result, (remainingTime - 1) * currentValve.rate + dfs(graph, currentValve, newOpenValves, remainingTime - 1, otherPlayers));
        }
        for (var neighbour : currentValve.neighbours) {
            result = Math.max(result, dfs(graph, neighbour, openValves, remainingTime - 1, otherPlayers));
        }

        graph.dp.put(key.toString(), result);
        return result;
    }
    private static ValveGraph readValves(List<String> input) {
        var graph = new ValveGraph();
        var leadsToTempMap = new HashMap<String, String[]>();

        var valveId = 0;
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
            var valve = new Valve(valveName, valveRate, valveId++);
            graph.valves.add(valve);
            graph.map.put(valve.name, valve);
            leadsToTempMap.put(valveName, targetValveNames);
        }

        for (var sourceValve : graph.valves) {
            var leadsTo = leadsToTempMap.get(sourceValve.name);
            for (var targetValveName : leadsTo) {
                var targetValve = graph.map.get(targetValveName);
                sourceValve.neighbours.add(targetValve);
            }
        }

        graph.startValve = graph.map.get("AA");

        return graph;
    }

    private static class ValveGraph {

        // the list of all valves in the order of their openings,
        // we are going to sort this list to produce maximum release of pressure.
        public final List<Valve> valves = new ArrayList<>();

        // Dictionary of valves, to quickly find a valve by name.
        public final HashMap<String, Valve> map = new HashMap<>();

        // Dynamic programming cache
        public final Map<String, Integer> dp = new HashMap<>(1024 * 1024 * 28);
        public Valve startValve;

    }

    private static class Valve {
        private final String name;

        private final int id;
        private final int rate;

        private final List<Valve> neighbours = new ArrayList<>();

        public Valve(String name, int rate, int id) {
            this.name = name;
            this.rate = rate;
            this.id = id;
        }
    }

}
