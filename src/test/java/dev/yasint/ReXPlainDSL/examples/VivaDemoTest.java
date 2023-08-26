package dev.yasint.ReXPlainDSL.examples;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.Expression;
import dev.yasint.RexPlainDSL.api.ReXPlainDSL;
import dev.yasint.RexPlainDSL.dsl.CharClasses;
import dev.yasint.RexPlainDSL.dsl.Operators;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.Anchors.exactLineMatch;
import static dev.yasint.RexPlainDSL.dsl.Anchors.exactWordBoundary;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.EscapeSequences.space;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.digit;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.hexDigit;
import static dev.yasint.RexPlainDSL.dsl.Groups.*;
import static dev.yasint.RexPlainDSL.dsl.Literals.literal;
import static dev.yasint.RexPlainDSL.dsl.Numeric.integerRange;
import static dev.yasint.RexPlainDSL.dsl.Numeric.leadingZero;
import static dev.yasint.RexPlainDSL.dsl.Operators.either;
import static dev.yasint.RexPlainDSL.dsl.Repetition.*;
import static org.junit.jupiter.api.Assertions.*;

public class VivaDemoTest {

    @Test
    public void test() {
        String set = (CharClasses.simpleSetStr("a", "b", "c", "d", "e").toRegex().toString());
        System.out.println(integerRange(0, 100).toRegex().toString());
    }

    @Test
    public void dateMatchingExample() {

        final String[] months = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", /*"Aug",*/ "Sep", "Oct", "Nov", /*"Dec"*/
        };

        final Pattern expression = new ReXPlainDSL(
                captureGroup(exactLineMatch( // Enclosed in ^...$
                        integerRange(2012, 2020), // Year
                        literal("-"), // Delimiter
                        Operators.eitherStr(months), // Month abbreviations
                        literal("-"), // Delimiter
                        either(
                                leadingZero(integerRange(1, 9)),
                                integerRange(10, 31)
                        ) // Day
                ))
        ).compile().patternInstance();

        assertEquals("(^(?:2020|201[2-9])\\-(?:Apr|Feb|J(?:an|u[ln])|Ma[ry]|" +
                "Nov|Oct|Sep)\\-(?:(?:0?[1-9])|(?:3[01]|[12][0-9]))$)", expression.pattern());

        assertTrue(expression.matches("2012-Jan-01"));
        assertTrue(expression.matches("2013-Oct-27"));
        assertTrue(expression.matches("2018-Nov-9"));
        assertFalse(expression.matches("2019-Dec-31"));
        assertFalse(expression.matches("2012-Aug-01"));

    }

    @Test
    public void rgbaCodeMatchingExample() {

        /* start partial expressions */
        Expression argDelimiter = nonCaptureGroup(
                literal(","), optional(space())
        );
        Expression code0to255 = integerRange(0, 255);
        Expression alphaValue = either(
                Operators.concatMultiple(
                        optional(literal("0")),
                        literal("."),
                        between(1, 2, digit())
                ),
                literal("1.00")
        );
        Expression range0To100 = integerRange(0, 100);
        Expression percentageSign = literal("%");
        /* end partial expressions */

        // to match rgb or rgba color schemes:-
        // rgb(255,0,24), rgb(255, 0, 24), rgba(255, 0, 24, .5)
        Expression RGBA = namedCaptureGroup("rgba_codes",
                Operators.eitherStr("rgb", "rgba"),
                literal("("),
                code0to255, argDelimiter, // R  (can be further simplified to exactly(2, ...)
                code0to255, argDelimiter, // G
                code0to255, // B
                optional(
                        nonCaptureGroup(argDelimiter, alphaValue)
                ),
                literal(")")
        );

        // to match hsla notation color schemes:-
        // hsla(360,100%,100%,1.0)
        Expression HSLA = namedCaptureGroup("hsla_codes",
                literal("hsla("),
                integerRange(0, 360), // hue
                argDelimiter, // delimiter comma and a optional space
                range0To100, percentageSign, // saturation
                argDelimiter, // delimiter comma and a optional space
                range0To100, percentageSign, // lightness
                argDelimiter,
                alphaValue,
                literal(")")
        );

        // to match hex notation color schemes:-
        // #FFFFFF or 0x00FFFF or #FFF
        Expression HEX = namedCaptureGroup("hex_codes",
                Operators.eitherStr("#", "0x"),
                either(
                        exactly(6, hexDigit()),
                        exactWordBoundary(exactly(3, hexDigit()))
                )
        );

        Pattern expression = new ReXPlainDSL(
                either(RGBA, HSLA, HEX)
        ).compile().patternInstance();

        System.out.println(expression.pattern());

        assertEquals(expression.pattern(), "(?:(?P<rgba_codes>rgba?\\((?:25[0-5]|2[0-4][0-9]|1[0-9]" +
                "{2}|[1-9][0-9]|[0-9])(?:,(?: )?)(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])(?" +
                ":,(?: )?)(?:25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])(?:(?:(?:,(?: )?)(?:(?:0)" +
                "?\\.(?:[0-9]){1,2}|1\\.00)))?\\))|(?P<hsla_codes>hsla\\((?:360|3[0-5][0-9]|[12][0-9]" +
                "{2}|[1-9][0-9]|[0-9])(?:,(?: )?)(?:100|[1-9][0-9]|[0-9])%(?:,(?: )?)(?:100|[1-9][0-9]" +
                "|[0-9])%(?:,(?: )?)(?:(?:0)?\\.(?:[0-9]){1,2}|1\\.00)\\))|(?P<hex_codes>(?:0x|#)(?:(?" +
                ":[0-9A-Fa-f]){6}|\\b(?:[0-9A-Fa-f]){3}\\b)))");

    }

    @Test
    public void complexExpressionSegregationExample() {

        // Matches dates in range 2010-1-1 to 2020-12-31,
        // has a space delimiter, and matches any string in the set
        // {SO, SSE, PE, PA, SS}, has a space delimiter, matches a number
        // in range 58499 to 68599, has a space delimiter, 100 to 500 item stock

        // Example of how to segregate complex regular expressions
        // into simpler format. So it's easy to debug, test, and read.

        final Expression DATE = captureGroup(
                integerRange(2010, 2020), literal("-"),
                leadingZero(integerRange(1, 12)), literal("-"),
                leadingZero(integerRange(1, 31))
        );
        final Expression DEPT_CODE = captureGroup(Operators.eitherStr("SO", "SS", "PE", "PA", "SSE")); // Department code
        final Expression ITEM_CODE = captureGroup(integerRange(58499, 68599)); // Item code
        final Expression ITEM_S_COUNT = captureGroup(integerRange(100, 500)); // Item stock count
        final Expression DELIMITER = space(); // Delimiter

        // Compose all the segregated expressions into one
        final Pattern pattern = new ReXPlainDSL(
                DATE, DELIMITER, DEPT_CODE, DELIMITER,
                ITEM_CODE, DELIMITER, ITEM_S_COUNT
        ).compile().patternInstance();

        assertEquals(pattern.pattern(), "((?:2020|201[0-9])\\-(?:0?(?:1[0-2]|[1-9]))\\-(?:0?(?:3[01]" +
                "|[12][0-9]|[1-9]))) ((?:P[AE]|S(?:SE?|O))) ((?:68[0-5][0-9]{2}|6[0-7][0-9]{3}|59[0-9]" +
                "{3}|58[5-9][0-9]{2}|58499)) ((?:500|[1-4][0-9]{2}))");

    }

}
