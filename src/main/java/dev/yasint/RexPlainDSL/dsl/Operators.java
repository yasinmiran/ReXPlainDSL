package dev.yasint.RexPlainDSL.dsl;

import dev.yasint.RexPlainDSL.api.Expression;
import dev.yasint.RexPlainDSL.complex.TrieExpression;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.yasint.RexPlainDSL.api.MetaCharacters.ALTERNATION;
import static dev.yasint.RexPlainDSL.dsl.Groups.nonCaptureGroup;

public final class Operators {

    /**
     * Creates an alternation between the passed expressions.
     * <code>[A-Z]|[a-z]|[0-9]</code>
     *
     * @param expressions alternations
     * @return wrapped alternated expressions
     */
    public static Expression either(final Expression... expressions) {
        final String alternations = Arrays.stream(Objects.requireNonNull(expressions))
                .map(Expression::toRegex)
                .collect(Collectors.joining(ALTERNATION));
        return nonCaptureGroup(() -> new StringBuilder(alternations));
    }

    /**
     * Creates an alternation between multiple strings.
     * <code>{January,February,March} = (?:Jan|Febr)uary|March)</code>
     *
     * @param strings alternation strings
     * @return wrapped alternated strings
     */
    public static Expression eitherStr(final String... strings) {
        return eitherStrSet(new HashSet<>(Arrays.asList(strings)));
    }

    /**
     * Creates an alternation between multiple strings.
     * <code>{January,February,March} = (?:Jan|Febr)uary|March)</code>
     *
     * @param strings alternation strings
     * @return wrapped alternated strings
     */
    public static Expression eitherStrSet(final Set<String> strings) {
        final TrieExpression trie = new TrieExpression();
        trie.insertAll(strings);
        return trie;
    }

    /**
     * Creates a concatenation of two given regular expressions. Note:
     * it simply just append the second expression. (a followed by b)
     *
     * @param a expression a
     * @param b expression b
     * @return concatenated expression.
     */
    public static Expression concat(final Expression a, final Expression b) {
        return () -> Objects.requireNonNull(a).toRegex()
                .append(Objects.requireNonNull(b).toRegex());
    }

    /**
     * Concatenates multiple expressions at once. Expressions
     * will combine in absolute order.
     *
     * @param expressions multiple expressions
     * @return concatenated expressions.
     */
    public static Expression concatMultiple(final Expression... expressions) {
        return () -> Arrays.stream(Objects.requireNonNull(expressions))
                .map(Expression::toRegex)
                .reduce(new StringBuilder(), StringBuilder::append);
    }

}
