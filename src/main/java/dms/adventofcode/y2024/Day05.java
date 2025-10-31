package dms.adventofcode.y2024;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Day05: Print Queue
 *
 */
public class Day05 {

    private record PrintRule (int page, int before) { }

    private record PrintData(List<PrintRule> rules, List<List<Integer>> batches) { }

    private static PrintData readInput(List<String> input) {
        var rules = new ArrayList<PrintRule>();
        // read rules
        var batchLineIndex = 0;
        for (var i = 0; i < input.size(); i++) {
            batchLineIndex = i + 1;
            var line = input.get(i);
            if (line.trim().isEmpty()) break;
            var parts = line.split("\\|");
            var page = Integer.parseInt(parts[0]);
            var before = Integer.parseInt(parts[1]);
            rules.add(new PrintRule(page, before));
        }

        // read batches
        var batches = new ArrayList<List<Integer>>();
        for (var i = batchLineIndex; i < input.size(); i++) {
            var batch = new ArrayList<Integer>();
            var line = input.get(i);
            var parts = line.split(",");
            for (var part : parts) {
                batch.add(Integer.parseInt(part));
            }
            batches.add(batch);
        }
        return new PrintData(rules, batches);
    }

    private static boolean isValidBatch(List<Integer> batch, HashMap<Integer, List<PrintRule>> rulesMap) {
        if (batch.size() % 2 == 0) return false;

        for (var i = 0; i < batch.size() - 1; i++) {
            for (var j = i + 1; j < batch.size(); j++) {
                var page1 = batch.get(i);
                var page2 = batch.get(j);
                // check of there any rule violation that page 2 has to be printed before page 1
                var rulesForPage2 = rulesMap.get(page2);
                if (rulesForPage2 != null) {
                    for (var page2Rule : rulesForPage2) {
                        if (page2Rule.before == page1) return false;
                    }
                }
            }
        }
        return true;
    }

    private static int calcPrintQueue(List<String> input, boolean calcInvalidQueue) {
        var result = 0;
        var printData = readInput(input);
        var rulesMap = new HashMap<Integer, List<PrintRule>>();
        for (var rule : printData.rules) {
            var rules = rulesMap.computeIfAbsent(rule.page, k -> new ArrayList<>());
            rules.add(rule);
        }

        var validBatches = new ArrayList<List<Integer>>();
        var invalidBatches = new ArrayList<List<Integer>>();
        for  (var batch : printData.batches) {
            if (isValidBatch(batch, rulesMap)) {
                validBatches.add(batch);
            } else {
                invalidBatches.add(batch);
            }
        }

        // order invalid batches
        if (calcInvalidQueue) {
            for (var batch : invalidBatches) {
                batch.sort((page1, page2) -> {
                    var rulesForPage2 = rulesMap.get(page2);
                    if (rulesForPage2 != null) {
                        for (var page2Rule : rulesForPage2) {
                            if (page2Rule.before == page1) return -1;
                        }
                    }
                    return 0;

                });
                result += batch.get(batch.size() / 2);
            }
        } else {
            for (var batch : validBatches) {
                result += batch.get(batch.size() / 2);
            }
        }
        return result;
    }

    public static long part1(List<String> input) {
        return calcPrintQueue(input, false);
    }

    public static long part2(List<String> input) {
        return calcPrintQueue(input, true);
    }
}
