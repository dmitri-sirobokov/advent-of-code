package dms.adventofcode.y2022;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Day 5: Supply Stacks
 */
public class Day05 {

    public enum CraneType {
        CRATE_MOVER_9000,
        CRATE_MOVER_9001
    }

    public static String rearrange(List<String> input, CraneType craneType) {
        var stacks = readStacks(input);

        var firstMoveIndex = input.indexOf("") + 1;
        for (var i = firstMoveIndex; i < input.size(); i++) {
            var lineParts = input.get(i).split(" ");
            var count = Integer.parseInt(lineParts[1]);
            var from = Integer.parseInt(lineParts[3]) - 1;
            var to = Integer.parseInt(lineParts[5]) - 1;
            var fromStack = stacks.get(from);
            if (craneType == CraneType.CRATE_MOVER_9001) {
                fromStack = new Stack<>();
                for (var j = 0; j < count; j++) {
                    var crate = stacks.get(from).pop();
                    fromStack.push(crate);
                }
            }
            for (var j = 0; j < count; j++) {
                var crate = fromStack.pop();
                stacks.get(to).push(crate);
            }
        }

        return stacks.stream()
                .map(Stack::peek).reduce((a, b) -> a + b)
                .orElse("");
    }

    private static List<Stack<String>> readStacks(List<String> input) {
        var result = new ArrayList<Stack<String>>();
        var stackCount = (input.get(0).length() + 1) / 4;
        for (var i = 0; i < stackCount; i++) {
            result.add(new Stack<>());
        }

        // get stacks block
        var firstStackLineIndex = input.indexOf("") - 2;
        for (var i = firstStackLineIndex; i >= 0; i --) {
            var line = input.get(i);
            for (var j = 0; j < result.size(); j++) {
                var crate = line.substring(4 * j + 1, 4 * j + 2);
                if (!crate.isBlank()) {
                    result.get(j).push(crate);
                }

            }
        }
        return result;
    }

}
