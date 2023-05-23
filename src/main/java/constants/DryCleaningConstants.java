package constants;

import java.time.format.DateTimeFormatter;

public class DryCleaningConstants {
    public static final DateTimeFormatter EXPECTED_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m");
    public static final DateTimeFormatter RETURN_DATE_FORMAT = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:m");
}
