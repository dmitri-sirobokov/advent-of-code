package dms.adventofcode.y2021;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntUnaryOperator;

public class Day07 {

    public static long calcMinFuelConsumptionPart1(List<String> input) {
        IntUnaryOperator fuelFunc = (dist) -> dist;
        return calcMinFuelConsumption(input, fuelFunc);
    }

    public static long calcMinFuelConsumptionPart2(List<String> input) {
        IntUnaryOperator fuelFunc = (dist) -> dist * (dist + 1) / 2;
        return calcMinFuelConsumption(input, fuelFunc);
    }

    private static long calcMinFuelConsumption(List<String> input, IntUnaryOperator fuelFunc) {
        var positions = Arrays.stream(input.get(0).split(",")).mapToInt(Integer::parseInt).toArray();
        var position = calcMinFuelPosition(positions, fuelFunc);
        return calcFuelSum(positions, position, fuelFunc);
    }


    /** calcMinFuelPosition is a modified Geometric median function that calculates a point with a minimized total
     * distance to all positions. The difference is you can provide fuel function to modify algorithm.
     * @param positions Input positions
     * @param fuelFunc Fuel consumption function that translates distance to fuel.
     * @return Return the geometric mean value.
     */
    private static int calcMinFuelPosition(int[] positions, IntUnaryOperator fuelFunc) {
        var min = Arrays.stream(positions).min().getAsInt();
        var max = Arrays.stream(positions).max().getAsInt();
        var currentPosition = (max + min) / 2;
        var testDistance = (max - min) / 2;
        var minDist = calcFuelSum(positions, currentPosition, fuelFunc);
        while (testDistance > 0) {
            var testPositions = new int[] { currentPosition - testDistance, currentPosition + testDistance };
            boolean foundMinDist = false;
            for (int testPosition : testPositions ) {
                var newDist = calcFuelSum(positions, testPosition, fuelFunc);
                if (newDist < minDist) {
                    minDist = newDist;
                    currentPosition = testPosition;
                    foundMinDist = true;
                    break;
                }
            }
            if (!foundMinDist) {
                testDistance = Math.floorDiv(testDistance, 2);
            }
        }

        return currentPosition;
    }

    private static int calcFuelSum(int[] positions, int pos, IntUnaryOperator fuelFunc) {
        return Arrays.stream(positions).map(p -> fuelFunc.applyAsInt(Math.abs(p - pos))).sum();
    }

}
