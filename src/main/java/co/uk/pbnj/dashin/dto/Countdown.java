package co.uk.pbnj.dashin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Countdown {
    private String name;
    private String description;
    private long daysUntil;
    private long hoursUntil;
    private long minutesUntil;
    private long secondsUntil;
}
