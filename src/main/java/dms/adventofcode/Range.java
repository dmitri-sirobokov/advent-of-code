package dms.adventofcode;

public record Range(long start, long end) {

    @Override
    public String toString() {
        return "[" + start + ", " + end + "]";
    }

    public long length() {
        return end - start + 1;
    }

}
