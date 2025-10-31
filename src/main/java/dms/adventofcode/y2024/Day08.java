package dms.adventofcode.y2024;


import java.util.ArrayList;
import java.util.List;

/**
 * Day08: Resonant Collinearity
 *
 */
public class Day08 {

    private record Position(int x, int y) { }

    private record Antenna(Position pos, char frequency) { }

    private static List<Antenna> readAntennas(List<String> input) {
        var result = new ArrayList<Antenna>();
        for  (int i = 0; i < input.size(); i++) {
            var line = input.get(i);
            for (var j = 0; j < line.length(); j++) {
                var ch =  line.charAt(j);
                if (ch != '.') {
                    var antenna = new Antenna(
                            new Position(j, i),
                            ch
                    );
                    result.add(antenna);
                }
            }
        }
        return result;
    }

    private static int countAntinodes(List<String> input, boolean includeHarmonics) {
        var antennas = readAntennas(input);
        var mapSizeX = input.getFirst().length();
        var mapSizeY = input.size();
        var antiNodesMap = new boolean[mapSizeY][mapSizeX];
        for (var i = 0; i < antennas.size(); i++) {
            var antenna1 = antennas.get(i);
            for (var j = 0; j < antennas.size(); j++) {
                if (i == j) continue;

                var antenna2 = antennas.get(j);

                if (antenna1.frequency == antenna2.frequency) {
                    var dist = new Position(antenna2.pos.x - antenna1.pos.x,  antenna2.pos.y - antenna1.pos.y);
                    var harmonic = 0;
                    while (true) {
                        harmonic++;
                        var antiNodePosition = new Position(antenna1.pos.x + harmonic * dist.x, antenna1.pos.y + harmonic * dist.y);
                        if (antiNodePosition.x < 0 || antiNodePosition.y < 0 || antiNodePosition.x >= mapSizeX || antiNodePosition.y >= mapSizeY) {
                            break;
                        }

                        // for part1 we need only 2nd harmonic
                        if (includeHarmonics || harmonic == 2) {
                            antiNodesMap[antiNodePosition.x][antiNodePosition.y] = true;
                        }
                    }
                }
            }

        }

        var result = 0;
        for (boolean[] booleans : antiNodesMap) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) result++;
            }
        }
        return result;
    }

    public static long part1(List<String> input) {
        return countAntinodes(input, false);
    }

    public static long part2(List<String> input) {
        return countAntinodes(input, true);
    }


}
