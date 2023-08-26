package dev.yasint.ReXPlainDSL.dsl;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.ReXPlainDSL;
import dev.yasint.RexPlainDSL.dsl.Operators;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.*;
import static dev.yasint.RexPlainDSL.dsl.Operators.concat;
import static dev.yasint.RexPlainDSL.dsl.Operators.either;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class OperatorsTest {

    @Test
    public void itShouldCreateAlternationBetweenMultipleExpressions() {
        Pattern pattern = new ReXPlainDSL(
                either(digit(), uppercase(), lowercase())
        ).compile().patternInstance();
        assertEquals(pattern.pattern(), "(?:[0-9]|[A-Z]|[a-z])");
    }

    @Test
    public void itShouldCreateAlternationBetweenMultipleStrings() {
        Pattern pattern = new ReXPlainDSL(
                Operators.eitherStr("http", "https", "ws", "wss")
        ).compile().patternInstance();
        assertEquals(pattern.pattern(), "(?:https?|wss?)");
    }

    @Test
    public void itShouldConcatMultipleExpressionsIntoOne() {
        Pattern pattern = new ReXPlainDSL(
                concat(digit(), punctuation())
        ).compile().patternInstance();
        assertEquals(pattern.pattern(), "[0-9][!-\\/:-@[-\\`{-~]");
    }

}
