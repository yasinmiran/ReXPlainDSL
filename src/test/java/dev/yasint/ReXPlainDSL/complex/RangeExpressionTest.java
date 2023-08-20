package dev.yasint.ReXPlainDSL.complex;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.RegexSynth;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.Numeric.integerRange;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class RangeExpressionTest {

    @Test
    public void itShouldReturnExpectedRange() {
        int start = 65555, end = 78000;
        Pattern expression = new RegexSynth(
                integerRange(start, end)
        ).compile().patternInstance();
        for (int i = start; i <= end; i++) {
            assertTrue(expression.matches(String.valueOf(i)));
        }
    }

}
