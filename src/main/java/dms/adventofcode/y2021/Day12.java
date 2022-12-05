package dms.adventofcode.y2021;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Day12 {


    // TODO: Comeback to fix mess in the code and improve performance (takes around 1s to walk through all paths using input data)
    public static int countPaths(List<String> input, boolean canVisitSingleSmallCaveTwice) {
        var graph = Graph.parse(input);
        var paths = graph.walk("start", "end", canVisitSingleSmallCaveTwice);
        return paths.size();
    }

    private Day12() { }

    private static class Graph {
        private final List<Cave> nodes = new ArrayList<>();

        private Graph() { }

        public static Graph parse(List<String> input) {
            var result = new Graph();
            for (var line : input) {
                var lineParts = line.split("-");
                var node1 = result.createNode(lineParts[0]);
                var node2 = result.createNode(lineParts[1]);
                node1.link(node2);
            }
            return result;
        }

        public List<GraphPath> walk(String start, String end, boolean canVisitSingleSmallCaveTwice) {
            var result = new ArrayList<GraphPath>();
            var startNode = this.findNode(start);
            var endNode = this.findNode(end);
            if (startNode == null || endNode == null) {
                return result;
            }
            var graphPath = new GraphPath();

            walkNode(startNode, endNode, graphPath, result, canVisitSingleSmallCaveTwice);
            return result;
        }

        public Cave createNode(String name) {
            var node = this.findNode(name);
            if (node == null) {
                node = new Cave(name);
                this.nodes.add(node);
            }
            return node;
        }

        public Cave findNode(String name) {
            return this.nodes.stream()
                    .filter(n -> StringUtils.equals(n.name, name))
                    .findFirst()
                    .orElse(null);
        }

        private void walkNode(Cave currentNode, Cave endNode, GraphPath currentGraphPath, ArrayList<GraphPath> result, boolean canVisitSingleSmallCaveTwice) {

            // if we are back at the start, cancel this path
            if (!currentGraphPath.nodes.isEmpty() && currentGraphPath.nodes.get(0) == currentNode) {
                return;
            }

            if (!currentGraphPath.canVisitCave(currentNode, canVisitSingleSmallCaveTwice)) {
                return;
            }

            currentGraphPath.add(currentNode);

            // we reach end node - this is the end of the path.
            if (currentNode == endNode) {
                result.add(currentGraphPath);
                return;
            }

            for (var nextNode : currentNode.nodes) {
                var graphBranch = new GraphPath();
                graphBranch.nodes.addAll(currentGraphPath.nodes);
                walkNode(nextNode, endNode, graphBranch, result, canVisitSingleSmallCaveTwice);
            }
        }

    }

    private static class Cave {
        private final String name;

        private final List<Cave> nodes = new ArrayList<>();

        private Cave(String name) {
            this.name = name;
        }

        public void link(Cave to) {
            this.nodes.add(to);
            to.nodes.add(this);
        }

        @Override
        public String toString() {
            return "Cave[" + name + "]";
        }

        public boolean isSmallCave() {
            return StringUtils.equals(name.toLowerCase(), name);
        }
    }

    private static class GraphPath {
        private final List<Cave> nodes = new ArrayList<>();

        public void add(Cave node) {
            this.nodes.add(node);
        }

        @Override
        public String toString() {
            return nodes.stream().map(cave -> cave.name).reduce((a,b) -> a + "," + b).orElse("");
        }

        /** return true if node can be added to path,
         * - that is adding this node to the path will create a unique path
         * - and check if node can be visited multiple times
         * **/
        public boolean canVisitCave(Cave currentNode, boolean canVisitSingleSmallCaveTwice) {
            if (nodes.isEmpty()) {
                return true;
            }


            var lastNode = nodes.get(nodes.size() - 1);
            if (currentNode == lastNode) {
                return false;
            }

            var smallCaveCounters = new HashMap<String, Integer>();
            for (var node : nodes) {
                // if the node we are adding is lowercase then we can not add it to the path second time
                if (node.isSmallCave()) {
                    smallCaveCounters.putIfAbsent(node.name, 0);
                    smallCaveCounters.put(node.name, smallCaveCounters.get(node.name) + 1);
                }

            }

            if (!canVisitSingleSmallCaveTwice && smallCaveCounters.getOrDefault(currentNode.name, 0) > 0) {
                return false;
            }

            if (canVisitSingleSmallCaveTwice
                    && smallCaveCounters.entrySet().stream().anyMatch(set -> set.getValue() > 1)
                    && smallCaveCounters.getOrDefault(currentNode.name, 0) > 0) {
                    return false;
            }

            return true;
        }
    }
}
