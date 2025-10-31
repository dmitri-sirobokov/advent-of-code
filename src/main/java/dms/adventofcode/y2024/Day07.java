package dms.adventofcode.y2024;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Day07: Bridge Repair
 *
 */
public class Day07 {

    private static class CalibrationEquation {
        private final long expectedValue;
        private final int[] numbers;
        private CalibrationEquation(long expectedValue, int[] numbers) {
            this.expectedValue = expectedValue;
            this.numbers = numbers;
        }
    }

    private static class CalibrationData {

        private final List<CalibrationEquation> equations;
        private final List<CalibrationEquation> matchedEquations;

        public CalibrationData(List<String> input) {
            this.equations = new ArrayList<>();
            this.matchedEquations = new ArrayList<>();
            for (var line : input) {
                var parts = line.split(": ");
                var expectedValue = Long.parseLong(parts[0]);
                var numbers = Arrays.stream(parts[1].split(" "))
                        .mapToInt(Integer::parseInt).toArray();
                var equation = new CalibrationEquation(expectedValue, numbers);
                this.equations.add(equation);
            }
        }

        private boolean searchEquation(CalibrationEquation eq, long currentValue, int nextNumIndex, boolean includeConcatOp) {
            if (currentValue > eq.expectedValue) {
                return false;
            }

            if (nextNumIndex >= eq.numbers.length) {
                return false;
            }

            var nextValue = eq.numbers[nextNumIndex];

            var opCount = includeConcatOp ? 3 : 2;
            for (var op = 0; op < opCount; op++) {
                var nextCurrentValue = currentValue;
                if (op == 0) {
                    nextCurrentValue *= nextValue;
                } else if (op == 1) {
                    nextCurrentValue += nextValue;
                } else {
                    nextCurrentValue = Long.parseLong(currentValue + Long.toString(nextValue));
                }

                if ((nextNumIndex == eq.numbers.length - 1) && (nextCurrentValue == eq.expectedValue)) {
                    return true;
                }

                if (searchEquation(eq, nextCurrentValue, nextNumIndex + 1, includeConcatOp)) {
                    return true;
                }
            }
            return false;
        }

        public void run(boolean includeConcatOp) {
            for (var equation : this.equations) {
                var currentValue = equation.numbers[0];
                if (searchEquation(equation, currentValue, 1, includeConcatOp)) {
                    this.matchedEquations.add(equation);
                }
            }
        }
    }

    private static long run(List<String> input, boolean includeConcatOp) {
        var calibrationData = new CalibrationData(input);
        calibrationData.run(includeConcatOp);
        return calibrationData.matchedEquations
                .stream()
                .mapToLong(eq -> eq.expectedValue)
                .sum();
    }

    public static long part1(List<String> input) {
        return run(input, false);
    }

    public static long part2(List<String> input) {
        return run(input, true);
    }
}
