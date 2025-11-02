package dms.adventofcode.y2024;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Day13: Claw Contraption
 *
 */
public class Day13 {

    private record Vector(long x, long y) {
        public boolean isEmpty() {
            return x == 0 && y == 0;
        }

        public boolean isValid(long maxX, long maxY) {
            return !isEmpty() && x <= maxX && y <= maxY;
        }
    }
    private static class Game {
        private final Vector buttonA;
        private final Vector buttonB;
        private final Vector priceLocation;
        private final boolean part2Mode;
        private long price;
        Game(Vector buttonA, Vector buttonB, Vector priceLocation, boolean part2Mode) {
            this.buttonA = buttonA;
            this.buttonB = buttonB;
            this.priceLocation = priceLocation;
            this.part2Mode = part2Mode;
        }

        private static Vector solveAB(Vector priceLocation, Vector buttonA, Vector buttonB) {
            // solve equation for number of buttons to be pressed (a,b)
            // X = a * ax + b * bx
            // Y = a * ay + b * by
            // where (X,Y) location of the price, (ax,ay) - claw movement for button A,
            // (bx,by) - claw movement for button B
            var divisor = buttonB.x * buttonA.y - buttonB.y * buttonA.x;
            var dividend = priceLocation.x * buttonA.y - priceLocation.y * buttonA.x;
            if (dividend % divisor != 0) return new Vector(0, 0);
            var b = dividend / divisor;
            var a = (priceLocation.x - b * buttonB.x) / buttonA.x;
            return new Vector(a, b);
        }

        public void play() {
            var solution = solveAB(priceLocation, buttonA, buttonB);
            var maxX = this.part2Mode ? Long.MAX_VALUE : 100L;
            var maxY = this.part2Mode ? Long.MAX_VALUE : 100L;
            this.price = solution.isValid(maxX, maxY) ? 3 * solution.x + solution.y : 0;
        }

    }

    private static final Pattern xyPattern = Pattern.compile("(\\d+)[^\\d]+(\\d+)");

    private static Vector parseVector(String line) {
        var matcher = xyPattern.matcher(line);
        if (!matcher.find()) throw new RuntimeException("No match found");
        var x = Integer.parseInt(matcher.group(1));
        var y = Integer.parseInt(matcher.group(2));
        return new Vector(x, y);
    }

    public static long playGame(List<String> input, boolean part2) {

        var games = new ArrayList<Game>();
        for (var i = 0; i < input.size(); i++) {
            var buttonA = parseVector(input.get(i));
            var buttonB = parseVector(input.get(i + 1));
            var priceLocation = parseVector(input.get(i + 2));
            if (part2) {
                priceLocation = new Vector(priceLocation.x + 10000000000000L, priceLocation.y + 10000000000000L);
            }
            var game = new Game(buttonA, buttonB, priceLocation, part2);
            games.add(game);
            i += 3;
        }

        for (var game: games) {
            game.play();
        }
        return games.stream()
                .mapToLong(game -> game.price)
                .sum();
    }

    public static long part1(List<String> input) {

        return playGame(input, false);
    }

    public static long part2(List<String> input) {
        return playGame(input, true);
    }
}
