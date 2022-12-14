package dms.adventofcode.y2021;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.List;

public class Day16 extends CodeBase {

    public static long part1(List<String> input) {

        Packet packet = readPacket(input);

        return countPacketsVersionNumbers(packet);
    }

    public static long part2(List<String> input) {
        Packet packet = readPacket(input);

        return calcValue(packet);
    }

    private static long countPacketsVersionNumbers(Packet packet) {
        var sum = packet.packetVersion;
        for (var subPacket : packet.subPackets) {
            sum += countPacketsVersionNumbers(subPacket);
        }
        return sum;
    }

    private static long calcValue(Packet packet) {
        if (packet instanceof LiteralPacket lp) {
            return lp.literal;
        }

        if (packet instanceof OperatorPacket op) {
            return switch (Long.valueOf(op.packetTypeId).intValue()) {
                case 0 -> op.subPackets.stream().mapToLong(Day16::calcValue).sum();
                case 1 -> op.subPackets.stream().mapToLong(Day16::calcValue).reduce((a, b) -> a * b).orElse(0);
                case 2 -> op.subPackets.stream().mapToLong(Day16::calcValue).min().orElse(0);
                case 3 -> op.subPackets.stream().mapToLong(Day16::calcValue).max().orElse(0);
                case 5 -> (calcValue(op.subPackets.get(0)) > calcValue(op.subPackets.get(1))) ? 1L : 0L;
                case 6 -> (calcValue(op.subPackets.get(0)) < calcValue(op.subPackets.get(1))) ? 1L : 0L;
                case 7 -> (calcValue(op.subPackets.get(0)) == calcValue(op.subPackets.get(1))) ? 1L : 0L;
                default -> throw new IllegalStateException("Unexpected value: " + op.packetTypeId);
            };
        }
        return 0;
    }

    private static Packet readPacket(List<String> input) {
        var binaryStringBuilder = new StringBuilder(input.get(0).length() * 4);
        for (var i = 0; i < input.get(0).length(); i++) {
            var decimal = Integer.parseInt(String.valueOf(input.get(0).charAt(i)), 16);
            binaryStringBuilder.append(String.format("%4s", Integer.toBinaryString(decimal)).replace(" ", "0"));
        }


        var binaryStringReader = new BinaryStringReader(binaryStringBuilder);
        return readPacket(binaryStringReader);
    }

    private static Packet readPacket(BinaryStringReader binaryStringReader) {
        var packetVersion = binaryStringReader.readBits(3);
        var packetTypeId = binaryStringReader.readBits(3);
        if (packetTypeId == 4) {
            // read literal
            var literal = readLiteral(binaryStringReader);
            return new LiteralPacket(packetVersion, packetTypeId, literal);
        } else {
            var operatorPacket = new OperatorPacket(packetVersion, packetTypeId);
            var lengthTypeID = binaryStringReader.readBits(1);
            var length = lengthTypeID == 0 ? binaryStringReader.readBits(15) : binaryStringReader.readBits(11);
            if (lengthTypeID == 0) {
                // length type is number of bits
                var subPacketData = binaryStringReader.readString(Long.valueOf(length).intValue());
                var subPacketStringReader = new BinaryStringReader(new StringBuilder(subPacketData));
                while (subPacketStringReader.next < subPacketStringReader.length) {
                    var subPacket = readPacket(subPacketStringReader);
                    operatorPacket.subPackets.add(subPacket);
                }
            } else if (lengthTypeID == 1) {
                // length type is number of packets
                for (var i = 0; i < length; i++) {
                    var subPacket = readPacket(binaryStringReader);
                    operatorPacket.subPackets.add(subPacket);
                }
            }
            return operatorPacket;
        }
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

    private static class BinaryStringReader {
        private final StringBuilder binaryString;
        private final int length;
        private int next = 0;

        public BinaryStringReader(StringBuilder binaryString) {
            this.binaryString = binaryString;
            this.length = binaryString.length();
        }

        public long readBits(int num) {
            var result = 0;
            for (var i = 0; i < num && next < binaryString.length(); i++, next++) {
                result = result * 2 + (binaryString.charAt(next) == '1' ? 1 : 0);
            }
            return result;
        }

        public String readString(int num) {
            var result = binaryString.substring(next, next + num);
            next += num;
            return result;
        }

        public boolean hasMore() {
            return next < binaryString.length();
        }
    }

    private static sealed class Packet permits LiteralPacket, OperatorPacket {
        public final List<Packet> subPackets = new ArrayList<>();
        public final long packetVersion;
        public final long packetTypeId;

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
