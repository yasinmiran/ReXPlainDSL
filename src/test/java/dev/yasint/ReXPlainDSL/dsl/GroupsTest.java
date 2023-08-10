package dev.yasint.ReXPlainDSL.dsl;

import com.google.re2j.Pattern;
import dev.yasint.RexPlainDSL.api.RegexSynth;
import dev.yasint.RexPlainDSL.exceptions.InvalidGroupNameException;
import org.junit.jupiter.api.Test;

import static dev.yasint.RexPlainDSL.dsl.CharClasses.Posix.*;
import static dev.yasint.RexPlainDSL.dsl.CharClasses.union;
import static dev.yasint.RexPlainDSL.dsl.Groups.namedCaptureGroup;
import static dev.yasint.RexPlainDSL.dsl.Groups.nonCaptureGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class GroupsTest {

    @Test
    public void itShouldCreateANonCapturingGroup() {
        final Pattern pattern = new RegexSynth(
                nonCaptureGroup(digit())
        ).compile().getPattern();
        assertEquals(pattern.pattern(), "(?:[0-9])");
    }

    @Test
    public void itShouldCreateACapturingGroup() {
        final Pattern pattern = new RegexSynth(
                nonCaptureGroup(union(digit(), punctuation()))
        ).compile().getPattern();
        assertEquals(pattern.pattern(), "(?:[!-@[-\\`{-~])");
    }

    @Test
    public void itShouldCreateANamedCaptureGroup() {
        final Pattern pattern = new RegexSynth(
                namedCaptureGroup("someName", union(word(), punctuation()))
        ).compile().getPattern();
        assertEquals(pattern.pattern(), "(?P<someName>[!-~])");
    }

    @Test()
    public void itShouldThrowAnExceptionIfTheNamedCaptureGroupNameIsInvalid() {
        assertThrows(
                InvalidGroupNameException.class,
                () -> namedCaptureGroup(
                        "- 902 someName",
                        union(word(), punctuation())
                ).toRegex()
        );
    }

}
