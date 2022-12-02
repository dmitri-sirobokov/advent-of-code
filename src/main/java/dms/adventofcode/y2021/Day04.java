package dms.adventofcode.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day04 {

    public enum Strategy {
        FirstBoard,
        LastBoard
    }
    public static long calcBingoScore(List<String> input, Strategy strategy) {
        var randomNumbers = Arrays.stream(input.get(0).split(",")).mapToInt(Byte::parseByte).toArray();

        ArrayList<Board> boards = loadBoards(input);

        for (var randomNumber : randomNumbers) {
            var score = 0;
            for (var board : boards) {
                score = board.applyNumber(randomNumber);
                if (score > 0) {
                    if (Strategy.FirstBoard.equals(strategy)) {
                        return score;
                    }
                }
            }

            boards.removeIf(b -> b.score > 0);
            if (boards.size() == 0) {
                return score;
            }
        }
        return 0;
    }

    private static ArrayList<Board> loadBoards(List<String> input) {
        var boards = new ArrayList<Board>();
        for (var i = 2; i < input.size(); i += 6) {
            var board = new Board();
            for (var r = 0; r < 5; r++) {
                var line = input.get(i + r);
                for (var c = 0; c < 5; c++) {
                    board.values[r][c] = Byte.parseByte(line.substring(3 * c, 3 * c + 2).trim());
                }
            }
            boards.add(board);
        }
        return boards;
    }

    private static class Board {
        private int score;
        private final byte[][] values = new byte[5][5];
        private final boolean[][] marks = new boolean[5][5];

        /** Search for specified number on the board and mark this number.
         * If the number is winning then return calculated score. Otherwise, return 0.
         * @param number - number to mark on the board.
         * @return - The score of the board, if the board is winning.
         */
        // search for specified number on the board and mark this number. If the number is winning then return calculated score.
        public int applyNumber(int number) {
            // if board is already winning, no need to calculate again
            if (this.score > 0) {
                return this.score;
            }

            // mark the number
            setMark(number);

            // is board is winning ?
            if (isWinning()) {
                updateScore(number);
            }
            return this.score;
        }

        private void updateScore(int number) {
            var sumUnmarked = 0;
            for (var i = 0; i < 5; i++) {
                for (var j = 0; j < 5; j++) {
                    if (!this.marks[i][j]) {
                        sumUnmarked += this.values[i][j];
                    }
                }
            }
            this.score = number * sumUnmarked;
        }

        private boolean isWinning() {
            for (var i = 0; i < 5; i++) {
                var rowMarks = 0;
                for (var j = 0; j < 5; j++) {
                    if (this.marks[i][j]) {
                        rowMarks++;
                    }
                }
                if (rowMarks == 5) {
                    return true;
                }

                var colMarks = 0;
                for (var j = 0; j < 5; j++) {
                    if (this.marks[j][i]) {
                        colMarks++;
                    }
                }
                if (colMarks == 5) {
                    return true;
                }
            }
            return false;
        }

        private void setMark(int number) {
            for (var i = 0; i < 5; i++) {
                for (var j = 0; j < 5; j++) {
                    if (this.values[i][j] == number) {
                        this.marks[i][j] = true;
                        return;
                    }
                }
            }
        }
    }
}
