package dms.adventofcode.y2022;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day02 extends CodeBase {

    public static int calcScore(String input) throws IOException {
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
        PlayerChoice opponent;
        PlayerChoice me;
    }

    private static List<PlayerRound> readInput(String input) throws IOException {
        var result = new ArrayList<PlayerRound>();
        var lines = readLines(input);
        for (var line : lines) {
            var lineParts = line.split(" ");
            if (lineParts.length != 2) {
                throw new RuntimeException(
                        String.format("Invalid line in the input '{}'. Each line should contain 2 columns seperated by space.", line));
            }
            var playerRound = new PlayerRound();
            switch (lineParts[0].trim()) {
                case "A": {
                    playerRound.opponent = PlayerChoice.Rock;
                    break;
                }
                case "B": {
                    playerRound.opponent = PlayerChoice.Paper;
                    break;
                }
                case "C": {
                    playerRound.opponent = PlayerChoice.Scissors;
                    break;
                }
                default: throw new RuntimeException("Invalid value for player 1. Line input: " + line);
            }

            switch (lineParts[1].trim()) {
                // need to lose
                case "X": {
                    playerRound.me = switch (playerRound.opponent) {
                        case Rock -> PlayerChoice.Scissors;
                        case Paper -> PlayerChoice.Rock;
                        case Scissors -> PlayerChoice.Paper;
                    };
                    break;
                }
                // need to draw
                case "Y": {
                    playerRound.me = switch (playerRound.opponent) {
                        case Rock -> PlayerChoice.Rock;
                        case Paper -> PlayerChoice.Paper;
                        case Scissors -> PlayerChoice.Scissors;
                    };
                    break;
                }
                // need to win
                case "Z": {
                    playerRound.me = switch (playerRound.opponent) {
                        case Rock -> PlayerChoice.Paper;
                        case Paper -> PlayerChoice.Scissors;
                        case Scissors -> PlayerChoice.Rock;
                    };
                    break;
                }
                default: throw new RuntimeException("Invalid value for player 2. Line input: " + line);
            }

            result.add(playerRound);
        }
        return result;
    }

    private static int calcPlayerRoundScore(PlayerRound playerRound) {
        return switch (playerRound.me) {
            case Rock -> 1 + switch (playerRound.opponent) {
                case Rock -> 3;
                case Paper -> 0;
                case Scissors -> 6;
            };
            case Paper -> 2 + switch (playerRound.opponent) {
                case Rock -> 6;
                case Paper -> 3;
                case Scissors -> 0;
            };
            case Scissors -> 3 + switch (playerRound.opponent) {
                case Rock -> 0;
                case Paper -> 6;
                case Scissors -> 3;
            };
        };
    }

}
