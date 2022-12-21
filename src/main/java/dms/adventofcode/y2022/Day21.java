package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Day 21: Monkey Math
 */
public class Day21 extends CodeBase {

    public static long part1(List<String> input) {
        HashMap<String, Monkey> monkeys = readMonkeys(input);
        play(monkeys, false);
        return monkeys.get("root").value;
    }

    public static long part2(List<String> input) {
        HashMap<String, Monkey> monkeys = readMonkeys(input);

        var root = monkeys.get("root");
        var me = monkeys.get("humn");

        // In my case I had actually more correct answers in a small range,
        // Better solution would be to iterate further to find minimum of those possible values, because it looks like that is the correct answer,
        // but because the range is only 1 or 2 numbers, we can just try manually to play with the range.
        var rangeMin = 0L;
        var rangeMax = 10000000000000L;
        me.value = rangeMin;
        play(monkeys, true);
        if (root.value == 0) return me.value;
        var rootValue1 = root.value;

        me.value = rangeMax;
        play(monkeys, true);
        if (root.value == 0) return me.value;
        var rootValue2 = root.value;

        // root results should have opposite signs when doing binary search at min and max range,
        // otherwise we never find the result, or initial range is too small.
        assert rootValue1 * rootValue2 < 0;

        while (root.value != 0) {
            // reduce the range of the number which I should yell by two with every iteration.
            me.value = (rangeMin + rangeMax) / 2;
            play(monkeys, true);
            if (root.value == 0) break;

            if (root.value * rootValue1 < 0) {
                rangeMax = me.value;
            } else if (root.value * rootValue2 < 0) {
                rangeMin = me.value;
            } else {
                throw new RuntimeException("This should never happen. One of the range should come close to the desired value");
            }
        }
        return me.value;
    }

    private static void play(HashMap<String, Monkey> monkeys, boolean part2) {
        monkeys.values().forEach(m -> m.ready = false);
        monkeys.get("humn").ready = part2;
        var notReadyMonkeys = new ArrayList<>(monkeys.values());
        while (!notReadyMonkeys.isEmpty()) {
            var readyMonkeys = new ArrayList<Monkey>();
            for (var monkey : notReadyMonkeys) {
                monkey.evaluate(monkeys, part2);
                if (monkey.ready) {
                    readyMonkeys.add(monkey);
                }

            }
            notReadyMonkeys.removeAll(readyMonkeys);
        }
    }

    private static HashMap<String, Monkey> readMonkeys(List<String> input) {
        var monkeys = new HashMap<String, Monkey>();
        for (var line: input) {
            var monkeyName = line.substring(0, 4);
            var expressionParts = line.substring(6).split(" ");
            var monkey = new Monkey(monkeyName, expressionParts);
            monkeys.put(monkey.name, monkey);
        }
        return monkeys;
    }

    private static class Monkey {
        public final String[] expression;
        public final String name;
        public boolean ready;

        public long value;

        public Monkey(String name, String[] expression) {
            this.name = name;
            this.expression = expression;
        }

        public void evaluate(Map<String, Monkey> monkeys, boolean part2) {
            if (ready) {
                return;
            }
            if (expression.length == 1) {
                value = Integer.parseInt(expression[0]);
                ready = true;
                return;
            }

            var monkey1 = monkeys.get(expression[0]);
            var monkey2 = monkeys.get(expression[2]);
            if (monkey1.ready && monkey2.ready) {
                var isRootMonkeyPart2 = part2 && StringUtils.equals("root", name);
                var op = isRootMonkeyPart2 ? "=" : expression[1];
                value = switch (op) {
                    case "+" -> monkey1.value + monkey2.value;
                    case "-" -> monkey1.value - monkey2.value;
                    case "*" -> monkey1.value * monkey2.value;
                    case "/" -> monkey1.value / monkey2.value;
                    case "=" -> Long.compare(monkey1.value, monkey2.value);
                    default -> throw new RuntimeException("Invalid monkey operation: " + op + ". Monkey: " + name);
                };
                ready = true;
            }
        }
    }

}
