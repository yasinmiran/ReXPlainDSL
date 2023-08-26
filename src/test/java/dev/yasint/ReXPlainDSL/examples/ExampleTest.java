package dev.yasint.ReXPlainDSL.examples;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.Expression;
import dev.yasint.RexPlainDSL.api.ReXPlainDSL;
import dev.yasint.RexPlainDSL.unicode.UnicodeScript;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static dev.yasint.RexPlainDSL.dsl.Anchors.*;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.EscapeSequences.space;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.*;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.*;
import static dev.yasint.RexPlainDSL.dsl.Groups.*;
import static dev.yasint.RexPlainDSL.dsl.Literals.literal;
import static dev.yasint.RexPlainDSL.dsl.Numeric.integerRange;
import static dev.yasint.RexPlainDSL.dsl.Numeric.leadingZero;
import static dev.yasint.RexPlainDSL.dsl.Operators.either;
import static dev.yasint.RexPlainDSL.dsl.Operators.eitherStr;
import static dev.yasint.RexPlainDSL.dsl.Repetition.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ExampleTest {

    // For research demo only

    public static Expression higherOrderFunc(final Expression anotherFunc) {
        // We can achieve strict immutability behaviour by doing this
        return () -> anotherFunc.toRegex().append("world!");
    }

    @Test
    public void powerOfExpressionInterface() {
        // Wrap the expression a higher-order function
        final Expression e = higherOrderFunc(() -> new StringBuilder("Hello "));
        assertEquals(e.toRegex().toString(), "Hello world!"); // yields true
    }

    @Test
    public void randomTests() {
        Expression e = includeUnicodeScript(emptySet(), UnicodeScript.ARABIC, false);
        System.out.println(e.toRegex());
    }

    @Test
    public void complexExpressionSegregationExample() {

        // Matches dates in range 2010-1-1 to 2020-12-31,
        // has a space delimiter, and matches any string in the set
        // {SO, SSE, PE, PA, SS}, has a space delimiter, matches a number
        // in range 58499 to 68599, has a space delimiter, 100 to 500 item stock

        // Example of how to segregate complex regular expressions
        // into more simpler format. So it's easy to debug, test, and read.

        final Expression DATE = captureGroup(
                integerRange(2010, 2020), literal("-"),
                leadingZero(integerRange(1, 12)), literal("-"),
                leadingZero(integerRange(1, 31))
        );
        final Expression DEPT_CODE = captureGroup(eitherStr("SO", "SS", "PE", "PA", "SSE")); // Department code
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

    @Test
    public void simpleExpressionSegregationExample() {

        // department-code stock-count some-date

        final Expression DEPARTMENT_CODE = captureGroup(eitherStr("K", "KS", "KLE", "KLL"));
        final Expression ITEM_STOCK_COUNT = captureGroup(exactly(3, digit()));
        final Expression DATE = captureGroup(
                exactly(4, digit()), literal("-"),
                exactly(2, digit()), literal("-"),
                exactly(2, digit())
        );
        final Expression DELIMITER = literal("**");

        // final expression
        final Pattern expression = new ReXPlainDSL(
                DEPARTMENT_CODE, DELIMITER,
                ITEM_STOCK_COUNT, DELIMITER,
                DATE
        ).compile().patternInstance();

        // Matches any string in the set {K, S, KS, KLE, KLL}, followed by two asterisks
        // and 0 or 9 digit 3 times, followed by two asterisks, and matches date formats like 2020-11-31
        final String expr = "(K(?:(?:L[EL]|S))?)\\*\\*([0-9]{3})\\*\\*([0-9]{4}\\-[0-9]{2}\\-[0-9]{2})";

    }

    // Documentation examples for other developers

    @Test
    public void dateMatchingExample() {

        final String[] months = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "January", "February", "March", "April", "May", "June",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                "July", "August", "September", "October", "November", "December",
        };

        final Pattern expression = new ReXPlainDSL(
                exactLineMatch( // Enclosed in ^...$
                        integerRange(2012, Year.now().getValue()), // Year
                        literal("-"), // Delimiter
                        captureGroup(eitherStr(months)), // Month abbreviations - group 1
                        literal("-"), // Delimiter
                        captureGroup(leadingZero(integerRange(1, 31))) // Day - group 2
                )
        ).compile().patternInstance();

        System.out.println(expression.pattern());

//        assertEquals(expression.pattern(),
//                "^(?:202[0-3]|201[2-9])\\-((?:A(?:pr|ug)|Dec|" +
//                        "Feb|J(?:an|u[ln])|Ma[ry]|Nov|Oct|Sep))\\-((?" +
//                        ":0?(?:3[01]|[12][0-9]|[1-9])))$");

    }

    @Test
    public void emailMatchingExample() {

        // ^(?=.{1,256}): Ensure the entire email address is no more than 256 characters.
        //(?=.{1,64}@.{1,255}$): Ensure the local part (before the @) is no more than 64 characters and the domain part (after the @) is no more than 255 characters.
        //(?=.{1,64}\..{1,255}$): Ensure the local part is no more than 64 characters and the domain part (including subdomains) is no more than 255 characters.
        //[a-zA-Z0-9!#$%&'*+/=?^_{|}~-]+`: Match one or more valid characters for the local part.
        //(?:\.[a-zA-Z0-9!#$%&'*+/=?^_{|}~-]+)*`: Optionally match dot-separated parts for the local part.
        //@: Match the @ symbol.
        //(?:(?:)?\.)+: Match one or more domain parts separated by dots.
        //[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$: Match the top-level domain.

        Expression allowed = union(
                alphanumeric(),
                simpleSetStr("!#$%&'*+/=?^_{|}~-".split(""))
        );

        Expression local_part = captureGroup(
                oneOrMoreTimes(allowed),
                zeroOrMoreTimes(
                        nonCaptureGroup(
                                literal("."),
                                oneOrMoreTimes(allowed)
                        )
                )
        );

        Expression at_symbol = literal("@");

        Expression domain_part = captureGroup(
                oneOrMoreTimes(nonCaptureGroup(
                        alphanumeric(),
                        optional(nonCaptureGroup(
                                zeroOrMoreTimes(
                                        union(
                                                alphanumeric(),
                                                simpleSetStr("-")
                                        )
                                ),
                                alphanumeric()
                        )),
                        literal(".")
                ))
        );

        Expression top_level = captureGroup(
                alphanumeric(),
                optional(nonCaptureGroup(
                        zeroOrMoreTimes(union(
                                alphanumeric(),
                                simpleSetStr("-")
                        )),
                        alphanumeric()
                ))
        );

        String email_regex = new ReXPlainDSL(
                startOfLine(),
                local_part,
                at_symbol,
                domain_part,
                top_level,
                endOfLine(false)
        ).regexExpression();

        System.out.println(email_regex);


    }

    @Test
    public void urlMatchingExample() {

        Expression protocol = namedCaptureGroup(
                "protocol",
                eitherStr("http", "https", "ftp")
        );

        Expression sub_domain = namedCaptureGroup(
                "subDomain", oneOrMoreTimes(
                        union(
                                alphanumeric(),
                                simpleSetStr("-", ".")
                        )
                )
        );

        Expression tld = namedCaptureGroup(
                "tld",
                between(2, 4, alphabetic())
        );

        Expression port = optional(
                namedCaptureGroup(
                        "port", literal(":"),
                        integerRange(1, 65535)
                )
        );

        Expression resource = namedCaptureGroup("resource", zeroOrMoreTimes(anything()));

        // Combine all isolated partial expressions
        final Pattern expression = new ReXPlainDSL(
                exactLineMatch(
                        protocol, literal("://"), sub_domain, literal("."), tld,
                        port, optional(literal("/")), resource
                )
        ).compile().patternInstance();

        assertEquals(expression.pattern(), "^(?P<protocol>(?:ftp|https?)):\\/\\/(?P<subDomain>(?:[\\-.0-9A-Za-z])+)" +
                "\\.(?P<tld>(?:[A-Za-z]){2,4})(?:(?P<port>:(?:6553[0-5]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]" +
                "{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])))?(?:\\/)?(?P<resource>(?:.)*)$");

    }

    @Test
    public void doubleNumberMatchingWith1to3FractionDigits() {
        Pattern pattern = new ReXPlainDSL(
                exactWordBoundary(
                        integerRange(0, 1000),
                        literal("."),
                        between(1, 3, digit())
                )
        ).compile().patternInstance();
    }

    @Test
    public void cssColorMatchingExample() {

        // rgb(255,0,24), rgb(255, 0, 24), rgba(255, 0, 24, .5)

        Expression delimiter = nonCaptureGroup(
                literal(","), optional(space())
        );
        Expression codeRange = integerRange(0, 255);

        Expression alpha = nonCaptureGroup(
                delimiter, // <comma><space> after rgb
                optional(literal("0")), // optional zero
                literal("."), // decimal point
                between(1, 5, digit()) // Max 5 fraction digits
        );


        Pattern rgbaExpression = new ReXPlainDSL(
                namedCaptureGroup("rbg_color",
                        eitherStr("rgb", "rgba"),
                        literal("("),
                        codeRange,
                        delimiter, // R
                        codeRange,
                        delimiter, // G
                        codeRange, // B
                        optional(alpha),
                        literal(")")
                )
        ).compile().patternInstance();

        System.out.println(rgbaExpression.pattern());

    }

    @Test
    public void passwordMatchingExample() {
        ReXPlainDSL reXPlainDSL = new ReXPlainDSL(
                startOfLine(),
                alphabetic(), // start with any alphanumeric char (1)
                between(8, 15, alphanumeric()), // (15)
                endOfLine(false)
        );
        System.out.println(reXPlainDSL.regexExpression());
    }

    // ^(?:[A-Fa-f0-9]{1,4}:){7}[A-Fa-f0-9]{1,4}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,7}:$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,6}:[A-Fa-f0-9]{1,4}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,5}(?::[A-Fa-f0-9]{1,4}){1,2}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,4}(?::[A-Fa-f0-9]{1,4}){1,3}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,3}(?::[A-Fa-f0-9]{1,4}){1,4}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,2}(?::[A-Fa-f0-9]{1,4}){1,5}$|
    // ^[A-Fa-f0-9]{1,4}:(?::[A-Fa-f0-9]{1,4}){1,6}$|
    // ^:(?::[A-Fa-f0-9]{1,4}){1,7}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,5}::[A-Fa-f0-9]{1,4}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,4}::(?:[A-Fa-f0-9]{1,4}:){0,1}[A-Fa-f0-9]{1,4}$|
    // ^:(?::[A-Fa-f0-9]{1,4}){0,5}:[A-Fa-f0-9]{1,4}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,3}::(?:[A-Fa-f0-9]{1,4}:){0,3}[A-Fa-f0-9]{1,4}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,2}::(?:[A-Fa-f0-9]{1,4}:){0,4}[A-Fa-f0-9]{1,4}$|
    // ^[A-Fa-f0-9]{1,4}:(?::[A-Fa-f0-9]{1,4}){0,6}$|
    // ^(?:[A-Fa-f0-9]{1,4}:){1,6}:$|
    // ^:(?::[A-Fa-f0-9]{1,4}){1,6}$|
    // ^[A-Fa-f0-9]{1,4}:(?::[A-Fa-f0-9]{1,4}){0,5}:$|
    // ^:(?::[A-Fa-f0-9]{1,4}){0,6}:[A-Fa-f0-9]{1,4}$

    @Test
    public void ipv6MatchingExample() {

        Expression allowed = union(
                union(
                        rangedSetStr("A", "F"),
                        rangedSetStr("a", "f")
                ),
                digit()
        );

        Expression fragment = nonCaptureGroup(
                between(1, 4, allowed),
                literal(":")
        );

        // For blocks that don't end in ":"
        Expression fragmentWithOptionalEnd = either(
                fragment,
                between(1, 4, allowed)
        );

        List<Expression> patterns = new ArrayList<>();

        // Full form without compression
        patterns.add(
                exactLineMatch(
                        exactly(7, fragment),
                        between(1, 4, allowed)
                )
        );

        // Compressed forms
        for (int i = 6; i >= 1; i--) {
            patterns.add(
                    exactLineMatch(
                            between(1, i, fragment),
                            nonCaptureGroup(
                                    literal(":"),
                                    between(1, 7 - i, fragmentWithOptionalEnd)
                            )
                    )
            );
        }

        // Special cases for starting and ending
        // with compressed forms.
        patterns.add(
                exactLineMatch(
                        literal("::"),
                        between(1, 7, fragmentWithOptionalEnd)
                )
        );

        patterns.add(
                exactLineMatch(
                        between(1, 7, fragment),
                        literal("::")
                )
        );

        new ReXPlainDSL(either(patterns.toArray(new Expression[0])));

    }


}
