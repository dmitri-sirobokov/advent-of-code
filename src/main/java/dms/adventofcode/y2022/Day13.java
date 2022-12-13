package dms.adventofcode.y2022;

import dms.adventofcode.CodeBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Day 13: Distress Signal
 */
public class Day13 extends CodeBase {

    public static long part1(List<String> input) {
        var pairs = readPairs(input);
        var result = 0;
        for (var i = 0; i < pairs.size(); i++) {
            var pair = pairs.get(i);
            if (Pair.compare(pair.left, pair.right) < 0) {
                result = result + (i + 1);
            }
        }
        return result;
    }

    public static long part2(List<String> input) {
        var pairs = readPairs(input);
        var allValues = new ArrayList<PairValue>();
        for (Pair pair : pairs) {
            allValues.add(pair.left);
            allValues.add(pair.right);

        }

        var decoderValue1 = new ArrayValue();
        decoderValue1.values.add(new ArrayValue(new LiteralValue(2)));
        var decoderValue2 = new ArrayValue();
        decoderValue2.values.add(new ArrayValue(new LiteralValue(6)));
        allValues.add(decoderValue1);
        allValues.add(decoderValue2);
        allValues.sort(Pair::compare);
        var decoderIndex1 = allValues.indexOf(decoderValue1);
        var decoderIndex2 = allValues.indexOf(decoderValue2);
        return (long) (decoderIndex1 + 1) * (decoderIndex2 + 1);
    }
    private static List<Pair> readPairs(List<String> input) {
        var result = new ArrayList<Pair>();
        for (var i = 0; i < input.size(); i += 3) {
            var left = PairValue.parse(input.get(i));
            var right = PairValue.parse(input.get(i + 1));
            result.add(new Pair(left, right));
        }
        return result;
    }

    public static class Pair {
        private final PairValue left;
        private final PairValue right;

        public Pair(PairValue left, PairValue right) {
            this.left = left;
            this.right = right;
        }
        public static int compare(PairValue left, PairValue right) {
            if (left instanceof LiteralValue l && right instanceof LiteralValue r) {
                return l.compare(r);
            }

            var l = switch (left) {
                case LiteralValue lv -> new ArrayValue(lv);
                case ArrayValue av -> av;
            };
            var r = switch (right) {
                case LiteralValue lv -> new ArrayValue(lv);
                case ArrayValue av -> av;
            };

            return l.compare(r);
        }
    }

    public static abstract sealed class PairValue permits LiteralValue, ArrayValue {
        public static ArrayValue parse(String value) {
            var parent = new ArrayValue();
            var parents = new Stack<ArrayValue>();
            parents.add(parent);
            for (var i = 1; i < value.length(); i++) {
                var ch = value.charAt(i);
                if (ch == '[') {
                    var newParent = new ArrayValue();
                    parent.values.add(newParent);
                    parents.push(parent);
                    parent = newParent;
                } else if (ch == ']') {
                    parent = parents.pop();
                } else if (ch == ',') {
                    // do nothing
                } else {
                    var nextIndex = findLiteralEndIndex(value, i);
                    if (nextIndex > i) {
                        var literalValue = Integer.parseInt(value, i, nextIndex, 10);
                        parent.values.add(new LiteralValue(literalValue));
                    }
                    i = nextIndex - 1;
                }
            }
            return parent;
        }

        private static int findLiteralEndIndex(String value, int startIndex) {
            for (var i = startIndex; i < value.length(); i++) {
                if (value.charAt(i) < '0' || value.charAt(i) > '9') {
                    return i;
                }
            }
            return -1;
        }

    }

    public static final class LiteralValue extends PairValue {
        private final int value;

        public LiteralValue(int value) {
            this.value = value;
        }

        public int compare(LiteralValue other) {
            return Integer.compare(this.value, other.value);
        }
    }

    public static final class ArrayValue extends PairValue {
        private final List<PairValue> values = new ArrayList<>();

        public ArrayValue() { }
        public ArrayValue(LiteralValue value) {
            values.add(value);
        }

        public int compare(ArrayValue v2) {
            for (var i = 0; i < Math.max(values.size(), v2.values.size()); i++) {
                if (i >= v2.values.size()) {
                    return 1;
                }
                if (i >= values.size()) {
                    return -1;
                }

                var left = values.get(i);
                var right = v2.values.get(i);
                var r = Pair.compare(left, right);
                if (r != 0) {
                    return r;
                }
            }
            return 0;
        }
    }

}
