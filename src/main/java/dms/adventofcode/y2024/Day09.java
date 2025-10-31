package dms.adventofcode.y2024;


import java.util.ArrayList;
import java.util.List;

/**
 * Day09: Disk Fragmenter
 *
 */
public class Day09 {

    private static class Block {
        private final int id;
        private int pos;
        private int size;

        Block(int id, int pos, int size) {
            this.id = id;
            this.pos = pos;
            this.size = size;
        }

        public long calcCheckSum() {
            // general formula to multiply ids with position inside a file block
            return id * size * (2L * pos + (size - 1)) / 2;
        }
    }

    private static Block[] readBlocks(List<String> input) {
        var line = input.getFirst();
        var blocks = new Block[line.length()];

        var pos = 0;
        for (int i = 0; i < line.length(); i++) {
            var id = i % 2 == 0 ? i / 2 : 0;
            blocks[i] = new Block(id, pos, Character.digit(line.charAt(i), 10));
            pos += blocks[i].size;
        }
        return blocks;
    }

    private static ArrayList<Block> defragmentBlocksPart1(Block[] blocks) {
        var movedBlocks = new ArrayList<Block>();
        var blocksLastIndex = blocks.length - 1;
        for (var i = 0; i <= blocksLastIndex; i++) {
            var block = blocks[i];
            var pos = block.pos;
            var isFileBlock = i % 2 == 0;
            if (isFileBlock) {
                movedBlocks.add(block);
            } else {
                // move file block from the end to fill this free space
                while (block.size > 0) {
                    var lastBlock = blocks[blocksLastIndex];
                    if (lastBlock.size == 0) {
                        blocksLastIndex -= 2;
                        continue;
                    }
                    var moveBlockSize = (byte)Math.min(lastBlock.size, block.size);
                    var moveBlock = new  Block(lastBlock.id, pos, moveBlockSize);
                    movedBlocks.add(moveBlock);
                    lastBlock.size -= moveBlock.size;
                    block.size -= moveBlockSize;
                    pos += moveBlockSize;
                }
            }
        }
        return movedBlocks;
    }

    private static ArrayList<Block> defragmentBlocksPart2(Block[] blocks) {
        var movedBlocks = new ArrayList<Block>();
        for (var i = blocks.length - 1; i >= 0; i -= 2) {
            var block = blocks[i];
            var isMoved = false;
            for (var j = 1; j < i; j += 2) {
                var freeBlock = blocks[j];
                if (freeBlock.size >= block.size) {
                    movedBlocks.add(new Block(block.id, freeBlock.pos, block.size));
                    freeBlock.size -= block.size;
                    freeBlock.pos += block.size;
                    isMoved = true;
                    break;
                }
            }
            if (!isMoved) {
                movedBlocks.add(block);
            }
        }
        return movedBlocks;
    }

    public static long part1(List<String> input) {
        var blocks = readBlocks(input);

        var movedBlocks = defragmentBlocksPart1(blocks);
        return movedBlocks
                .stream()
                .mapToLong(Block::calcCheckSum)
                .sum();
    }

    public static long part2(List<String> input) {
        var blocks = readBlocks(input);

        var movedBlocks = defragmentBlocksPart2(blocks);
        return movedBlocks
                .stream()
                .mapToLong(Block::calcCheckSum)
                .sum();
    }

}
