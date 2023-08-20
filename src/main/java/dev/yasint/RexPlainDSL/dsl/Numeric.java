package dev.yasint.RexPlainDSL.dsl;

import dev.yasint.RexPlainDSL.api.Expression;
import dev.yasint.RexPlainDSL.exceptions.NumericRangeException;
import dev.yasint.RexPlainDSL.complex.RangeExpression;

import static dev.yasint.RexPlainDSL.api.MetaCharacters.QUESTION_MARK;
import static dev.yasint.RexPlainDSL.dsl.Groups.nonCaptureGroup;

public final class Numeric {

    /**
     * Appends an optional zero to any expression where usually this is
     * used with digits/ranges.
     *
     * @param another expression
     * @return expression with optional leading zero
     */
    public static Expression leadingZero(final Expression another) {
        // Wrap in non capture group to avoid expression collisions.
        // Insert the leading zero and append the zero or one quantifier
        return nonCaptureGroup(() -> another.toRegex().insert(0, "0" + QUESTION_MARK));
    }

    /**
     * Creates a ranged integer based on from and to values inclusively.
     * The resulting expression is wrapped around a non-capturing group
     * to avoid condition collisions.
     *
     * @param from starting integer MIN_INT = 0
     * @param to   ending integer MAX_INT = 10^9 - 1
     * @return range expression
     */
    public static Expression integerRange(final int from, final int to) {
        if (from > to)
            throw new NumericRangeException("integer range is out of order");
        if (from == to)
            return Literals.literal(String.valueOf(from));
        if (from >= 0 && to <= 9)
            return CharClasses.rangedSetStr(String.valueOf(from), String.valueOf(to));
        RangeExpression rangeExpression = new RangeExpression(from, to);
        StringBuilder regex = rangeExpression.toRegex();
        return nonCaptureGroup(rangeExpression);
    }

}
