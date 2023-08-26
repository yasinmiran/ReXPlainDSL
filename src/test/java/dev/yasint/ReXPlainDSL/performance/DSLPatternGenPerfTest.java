package dev.yasint.ReXPlainDSL.performance;

import dev.yasint.RexPlainDSL.api.Expression;
import dev.yasint.RexPlainDSL.api.ReXPlainDSL;
import dev.yasint.RexPlainDSL.dsl.Operators;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static dev.yasint.RexPlainDSL.dsl.Anchors.*;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.alphanumeric;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.simpleSetStr;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.union;
import static dev.yasint.RexPlainDSL.dsl.Groups.captureGroup;
import static dev.yasint.RexPlainDSL.dsl.Groups.nonCaptureGroup;
import static dev.yasint.RexPlainDSL.dsl.Literals.literal;
import static dev.yasint.RexPlainDSL.dsl.Numeric.integerRange;
import static dev.yasint.RexPlainDSL.dsl.Numeric.leadingZero;
import static dev.yasint.RexPlainDSL.dsl.Operators.eitherStr;
import static dev.yasint.RexPlainDSL.dsl.Repetition.*;

public class DSLPatternGenPerfTest {

    private static final int ITERATIONS = 100_000;

    @Test
    public void benchmark() {
        benchmarkDSLPatternMatchTime(
                generateDateMatchingPattern(),
                "Date Matching"
        );
        benchmarkDSLPatternMatchTime(
                generateStringMatchingPattern(),
                "String Matching"
        );
        benchmarkDSLPatternMatchTime(
                generateEmailMatchingPattern(),
                "Email Matching"
        );
        benchmarkDSLPatternMatchTime(
                generateNumberRangeMatchingPattern(),
                "Number Range Matching"
        );
    }

    public void benchmarkDSLPatternMatchTime(ReXPlainDSL dsl, String label) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            dsl.compile();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Generation Time: " + label + ":\t" + (endTime - startTime) + "ms");
    }

    private static ReXPlainDSL generateDateMatchingPattern() {

        final String[] months = new String[]{
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "January", "February", "March", "April", "May", "June",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                "July", "August", "September", "October", "November", "December",
        };

        return new ReXPlainDSL(
                exactLineMatch( // Enclosed in ^...$
                        integerRange(2012, Year.now().getValue()), // Year
                        literal("-"), // Delimiter
                        captureGroup(Operators.eitherStr(months)), // Month abbreviations - group 1
                        literal("-"), // Delimiter
                        captureGroup(leadingZero(integerRange(1, 31))) // Day - group 2
                )
        ).compile();

    }

    private static ReXPlainDSL generateStringMatchingPattern() {
        return new ReXPlainDSL(
                eitherStr(
                        "Apple", "Application", "Appeal",
                        "Contour", "Camera", "Connotation"
                )
        ).compile();
    }

    private static ReXPlainDSL generateEmailMatchingPattern() {

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

        return new ReXPlainDSL(
                startOfLine(),
                local_part,
                at_symbol,
                domain_part,
                top_level,
                endOfLine(false)
        ).compile();

    }

    private static ReXPlainDSL generateNumberRangeMatchingPattern() {

        int start = 0, end = 65535;

        return new ReXPlainDSL(
                integerRange(start, end)
        ).compile();

    }

//    @Test
//    public void benchmarkVanillaPatternCompileTime() {
//
//        int iterations = 100000;
//
//        // Expression Construction
//        // department-code,stock-count,some-date
//
//        long startTime = System.currentTimeMillis();
//
//        java.util.regex.Pattern p = null;
//        for (int i = 0; i < iterations; i++) {
//            p = java.util.regex.Pattern.compile(
//                    "(K|KS|KLE|KLL),((?:[0-9]){3}),((?:[0-9]){4}\\-(?:[0-9]){1,2}\\-(?:[0-9]){1,2})"
//            );
//        }
//
//        long endTime = System.currentTimeMillis();
//
//        System.out.println("Vanilla Pattern Generation Time (ms): " + (endTime - startTime));
//
//    }

}
