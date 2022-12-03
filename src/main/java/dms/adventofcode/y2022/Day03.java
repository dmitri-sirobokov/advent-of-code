package dms.adventofcode.y2022;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Day03 {

    public static int rucksackReorganizationPrioritySum(List<String> input) {
        int prioritySum = 0;
        for (var line : input) {
            var compartment1 = line.substring(0, line.length() / 2);
            var compartment2 = line.substring(line.length() / 2);

            var compartments = List.of(compartment1, compartment2);

            int compartmentsDuplicatePriority = getCompartmentsDuplicatePriority(compartments);

            prioritySum += compartmentsDuplicatePriority;
        }
        return prioritySum;
    }

    public static int groupPrioritySum(List<String> input) {
        int prioritySum = 0;
        for (var lineIndex = 0; lineIndex < input.size(); lineIndex += 3) {
            var rucksack1 = input.get(lineIndex);
            var rucksack2 = input.get(lineIndex + 1);
            var rucksack3 = input.get(lineIndex + 2);

            var rucksacks = List.of(rucksack1, rucksack2, rucksack3);

            int rucksacksDuplicatePriority = getCompartmentsDuplicatePriority(rucksacks);

            prioritySum += rucksacksDuplicatePriority;

        }
        return prioritySum;
    }

    private static int getCompartmentsDuplicatePriority(List<String> compartments) {
        var compartmentsDuplicatePriority = 0;
        // use char codes (bytes) for quick counting to find duplicates
        var compartmentDuplicates = new byte[256];
        for (var compartment : compartments) {
            byte[] compartmentItemsCount = new byte[256];
            var compartmentBytes = compartment.getBytes(StandardCharsets.UTF_8);
            for (var i = 0; i < compartmentBytes.length; i++) {
                compartmentItemsCount[compartmentBytes[i]]++;
            }

            for (var itemByteCode = 0; itemByteCode < compartmentItemsCount.length; itemByteCode++) {
                if (compartmentItemsCount[itemByteCode] > 0) {
                    compartmentDuplicates[itemByteCode]++;
                }
            }
        }

        for (var itemByteCode = 0; itemByteCode < compartmentDuplicates.length; itemByteCode++) {
            if (compartmentDuplicates[itemByteCode] == compartments.size()) {
                // we found a duplicate, calculate the priority and add it to the sum
                if (itemByteCode >= 'a' && itemByteCode <= 'z') {
                    compartmentsDuplicatePriority = 1 + itemByteCode - 'a';
                } else {
                    compartmentsDuplicatePriority = 27 + itemByteCode - 'A';
                }
                break;
            }
        }
        return compartmentsDuplicatePriority;
    }
}
