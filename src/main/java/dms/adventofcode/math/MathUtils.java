package dms.adventofcode.math;

public final class MathUtils {
    /**
     * Calculate common greatest divisor for integers a and b. Can be useful to calculate a number of repetitions in various algorithms.
     * See <a href="https://en.wikipedia.org/wiki/Euclidean_algorithm">Euclidean algorithm</a>
     */
    public static int gcd(int a, int b) {
        while (b != 0) {
            var t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /**
     * Calculate common greatest divisor for integers a and b. Can be useful to calculate a number of repetitions in various algorithms.
     * See <a href="https://en.wikipedia.org/wiki/Euclidean_algorithm">Euclidean algorithm</a>
     */
    public static long gcd(long a, long b) {
        while (b != 0) {
            var t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

}
