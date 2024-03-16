package co.uk.pbnj.dashin.dto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

public record DisplayConfig(String days, String times) {

    public static final String GENERAL_SPLITTER = "-";
    public static final String TIME_SPLITTER = ":";

    public boolean shouldDisplay(LocalDateTime dateTimeToCheck){
        return dayMatches(dateTimeToCheck) && timeMatches(dateTimeToCheck);
    }

    private boolean dayMatches(LocalDateTime dateTimeToCheck){
        String[] days = this.days.split(GENERAL_SPLITTER);
        int currentDayOfWeek = dateTimeToCheck.getDayOfWeek().getValue();
        int startDay = DayOfWeek.valueOf(days[0].toUpperCase().trim()).getValue();

        if(days.length == 2) {
            int endDay = DayOfWeek.valueOf(days[1].toUpperCase().trim()).getValue();
            return currentDayOfWeek >= startDay && currentDayOfWeek <= endDay;
        } else {
            return currentDayOfWeek == startDay;
        }
    }

    private boolean timeMatches(LocalDateTime dateTimeToCheck){
        String[] times = this.times.split(GENERAL_SPLITTER);
        int hourToCheck = dateTimeToCheck.getHour();
        int minuteToCheck = dateTimeToCheck.getMinute();

        if(times.length == 2) {
            String startTime = times[0];
            String endTime = times[1];
            int startHour = Integer.parseInt(StringUtils.substringBefore(startTime, TIME_SPLITTER).trim());
            int startMinute = Integer.parseInt(StringUtils.substringAfter(startTime, TIME_SPLITTER).trim());
            int endHour = Integer.parseInt(StringUtils.substringBefore(endTime, TIME_SPLITTER).trim());
            int endMinute = Integer.parseInt(StringUtils.substringAfter(endTime, TIME_SPLITTER).trim());

            return (startHour <= hourToCheck && endHour >= hourToCheck)
                    && (startMinute <= minuteToCheck && endMinute >= minuteToCheck);

        }

        return false;
    }

}
