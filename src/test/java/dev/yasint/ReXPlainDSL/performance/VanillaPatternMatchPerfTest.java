package dev.yasint.ReXPlainDSL.performance;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VanillaPatternMatchPerfTest {

    private static final int ITERATIONS = 100_000;

    @Test
    public void benchmark() {
        benchmarkVanillaPatternMatchTime(
                generateDateMatchingPattern(),
                "Date Matching",
                new String[]{
                        "2022-January-03 | 2022-Jan-03",
                        "2022-February-20 | 2022-Feb-20",
                        "2022-March-11 | 2022-Mar-11",
                        "2022-April-27 | 2022-Apr-27",
                        "2022-May-14 | 2022-May-14",
                        "2022-June-08 | 2022-Jun-08",
                        "2022-July-23 | 2022-Jul-23",
                        "2022-August-05 | 2022-Aug-05",
                        "2022-September-18 | 2022-Sep-18",
                        "2022-October-31 | 2022-Oct-31",
                        "2022-November-25 | 2022-Nov-25",
                        "2022-December-12 | 2022-Dec-12",
                        "2023-January-09 | 2023-Jan-09",
                        "2023-February-14 | 2023-Feb-14",
                        "2023-March-21 | 2023-Mar-21",
                        "2023-April-06 | 2023-Apr-06",
                        "2023-May-17 | 2023-May-17",
                        "2023-June-29 | 2023-Jun-29",
                        "2023-July-07 | 2023-Jul-07",
                        "2023-August-25 | 2023-Aug-25",
                        "2023-September-13 | 2023-Sep-13",
                        "2023-October-23 | 2023-Oct-23",
                        "2023-November-19 | 2023-Nov-19",
                        "2023-December-08 | 2023-Dec-08",
                        "2022-January-28 | 2022-Jan-28",
                        "2022-February-09 | 2022-Feb-09",
                        "2022-March-04 | 2022-Mar-04",
                        "2022-April-19 | 2022-Apr-19",
                        "2022-May-30 | 2022-May-30",
                        "2022-June-21 | 2022-Jun-21",
                        "2022-July-16 | 2022-Jul-16",
                        "2022-August-28 | 2022-Aug-28",
                        "2022-September-07 | 2022-Sep-07",
                        "2022-October-12 | 2022-Oct-12",
                        "2022-November-13 | 2022-Nov-13",
                        "2022-December-30 | 2022-Dec-30",
                        "2023-January-19 | 2023-Jan-19",
                        "2023-February-03 | 2023-Feb-03",
                        "2023-March-26 | 2023-Mar-26",
                        "2023-April-08 | 2023-Apr-08",
                        "2023-May-22 | 2023-May-22",
                        "2023-June-15 | 2023-Jun-15",
                        "2023-July-31 | 2023-Jul-31",
                        "2023-August-10 | 2023-Aug-10",
                        "2023-September-28 | 2023-Sep-28",
                        "2023-October-05 | 2023-Oct-05",
                        "2023-November-08 | 2023-Nov-08",
                        "2023-December-22 | 2023-Dec-22",
                        "2022-January-10 | 2022-Jan-10",
                        "2022-February-25 | 2022-Feb-25",
                        "2022-March-14 | 2022-Mar-14",
                        "2022-April-04 | 2022-Apr-04",
                        "2022-May-24 | 2022-May-24",
                        "2022-June-26 | 2022-Jun-26",
                        "2022-July-19 | 2022-Jul-19",
                        "2022-August-11 | 2022-Aug-11",
                        "2022-September-22 | 2022-Sep-22",
                        "2022-October-15 | 2022-Oct-15",
                        "2022-November-10 | 2022-Nov-10",
                        "2022-December-15 | 2022-Dec-15",
                        "2023-January-28 | 2023-Jan-28",
                        "2023-February-08 | 2023-Feb-08",
                        "2023-March-09 | 2023-Mar-09",
                        "2023-April-15 | 2023-Apr-15",
                        "2023-May-03 | 2023-May-03",
                        "2023-June-17 | 2023-Jun-17",
                        "2023-July-23 | 2023-Jul-23",
                        "2023-August-20 | 2023-Aug-20",
                        "2023-September-11 | 2023-Sep-11",
                        "2023-October-19 | 2023-Oct-19",
                        "2023-November-27 | 2023-Nov-27",
                        "2023-December-16 | 2023-Dec-16",
                        "2022-January-13 | 2022-Jan-13",
                        "2022-February-17 | 2022-Feb-17",
                        "2022-March-30 | 2022-Mar-30",
                        "2022-April-13 | 2022-Apr-13",
                        "2022-May-07 | 2022-May-07",
                        "2022-June-03 | 2022-Jun-03",
                        "2022-July-27 | 2022-Jul-27",
                        "2022-August-06 | 2022-Aug-06",
                        "2022-September-20 | 2022-Sep-20",
                        "2022-October-09 | 2022-Oct-09",
                        "2022-November-15 | 2022-Nov-15",
                        "2022-December-17 | 2022-Dec-17",
                        "2023-January-30 | 2023-Jan-30",
                        "2023-February-20 | 2023-Feb-20",
                        "2023-March-12 | 2023-Mar-12",
                }
        );
        benchmarkVanillaPatternMatchTime(
                generateStringMatchingPattern(),
                "String Matching",
                new String[]{
                        "Apple",
                        "Camera",
                        "Connotation",
                        "Application",
                        "Appeal",
                        "Contour",
                }
        );
        benchmarkVanillaPatternMatchTime(
                generateEmailMatchingPattern(),
                "Email Matching",
                new String[]{
                        "user1@example.com",
                        "john.doe@gmail.com",
                        "info@subdomain1.com",
                        "mary.smith@yahoo.com",
                        "contact@randomdomain.net",
                        "jessica.jones@hotmail.com",
                        "support@subdomain2.org",
                        "alexander.brown@email.com",
                        "hello@anotherdomain.org",
                        "samantha.wilson@provider.com",
                        "webmaster@subdomain3.net",
                        "robert.miller@domain.com",
                        "admin@randomdomain2.com",
                        "emily.williams@service.org",
                        "marketing@subdomain4.net",
                        "andrew.johnson@example.net",
                        "help@anotherdomain2.org",
                        "sarah.jones@email.com",
                        "sales@subdomain5.com",
                        "michael.davis@gmail.com",
                        "contact@randomdomain3.net",
                        "christina.thompson@hotmail.com",
                        "support@subdomain6.org",
                        "william.jackson@email.com",
                        "info@yetanotherdomain.com",
                        "laura.white@provider.net",
                        "jason.martinez@domain.org",
                        "webmaster@subdomain7.com",
                        "olivia.anderson@yahoo.com",
                        "admin@randomdomain4.net",
                        "daniel.robinson@service.org",
                        "elizabeth.harris@example.com",
                        "marketing@subdomain8.email",
                        "matthew.perez@anotherdomain.net",
                        "ashley.lewis@email.com",
                        "help@subdomain9.org",
                        "james.thompson@gmail.com",
                        "lauren.green@randomdomain5.com",
                        "support@subdomain10.net",
                        "benjamin.evans@provider.org",
                        "anna.morris@domain.net",
                        "webmaster@subdomain11.com",
                        "christopher.brown@yahoo.com",
                        "emma.rodriguez@email.com",
                        "info@randomdomain6.org",
                        "alexandra.cooper@service.net",
                        "jacob.murphy@example.org",
                        "marketing@subdomain12.com",
                        "grace.cook@anotherdomain2.email",
                        "nathan.bell@subdomain13.net",
                        "oliver.howard@gmail.com",
                        "admin@randomdomain7.com",
                        "mia.ward@provider.org",
                        "support@subdomain14.net",
                        "william.morris@email.com",
                        "sophia.roberts@domain3.com",
                }
        );
        benchmarkVanillaPatternMatchTime(
                generateNumberRangeMatchingPattern(),
                "Number Range Matching",
                new String[]{
                        "2345",
                        "4189",
                        "5967",
                        "1024",
                        "8080",
                        "5432",
                        "9999",
                        "3333",
                        "65000",
                        "80",
                        "161",
                        "443",
                        "7000",
                        "49152",
                        "15000",
                        "2222",
                        "9090",
                        "49153",
                        "8000",
                        "2525",
                }
        );
    }

    public void benchmarkVanillaPatternMatchTime(String regex, String label, String[] inputs) {
        Pattern pattern = java.util.regex.Pattern.compile(regex);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            for (String input : inputs) {
                Matcher matcher = pattern.matcher(input);
                matcher.matches(); // Perform matching
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Vanilla: Match Time: " + label + ":\t" + (endTime - startTime) + "ms");
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


