package dev.yasint.ReXPlainDSL.dsl;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.ReXPlainDSL;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.Anchors.*;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.alphabetic;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.word;
import static dev.yasint.RexPlainDSL.dsl.Groups.captureGroup;
import static dev.yasint.RexPlainDSL.dsl.Literals.literal;
import static dev.yasint.RexPlainDSL.dsl.Repetition.oneOrMoreTimes;
import static org.junit.jupiter.api.Assertions.assertEquals;


public final class AnchorsTest {

    @Test
    public void itShouldAppendAWordBoundaryAtPosition() {
        final Pattern expression = new ReXPlainDSL(
                captureGroup(wordBoundary()
                        .debug(System.out::println), word())
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "(\\b[0-9A-Z_a-z])");
    }

    @Test
    public void itShouldAppendANonWordBoundaryAtPosition() {
        final Pattern expression = new ReXPlainDSL(
                nonWordBoundary(),
                word()
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "\\B[0-9A-Z_a-z]");
    }

    @Test
    public void itShouldAppendAStartOfLineAssertionAtPosition() {
        final Pattern expression = new ReXPlainDSL(
                startOfLine(),
                word()
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "^[0-9A-Z_a-z]");
    }

    @Test
    public void itShouldAppendEndOfLineAssertionAtPosition() {
        final Pattern expression = new ReXPlainDSL(
                startOfLine(),
                oneOrMoreTimes(word()),
                endOfLine(false)
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "^(?:[0-9A-Z_a-z])+$");
    }

    @Test
    public void itShouldAppendEndOfLineAssertionWithOptionalCarriageReturnAtPosition() {
        final Pattern expression = new ReXPlainDSL(
                startOfLine(),
                oneOrMoreTimes(word()),
                endOfLine(true)
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "^(?:[0-9A-Z_a-z])+\\x0D?$");
    }

    @Test
    public void itShouldAppendStartOfTextAssertionAtPosition() {
        final Pattern expression = new ReXPlainDSL(
                startOfText(),
                word()
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "\\A[0-9A-Z_a-z]");
    }

    @Test
    public void itShouldAppendEndOfTextAssertionAtPosition() {
        final Pattern expression = new ReXPlainDSL(
                word(),
                endOfText()
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "[0-9A-Z_a-z]\\z");
    }

    @Test
    public void itShouldWrapTheExpressionInLineMatcher() {
        final Pattern expression = new ReXPlainDSL(
                exactLineMatch(
                        wordBoundary(),
                        word()
                )
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "^\\b[0-9A-Z_a-z]$");
    }

    @Test
    public void itShouldWrapTheExpressionInWordBoundary() {
        final Pattern expression = new ReXPlainDSL(
                exactWordBoundary(
                        literal("p"),
                        oneOrMoreTimes(alphabetic()),
                        literal("p")
                )
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "\\bp(?:[A-Za-z])+p\\b");
    }

}
