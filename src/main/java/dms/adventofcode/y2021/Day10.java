package dms.adventofcode.y2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day10 {

    static {
        new ChunkDef('(', ')', 3, 1);
        new ChunkDef('[', ']', 57, 2);
        new ChunkDef('{', '}', 1197, 3);
        new ChunkDef('<', '>', 25137, 4);
    }

    public static long errorScore(List<String> input) {
        return input.stream().mapToInt(line -> errorScoreLine(line).score).sum();
    }

    public static long repairScore(List<String> input) {
        // filter lines with errors, leave unclosed chunks only
        var nonCompletedChunks = input.stream()
                .map(line -> errorScoreLine(line))
                .filter(errorScore -> errorScore.score == 0)
                .toList();

        // close chunks
        var repairScores = new ArrayList<Long>();
        for (var nonCompletedChunk : nonCompletedChunks) {

            var nonCompletedChunkDefs = nonCompletedChunk.openChunks.stream()
                    .map(openChunk -> ChunkDef.openTokens[openChunk.openToken])
                    .toList();

            // close in reverse order
            nonCompletedChunkDefs = new ArrayList<>(nonCompletedChunkDefs);
            Collections.reverse(nonCompletedChunkDefs);

            // calc repair score for the non-completed line
            var lineRepairScore = nonCompletedChunkDefs.stream()
                    .map(chunkDef -> chunkDef.repairScore)
                    .mapToLong(Integer::longValue)
                    .reduce((a,b) -> a * 5 + b)
                    .orElse(0);
            repairScores.add(lineRepairScore);
        }
        var repairScoresSorted = repairScores.stream().sorted().toList();
        // get the score in the middle
        return repairScoresSorted.get(repairScoresSorted.size() / 2);
    }

    private static ErrorScore errorScoreLine(String line) {
        var chunks = new ArrayList<Chunk>();
        for (var i = 0; i < line.length(); i++) {
            var ch = line.charAt(i);
            if (ChunkDef.isOpenToken(ch)) {
                chunks.add(new Chunk(ch));
            } else if (ChunkDef.isCloseToken(ch)) {
                ChunkDef chunkDef = ChunkDef.closeTokens[ch];
                if (chunks.size() == 0 || chunks.get(chunks.size() - 1).openToken != chunkDef.openToken) {
                    return new ErrorScore(chunkDef.errorScore, chunks, line);
                }
                chunks.remove(chunks.size() - 1);
            } else {
                throw new RuntimeException("Error parsing input: " + line);
            }
        }
        return new ErrorScore(0, chunks, line);
    }

    private record Chunk(char openToken) {
    }

    private record ChunkDef(char openToken, char closeToken, int errorScore, int repairScore) {

        private static final ChunkDef[] openTokens = new ChunkDef[256];
        private static final ChunkDef[] closeTokens = new ChunkDef[256];

        private ChunkDef {
            openTokens[openToken] = this;
            closeTokens[closeToken] = this;
        }

        public static boolean isOpenToken(char ch) {
            return openTokens[ch] != null;
        }

        public static boolean isCloseToken(char ch) {
            return closeTokens[ch] != null;
        }
    }

    private record ErrorScore (int score, List<Chunk> openChunks, String line) {

    }
}
