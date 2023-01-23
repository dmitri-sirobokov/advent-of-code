package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.Arrays;
import java.util.List;

/**
 * Day 8: Treetop Tree House.
 * Find the best place for the tree house.
 */
public class Day08 extends CodeBase {

    public static long part1(List<String> input) {
        var trees = getTreeInfo(input);
        return Arrays.stream(trees)
                .flatMap(Arrays::stream)
                .filter(tree -> tree.visible)
                .count();
    }

    public static long part2(List<String> input) {
        var trees = getTreeInfo(input);
        return Arrays.stream(trees)
                .flatMap(Arrays::stream)
                .map(tree -> tree.score)
                .max(Integer::compareTo)
                .orElse(0);
    }

    private static TreeInfo[][] getTreeInfo(List<String> input) {
        var levels = readMatrix(input);
        var trees = new TreeInfo[levels.length][levels[0].length];

        forEach(levels, (x, y) -> {
                var tree = new TreeInfo(levels[y][x]);
                tree.visibleInX = true;
                tree.visibleInY = true;
                trees[y][x] = tree;
        });

        forEach(levels, (x, y) -> processVisibilityInX(trees, x, y));

        forEach(levels, (x, y) -> processVisibilityInY(trees, x, y));

        Arrays.stream(trees).flatMap(Arrays::stream)
                .forEach(tree -> {
                    tree.score = tree.countTop * tree.countBottom * tree.countLeft * tree.countRight;
                    tree.visible = tree.visibleInX || tree.visibleInY;
                });


        return trees;
    }

    private static void processVisibilityInY(TreeInfo[][] trees, int x, int y) {
        var tree = trees[y][x];

        var visibleFromTop = true;
        tree.countTop = 0;
        for (var k = y - 1; k >= 0; k--) {
            if (visibleFromTop) tree.countTop++;
            if (trees[k][x].level >= tree.level) {
                visibleFromTop = false;
            }
        }
        var visibleFromBottom = true;
        tree.countBottom = 0;
        for (var k = y + 1; k < trees.length; k++) {
            if (visibleFromBottom) tree.countBottom++;
            if (trees[k][x].level >= tree.level) {
                visibleFromBottom = false;
            }
        }
        tree.visibleInY = visibleFromTop || visibleFromBottom;
    }

    private static void processVisibilityInX(TreeInfo[][] trees, int x, int y) {
        var tree = trees[y][x];
        var visibleFromLeft = true;
        tree.countLeft = 0;
        for (var k = x - 1; k >= 0; k--) {
            if (visibleFromLeft) tree.countLeft++;
            if (trees[y][k].level >= tree.level) {
                visibleFromLeft = false;
            }
        }
        var visibleFromRight = true;
        tree.countRight = 0;
        for (var k = x + 1; k < trees[y].length; k++) {
            if (visibleFromRight) tree.countRight++;
            if (trees[y][k].level >= tree.level) {
                visibleFromRight = false;
            }
        }
        tree.visibleInX = visibleFromLeft || visibleFromRight;
    }

    private static class TreeInfo {

        private final int level;
        private int countTop;
        private int countBottom;
        private int countLeft;
        private int countRight;
        private int score;

        private boolean visibleInX;

        private boolean visibleInY;

        private boolean visible;

        public TreeInfo(int level) {
            this.level = level;
        }

    }
}
