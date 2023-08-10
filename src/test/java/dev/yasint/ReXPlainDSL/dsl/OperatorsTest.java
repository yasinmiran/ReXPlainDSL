package dev.yasint.ReXPlainDSL.dsl;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.RegexSynth;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.*;
import static dev.yasint.RexPlainDSL.dsl.Operators.concat;
import static dev.yasint.RexPlainDSL.dsl.Operators.either;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class OperatorsTest {

    @Test
    public void itShouldCreateAlternationBetweenMultipleExpressions() {
        Pattern pattern = new RegexSynth(
                either(digit(), uppercase(), lowercase())
        ).compile().getPattern();
        assertEquals(pattern.pattern(), "(?:[0-9]|[A-Z]|[a-z])");
    }

    @Test
    public void itShouldCreateAlternationBetweenMultipleStrings() {
        Pattern pattern = new RegexSynth(
                either("http", "https", "ws", "wss")
        ).compile().getPattern();
        assertEquals(pattern.pattern(), "(?:https?|wss?)");
    }

    @Test
    public void itShouldConcatMultipleExpressionsIntoOne() {
        Pattern pattern = new RegexSynth(
                concat(digit(), punctuation())
        ).compile().getPattern();
        assertEquals(pattern.pattern(), "[0-9][!-\\/:-@[-\\`{-~]");
    }

}
