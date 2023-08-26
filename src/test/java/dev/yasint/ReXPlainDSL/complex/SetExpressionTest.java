package dev.yasint.ReXPlainDSL.complex;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.Expression;
import dev.yasint.RexPlainDSL.api.ReXPlainDSL;
import dev.yasint.RexPlainDSL.unicode.UnicodeScript;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.CharClasses.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class SetExpressionTest {

    @Test
    public void itShouldCreateANonNegatedCharacterClass() {
        final Expression simpleSet = simpleSetStr("A", "B", "D", "E", "C");
        assertEquals(simpleSet.toRegex().toString(), "[A-E]");
    }

    @Test
    public void itShouldCreateANegatedCharacterClass() {
        final Expression simpleSet = negated(simpleSetStr("a", "b", "c", "d", "Z"));
        assertEquals(simpleSet.toRegex().toString(), "[^Za-d]");
    }

    @Test
    public void itShouldCreateASimpleCharacterClassWithoutRanges() {
        final Expression simpleSet = simpleSetStr("a", "d", "f", "h", "Z");
        assertEquals(simpleSet.toRegex().toString(), "[Zadfh]");
    }

    @Test
    public void itShouldDoASetUnionOperationOnTwoSets() {
        final Expression set = union(
                rangedSetStr("A", "Z"),
                simpleSetStr("a", "d", "f", "h", "Z")
        );
        assertEquals(set.toRegex().toString(), "[A-Zadfh]");
    }

    @Test
    public void itShouldDoASetIntersectionOperationOnTwoSets() {
        assertEquals(
                intersection(
                        union(rangedSetStr("A", "Z"), rangedSetStr("a", "z")),
                        simpleSetStr("d", "e", "f")
                ).toRegex().toString(),
                "[d-f]"
        );
    }

    @Test
    public void itShouldDoADifferenceOperationOnTwoSets() {
        final Expression setA = union(rangedSetStr("A", "Z"), rangedSetStr("a", "z"));
        final Expression setB = union(rangedSetStr("M", "P"), rangedSetStr("m", "p"));
        assertEquals(difference(setA, setB).toRegex().toString(), "[A-LQ-Za-lq-z]");
    }

    @Test
    public void itShouldDoASetUnionOperationOnInlineRegex() {
        Pattern expression = new ReXPlainDSL(
                union(rangedSetStr("1", "3"), rangedSetStr("4", "6"))
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "[1-6]");
    }

    @Test
    public void itShouldDoASetIntersectionOperationOnInlineRegex() {
        Pattern expression = new ReXPlainDSL(
                intersection(rangedSetStr("1", "3"), rangedSetStr("4", "6"))
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "");
    }

    @Test
    public void itShouldDoASetDifferenceOperationOnInlineRegex() {
        Pattern expression = new ReXPlainDSL(
                difference(rangedSetStr("1", "3"), simpleSetStr("2", "4", "5", "6"))
        ).compile().patternInstance();
        assertEquals(expression.pattern(), "[13]");
    }

    @Test
    public void itShouldAppendANonNegatedUnicodeClassesToASetExpression() {
        final Pattern expression = new ReXPlainDSL(
                includeUnicodeScript(simpleSetStr("-", "."), UnicodeScript.SINHALA, false)
        ).compile().patternInstance();
        System.out.println(expression.pattern());
        assertEquals(expression.pattern(), "[\\-.\\p{Sinhala}]");
    }

    @Test
    public void itShouldAppendANegatedUnicodeClassesToASetExpression() {
        final Pattern expression = new ReXPlainDSL(
                includeUnicodeScript(simpleSetStr("-", "."), UnicodeScript.SINHALA, true)
        ).compile().patternInstance();
        System.out.println(expression.pattern());
        assertEquals(expression.pattern(), "[\\-.\\P{Sinhala}]");
    }

}
