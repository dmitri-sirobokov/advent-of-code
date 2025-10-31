package dms.adventofcode.y2024;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Day 03: Mull It Over
 *
 */
public class Day03 {

    private sealed static class Instruction permits MulInstruction, DoInstruction{
        private final int pos;
        Instruction(int pos) {
            this.pos = pos;
        }
    }

    private static final class MulInstruction extends Instruction {
        private final int a;
        private final int b;

        MulInstruction(int pos, int a, int b) {
            super(pos);
            this.a = a;
            this.b = b;
        }
    }

    private static final class DoInstruction extends Instruction {
        private final boolean enable;
        DoInstruction(int pos, boolean enable) {
            super(pos);
            this.enable = enable;
        }
    }

    private static List<Instruction> readInstructions(List<String> input, boolean parseDoInstructions) {
        var result = new ArrayList<Instruction>();
        int pos = 0;
        for (var line : input) {
            Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
            Matcher mulMatcher = mulPattern.matcher(line);
            while (mulMatcher.find()) {
                int a = Integer.parseInt(mulMatcher.group(1));
                int b = Integer.parseInt(mulMatcher.group(2));
                result.add(new MulInstruction(pos + mulMatcher.start(), a, b));
            }

            if (parseDoInstructions) {
                parseDoInstructions(pos, line, result, true);
                parseDoInstructions(pos, line, result, false);
            }
            pos += line.length();
        }
        result.sort(Comparator.comparingInt(a -> a.pos));
        return result;
    }

    private static void parseDoInstructions(int pos, String line, ArrayList<Instruction> result, boolean enable) {
        var reg = enable ? "do\\(\\)" : "don't\\(\\)";
        Pattern doPattern = Pattern.compile(reg);
        Matcher doMatcher = doPattern.matcher(line);
        while (doMatcher.find()) {
            result.add(new DoInstruction(pos + doMatcher.start(), enable));
        }
    }

    private static long calcMul(List<Instruction> input) {
        var result = 0;
        boolean enable = true;
        for (var instruction : input) {

            switch (instruction) {
                case DoInstruction i -> enable = i.enable;
                case MulInstruction i -> {
                    if (enable) {
                        result += i.a * i.b;
                    }
                }
                default -> throw new IllegalArgumentException("Unknown instruction: " + instruction);
            }
        }
        return result;
    }

    public static long part1(List<String> input) {
        var instructions = readInstructions(input, false);
        return calcMul(instructions);
    }

    public static long part2(List<String> input) {
        var instructions = readInstructions(input, true);
        return calcMul(instructions);
    }
}
