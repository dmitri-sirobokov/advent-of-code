package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;
import dms.adventofcode.Graph;

import java.util.List;

public class Day15 extends CodeBase {

    public static long part1(List<String> input) {
        return shortestPath(input, 1);
    }

    public static long part2(List<String> input) {
        return shortestPath(input, 5);
    }


    private static Integer shortestPath(List<String> input, int duplicateCount) {
        var risks = readMatrix(input);
        risks = duplicate(risks, duplicateCount);
        var graph = createGraph(risks);

        var result = graph.findShortestPath(graph.start);

        return result.weights().get(graph.end);
    }

    private static int[][] duplicate(int[][] risks, int duplicateCount) {
        var result = new int[risks.length * duplicateCount][risks[0].length * duplicateCount];
        for (var repeatX = 0; repeatX < duplicateCount; repeatX++) {
            for (var repeatY = 0; repeatY < duplicateCount; repeatY++) {
                for (var y = 0; y < risks.length; y++) {
                    for (var x = 0; x < risks[y].length; x++) {
                        result[repeatY * risks.length + y][repeatX * risks[0].length + x] = (risks[y][x] + repeatX + repeatY - 1) % 9 + 1;
                    }
                }
            }
        }
        return result;
    }

    private static RiskGraph createGraph(int[][] risks) {
        var graph = new RiskGraph();
        var graphNodes = mapArray(risks, (x, y, element) -> new Node(risks[y][x]));
        forEachAdjacent(graphNodes, (source, dest) -> graph.addEdge(source, dest, dest.risk));

        graph.start = graphNodes[0][0];
        graph.end = graphNodes[graphNodes.length - 1][graphNodes[0].length - 1];
        return graph;
    }

    // Making this class a record will have a side effect, it generates non-unique cache for hashmaps.
    @SuppressWarnings("ClassCanBeRecord")
    private static class Node {
        private final int risk;

        public Node(int risk) {
            this.risk = risk;
        }
    }

    private record Position(int x, int y) { }

    private static class RiskGraph extends Graph<Node> {
        private Node start;
        private Node end;
    }

}
