package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Day 19: Not Enough Minerals. Robots collecting minerals, find the way to collect most of the geodes.
 */
public class Day19 extends CodeBase {

    public static long part1(List<String> input) {
        var bluePrints = readBluePrints(input);

        var total = 0;

        for (var i = 0; i < bluePrints.size(); i++) {
            var bluePrint = bluePrints.get(i);
            var resources = new int[] { 0, 0, 0, 0 };
            var bots = new int[] { 1, 0, 0, 0 };
            var cache = new HashMap<String, Integer>();
            var resourcesTrack = new ArrayList<int[]>();
            var botsTrack = new ArrayList<int[]>();
            var geos = runBluePrintDfs(bluePrint, cache, 24, 0, resources, bots, resourcesTrack, botsTrack);
            total += (i + 1) * geos;
            System.out.println("Blueprint " + (i + 1) + " score=" + geos + "; Calculated steps: " + bluePrint.stepCounts
                    + "; Cache size: " + cache.size() + "; Cache hits: " + bluePrint.cacheHits);

            System.out.println("Minerals and bots tracking:");
            for (var j = 0; j < resourcesTrack.size(); j++) {
                System.out.print("Minute " + (j) + ": ");
                System.out.print("[" + Arrays.stream(resourcesTrack.get(j)).mapToObj(Integer::toString).collect(Collectors.joining(",")) + "]");
                System.out.println(", [" + Arrays.stream(botsTrack.get(j)).mapToObj(Integer::toString).collect(Collectors.joining(",")) + "]");
            }
            System.out.println();
        }
        return total;
    }

    public static long part2(List<String> input) {
        var bluePrints = readBluePrints(input);

        var result = 1;

        for (var i = 0; i < Math.min(3, bluePrints.size()); i++) {
            var bluePrint = bluePrints.get(i);
            var resources = new int[] { 0, 0, 0, 0 };
            var bots = new int[] { 1, 0, 0, 0 };
            var cache = new HashMap<String, Integer>();
            var resourcesTrack = new ArrayList<int[]>();
            var botsTrack = new ArrayList<int[]>();
            var geos = runBluePrintDfs(bluePrint, cache, 32, 0, resources, bots, resourcesTrack, botsTrack);
            result *= geos;
            System.out.println("Blueprint " + (i + 1) + " score=" + geos + "; Calculated steps: " + bluePrint.stepCounts
                    + "; Cache size: " + cache.size() + "; Cache hits: " + bluePrint.cacheHits);

        }
        return result;
    }

    private static int runBluePrintDfs(BluePrint bluePrint, Map<String, Integer> cache, int time, int currentMax,
                                       int[] resources, int[] botsCount, List<int[]> resourcesTrack, List<int[]> botsTrack) {
        bluePrint.stepCounts++;

        resourcesTrack.add(resources);
        botsTrack.add(botsCount);

        if (time == 0) {
            return resources[3];
        }

        var key = getCacheKey(time, resources, botsCount);
        var cacheValue = cache.get(key);
        if (cacheValue != null) {
            bluePrint.cacheHits++;
            return cacheValue;
        }

        // lets assume we can produce geo robot every next turn. It will generate these max amount of geo resources:
        var maxPossible = time * botsCount[3] + resources[3] + time * (time - 1) / 2;
        if (maxPossible < currentMax) {
            return 0;
        }

        var result = 0;

        // Always use first resource to buy something if we have enough.
        var robotBought = -1;

        // or we can buy another robot, we can buy only one robot per time, so we have 4 options.
        var bestResourcesTrack = new ArrayList<int[]>();
        var bestBotsTrack = new ArrayList<int[]>();

        for (var robot = 3; robot >= 0; robot--) {
            // Optimisation 1: It does not make a sense to produce more robots than a maximum costs of the robot,
            // because we start to generate more resources than we can actually spend. It is better to spend them for geo robot.
            if (robot != 3 && botsCount[robot] >= bluePrint.maxCosts[robot]) {
                continue;
            }
            // Optimisation 2: If we have more material than we ever can spend in the remaining time,
            // then we do not need to produce more robot of this type.
            if (robot != 3 && resources[robot] >= time * (bluePrint.maxCosts[robot] - botsCount[robot])) {
                continue;
            }
            // do we have enough material to buy another robot?
            var enoughMat = true;
            for (var mat = 0; mat < 4; mat++) {
                enoughMat &= resources[mat] >= bluePrint.cost[robot][mat];
                if (!enoughMat) {
                    break;
                }
            }
            if (enoughMat) {
                var newResources = Arrays.copyOf(resources, resources.length);
                var newRobots = Arrays.copyOf(botsCount, botsCount.length);
                newRobots[robot]++;
                for (var mat = 0; mat < 4; mat++) {
                    newResources[mat] = newResources[mat] - bluePrint.cost[robot][mat] + botsCount[mat];
                }
                if (false) {
                    System.out.println("Creating a robot " + robot + " at the remaining time " + time);
                }
                var newResourcesTrack = new ArrayList<int[]>();
                var newBotsTrack = new ArrayList<int[]>();
                var newResult = runBluePrintDfs(bluePrint, cache, time - 1, currentMax, newResources, newRobots, newResourcesTrack, newBotsTrack);
                if (newResult > result) {
                    result = newResult;
                    currentMax = newResult;
                    robotBought = robot;
                    bestResourcesTrack.clear();
                    bestResourcesTrack.addAll(newResourcesTrack);
                    bestBotsTrack.clear();
                    bestBotsTrack.addAll(newBotsTrack);
                }

                // if we can buy a geo robot, we always buy it. This is proposed optimisation frm Reedit
                if (robot == 3) {
                    // break;
                }
            }
        }

        if (robotBought != 3) {
            // generate resources
            var newResources = Arrays.copyOf(resources, resources.length);
            for (var robot = 0; robot < 4; robot++) {
                newResources[robot] += botsCount[robot];
            }

            // Option: we can wait without buying anything
            var newResourcesTrack = new ArrayList<int[]>();
            var newBotsTrack = new ArrayList<int[]>();
            var newResult = runBluePrintDfs(bluePrint, cache, time - 1, currentMax, newResources, botsCount, newResourcesTrack, newBotsTrack);
            if (newResult > result) {
                bestResourcesTrack.clear();
                bestResourcesTrack.addAll(newResourcesTrack);
                bestBotsTrack.clear();
                bestBotsTrack.addAll(newBotsTrack);
                result = newResult;
            }
        }

        resourcesTrack.addAll(bestResourcesTrack);
        botsTrack.addAll(bestBotsTrack);

        if (resources[3] == 0) {
            cache.put(key, result);
        }
        return result;
    }

    private static String getCacheKey(int time, int[] resources, int[] botsCount) {
        var sb = new StringBuilder();
        sb.append(time);
        sb.append('-');
        for (var i = 0; i < 4; i++) {
            sb.append(resources[i]);
            sb.append(',');
        }
        sb.append('-');
        for (var i = 0; i < 4; i++) {
            sb.append(botsCount[i]);
            sb.append(',');
        }
        return sb.toString();
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
            var costs = new int[6];
            for (var i = 0; i <= 5; i++) {
                var strPart = bluePrintParts[i].split(" ");
                costs[i] = Integer.parseInt(strPart[0]);
            }

            bluePrint.cost[0][0] = costs[0];
            bluePrint.cost[1][0] = costs[1];
            bluePrint.cost[2][0] = costs[2];
            bluePrint.cost[2][1] = costs[3];
            bluePrint.cost[3][0] = costs[4];
            bluePrint.cost[3][2] = costs[5];


            // calc max usable resource for optimizations
            for (var robot = 0; robot < 4; robot++) {
                for (var mat = 0; mat < 4; mat++) {
                    bluePrint.maxCosts[mat] = Math.max(bluePrint.maxCosts[mat], bluePrint.cost[robot][mat]);
                }
            }

            bluePrints.add(bluePrint);
        }
        return bluePrints;
    }

    public static class BluePrint {
        private final int[][] cost = new int[4][4];
        public final int[] maxCosts = new int[4];

        public long stepCounts;
        public int cacheHits;
    }

}
