package service.impl;


import model.OpeningHours;
import service.BusinessHourCalculator;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class BusinessHourCalculatorImpl implements BusinessHourCalculator {

    private LocalTime defaultOpeningTime;
    private LocalTime defaultClosingTime;
    private Map<DayOfWeek, OpeningHours> dayOfWeekOpeningHours = new HashMap<>();
    private Map<String, OpeningHours> dateOpeningHours = new HashMap<>();
    private static final DateTimeFormatter EXPECTED_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd H:m");
    private static final DateTimeFormatter RETURN_DATE_FORMAT = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:m");

    public BusinessHourCalculatorImpl(String defaultOpeningTime, String defaultClosingTime) {
        this.defaultOpeningTime = LocalTime.parse(defaultOpeningTime);
        this.defaultClosingTime = LocalTime.parse(defaultClosingTime);
        createOpeningHoursForDaysOfWeek();
    }

    private void createOpeningHoursForDaysOfWeek(){
        Arrays.stream(DayOfWeek.values()).forEach(day -> {
            OpeningHours openingHours = new OpeningHours(defaultOpeningTime, defaultClosingTime, true);
            dayOfWeekOpeningHours.put(day, openingHours);
        });
    }

    @Override
    public void setOpeningHours(DayOfWeek day, String openTime, String closeTime) {
        OpeningHours openingHours = dayOfWeekOpeningHours.get(day);
        openingHours.setOpenTime(LocalTime.parse(openTime, TIME_FORMAT));
        openingHours.setCloseTime(LocalTime.parse(closeTime, TIME_FORMAT));
    }

    @Override
    public void setOpeningHours(String date, String openTime, String closeTime) {
        OpeningHours openingHours = new OpeningHours(LocalTime.parse(openTime, TIME_FORMAT), LocalTime.parse(closeTime, TIME_FORMAT), true);
        dateOpeningHours.put(date, openingHours);
    }

    @Override
    public void setClosed(DayOfWeek... days) {
        for (DayOfWeek day: days) {
            OpeningHours openingHours = dayOfWeekOpeningHours.get(day);
            openingHours.setOpen(false);
        }
    }

    @Override
    public void setClosed(String... dates) {
        for(String date: dates) {
            OpeningHours openingHours = dateOpeningHours.containsKey(date) ? dateOpeningHours.get(date) : new OpeningHours(defaultOpeningTime, defaultClosingTime, false);
            if (dateOpeningHours.containsKey(date)) openingHours.setOpen(false);
            dateOpeningHours.putIfAbsent(date, openingHours);
        }
    }

    @Override
    public String calculateDeadline(int dryCleanTime, String startTimeString){
        LocalDateTime startTime = LocalDateTime.parse(startTimeString, EXPECTED_FORMAT);
        LocalDate date = startTime.toLocalDate();
        DayOfWeek day = startTime.getDayOfWeek();

        OpeningHours openingHours = dateOpeningHours.containsKey(date.toString()) ? dateOpeningHours.get(date.toString()) : dayOfWeekOpeningHours.get(day);

        if (startTime.toLocalTime().isBefore(openingHours.getOpenTime())) {
            startTime = date.atTime(openingHours.getOpenTime());
        }

        if (startTime.toLocalTime().plusSeconds(dryCleanTime).isAfter(openingHours.getCloseTime())) {
            Duration cleanBeforeClosing = Duration.between(startTime.toLocalTime(), openingHours.getCloseTime());
            Duration cleanAfterClosing = Duration.ofSeconds(dryCleanTime).minusSeconds(cleanBeforeClosing.toSeconds());
            LocalDate nextOpenDay = getNextOpenDay(date);
            OpeningHours nextDayOpeningHours = dayOfWeekOpeningHours.get(nextOpenDay.getDayOfWeek());
            LocalTime finishedCleanTime = nextDayOpeningHours.getOpenTime().plusSeconds(cleanAfterClosing.toSeconds());
            LocalDateTime finishedCleanDateTime = nextOpenDay.atTime(finishedCleanTime);
            return finishedCleanDateTime.format(RETURN_DATE_FORMAT);
        }

        return startTime.plusSeconds(dryCleanTime).format(RETURN_DATE_FORMAT);
    }


    private LocalDate getNextOpenDay(LocalDate date) {
        int i = 1;

        while(!isOpenOnDay(date.plusDays(i))) {
            i++;
        }

        return date.plusDays(i);
    }

    private boolean isOpenOnDay(LocalDate date) {
        if (dateOpeningHours.containsKey(date.toString())) {
            return dateOpeningHours.get(date.toString()).isOpen();
        }
        return dayOfWeekOpeningHours.get(date.getDayOfWeek()).isOpen();
    }
}
