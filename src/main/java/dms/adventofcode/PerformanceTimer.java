package dms.adventofcode;

public class PerformanceTimer implements AutoCloseable {
    private final long t1;

    public PerformanceTimer() {
        this.t1 = System.nanoTime();
    }

    @Override
    public void close() {
        var t2 = System.nanoTime();
        System.out.printf("%.3f ms%n", (t2 - t1) / 1_000_000.0);
    }
}
