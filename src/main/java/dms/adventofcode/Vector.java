package dms.adventofcode;

public record Vector(long x, long y) {

    public static Vector parse(String value) {
        var valueParts = value.trim().split(",");
        assert valueParts.length >= 2;
        var x = Long.parseLong(valueParts[0].trim());
        var y = Long.parseLong(valueParts[1].trim());
        return new Vector(x, y);
    }

    public boolean isEmpty() {
        return x == 0 || y == 0;
    }

    public int intX() {
        return (int) x;
    }
    public int intY() {
        return (int) y;
    }

    public Vector add(Vector vector) {
        return new Vector(x + vector.x, y + vector.y);
    }

    public Vector subtract(Vector vector) {
        return new Vector(x - vector.x, y - vector.y);
    }

    public Vector multiply(Vector vector) {
        return new Vector(x * vector.x, y * vector.y);
    }

    public Vector divide(Vector vector) {
        return new Vector(x / vector.x, y / vector.y);
    }

    public Vector mod(Vector vector) {
        return new Vector(x % vector.x, y % vector.y);
    }

    public long dot(Vector vector) {
        return x * vector.x + y * vector.y;
    }
}
