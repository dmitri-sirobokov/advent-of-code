package dms.adventofcode.y2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Day08: Playground (connect circuits)
 *
 */
public class Day08 {

    private static class Box {
        private final long x;
        private final long y;
        private final long z;
        private Circuit circuit;

        Box(long x, long y, long z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        long distanceSqr(Box box) {
            return (box.x - this.x) * (box.x - this.x) + (box.y - this.y) * (box.y - this.y) + (box.z - this.z) * (box.z - this.z);
        }

        @Override
        public String toString() {
            return "[" + this.x + "," + this.y + "," + this.z + "]";
        }
    }

    private record Connection(int i, int j, long dist) { }

    private record Circuit(List<Box> boxes) { }

    public static long part1(List<String> input, int maxConnections) {
        var boxes = loadBoxes(input);
        var connections = getSortedConnections(boxes, maxConnections);
        var circuits = loadCircuits(boxes);
        connectBoxes(boxes, connections, circuits);

        circuits.sort((c1, c2) -> c2.boxes.size() - c1.boxes.size());
        var result = 1;
        for (var i = 0; i < 3; i++) {
            result *= circuits.get(i).boxes.size();
        }
        return result;
    }

    public static long part2(List<String> input, int maxConnections) {
        var boxes = loadBoxes(input);
        var connections = getSortedConnections(boxes, maxConnections);
        var circuits = loadCircuits(boxes);
        Connection lastConnection = connectBoxes(boxes, connections, circuits);

        Box box1 = boxes.get(lastConnection.i);
        Box box2 = boxes.get(lastConnection.j);
        return box1.x * box2.x;
    }

    private static List<Box> loadBoxes(List<String> input) {
        return input.stream().map(line -> {
            var parts = line.split(",");
            var x = Long.parseLong(parts[0]);
            var y = Long.parseLong(parts[1]);
            var z = Long.parseLong(parts[2]);
            return new Box(x, y, z);
        }).toList();
    }

    private static ArrayList<Circuit> loadCircuits(List<Box> boxes) {
        var circuits = new ArrayList<Circuit>();
        for (Box box : boxes) {
            box.circuit = new Circuit(new ArrayList<>());
            box.circuit.boxes.add(box);
            circuits.add(box.circuit);
        }
        return circuits;
    }

    private static ArrayList<Connection> getSortedConnections(List<Box> boxes, int maxConnections) {
        var connections = new ArrayList<Connection>(boxes.size() * boxes.size());

        for (var i = 0; i < boxes.size(); i++) {
            var box1 = boxes.get(i);
            for (var j = i + 1; j < boxes.size(); j++) {
                var box2 = boxes.get(j);
                connections.add(new Connection(i, j, box1.distanceSqr(box2)));
            }
        }

        return sortConnections(connections, maxConnections);
    }

    // we limit sorted output to the maxConnections, no need to sort all list
    private static ArrayList<Connection> sortConnections(ArrayList<Connection> connections, int maxConnections) {
        var cmp = Comparator.comparingLong(Connection::dist);
        var pq = new PriorityQueue<>(maxConnections, cmp.reversed());
        for (Connection con : connections) {
            if (pq.size() < maxConnections) {
                pq.offer(con);
            } else {
                assert pq.peek() != null;
                if (con.dist < pq.peek().dist) {
                    pq.poll();
                    pq.offer(con);
                }
            }

        }
        var result = new ArrayList<>(pq);
        result.sort(cmp);
        return result;
    }

    private static Connection connectBoxes(List<Box> boxes, ArrayList<Connection> connections, ArrayList<Circuit> circuits) {
        Connection lastConnection = null;
        for (var connection : connections) {
            lastConnection = connection;
            Box box1 = boxes.get(connection.i);
            Box box2 = boxes.get(connection.j);

            if (box1.circuit != box2.circuit) {
                // join circuits
                circuits.remove(box2.circuit);
                box1.circuit.boxes.addAll(box2.circuit.boxes);
                for (var box : box2.circuit.boxes) {
                    box.circuit = box1.circuit;
                }

                if (circuits.size() == 1) {
                    break;
                }
            }

        }
        return lastConnection;
    }
}
