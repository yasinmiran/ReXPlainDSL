package dev.yasint.ReXPlainDSL.dsl;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.RegexSynth;
import dev.yasint.RexPlainDSL.unicode.UnicodeScript;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.Literals.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class LiteralsTest {

    @Test
    public void itShouldEscapeAllSpecialCharacters() {
        Pattern pattern = new RegexSynth(
                literal("https://swtch.com/~rsc/regexp&id=1")
        ).compile().patternInstance();
        assertEquals(pattern.pattern(), "https:\\/\\/swtch\\.com\\/~rsc\\/regexp&id\\=1");
    }

    @Test
    public void itShouldCreateStrictQuoteString() {
        Pattern pattern = new RegexSynth(
                quotedLiteral("https://swtch.com/~rsc/regexp&id=1")
        ).compile().patternInstance();
        assertEquals(pattern.pattern(), "\\Qhttps://swtch.com/~rsc/regexp&id=1\\E");
    }

    @Test
    public void itShouldCreateANonNegatedUnicodeScriptBlock() {
        Pattern pattern = new RegexSynth(
                unicodeScriptLiteral(UnicodeScript.SINHALA, false)
        ).compile().patternInstance();
        assertEquals(pattern.pattern(), "\\p{Sinhala}");
    }

    @Test
    public void itShouldCreateANegatedUnicodeScriptBlock() {
        Pattern pattern = new RegexSynth(
                unicodeScriptLiteral(UnicodeScript.ARMENIAN, true)
        ).compile().patternInstance();
        assertEquals(pattern.pattern(), "\\P{Armenian}");
    }

}
