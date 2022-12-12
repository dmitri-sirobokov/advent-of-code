package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Day15 extends CodeBase {

    public static long part1(List<String> input) {
        var risks = readMatrix(input);
        var nodes = new Node[risks.length][risks[0].length];
        for (var y = 0; y < nodes.length; y++) {
            for (var x = 0; x < nodes[y].length; x++) {
                nodes[y][x] = new Node(new Position(x, y));
            }
        }

        findBestNextNode(nodes[0][0], nodes[9][9], nodes, risks);

        var pathGrid = new boolean[10][10];
        var node = nodes[0][0];
        while (node != null) {

            System.out.println(risks[node.position.y][node.position.x]);
            pathGrid[node.position.y][node.position.x] = true;
            node = node.nextNode;
        }

        for (var y = 0; y < nodes.length; y++) {
            for (var x = 0; x < nodes[0].length ; x++) {
                var printStr = pathGrid[y][x] ? " [" + risks[y][x] + "]" : "  " + risks[y][x] + " ";
                System.out.print(printStr);
            }
            System.out.println();
        }
        return nodes[0][0].bestRisk;
    }

    /**
     * Find next best node in the best path with the lowest total risk
     */
    private static void findBestNextNode(Node current, Node to,
                                         Node[][] nodes, int[][] risks) {
        if (current.visiting || current.nextNode != null) {
            return;
        }

        if (current.equals(to)) {
            // we reach the end, set best path
            return;
        }

        // recursively calculate the lowest risk of adjacent nodes
        current.visiting = true;
        var dx = new int[] { -1, 0, 0, 1 };
        var dy = new int[] { 0, -1, 1, 0 };
        var nextNodes = new ArrayList<Node>();
        for (var k = 0; k < 4; k++) {
            var nextPosition = new Position(current.position.x + dx[k], current.position.y + dy[k]);
            if (nextPosition.x >= 0 && nextPosition.y >= 0 && nextPosition.x < nodes[0].length && nextPosition.y < nodes.length) {
                var nextNode = nodes[nextPosition.y][nextPosition.x];
                if (nextNode.equals(to)) {
                    nextNode.bestRisk = risks[nextNode.position.y][nextNode.position.x];
                    nextNodes.clear();
                    nextNodes.add(nextNode);
                    break;
                }
                if (!nextNode.visiting) {
                    findBestNextNode(nextNode, to, nodes, risks);
                    if (nextNode.nextNode != null) nextNodes.add(nextNode);
                } else {
                    nextNodes.clear();
                    break;
                }
            }
        }
        current.visiting = false;
        var bestNode = nextNodes.stream()
                .filter(Objects::nonNull).min(Comparator.comparingInt(node -> node.bestRisk))
                .orElse(null);

        if (bestNode != null) {
            current.nextNode = bestNode;
            current.bestRisk = risks[current.position.y][current.position.x] + bestNode.bestRisk;
        }
        return;
    }

    public static long part2(List<String> input) {
        return 0;
    }

    private static class Node {
        private final Position position;

        // Link to the best node for the best path (with the lowest risk).
        private Node nextNode;

        // The sum of all risks in the path from this node to end (if following best path)
        private int bestRisk;

        private boolean visiting;

        public Node(Position position) {
            this.position = position;
        }
    }

    private record Position(int x, int y) { }

}
