package dev.yasint.ReXPlainDSL.complex;

import dev.yasint.RexPlainDSL.complex.TrieExpression;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TrieExpressionTest {

    @Test
    public void shouldMinimizeString() {

        TrieExpression trieExpression = new TrieExpression();

        trieExpression.insert("Apple");
        trieExpression.insert("Application");
        trieExpression.insert("Appeal");
        trieExpression.insert("Contour");
        trieExpression.insert("Camera");
        trieExpression.insert("Connotation");

        assertEquals(
                "(?:App(?:eal|l(?:ication|e))|C(?:amera|on(?:notation|tour)))",
                trieExpression.toRegex().toString()
        );
    }

}
