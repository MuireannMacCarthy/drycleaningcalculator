import service.BusinessHourCalculator;
import service.impl.BusinessHourCalculatorImpl;

import java.time.DayOfWeek;

public class Main {


    public static void main(String[] args) {
        BusinessHourCalculator businessHourCalculator = new BusinessHourCalculatorImpl("09:00", "15:00");
        businessHourCalculator.setOpeningHours(DayOfWeek.FRIDAY, "10:00", "17:00");
        businessHourCalculator.setOpeningHours("2010-12-24", "8:00", "13:00");
        businessHourCalculator.setClosed(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
        businessHourCalculator.setClosed("2010-12-25");

        System.out.println(businessHourCalculator.calculateDeadline(2*60*60, "2010-06-07 9:10"));
        System.out.println(businessHourCalculator.calculateDeadline(15*60, "2010-06-08 14:48"));
        System.out.println(businessHourCalculator.calculateDeadline(7*60*60, "2010-12-24 06:45"));
    }
}
