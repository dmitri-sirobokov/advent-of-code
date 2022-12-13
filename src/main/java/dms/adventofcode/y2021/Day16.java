package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day16 extends CodeBase {

    public static long part1(List<String> input) throws IOException {

        var binaryStringBuilder = new StringBuilder(input.get(0).length() * 4);
        for (var i = 0; i < input.get(0).length(); i++) {
            var decimal = Integer.parseInt(String.valueOf(input.get(0).charAt(i)), 16);
            binaryStringBuilder.append(String.format("%4s", Integer.toBinaryString(decimal)).replace(" ", "0"));
        }


        var binaryStringReader = new BinaryStringReader(binaryStringBuilder);
        var packet = readPacket(binaryStringReader);

        return 0;
    }

    private static Packet readPacket(BinaryStringReader binaryStringReader) {
        var packetVersion = binaryStringReader.readBits(3);
        var packetTypeId = binaryStringReader.readBits(3);
        if (packetTypeId == 4) {
            // read literal
            var literal = readLiteral(binaryStringReader);
            return new LiteralPacket(packetVersion, packetTypeId, literal);
        } else {
            // operator packet
            var lengthTypeID = binaryStringReader.readBits(1);
            var length = lengthTypeID == 0 ? binaryStringReader.readBits(15) : binaryStringReader.readBits(11);
            if (lengthTypeID == 0) {

            }
        }
        return null;
    }

    private static long readLiteral(BinaryStringReader binaryStringReader) {
        var literalValue = 0L;
        while (true) {
            var hasMore = binaryStringReader.readBits(1);

            literalValue = 16 * literalValue + binaryStringReader.readBits(4);
            if (hasMore == 0) {
                break;
            }
        }
        return literalValue;
    }

    public static long part2(List<String> input) {
        return 0;
    }

    private static class BinaryStringReader {
        private final StringBuilder binaryString;
        private int next = 0;

        public BinaryStringReader(StringBuilder binaryString) {
            this.binaryString = binaryString;
        }

        public long readBits(int num) {
            var result = 0;
            for (var i = 0; i < num && next < binaryString.length(); i++, next++) {
                result = result * 2 + (binaryString.charAt(next) == '1' ? 1 : 0);
            }
            return result;
        }

        public boolean hasMore() {
            return next < binaryString.length();
        }
    }

    private static sealed class Packet permits LiteralPacket, OperatorPacket {
        private final List<Packet> subPackets = new ArrayList<>();
        private final long packetVersion;
        private final long packetTypeId;

        public Packet(long packetVersion, long packetTypeId) {
            this.packetVersion = packetVersion;
            this.packetTypeId = packetTypeId;
        }
    }

    private static final class LiteralPacket extends Packet {
        private final long literal;

        public LiteralPacket(long packetVersion, long packetTypeId, long literal) {
            super(packetVersion, packetTypeId);
            this.literal = literal;
        }
    }

    private static final class OperatorPacket extends Packet {
        public OperatorPacket(long packetVersion, long packetTypeId) {
            super(packetVersion, packetTypeId);
        }
    }
}
