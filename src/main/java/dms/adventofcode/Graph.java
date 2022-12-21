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

    /**
     * BFS version of the short path from start to destination.
     * The path distance in number of nodes, thus totally ignored weights.
     * Much quicker version in compare with Dijkstra findShortestPath, and preferred when weights of all edges are equal.
     */
    public List<T> findShortestPath(T source, T destination) {
        var path = new ArrayList<T>();
        if (source == destination) {
            return path;
        }
        path.add(source);

        // queue for current paths
        var queue = new ArrayDeque<ArrayList<T>>();
        queue.add(path);

        var visited = new HashSet<T>();

        queue.add(path);
        while (!queue.isEmpty()) {
            path = queue.pop();
            var node = path.get(path.size() - 1);
            if (!visited.contains(node)) {
                var pn = map.get(node);
                var edges = edgesMap.get(pn);
                for (var edge : edges) {
                    var newPath = new ArrayList<T>(path);
                    newPath.add(edge.destination.node);
                    if (edge.destination.node == destination) {
                        return newPath;
                    }

                    queue.add(newPath);
                }
                visited.add(node);
            }

        }
        return null;
    }

    /**
     * Dijkstra's implementation of the shortest path. When weights of all edges are equal, use another overload that
     * calculates the distance directly from source to destination. Another good reason when to use Dijkstra's implementation is
     * when you need to calculate the shortest paths to multiple destinations in one go.
     */
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

