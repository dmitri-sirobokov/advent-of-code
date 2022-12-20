package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 *
 */
public class Day19 extends CodeBase {

    public static long part1(List<String> input) {
        var bluePrints = readBluePrints(input);

        var total = 0;

        for (var i = 0; i < bluePrints.size(); i++) {
            var bluePrint = bluePrints.get(i);
            var resources = new int[] { 0, 0, 0, 0 };
            var bots = new int[] { 1, 0, 0, 0 };
            var geos = runBluePrintDfs(bluePrint, new HashMap<>(), 24, resources, bots);
            total += (i + 1) * geos;
        }
        return total;
    }

    public static long part2(List<String> input) {
        return 0;
    }

    private static int runBluePrintDfs(BluePrint bluePrint, HashMap<String, Integer> cache, int time, int[] resources, int[] botsCount) {
        if (time == 0) {
            return resources[3];
        }

        var key = time + ",";
        for (var i = 0; i < 4; i++) {
            key += resources[i] + "," + botsCount[i] + ",";
        }
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        var result = 0;

        result = resources[3] + botsCount[3] * time;

        for (var robot : bluePrint.robots) {
            if (robot.resourceType != ResourceType.Geode && botsCount[robot.resourceType.ordinal()] >= bluePrint.maxCosts[robot.resourceType.ordinal()]) {
                continue;
            }
            var wait = 0;
            var doBreak = false;
            for (var cost : robot.buildCosts) {
                if (botsCount[cost.type.ordinal()] == 0) {
                    doBreak = true;
                    break;
                }
                wait = Math.max(wait, (cost.cost - resources[cost.type.ordinal()]) / botsCount[cost.type.ordinal()]);
            }
            if (doBreak) {
                continue;
            }

            var remTime = time - wait - 1;
            if (remTime <= 0) {
                continue;
            }

            var newResources = Arrays.copyOf(resources, resources.length);
            var newBots = Arrays.copyOf(botsCount, botsCount.length);
            for (var i = 0; i < newResources.length; i++) {
                newResources[i] = resources[i] + botsCount[i] * (wait + 1);
            }
            for (var cost : robot.buildCosts) {
                newResources[cost.type.ordinal()] -= cost.cost;
            }
            newBots[robot.resourceType.ordinal()]++;

            for (var i = 0; i < 3; i++) {
                newResources[i] = Math.min(newResources[i], bluePrint.maxCosts[i] * remTime);
            }

            result = Math.max(result, runBluePrintDfs(bluePrint, cache, remTime, newResources, newBots));
        }
        cache.put(key, result);
        return result;
    }

    private static List<BluePrint> readBluePrints(List<String> input) {
        var bluePrints = new ArrayList<BluePrint>();
        for (var line : input) {
            var bluePrintParts = Pattern.compile("(\\d+) (\\w+)")
                    .matcher(line)
                    .results()
                    .map(MatchResult::group)
                    .toArray(String[]::new);

            var bluePrint = new BluePrint();
            var oreRobot = new Robot(ResourceType.Ore);
            oreRobot.buildCosts.add(ResourceCost.parse(bluePrintParts[0]));
            oreRobot.robotCount = 1;
            bluePrint.robots[0] = oreRobot;

            var clayRobot = new Robot(ResourceType.Clay);
            clayRobot.buildCosts.add(ResourceCost.parse(bluePrintParts[1]));
            bluePrint.robots[1] = clayRobot;

            var obsidianRobot = new Robot(ResourceType.Obsidian);
            obsidianRobot.buildCosts.add(ResourceCost.parse(bluePrintParts[2]));
            obsidianRobot.buildCosts.add(ResourceCost.parse(bluePrintParts[3]));
            bluePrint.robots[2] = obsidianRobot;

            var geodeRobot = new Robot(ResourceType.Geode);
            geodeRobot.buildCosts.add(ResourceCost.parse(bluePrintParts[4]));
            geodeRobot.buildCosts.add(ResourceCost.parse(bluePrintParts[5]));
            bluePrint.robots[3] = geodeRobot;

            // calc max usable resource for optimizations

            for (var i = 0; i < 4; i++) {
                for (var cost : bluePrint.robots[i].buildCosts){
                    bluePrint.maxCosts[cost.type.ordinal()] = Math.max(bluePrint.maxCosts[cost.type.ordinal()], cost.cost);
                }
            }

            bluePrints.add(bluePrint);
        }
        return bluePrints;
    }

    private static class Robot {

        private final List<ResourceCost> buildCosts = new ArrayList<>();
        private final ResourceType resourceType;

        private int resourceCount;

        private int robotCount;

        private Robot(ResourceType resourceType) {
            this.resourceType = resourceType;
        }
    }

    private enum ResourceType {
        Ore,
        Clay,
        Obsidian,
        Geode
    }

    private static class ResourceCost {
        private final int cost;
        private final ResourceType type;

        public ResourceCost(int cost, ResourceType type) {
            this.cost = cost;
            this.type = type;
        }

        public static ResourceCost parse(String str) {
            var strPart = str.split(" ");
            var resourceType = switch (strPart[1]) {
                case "ore" -> ResourceType.Ore;
                case "clay" -> ResourceType.Clay;
                case "obsidian" -> ResourceType.Obsidian;
                case "geode" -> ResourceType.Geode;
                default -> throw new RuntimeException("Invalid resource type " + strPart[1]);
            };
            var resourceAmount = Integer.parseInt(strPart[0]);
            return new ResourceCost(resourceAmount, resourceType);
        }
    }

    public static class BluePrint {
        private final Robot[] robots = new Robot[4];
        public final int[] maxCosts = new int[4];
    }
}
