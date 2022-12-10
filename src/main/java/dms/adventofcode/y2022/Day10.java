package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 10: Cathode-Ray Tube
 */
public class Day10 extends CodeBase {

    public static long part1(List<String> input) {
        var crtState = runInstructions(input);
        return crtState.checkSum;
    }

    public static String part2(List<String> input) {
        var crtState = runInstructions(input);
        return arrayToString(crtState.pixels);
    }

    private static CrtState runInstructions(List<String> input) {
        var instructions = readInstructions(input);
        var crtState = new CrtState();
        for (var currentInstruction : instructions) {
            var cycles = currentInstruction.cycles;

            for (var cycle = 0; cycle < cycles; cycle++) {
                var pixelXPos = crtState.clock % 40;
                var pixelYPos = crtState.clock / 40;
                crtState.pixels[pixelYPos][pixelXPos] = pixelXPos >= crtState.xReg - 1 && pixelXPos <= crtState.xReg + 1;
                crtState.clock++;

                if (((crtState.clock - 20) % 40 == 0)) {
                    crtState.checkSum += crtState.xReg * crtState.clock;
                }
            }
            crtState.xReg += currentInstruction.value();
        }
        return crtState;
    }

    private static List<Instruction> readInstructions(List<String> input) {
        var result = new ArrayList<Instruction>();
        for (var line : input) {
            var lineParts = line.split(" ");
            if ("noop".equals(lineParts[0])) {
                result.add(new Instruction(0, 1));
            } else {
                result.add(new Instruction(Integer.parseInt(lineParts[1]), 2));
            }
        }
        return result;
    }

    private record Instruction(int value, int cycles) { }

    private static class CrtState {
        private int clock;
        private int xReg = 1;
        private int checkSum;
        private final boolean[][] pixels = new boolean[6][40];
    }
}
