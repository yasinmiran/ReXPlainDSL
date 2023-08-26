package dev.yasint.ReXPlainDSL.performance;

import org.junit.jupiter.api.Test;

public class VanillaPatternGenPerfTest {

    private static final int ITERATIONS = 100_000;

    @Test
    public void benchmark() {
        benchmarkVanillaPatternMatchTime(
                generateDateMatchingPattern(),
                "Date Matching"
        );
        benchmarkVanillaPatternMatchTime(
                generateStringMatchingPattern(),
                "String Matching"
        );
        benchmarkVanillaPatternMatchTime(
                generateEmailMatchingPattern(),
                "Email Matching"
        );
        benchmarkVanillaPatternMatchTime(
                generateNumberRangeMatchingPattern(),
                "Number Range Matching"
        );
    }

    public void benchmarkVanillaPatternMatchTime(String regex, String label) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            java.util.regex.Pattern.compile(regex);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Vanilla: Generation Time: " + label + ":\t" + (endTime - startTime) + "ms");
    }

    private static String generateDateMatchingPattern() {
        return "^(?:201[2-9]|202[0-3])-(Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?)-(0[1-9]|[12]\\d|3[01])$";
    }

    private static String generateStringMatchingPattern() {
        return "Apple|Application|Appeal|Contour|Camera|Connotation";
    }

    private static String generateEmailMatchingPattern() {
        return "^((?:[!#-\\'*+\\-\\/-9=?A-Z\\^_a-~])+(?:(?:\\.(?:[!#-\\'*+\\-\\/-9=?A-Z\\^_a-~])+))*)@((?:(?:[0-9A-Za-z](?:(?:(?:[\\-0-9A-Za-z])*[0-9A-Za-z]))?\\.))+)([0-9A-Za-z](?:(?:(?:[\\-0-9A-Za-z])*[0-9A-Za-z]))?)$";
    }

    private static String generateNumberRangeMatchingPattern() {
        return "(?:[0-9]|[1-9][0-9]{1,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
    }

}
