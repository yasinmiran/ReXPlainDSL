package dev.yasint.ReXPlainDSL.util;

import dev.yasint.RexPlainDSL.exceptions.InvalidGroupNameException;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.util.Common.asRegexGroupName;
import static dev.yasint.RexPlainDSL.util.Common.asRegexLiteral;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CommonTest {

    @Test
    public void itShouldEscapeAllSpecialConstructs() {
        assertEquals(asRegexLiteral("<"), "\\<");
        assertEquals(asRegexLiteral("("), "\\(");
        assertEquals(asRegexLiteral("["), "\\[");
        assertEquals(asRegexLiteral("{"), "\\{");
        assertEquals(asRegexLiteral("\\"), "\\\\");
        assertEquals(asRegexLiteral("^"), "\\^");
        assertEquals(asRegexLiteral("-"), "\\-");
        assertEquals(asRegexLiteral("="), "\\=");
        assertEquals(asRegexLiteral("$"), "\\$");
        assertEquals(asRegexLiteral("!"), "\\!");
        assertEquals(asRegexLiteral("|"), "\\|");
        assertEquals(asRegexLiteral("]"), "\\]");
        assertEquals(asRegexLiteral("}"), "\\}");
        assertEquals(asRegexLiteral(")"), "\\)");
        assertEquals(asRegexLiteral("?"), "\\?");
        assertEquals(asRegexLiteral("*"), "\\*");
        assertEquals(asRegexLiteral("+"), "\\+");
        assertEquals(asRegexLiteral("."), "\\.");
        assertEquals(asRegexLiteral(">"), "\\>");
        assertEquals(asRegexLiteral("/"), "\\/");
    }

    @Test
    public void itShouldEscapeAllTheSpecialCharsAndEscapeSupplementaryCodepoints() {
        String someString = "Hi, this is developed by // Yasin. 🌚💀|{}";
        assertEquals(
                asRegexLiteral(someString),
                "Hi, this is developed by \\/\\/ Yasin\\. \\x{1f31a}\\x{1f480}\\|\\{\\}"
        );
    }

    @Test
    public void itShouldThrowExceptionsForInvalidGroupNames() {
        assertThrows(
                InvalidGroupNameException.class,
                () -> asRegexGroupName("--wowVeryWrong")
        );
        assertThrows(
                InvalidGroupNameException.class,
                () -> asRegexGroupName("1wowVeryWrong")
        );
        assertThrows(
                InvalidGroupNameException.class,
                () -> asRegexGroupName("+wowVeryWrong")
        );
    }

}
