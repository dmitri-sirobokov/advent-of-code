package dms.adventofcode.y2024;


import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Day10: Hoof It
 *
 */
public class Day10 extends CodeBase {
    private record Node(int x, int y, int level) { }

    private static void visitNextNode(int x, int y, int[][] topo, boolean[][] visited, List<Node> currentPath, List<List<Node>> allPaths, boolean isPart1) {
        if (x < 0 || y < 0 || x >= topo[0].length || y >= topo.length) {
            return;
        }

        // This algorithm definitely can be optimised, the difference in part 1 and part 2
        // lies in  how we trace back visited nodes. In part 1 we mark all possible nodes as visited,
        // such that we not come to the highest peak more than once.
        // In part 2 we check only visited nodes from the current path, so each path is distinct.
        // So in part 2 we do not need visited matrix at all, and for part 1 we do not need to check
        // current path as it is already covered by visited matrix.
        // Visited matrix is way too large for the sake of simplicity. We could also optimise it to
        // because we can't go further than 10 nodes from start node. In this implementation i am
        // creating a large matrix for every start node, just to simplify algorithm.
        if (currentPath.stream().anyMatch(p -> p.x == x && p.y == y)) {
            return;
        }

        if (isPart1) {
            if (visited[y][x]) {
                return;
            }
        }

        var level = topo[y][x];
        var prevLevel = currentPath.isEmpty() ? -1 : currentPath.getLast().level;

        if (level != prevLevel + 1) {
            return;
        }
        var node = new Node(x, y, level);
        currentPath.add(node);
        visited[y][x] = true;
        if (node.level == 9) {
            allPaths.add(currentPath);
        } else {
            visitNextNode(x + 1, y, topo, visited, new ArrayList<>(currentPath), allPaths, isPart1);
            visitNextNode(x, y + 1, topo, visited, new ArrayList<>(currentPath), allPaths, isPart1);
            visitNextNode(x - 1, y, topo, visited, new ArrayList<>(currentPath), allPaths, isPart1);
            visitNextNode(x, y - 1, topo, visited, new ArrayList<>(currentPath), allPaths, isPart1);
        }
    }

    public static long part1(List<String> input) {
        var topo = readMatrix(input);
        var startNodes = findAllStartNodes(topo);
        var score = 0;
        for (var startNode : startNodes) {
            var visited = new boolean[topo.length][topo[0].length];
            var allPaths = new ArrayList<List<Node>>();
            visitNextNode(startNode.x, startNode.y, topo, visited, new ArrayList<>(), allPaths, true);
            score += allPaths.size();
        }
        return score;
    }

    private static List<Node> findAllStartNodes(int[][] topo) {
        var result = new ArrayList<Node>();
        forEach(topo, (x, y) -> {
            if (topo[y][x] == 0) {
                result.add(new Node(x, y, 0));
            }
        });
        return result;
    }

    public static long part2(List<String> input) {
        var topo = readMatrix(input);
        var startNodes = findAllStartNodes(topo);
        var score = 0;
        for (var startNode : startNodes) {
            var visited = new boolean[topo.length][topo[0].length];
            var allPaths = new ArrayList<List<Node>>();
            visitNextNode(startNode.x, startNode.y, topo, visited, new ArrayList<>(), allPaths, false);
            score += allPaths.size();
        }
        return score;
    }
}
