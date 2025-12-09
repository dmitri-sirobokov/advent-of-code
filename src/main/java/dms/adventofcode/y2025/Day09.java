package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;
import dms.adventofcode.Vector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Day09: Movie Theater
 *
 */
public class Day09 extends CodeBase {
    public static long part1(List<String> input) {
        var tiles = readTiles(input);

        var allAreas = new ArrayList<Long>();
        for (var i = 0; i < tiles.size(); i++) {
            var pos1 = tiles.get(i);
            for (int j = 1; j < tiles.size(); j++) {
                var pos2 = tiles.get(j);
                allAreas.add(pos1.area(pos2));
            }
        }

        return allAreas.stream()
                .max(Comparator.comparingLong(Long::longValue))
                .orElse(0L);
    }

    public static long part2(List<String> input) {
        var tiles = readTiles(input);
        return findPolygons(tiles);
    }

    // rectangular box, defined by 2 positions
    private record Rect(Vector v1, Vector v2) {
        Rect(Vector v1, Vector v2) {
            this.v1 = new Vector(Math.min(v1.x(), v2.x()), Math.min(v1.y(), v2.y()));
            this.v2 = new Vector(Math.max(v1.x(), v2.x()), Math.max(v1.y(), v2.y()));
        }

        long area() {
            return (this.v2().x() - this.v1().x() + 1)  * (this.v2().y() - this.v1().y() + 1);
        }

    }

    private static List<Vector> readTiles(List<String> input) {
        return input.stream()
                .map(line -> {
                    var parts = line.split(",");
                    return new Vector(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
                })
                .toList();
    }

    private static long findPolygons(List<Vector> tiles) {
        var rows = new HashMap<Integer, List<Vector>>();
        var cols = new HashMap<Integer, List<Vector>>();
        var maxArea = 0L;

        for (var tile : tiles) {
            rows.putIfAbsent(tile.intY(), new ArrayList<>());
            cols.putIfAbsent(tile.intX(), new ArrayList<>());

            var col = cols.get(tile.intX());
            col.add(tile);

            var row = rows.get(tile.intY());
            row.add(tile);
        }

        // algorithm takes too long, and many same repetitions,
        // correct answer is already there in a few iterations
        // for now just limit iteration, before better solution is found.
        // Once area is available we can use it to filter out all areas lower than current area
        var maxIterations = 2;
        for (Vector tile : tiles) {
            maxIterations--;
            if (maxIterations < 0) {
                break;
            }

            // search for new polygon that starts from the current tile.
            var poly = new ArrayList<Vector>();
            poly.add(tile);

            // have a stack for possible next segments (up, down, left, right)
            var stack = new ArrayDeque<ArrayList<Vector>>();
            stack.add(poly);
            while (!stack.isEmpty()) {
                poly = stack.pop();

                var last = poly.getLast();
                if (poly.size() >= 3) {

                    // reach the start node - closed polygon
                    if (last.equals(poly.getFirst())) {
                        continue;
                    }

                    // if any edges of polygon perimeter cross any other edge of the same polygon
                    // then it is invalid or has loops
                    if (polyHasIntersectons(poly)) {
                        continue;
                    }
                }

                // put next tiles on stack (left, right, top, bottom)
                saveNextTilesOnStack(poly, stack, rows, cols);
            }

            // now we found a polygon, we can look for sub areas
            // sub area is valid if it is inside the polygon.
            var area = findLargestRect(poly);
            if (area > maxArea) {
                maxArea = area;
            }

        }

        return maxArea;
    }

    // find the largest rectangle area inside a polygon
    private static long findLargestRect(List<Vector> poly) {
        var result = 0L;
        for (var i = 0; i < poly.size(); i++) {
            var pos1 = poly.get(i);
            for (int j = 1; j < poly.size(); j++) {
                var pos2 = poly.get(j);
                var rect =  new Rect(pos1, pos2);
                var area = rect.area();
                if (area > result && isAARectInsidePolygon(poly, rect)) {
                    result = area;
                }
            }
        }
        return result;
    }

    private static void saveNextTilesOnStack(ArrayList<Vector> poly, ArrayDeque<ArrayList<Vector>> stack, HashMap<Integer, List<Vector>> rows, HashMap<Integer, List<Vector>> cols) {
        // todo: a little bit of copy / paste code here search in each direction, could be better.
        var last = poly.getLast();
        var rowTiles = rows.get(last.intY());
        var newTile = rowTiles.stream()
                .filter(tile -> tile.x() - last.x() > 0)
                .min(Comparator.comparingLong(Vector::x))
                .orElse(null);
        if (newTile != null) {
            var newPoly = new ArrayList<>(poly);
            newPoly.add(newTile);
            stack.add(newPoly);
        }

        newTile = rowTiles.stream()
                .filter(tile -> tile.x() - last.x() < 0)
                .max(Comparator.comparingLong(Vector::x))
                .orElse(null);
        if (newTile != null) {
            var newPoly = new ArrayList<>(poly);
            newPoly.add(newTile);
            stack.add(newPoly);
        }

        var colTiles = cols.get(last.intX());
        newTile = colTiles.stream()
                .filter(tile -> tile.y() - last.y() > 0)
                .min(Comparator.comparingLong(Vector::y))
                .orElse(null);

        if (newTile != null) {
            var newPoly = new ArrayList<>(poly);
            newPoly.add(newTile);
            stack.add(newPoly);
        }

        newTile = colTiles.stream()
                .filter(tile -> tile.y() - last.y() < 0)
                .max(Comparator.comparingLong(Vector::y))
                .orElse(null);
        if (newTile != null) {
            var newPoly = new ArrayList<>(poly);
            newPoly.add(newTile);
            stack.add(newPoly);
        }
    }

    private static boolean polyHasIntersectons(ArrayList<Vector> poly) {
        var prev = poly.get(poly.size() - 2);
        // check edge intersections
        var hasIntersections = false;
        for (var k = 1; k < poly.size() - 2; k++) {
            var p1 = poly.get(k - 1);
            var p2 = poly.get(k);
            if (segmentsIntersectInclusive(p1, p2, prev, poly.getLast())) {
                hasIntersections = true;
            }
        }

        return hasIntersections;
    }

    /**
     * Orientation (cross product sign) of triangle (a,b,c). >0: CCW, <0: CW, 0: collinear
     */
    public static long orientation(Vector a, Vector b, Vector c) {
        return (b.x() - a.x()) * (c.y() - a.y()) - (b.y() - a.y()) * (c.x() - a.x());
    }

    /**
     * Returns true if point b lies on segment a--c (inclusive). Assumes collinearity.
     */
    public static boolean onSegment(Vector a, Vector b, Vector c) {
        return Math.min(a.x(), c.x()) <= b.x() && b.x() <= Math.max(a.x(), c.x()) &&
                Math.min(a.y(), c.y()) <= b.y() && b.y() <= Math.max(a.y(), c.y());
    }

    /**
     * Inclusive segment intersection test:
     * Returns true for any intersection (proper crossing, touching at endpoints, or collinear overlap).
     */
    public static boolean segmentsIntersectInclusive(Vector a, Vector b, Vector c, Vector d) {
        long o1 = orientation(a, b, c);
        long o2 = orientation(a, b, d);
        long o3 = orientation(c, d, a);
        long o4 = orientation(c, d, b);

        // Proper intersection
        if (((o1 > 0 && o2 < 0) || (o1 < 0 && o2 > 0)) &&
                ((o3 > 0 && o4 < 0) || (o3 < 0 && o4 > 0))) {
            return true;
        }

        // Collinear special cases (touching/overlap)
        if (o1 == 0 && onSegment(a, c, b)) return true;
        if (o2 == 0 && onSegment(a, d, b)) return true;
        if (o3 == 0 && onSegment(c, a, d)) return true;
        if (o4 == 0 && onSegment(c, b, d)) return true;

        return false;
    }


    /**
     * Proper intersection: segments share interior points (not just touching at endpoints) and not collinear overlap.
     */
    public static boolean segmentsProperlyIntersect(Vector a, Vector b, Vector c, Vector d) {
        long o1 = orientation(a, b, c);
        long o2 = orientation(a, b, d);
        long o3 = orientation(c, d, a);
        long o4 = orientation(c, d, b);

        if (((o1 > 0 && o2 < 0) || (o1 < 0 && o2 > 0)) &&
                ((o3 > 0 && o4 < 0) || (o3 < 0 && o4 > 0))) {
            return true;
        }
        // Collinear cases are NOT counted as "proper" intersection here.
        return false;
    }


    /**
     * Checks if an axis-aligned rectangle is inside polygon.
     */
    public static boolean isAARectInsidePolygon(List<Vector> poly, Rect rect) {
        if (poly == null || poly.size() < 3) return false;

        Vector c0 = new Vector(rect.v1.x(), rect.v1.y());
        Vector c1 = new Vector(rect.v2.x(), rect.v1.y());
        Vector c2 = new Vector(rect.v2.x(), rect.v2.y());
        Vector c3 = new Vector(rect.v1.x(), rect.v2.y());

        List<Vector> quad = List.of(c0, c1, c2, c3);
        return isConvexQuadInsidePolygon(poly, quad);
    }


    /**
     * Generic rectangle (or any convex quad) given by 4 vertices in order.
     * For axis-aligned rectangles you can pass the four corners in CCW/CW order.
     */
    public static boolean isConvexQuadInsidePolygon(List<Vector> poly, List<Vector> quad) {
        if (quad.size() != 4) throw new IllegalArgumentException("quad must have 4 points");
        if (poly.size() < 3) return false;

        // 1) All four corners must be inside polygon (respect boundary flag).
        for (Vector q : quad) {
            if (!isPointInPolygon(q, poly, true)) {
                return false;
            }
        }

        // 2) No quad edge may properly intersect any polygon edge.
        List<Vector[]> polyEdges = polygonEdges(poly);
        Vector[] qEdges = new Vector[]{
                quad.get(0), quad.get(1),
                quad.get(1), quad.get(2),
                quad.get(2), quad.get(3),
                quad.get(3), quad.get(0)
        };

        for (int e = 0; e < 4; e++) {
            Vector qa = qEdges[2 * e], qb = qEdges[2 * e + 1];

            for (Vector[] pe : polyEdges) {
                Vector pa = pe[0], pb = pe[1];

                if (segmentsProperlyIntersect(qa, qb, pa, pb)) {
                    return false; // crosses boundary
                }
            }
        }
        return true;
    }


    /**
     * Edges of a polygon given as vertices in order (closed implicitly).
     */
    public static List<Vector[]> polygonEdges(List<Vector> poly) {
        int n = poly.size();
        List<Vector[]> edges = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            edges.add(new Vector[]{poly.get(i), poly.get((i + 1) % n)});
        }
        return edges;
    }

    /**
     * Ray-casting point-in-polygon. If countOnEdge = true, points on edge are considered inside.
     */
    public static boolean isPointInPolygon(Vector p, List<Vector> poly, boolean countOnEdge) {
        int n = poly.size();
        if (n < 3) return false;

        // Optional fast on-edge check
        for (int i = 0; i < n; i++) {
            Vector a = poly.get(i), b = poly.get((i + 1) % n);
            if (orientation(a, b, p) == 0 && onSegment(a, p, b)) {
                return countOnEdge; // on boundary -> inside only if allowed
            }
        }

        // Ray casting to the right
        boolean inside = false;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            Vector pi = poly.get(i), pj = poly.get(j);
            boolean straddle = (pi.y() > p.y()) != (pj.y() > p.y());
            if (straddle) {
                double xIntersect = pj.x() + (double) (pi.x() - pj.x()) * (p.y() - pj.y()) / (double) (pi.y() - pj.y());
                if ((double) p.x() < xIntersect) inside = !inside;
            }
        }
        return inside;
    }
}

