
import java.time.DayOfWeek;

public class OpeningHours {

    private DayOfWeek day;

    private String startTime;

    private String endTime;

    private boolean isOpen;

    public OpeningHours(DayOfWeek day, String startTime, String endTime, boolean isOpen) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isOpen = isOpen;
    }
}
