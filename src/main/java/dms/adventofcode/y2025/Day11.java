package dms.adventofcode.y2025;


import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Day11: Reactor
 *
 */
public class Day11 extends CodeBase {

    public static long part1(List<String> input) {
        return searchDevices(input, "you", true, true);
    }

    public static long part2(List<String> input) {
        return searchDevices(input, "svr", false, false);
    }

    private static class Device {
        private final String name;
        private final List<Device> links = new ArrayList<>();
        private final HashMap<VisitHash, Long> pathCount = new HashMap<>();

        private Device(String name) {
            this.name = name;
        }
    }

    private record VisitHash(boolean fft, boolean dac) { }

    private record DeviceKey(String name, List<String> links) { }

    private static class SearchResult {
        private Device start;
        private Device end;
    }

    private static Map<String, Device> readDevices(List<String> input) {
        var keys = readDeviceKeys(input);

        var result = new HashMap<String, Device>();
        for (var key : keys.keySet()) {
            var device = new Device(key);
            result.put(key, device);
        }

        var outDevice = new Device("out");
        result.putIfAbsent("out", outDevice);

        for (var keySet : keys.entrySet()) {
            var device = result.get(keySet.getKey());
            for (var linkName : keySet.getValue().links) {
                var link = result.get(linkName);
                device.links.add(link);
            }
        }
        return result;
    }

    private static HashMap<String, DeviceKey> readDeviceKeys(List<String> input) {
        var keys = new HashMap<String, DeviceKey>();
        for (var line : input) {
            var parts = line.split(":");
            var name = parts[0].trim();
            var linksStr = parts[1].trim();
            var links = linksStr.split(" ");
            var linksList = new ArrayList<String>();
            for (String s : links) {
                var link = s.trim();
                linksList.add(link);
            }
            var deviceKey = new DeviceKey(name, linksList);
            keys.put(name, deviceKey);
        }
        return keys;
    }

    private static long searchDevices(List<String> input, String startDevice, boolean dacVisited, boolean fftVisited) {
        var devices = readDevices(input);
        var start = devices.get(startDevice);
        var end = devices.get("out");
        var searchResult = new SearchResult();
        searchResult.start = start;
        searchResult.end = end;
        return searchDevices(searchResult.start, searchResult, dacVisited, fftVisited);
    }

    private static long searchDevices(Device current, SearchResult result, boolean dacVisited, boolean fftVisited) {
        var hash = new VisitHash(dacVisited, fftVisited);
        if (current.pathCount.containsKey(hash)) {
            return current.pathCount.get(hash);
        }

        if (current == result.end) {
            return dacVisited &&  fftVisited ? 1 : 0;
        }

        if (current.name.equals("dac")) {
            dacVisited = true;
        }

        if (current.name.equals("fft")) {
            fftVisited = true;
        }

        var count = 0L;
        for (var next : current.links) {
            count += searchDevices(next, result, dacVisited, fftVisited);
        }

        current.pathCount.put(hash, count);

        return count;
    }
}
