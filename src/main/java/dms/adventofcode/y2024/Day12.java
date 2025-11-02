package dms.adventofcode.y2024;


import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Day12: Garden Groups
 *
 */
public class Day12 extends CodeBase {

    private static class Region {
        private final char label;
        private int edgeCount;
        private final List<Node> nodes = new ArrayList<>();
        Region(char label) {
            this.label = label;
        }
    }

    private static class Node {
        private char label;
        private int x;
        private int y;
        private boolean left;
        private boolean right;
        private boolean top;
        private boolean bottom;
        private boolean visited;
        private boolean isTopEdgeVisited;
        private boolean isBottomEdgeVisited;
        private boolean isLeftEdgeVisited;
        private boolean isRightEdgeVisited;
        private int area;
        private int perimeter;
    }

    private static void visitRegion(Region region, int x, int y, Node[][] nodes) {
        if (x < 0 || y < 0 || x >= nodes[0].length || y >= nodes.length) return;
        if (nodes[y][x].visited) return;
        if (nodes[y][x].label != region.label)  return;
        region.nodes.add(nodes[y][x]);
        nodes[y][x].visited = true;

        visitRegion(region, x + 1, y, nodes);
        visitRegion(region, x, y + 1, nodes);
        visitRegion(region, x, y - 1, nodes);
        visitRegion(region, x - 1, y, nodes);
    }

    private static void visitEdges(Region region, int x, int y, Node[][] nodes) {
        var node = getNode(nodes, x, y);
        if (node == null) return;
        if (node.label != region.label) return;

        visitTopEdge(region, node, nodes);
        visitBottomEdge(region, node, nodes);
        visitLeftEdge(region, node, nodes);
        visitRightEdge(region, node, nodes);
    }

    private static Node getNode(Node[][] nodes, int x, int y) {
        if  (x < 0 || y < 0 || x >= nodes[0].length || y >= nodes.length) return null;
        return nodes[y][x];
    }

    private static void visitTopEdge(Region region, Node node, int dir, Node[][] nodes, List<Node> edge) {
        while (node != null && node.top && node.label == region.label) {
            edge.add(node);
            node = getNode(nodes, node.x + dir, node.y);
        }
    }

    private static void visitTopEdge(Region region, Node node, Node[][] nodes) {
        if (node.isTopEdgeVisited) return;
        var edge = new ArrayList<Node>();
        visitTopEdge(region, node, -1, nodes, edge);
        visitTopEdge(region, node, 1, nodes, edge);
        edge.forEach(n -> n.isTopEdgeVisited = true);
        if (!edge.isEmpty()) {
            region.edgeCount++;
        }
    }

    private static void visitBottomEdge(Region region, Node node, int dir, Node[][] nodes, List<Node> edge) {
        while (node != null && node.bottom && node.label == region.label) {
            edge.add(node);
            node = getNode(nodes, node.x + dir, node.y);
        }
    }

    private static void visitBottomEdge(Region region, Node node, Node[][] nodes) {
        if (node.isBottomEdgeVisited) return;
        var edge = new ArrayList<Node>();
        visitBottomEdge(region, node, -1, nodes, edge);
        visitBottomEdge(region, node, 1, nodes, edge);
        if (!edge.isEmpty()) {
            if (edge.stream().noneMatch(n -> n.isBottomEdgeVisited)) {
                region.edgeCount++;
            }
            edge.forEach(n -> n.isBottomEdgeVisited = true);
        }
    }

    private static void visitRightEdge(Region region, Node node, int dir, Node[][] nodes, List<Node> edge) {
        while (node != null && node.right && node.label == region.label) {
            edge.add(node);
            node = getNode(nodes, node.x, node.y + dir);
        }
    }

    private static void visitRightEdge(Region region, Node node, Node[][] nodes) {
        if (node.isRightEdgeVisited) return;
        var edge = new ArrayList<Node>();
        visitRightEdge(region, node, -1, nodes, edge);
        visitRightEdge(region, node, 1, nodes, edge);
        if (!edge.isEmpty()) {
            if (edge.stream().noneMatch(n -> n.isRightEdgeVisited)) {
                region.edgeCount++;
            }
            edge.forEach(n -> n.isRightEdgeVisited = true);
        }
    }

    private static void visitLeftEdge(Region region, Node node, int dir, Node[][] nodes, List<Node> edge) {
        while (node != null && node.left && node.label == region.label) {
            edge.add(node);
            node = getNode(nodes, node.x, node.y + dir);
        }
    }

    private static void visitLeftEdge(Region region, Node node, Node[][] nodes) {
        if (node.isLeftEdgeVisited) return;
        var edge = new ArrayList<Node>();
        visitLeftEdge(region, node, -1, nodes, edge);
        visitLeftEdge(region, node, 1, nodes, edge);
        if (!edge.isEmpty()) {
            if (edge.stream().noneMatch(n -> n.isLeftEdgeVisited)) {
                region.edgeCount++;
            }
            edge.forEach(n -> n.isLeftEdgeVisited = true);
        }
    }

    private static ArrayList<Region> calcRegions(List<String> input) {
        var map = readCharMatrix(input);
        var nodes = new Node[map.length][map[0].length];
        var regions = new ArrayList<Region>();

        for (var y = 0; y < map.length; y++) {
            for (var x = 0; x < map[0].length; x++) {
                var node = new Node();
                nodes[y][x] = node;

                node.label = map[y][x];
                node.x = x;
                node.y = y;

                // left border
                node.left = x == 0 || (map[y][x] != map[y][x - 1]);

                // top border
                node.top = y == 0 || (map[y][x] != map[y - 1][x]);

                // right border
                node.right = (x == map.length - 1) || (map[y][x] != map[y][x + 1]);

                // bottom border
                node.bottom = (y == map[0].length - 1) || (map[y][x] != map[y + 1][x]);

                if (node.left)  node.perimeter++;
                if (node.right) node.perimeter++;
                if (node.top) node.perimeter++;
                if (node.bottom) node.perimeter++;
                node.area = 1;

            }
        }

        for (var y = 0; y < map.length; y++) {
            for (var x = 0; x < map[0].length; x++) {
                var node = nodes[y][x];
                var region = new Region(node.label);
                visitRegion(region, x, y, nodes);
                if (region.nodes.isEmpty()) continue;
                regions.add(region);
            }
        }

        for (var region : regions) {
            for (var node : region.nodes) {
                visitEdges(region, node.x, node.y, nodes);
            }
        }
        return regions;
    }

    public static long part1(List<String> input) {
        var regions = calcRegions(input);
        var price = 0L;
        for (var region : regions) {
            var regionArea = region.nodes.stream()
                    .mapToLong(node -> node.area)
                    .sum();
            var regionPerimeter = region.nodes.stream()
                    .mapToLong(node -> node.perimeter)
                    .sum();
            price += regionArea * regionPerimeter;
        }
        return price;
    }

    public static long part2(List<String> input) {
        var regions = calcRegions(input);
        var price = 0L;
        for (var region : regions) {
            var regionArea = region.nodes.stream()
                    .mapToLong(node -> node.area)
                    .sum();
            var regionPerimeter = region.edgeCount;
            price += regionArea * regionPerimeter;
        }
        return price;
    }
}
