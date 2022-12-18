package dms.adventofcode;

import java.util.*;

public class Graph<T> {

    private final HashMap<T, PriorityNode<T>> map = new HashMap<>();
    private final Map<PriorityNode<T>, List<PriorityEdge<T>>> edgesMap = new HashMap<>();

    private final EdgeWeightFunction<T> edgeWeightFunction;


    public void addEdge(T source, T destination, int weight) {
        var sourcePriorityNode = map.computeIfAbsent(source, PriorityNode::new);
        var destinationPriorityNode = map.computeIfAbsent(destination, PriorityNode::new);
        var edges = edgesMap.computeIfAbsent(sourcePriorityNode, (key) -> new ArrayList<>());
        edges.add(new PriorityEdge<>(sourcePriorityNode, destinationPriorityNode, weight));
    }

    public ShortestPathResult<T> findShortestPath(T source) {
        var pq = new PriorityQueue<>(Comparator.comparingInt((PriorityNode<T> a) -> a.priority));
        var weights = new HashMap<T, Integer>();
        var parents = new HashMap<T, T>();
        var sourcePriorityNode = map.get(source);
        for (var node : edgesMap.keySet()) {
            node.priority = Integer.MAX_VALUE;
            node.dequeued = false;
        }
        sourcePriorityNode.priority = 0;
        pq.addAll(edgesMap.keySet());

        weights.put(source, 0);
        parents.put(source, null);
        while (!pq.isEmpty()) {
            var currentPriorityNode = pq.poll();
            currentPriorityNode.dequeued = true;
            var current = currentPriorityNode.node;
            var currentWeight = currentPriorityNode.priority;

            // update the shortest dist of current vertex from source
            weights.put(current, currentWeight);

            for (var edge : edgesMap.get(currentPriorityNode)) {
                PriorityNode<T> adj = (edge.source.node == current) ? edge.destination : edge.source;

                // skip already dequeued vertices.
                if (adj.dequeued)
                {
                    continue;
                }

                var edgeWeight = this.edgeWeightFunction == null ? edge.weight : edgeWeightFunction.apply(edge.source.node, edge.destination.node);
                int calcWeight = currentWeight + edgeWeight;

                if (calcWeight < adj.priority)
                {
                    // relax
                    adj.priority = calcWeight;

                    // reorder the node that just changed priority
                    pq.remove(adj);
                    pq.offer(adj);

                    parents.put(adj.node, current);
                }

            }
        }
        return new ShortestPathResult<>(source, weights, parents);
    }

    public Graph() {
        edgeWeightFunction = null;
    }

    public Graph(EdgeWeightFunction<T> edgeWeightFunction) {
        this.edgeWeightFunction = edgeWeightFunction;
    }

    public Collection<T> getNodes() {
        return map.keySet();
    }

    public void clear() {
        this.map.clear();
        this.edgesMap.clear();
    }

    private static class PriorityNode<T> {
        private final T node;
        private int priority = Integer.MAX_VALUE;

        private boolean dequeued;

        public PriorityNode(T node) {
            this.node = node;
        }
    }

    private record PriorityEdge<T>(PriorityNode<T> source, PriorityNode<T> destination, int weight) { }

    public record ShortestPathResult<T>(T source, Map<T, Integer> weights, Map<T, T> parents) { }

    public interface EdgeWeightFunction<T> {
        int apply(T source, T target);
    }
}

