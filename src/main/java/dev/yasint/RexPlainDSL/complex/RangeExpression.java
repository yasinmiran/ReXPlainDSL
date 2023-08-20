package dev.yasint.RexPlainDSL.complex;

import dev.yasint.RexPlainDSL.api.Expression;

import java.util.Collections;
import java.util.LinkedList;

import static dev.yasint.RexPlainDSL.api.MetaCharacters.*;

/**
 * Synthesis :: Regular Expression Integer Range
 * This generates a regular expression number range given
 * inclusive start and end integers. This implementation's
 * running time is O(log n).
 */
public class RangeExpression implements Expression {

    private final int _rStart;
    private final int _rEnd;

    /**
     * Creates an int range expression
     *
     * @param _rStart int start inclusive
     * @param _rEnd   int end inclusive
     */
    public RangeExpression(int _rStart, int _rEnd) {
        this._rStart = _rStart;
        this._rEnd = _rEnd;
    }

    /**
     * Marks the left boundaries from the range start
     *
     * @param start int range
     * @param end   int range
     * @return left boundaries
     */
    private static LinkedList<Range> leftBounds(int start, int end) {
        final LinkedList<Range> result = new LinkedList<>();
        while (start < end) {
            final Range range = Range.fromStart(start);
            result.add(range);
            start = range.end + 1;
        }
        return result;
    }

    /**
     * Marks the right boundaries from the range end
     *
     * @param start int range
     * @param end   int range
     * @return right boundaries
     */
    private static LinkedList<Range> rightBounds(int start, int end) {
        final LinkedList<Range> result = new LinkedList<>();
        while (start < end) {
            final Range range = Range.fromEnd(end);
            result.add(range);
            end = range.start - 1;
        }
        Collections.reverse(result);
        return result;
    }

    @Override
    public StringBuilder toRegex() {

        final LinkedList<Range> left = leftBounds(_rStart, _rEnd);
        final Range lastLeft = left.removeLast();
        final LinkedList<Range> right = rightBounds(lastLeft.start, _rEnd);
        final Range firstRight = right.removeFirst();

        // Merge all classes
        final LinkedList<Range> merged = new LinkedList<>(left);
        if (!lastLeft.overlaps(firstRight)) {
            merged.add(lastLeft);
            merged.add(firstRight);
        } else {
            merged.add(Range.join(lastLeft, firstRight));
        }
        merged.addAll(right);

        // Append the ranges from reverse order. So the match
        // will go from high to low. Otherwise, even if its
        // has 2-digits it'll only match 1; if it's an option.
        final StringBuilder expression = new StringBuilder();
        for (int i = merged.size() - 1; i >= 0; i--) {
            expression.append(merged.get(i).toRegex());
            if (i != 0) expression.append(ALTERNATION);
        }

        return expression;

    }

    /**
     * DynamicRange Expression
     */
    private static final class Range implements Expression {

        final private StringBuilder expression;
        final private int start;
        final private int end;

        private Range(int start, int end) {
            this.start = start;
            this.end = end;
            this.expression = new StringBuilder();
        }

        /**
         * Creates a range from the end int by setting the set
         * min range for digit.
         *
         * @param end int range
         * @return range with new start/end
         */
        private static Range fromEnd(int end) {
            final char[] chars = String.valueOf(end).toCharArray();
            for (int i = chars.length - 1; i >= 0; i--) {
                if (chars[i] == '9') {
                    chars[i] = '0';
                } else {
                    chars[i] = '0';
                    break;
                }
            }
            return new Range(Integer.parseInt(String.valueOf(chars)), end);
        }

        /**
         * Creates a range from the start int by setting the set
         * max range for digit.
         *
         * @param start int range
         * @return range with new start/end
         */
        private static Range fromStart(int start) {
            final char[] chars = String.valueOf(start).toCharArray();
            for (int i = chars.length - 1; i >= 0; i--) {
                if (chars[i] == '0') {
                    chars[i] = '9';
                } else {
                    chars[i] = '9';
                    break;
                }
            }
            return new Range(start, Integer.parseInt(String.valueOf(chars)));
        }

        /**
         * Joins two dynamic ranges together
         *
         * @param a left range
         * @param b right range
         * @return merged range
         */
        private static Range join(Range a, Range b) {
            // Simply just join two ranges
            return new Range(a.start, b.end);
        }

        /**
         * Checks whether two ranges overlaps together
         * cannot be [0-9][0-9]
         *
         * @param r target range
         * @return whether it's overlapping or not
         */
        private boolean overlaps(final Range r) {
            // Checks whether current range overlaps the previous range
            return this.end > r.start && r.end > this.start;
        }

        @Override
        public StringBuilder toRegex() {

            final String startStr = String.valueOf(start);
            final String endStr = String.valueOf(end);
            int repeatedCount = 0;
            char previousDigitA = 0, previousDigitB = 0;

            for (int pos = 0; pos < startStr.length(); pos++) {

                char currentDigitA = startStr.charAt(pos);
                char currentDigitB = endStr.charAt(pos);

                if (currentDigitA == currentDigitB) {
                    expression.append(currentDigitA);
                } else {
                    // previous is equal to this
                    if (previousDigitA == currentDigitA && previousDigitB == currentDigitB) {
                        repeatedCount++; // increment the quantifier
                        if (!(pos == startStr.length() - 1)) {
                            continue; // if not last
                        } else { // if it is last
                            expression
                                    .append(OPEN_CURLY_BRACE)
                                    .append(++repeatedCount)
                                    .append(CLOSE_CURLY_BRACE);
                            break;
                        }
                    }
                    if (repeatedCount > 0) {
                        expression
                                .append(OPEN_CURLY_BRACE)
                                .append(repeatedCount)
                                .append(CLOSE_CURLY_BRACE);
                        repeatedCount = 0;
                    }
                    expression.append(OPEN_SQUARE_BRACKET)
                            .append(currentDigitA)
                            .append(currentDigitB - currentDigitA == 1 ? "" : HYPHEN)
                            .append(currentDigitB)
                            .append(CLOSE_SQUARE_BRACKET);
                    previousDigitA = currentDigitA;
                    previousDigitB = currentDigitB;
                }
            }
            return expression;

        }

        @Override
        public String toString() {
            // debug only
            return String.format("RangeGen { start=%d, end=%d }",
                    start, end
            );
        }

    }

}
