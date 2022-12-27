package dms.adventofcode.other;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SudokuTest {

    @Test
    void solve() {
        // the most difficult Sudoku 9x9 combination in the world
        var board = new int[][] {
                { 8, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
                { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
                { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
                { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
                { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
                { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
                { 0, 9, 0, 0, 0, 0, 4, 0, 0 }
        };

        var expectedSolution = new int[][] {
                { 8, 1, 2, 7, 5, 3, 6, 4, 9 },
                { 9, 4, 3, 6, 8, 2, 1, 7, 5 },
                { 6, 7, 5, 4, 9, 1, 2, 8, 3 },
                { 1, 5, 4, 2, 3, 7, 8, 9, 6 },
                { 3, 6, 9, 8, 4, 5, 7, 2, 1 },
                { 2, 8, 7, 1, 6, 9, 5, 3, 4 },
                { 5, 2, 1, 9, 7, 4, 3, 6, 8 },
                { 4, 3, 8, 5, 2, 6, 9, 1, 7 },
                { 7, 9, 6, 3, 1, 8, 4, 5, 2 }
        };
        assertTrue(Sudoku.solve(board), getWrongResultMessage(expectedSolution, board));

    }

    @Test
    // random test to play with
    void solver_another() {
        var board = new int[][] {
                { 1, 0, 0, 9, 4, 0, 0, 0, 0 },
                { 0, 0, 6, 0, 7, 0, 0, 0, 0 },
                { 0, 2, 8, 0, 0, 0, 5, 0, 0 },
                { 3, 0, 7, 1, 0, 9, 2, 0, 0 },
                { 0, 0, 0, 8, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 3, 0, 0 },
                { 2, 0, 0, 0, 0, 0, 0, 0, 5 },
                { 0, 8, 0, 0, 0, 0, 7, 0, 0 },
                { 4, 0, 0, 0, 1, 6, 9, 0, 0 }
        };

        assertTrue(Sudoku.solve(board));
        System.out.println(sudokuBoardToString(board));

    }

    private String getWrongResultMessage(int[][] expected, int[][] actual) {
        var message = new StringBuilder();
        message.append("Expected\n");
        message.append(sudokuBoardToString(expected));
        message.append("\n");
        message.append("Actual\n");
        message.append(sudokuBoardToString(actual));
        return message.toString();
    }

    private String sudokuBoardToString(int[][] board) {
        var result = new StringBuilder();
        for (var row = 0; row < board.length; row++) {
            result.append(Arrays.stream(board[row]).mapToObj(v -> " " + v + " ").collect(Collectors.joining()));
            result.append("\n");
        }
        result.append("\n");
        return result.toString();
    }


}