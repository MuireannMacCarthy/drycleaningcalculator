package service;

import java.time.DayOfWeek;

public interface BusinessHourCalculator {

    void setOpeningHours(DayOfWeek day, String openTime, String closeTime);

    void setOpeningHours(String day, String openTime, String closeTime);

    void setClosed(DayOfWeek... day);

    void setClosed(String... day);

    String calculateDeadline(int dryCleanTime, String startTimeString);

}
