package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;

import java.awt.*;
import java.util.List;


/**
 * Day 17: Trick Shot
 * Chain of thoughts...My feeling is there are better ways to calculate the highest point of the trajectory, possibly with one arithmetic equations, but
 * I start with the simple math calculation, that is based on...
 * The series of numbers 0,1,2,..n has a sum of n * (n + 1) / 2
 * This is like a travel distance for an object that starts with 0 speed with acceleration of 1.
 * For horizontal speed the best starting point where drone is at 0 x-speed withing target area and then increasing towards starting point..
 * For the vertical motions we have to look at highest position where speed is zero, and then increasing towards starting point or towards target area. */

 public class Day17 extends CodeBase {

    public static long part1(List<String> input) {
        var target = readTarget(input);
        var xSpeed = 0;
        var ySpeed = 0;
        // xSpeed is also time spent until speed reach 0, after that x position does not change.
        // and maximum xSpeed is also target.x + target.width - 1, any higher x-speed guarantees overshoot.
        // we test for each of such time slot if x-position is in the target range,
        // note, that travel distance is speed * (speed + 1) / 2 until the object stops moving.
        var xTimes = new boolean[target.x + target.width - 1];
        // that is a maximum vertical speed down at y=0. all higher speed will overshoot the target.
        // note that vertical speed is symmetrical, and it takes 2x time to travel first up to the highest point
        // and then back to y=0 position with the same speed but opposite sign.
        var yTimes = new boolean[-target.y + target.height - 1];
        while (xSpeed < xTimes.length) {
            var xPos = xSpeed * (xSpeed + 1) / 2;
            xTimes[xSpeed] = xPos >= target.x && xPos < target.x + target.width;
            xSpeed++;
        }
        var ySpeedMax = -1;
        while (ySpeed < yTimes.length) {
            var yPos = ySpeed * (ySpeed + 1) / 2;
            yTimes[ySpeed] = yPos >= -target.y && yPos < -(target.y - target.height);
            ySpeed++;
        }

        for (var t = Math.max(xTimes.length, yTimes.length) - 1; t >= 0; t--) {
            var xMatch = t < xTimes.length ? xTimes[xTimes.length - 1] : xTimes[t];
            var yMatch = t < yTimes.length && yTimes[t];
            if (xMatch && yMatch) {
                return t * (t + 1) / 2;
            }
        }
        return -1;
    }

    public static long part2(List<String> input) {
        return 0;
    }

    private static Rectangle readTarget(List<String> input) {
        var xi = input.get(0).indexOf("x=");
        var xs = input.get(0).substring(xi + 2);
        var yi = xs.indexOf(", y=");
        var ys = xs.substring(yi + 4);
        xs = xs.substring(0, yi);
        var xParts = xs.split("\\.\\.");
        var yParts = ys.split("\\.\\.");
        var x1 = Integer.parseInt(xParts[0]);
        var x2 = Integer.parseInt(xParts[1]);
        var y1 = Integer.parseInt(yParts[0]);
        var y2 = Integer.parseInt(yParts[1]);
        return new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }

}
