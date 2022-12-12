package dms.adventofcode.y2022;

import dms.adventofcode.Graph;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 12: Hill Climbing Algorithm
 */
public class Day12 {

    public static long part1(List<String> input) {
        var graph = readGraph(input);
        var paths = graph.findShortestPath(graph.start);

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
        return paths.weights().get(graph.end);
    }

    public static long part2(List<String> input) {
        var graph = readGraph(input);
        var results = new ArrayList<Integer>();
        for (var startNode : graph.starts) {
            var paths = graph.findShortestPath(startNode);
            results.add(paths.weights().get(graph.end));
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

        for (var y = 0; y < nodes.length; y++) {
            for (var x = 0; x < nodes[y].length; x++) {
                var dx = new int[]{-1, 0, 0, 1};
                var dy = new int[]{0, 1, -1, 0};
                for (var k = 0; k < 4; k++) {
                    var destinationPos = new Position(x + dx[k], y + dy[k]);
                    if (destinationPos.x >= 0 && destinationPos.x < nodes[0].length && destinationPos.y >= 0 && destinationPos.y < nodes.length) {
                        var destinationNode = nodes[destinationPos.y][destinationPos.x];
                        var sourceNode = nodes[y][x];
                        int weight = destinationNode.height - sourceNode.height > 1 ? 1000000 : 1;
                        graph.addEdge(sourceNode, destinationNode, weight);
                    }
                }
            }
        }
        return graph;
    }

    private record Position(int x, int y) {

    }

    private record GraphNode(int height, Position position) {
    }

    private static class ClimbingGraph extends Graph<GraphNode> {
        private GraphNode start;
        private GraphNode end;

        private List<GraphNode> starts = new ArrayList<>();
    }
}
