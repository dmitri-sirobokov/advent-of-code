package dms.adventofcode.other;

import java.util.ArrayList;
import java.util.HashSet;

public class Sudoku {

    public static boolean solve(int[][] board) {
        for (var row = 0; row < board.length; row++) {
            for (var col = 0; col < board[0].length; col++) {
                if (board[row][col] == 0) {
                    for (var num = 1; num <= board.length; num++) {
                        board[row][col] = num;
                        if (isValid(board, row, col) && solve(board)) {
                            return true;
                        } else {
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isValid(int[][] board, int row, int col) {
        // check row
        var numSet = new HashSet<Integer>();
        var numList = new ArrayList<Integer>();
        for (var i = 0; i < board.length; i++) {
            addNum(board, row, i, numList, numSet);
        }
        if (numSet.size() != numList.size()) {
            return false;
        }
        // check col
        numList.clear();
        numSet.clear();
        for (var i = 0; i < board.length; i++) {
            addNum(board, i, col, numList, numSet);
        }
        if (numSet.size() != numList.size()) {
            return false;
        }

        // check subsection
        var subSectionSize = board.length / 3;
        var subSectionStartCol = col / subSectionSize * subSectionSize;
        var subSectionStartRow = row / subSectionSize * subSectionSize;

        numList.clear();
        numSet.clear();
        for (var subRow = subSectionStartRow; subRow < subSectionStartRow + subSectionSize; subRow++) {
            for (var subCol = subSectionStartCol; subCol < subSectionStartCol + subSectionSize; subCol++) {
                addNum(board, subRow, subCol, numList, numSet);
            }
        }
        if (numSet.size() != numList.size()) {
            return false;
        }

        return true;
    }

    private static void addNum(int[][] board, int row, int col, ArrayList<Integer> numList, HashSet<Integer> numSet) {
        if (board[row][col] != 0) {
            numSet.add(board[row][col]);
            numList.add(board[row][col]);
        }
    }
}
