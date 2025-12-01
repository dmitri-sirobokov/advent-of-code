package dms.adventofcode.y2024;


import dms.adventofcode.CodeBase;
import dms.adventofcode.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Day16: Reindeer Maze
 *
 */
public class Day16 extends CodeBase {

    private record PathStep(Vector pos, Vector dir) { }

    private static boolean isObstacle(PathStep step, char[][] map) {
        if (step.pos.x() < 0 || step.pos.y() < 0 || step.pos.x() >= map[0].length || step.pos.y() >= map.length) {
            return false;
        }

        var ch =  map[step.pos.intY()][step.pos.intX()];
        return ch != '.';
    }

    private static void search(PathStep step,
                               long stepScore,
                               List<PathStep> path,
                               char[][] map,
                               HashMap<PathStep, Long> bestScores,
                               HashMap<PathStep, List<PathStep>> bestPaths,
                               Vector end,
                               long endScore) {
        if (isObstacle(step, map)) {
            return;
        }

        var bestScore = bestScores.getOrDefault(step, Long.MAX_VALUE);
        if (stepScore >= bestScore) return;
        if (stepScore > endScore) return;
        if (stepScore < endScore) {
            endScore = stepScore;
        }
        bestScores.put(step, stepScore);
        var bestPath = new ArrayList<>(path);
        bestPath.add(step);
        bestPaths.put(step, bestPath);

        // choices at each step:
        // - step in current direction
        // - rotate clockwise
        // - rotate counterclockwise
        var nextSteps = new PathStep[3];
        var nextScores = new long[3];
        nextSteps[0] = new PathStep(step.pos.add(step.dir), step.dir);
        nextSteps[1] = new PathStep(step.pos, step.dir.rotate90deg(true));
        nextSteps[2] = new PathStep(step.pos, step.dir.rotate90deg(false));
        nextScores[0] = stepScore + 1;
        nextScores[1] = stepScore + 1000;
        nextScores[2] = stepScore + 1000;

        for (var i = 0; i < nextSteps.length; i++) {
            search(nextSteps[i], nextScores[i], bestPath, map, bestScores, bestPaths, end, endScore);
        }
    }

    public static long part1(List<String> input) {
        var map = readCharMatrix(input);

        // find start and end
        Vector start = Vector.ZERO, end = Vector.ZERO;
        for (var y = 0; y < map.length; y++) {
            for (var x = 0; x < map[0].length; x++) {
                if (map[y][x] == 'S') {
                    start = new Vector(x, y);
                    map[y][x] = '.';
                }
                if (map[y][x] == 'E') {
                    end = new Vector(x, y);
                    map[y][x] = '.';
                }
            }
        }

        var startStep = new PathStep(start, new Vector(1, 0));
        var scores = new HashMap<PathStep, Long>();
        var bestPaths = new HashMap<>();
        var paths = new ArrayList<PathStep>();
        var score2 = scores.getOrDefault(new PathStep(end, new Vector(0, -1)), Long.MAX_VALUE);
        var score3 = scores.getOrDefault(new PathStep(end, new Vector(1, 0)), Long.MAX_VALUE);
        var score4 = scores.getOrDefault(new PathStep(end, new Vector(-1, 0)), Long.MAX_VALUE);
        // return Math.min(score1, Math.min(score2, Math.min(score3, score4)));
        return 0L;
    }

    public static long part2(List<String> input) {
        return 0L;
    }
}
