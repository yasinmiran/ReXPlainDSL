package dev.yasint.ReXPlainDSL.complex;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.ReXPlainDSL;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.Numeric.integerRange;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class RangeExpressionTest {

    @Test
    public void itShouldReturnExpectedRange() {
        int start = 0, end = 65535;
        Pattern expression = new ReXPlainDSL(
                integerRange(start, end)
        ).compile().patternInstance();
        for (int i = start; i <= end; i++) {
            assertTrue(expression.matches(String.valueOf(i)));
        }
        System.out.println(expression.pattern());
    }

}
