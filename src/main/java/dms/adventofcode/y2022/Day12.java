package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import dms.adventofcode.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 12: Hill Climbing Algorithm
 */
public class Day12 extends CodeBase {

    @SuppressWarnings("CommentedOutCode")
    public static long part1(List<String> input) {
        var graph = readGraph(input);
        var paths = graph.findShortestPath(graph.end);

//        ======  to print the path ======
//        var visits = new boolean[input.size()][input.get(0).length()];
//        var endNode = graph.end;
//        while (endNode != null) {
//            visits[endNode.position.y][endNode.position.x] = true;
//            endNode = paths.parents().get(endNode);
//        }
//        for (var y = 0; y < input.size(); y++) {
//            for (var x = 0; x < input.get(y).length(); x++) {
//                var c = input.get(y).charAt(x);
//                if (visits[y][x]) {
//                    System.out.print("[" + c + "]");
//                } else {
//                    System.out.print(" " + c + " ");
//                }
//            }
//            System.out.println();
//        }
        return paths.weights().get(graph.start);
    }

    public static long part2(List<String> input) {
        var graph = readGraph(input);
        var results = new ArrayList<Integer>();
        var paths = graph.findShortestPath(graph.end);
        for (var startNode : graph.starts) {
            results.add(paths.weights().get(startNode));
        }
        return results.stream().mapToInt(Integer::intValue).min().orElse(0);
    }

    private static ClimbingGraph readGraph(List<String> input) {
        var graph = new ClimbingGraph();

        var nodes = new GraphNode[input.size()][input.get(0).length()];
        for (var y = 0; y < input.size(); y++) {
            var line = input.get(y);
            for (var x = 0; x < line.length(); x++) {
                var position = new Position(x, y);
                var ch = line.charAt(x);
                boolean isStart = ch == 'S';
                boolean isEnd = ch == 'E';
                if (isStart) {
                    ch = 'a';
                }

                if (isEnd) {
                    ch = 'z';
                }
                var height = ch - 'a';
                nodes[y][x] = new GraphNode(height, position);
                if (isStart) {
                    graph.start = nodes[y][x];
                }
                if (isEnd) {
                    graph.end = nodes[y][x];
                }
                if (ch == 'a') {
                    graph.starts.add(nodes[y][x]);
                }
            }
        }

        forEachAdjacent(nodes, (source, dest) -> {
            int weight = source.height - dest.height > 1 ? 1000000 : 1;
            graph.addEdge(source, dest, weight);
        });
        return graph;
    }

    private record Position(int x, int y) {

    }

    private record GraphNode(int height, Position position) {
    }

    private static class ClimbingGraph extends Graph<GraphNode> {
        private GraphNode start;
        private GraphNode end;

        private final List<GraphNode> starts = new ArrayList<>();
    }
}
