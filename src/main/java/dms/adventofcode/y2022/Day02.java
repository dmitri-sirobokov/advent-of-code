package dms.adventofcode.y2022;

import java.util.ArrayList;
import java.util.List;

public class Day02 {

    public static int calcScore(List<String> input) {
        var playerRounds = readInput(input);
        var sum = 0;
        for (var playerRound : playerRounds) {
            sum += calcPlayerRoundScore(playerRound);
        }

        return sum;
    }

    private enum PlayerChoice {
        Rock,
        Paper,
        Scissors
    }

    private static class PlayerRound {
        PlayerChoice elf;
        PlayerChoice me;
    }

    private static List<PlayerRound> readInput(List<String> input) {
        var result = new ArrayList<PlayerRound>();
        for (var line : input) {
            var lineParts = line.split(" ");
            if (lineParts.length != 2) {
                throw new RuntimeException(
                        String.format("Invalid line in the input '%s'. Each line should contain 2 columns seperated by space.", line));
            }
            var playerRound = new PlayerRound();
            switch (lineParts[0].trim()) {
                case "A" -> playerRound.elf = PlayerChoice.Rock;
                case "B" -> playerRound.elf = PlayerChoice.Paper;
                case "C" -> playerRound.elf = PlayerChoice.Scissors;
                default -> throw new RuntimeException("Invalid value for player 1. Line input: " + line);
            }

            switch (lineParts[1].trim()) {
                // need to lose
                case "X" -> playerRound.me = switch (playerRound.elf) {
                        case Rock -> PlayerChoice.Scissors;
                        case Paper -> PlayerChoice.Rock;
                        case Scissors -> PlayerChoice.Paper;

                };

                // need to draw
                case "Y" -> playerRound.me = switch (playerRound.elf) {
                    case Rock -> PlayerChoice.Rock;
                    case Paper -> PlayerChoice.Paper;
                    case Scissors -> PlayerChoice.Scissors;
                };

                // need to win
                case "Z" -> playerRound.me = switch (playerRound.elf) {
                    case Rock -> PlayerChoice.Paper;
                    case Paper -> PlayerChoice.Scissors;
                    case Scissors -> PlayerChoice.Rock;
                };
                default -> throw new RuntimeException("Invalid value for player 2. Line input: " + line);
            }

            result.add(playerRound);
        }
        return result;
    }

    private static int calcPlayerRoundScore(PlayerRound playerRound) {
        return switch (playerRound.me) {
            case Rock -> 1 + switch (playerRound.elf) {
                case Rock -> 3;
                case Paper -> 0;
                case Scissors -> 6;
            };
            case Paper -> 2 + switch (playerRound.elf) {
                case Rock -> 6;
                case Paper -> 3;
                case Scissors -> 0;
            };
            case Scissors -> 3 + switch (playerRound.elf) {
                case Rock -> 0;
                case Paper -> 6;
                case Scissors -> 3;
            };
        };
    }

}
