package dms.adventofcode.y2022;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Day 11: Monkey in the Middle
 */
public class Day11 {

    public static long part1(List<String> input) {

        return play(input, 20, 3);
    }

    public static long part2(List<String> input) {
        return play(input, 10000, 1);
    }


    private static long play(List<String> input, int rounds, int reliefLevel) {
        var monkeyGame = readMonkeyGame(input);
        monkeyGame.play(rounds, reliefLevel);

        return monkeyGame.monkeys.stream().map(monkey -> monkey.throwCount)
                .sorted(Comparator.reverseOrder())
                .limit(2)
                .reduce((a, b) -> a * b)
                .orElse(0L);
    }

    private static MonkeyGame readMonkeyGame(List<String> input) {
        var monkeyGame = new MonkeyGame();
        for (var lineIndex = 0; lineIndex < input.size(); lineIndex += 7) {
            // items line
            var line = input.get(lineIndex + 1);
            var lineParts = line.substring(18).split(", ");
            var items = Arrays.stream(lineParts).mapToLong(Long::parseLong).boxed().toList();

            // Operation line
            line = input.get(lineIndex + 2);
            lineParts = line.substring(19).split(" ");

            var operation = switch (lineParts[1]) {
                case "+" -> Operation.ADD;
                default -> Operation.MUL;
            };

            var operationValue = 0;
            if (StringUtils.equals("old", lineParts[2])) {
                operation = Operation.SQR;
            } else {
                operationValue = Integer.parseInt(lineParts[2]);
            }

            // test value
            line = input.get(lineIndex + 3);
            var testValue = Integer.parseInt(line.substring(21));

            // true value
            line = input.get(lineIndex + 4);
            var trueValue = Integer.parseInt(line.substring(29));

            // false value
            line = input.get(lineIndex + 5);
            var falseValue = Integer.parseInt(line.substring(30));

            var monkey = new Monkey(monkeyGame, items, operation, operationValue, testValue, trueValue, falseValue);
            monkeyGame.monkeys.add(monkey);

        }
        return monkeyGame;
    }

    private static class Monkey {
        private final MonkeyGame game;

        private final Queue<Long> items = new ArrayDeque<>();
        private final Operation operation;
        private final int operationValue;
        private final int throwTrue;
        private final int throwFalse;
        private final long testValue;
        private long throwCount;

        public Monkey(MonkeyGame game, List<Long> items, Operation operation, int operationValue, int testValue, int throwTrue, int throwFalse) {
            this.game = game;
            this.items.addAll(items);
            this.operation = operation;
            this.operationValue = operationValue;
            this.testValue = testValue;
            this.throwTrue = throwTrue;
            this.throwFalse = throwFalse;
        }

        public void play(int reliefLevel) {
            Long itemValue;
            while ((itemValue = items.poll()) != null) {
                var newValue = switch (operation) {
                    case ADD -> itemValue + operationValue;
                    case MUL -> itemValue * operationValue;
                    case SQR -> itemValue * itemValue;
                };
                newValue = newValue / reliefLevel;

                newValue = newValue % game.commonDenominator;
                var throwToMonkey = newValue % testValue == 0 ? game.monkeys.get(throwTrue) : game.monkeys.get(throwFalse);
                throwToMonkey.items.add(newValue);
                this.throwCount++;
            }
        }
    }

    private static class MonkeyGame {
        private final List<Monkey> monkeys = new ArrayList<>();
        private Long commonDenominator;

        public void play(int rounds, int reliefLevel) {
            this.commonDenominator = monkeys.stream().map(m -> m.testValue).reduce((a,b) -> a * b).orElse(0L);
            for (var round = 0; round < rounds; round++) {
                for (var monkey : monkeys) {
                    monkey.play(reliefLevel);
                }
            }
        }
    }

    private enum Operation {
        ADD,
        MUL,
        SQR
    }
}
