package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Day06: Trash Compactor
 *
 */
public class Day06 extends CodeBase {

    private record Expression(List<Long> operands, char operator) {

        public long value() {
            var result = operands.getFirst();
            for (var i = 1; i < operands.size(); i++) {
                var nextOperand = operands.get(i);
                result = switch (operator) {
                    case '+' -> result + nextOperand;
                    case '*' -> result * nextOperand;
                    default -> result;
                };
            }
            return result;
        }
    }

    public static long part1(List<String> input) {
        // parse all numbers in the input
        var allOperands = input.subList(0, input.size() - 1).stream()
                .map(line -> Arrays.stream(line.split(" "))
                        .filter(StringUtils::isNotBlank)
                        .map(Long::parseLong)
                        .toList()
                ).toList();

        // parse all operators
        var opParts = Arrays.stream(input.getLast().split(" "))
                .filter(StringUtils::isNotBlank).map(op -> op.charAt(0)).toList();

        var total = 0L;
        for (var col  = 0; col < opParts.size(); col++) {
            var operands = new ArrayList<Long>();
            for (List<Long> allOperand : allOperands) {
                operands.add(allOperand.get(col));
            }
            var expr = new Expression(operands, opParts.get(col));
            total += expr.value();
        }

        return total;
    }

    public static long part2(List<String> input) {
        var maxLen = input.stream().mapToInt(String::length).max().orElse(0);
        var paddedInput = input.stream().map(line -> StringUtils.rightPad(line, maxLen, ' ')).toList();
        var grid = readCharMatrix(paddedInput);

        var total = 0L;
        var operand = 0L;
        var operator = ' ';
        var operands = new ArrayList<Long>();

        for (var i = 0; i < grid[0].length; i++) {
            var nextOperator = grid[grid.length - 1][i];
            if (nextOperator != ' ') {
                operator = nextOperator;
            }

            for (var j = 0; j < grid.length - 1; j++) {
                var ch = grid[j][i];
                if (Character.isDigit(ch)) {
                    operand = 10 * operand + Character.digit(ch, 10);
                }
            }

            if (operand != 0) {
                operands.add(operand);
            }

            if ((operand == 0 || i == grid[0].length - 1) && operator != ' ') {
                var expr = new Expression(operands, operator);
                total += expr.value();
                operator = ' ';
                operands.clear();
            }
            operand = 0;
        }
        return total;
    }
}
