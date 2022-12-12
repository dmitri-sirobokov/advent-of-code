package dms.adventofcode;

import java.util.*;

public class Graph<T> {

    public void addEdge(T source, T destination, int weight) {
        var edges = edgesMap.computeIfAbsent(source, (key) -> new ArrayList<>());
        edges.add(new Edge(source, destination, weight));
    }

    public Map<T, Integer> findShortestPath(T source) {
        var map = new HashMap<T, PriorityNode>();
        var pq = new ArrayList<PriorityNode<T>>();
        var weights = new HashMap<T, Integer>();
        var parents = new HashMap<T, T>();
        var dq = new HashSet<T>();
        for (var node : edgesMap.keySet() ) {
            var priority = source == node ? 0 : Integer.MAX_VALUE;
            var priorityNode = new PriorityNode<T>(node, priority);
            map.put(node, priorityNode);
            pq.add(priorityNode);
        }

        weights.put(source, 0);
        parents.put(source, null);
        while (!pq.isEmpty()) {
            pq.sort(Comparator.comparingInt((PriorityNode<T> a) -> a.priority));
            var currentPriorityNode = pq.remove(0);
            var current = currentPriorityNode.node;
            var weight = currentPriorityNode.priority;
            dq.add(current);

            // update shortest dist of current vertex from source
            weights.put(current, weight);

            for (var edge : edgesMap.get(current)) {
                T adj = (edge.source == current) ? edge.destination : edge.source;

                // skip already dequeued vertices. O(1)
                if (dq.contains(adj))
                {
                    continue;
                }

                int calcWeight = weights.get(current) + edge.weight;

                var adjNode = map.get(adj);
                int adjWeight = adjNode.priority;

                if (calcWeight < adjWeight)
                {
                    // relax
                    map.get(adj).priority = calcWeight;
                    // potentially O(n)
                    pq.stream().filter(n -> n == adjNode).forEach(n -> n.priority = calcWeight);

                    parents.put(adj, current);
                }

            }
        }
        return weights;
    }

    private final Map<T, List<Edge<T>>> edgesMap = new HashMap<>();

    private class PriorityNode<T> {
        private final T node;
        private int priority;

        public PriorityNode(T node, int priority) {
            this.node = node;
            this.priority = priority;
        }
    }

    public record Edge<T>(T source, T destination, int weight) { }
}

