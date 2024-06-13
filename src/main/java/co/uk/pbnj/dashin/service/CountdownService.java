package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.config.CountdownAppConfig;
import co.uk.pbnj.dashin.dto.CountdownConfig;
import co.uk.pbnj.dashin.dto.Countdown;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.util.Comparator.comparingLong;

@Service
public class CountdownService {
    private final Clock clock;
    private final CountdownAppConfig countdownConfig;

    public CountdownService(Clock clock, CountdownAppConfig countdownConfig){
        this.clock = clock;
        this.countdownConfig = countdownConfig;
    }

    public Optional<Countdown> getCountdownRepresentation(String name) {
        LocalDateTime now = LocalDateTime.now(clock);

        return Optional.ofNullable(countdownConfig.getCountdownConfig().get(name))
                .map(item -> getCountdownRepresentation(now, name, item));
    }

    public List<String> getCountdownItems() {
        return countdownConfig.getCountdownConfig()
                .entrySet()
                .stream()
                .sorted(comparingLong(e -> e.getValue().getDate().toEpochSecond(ZoneOffset.UTC)))
                .map(Map.Entry::getKey)
                .toList();
    }

    private static Countdown getCountdownRepresentation(LocalDateTime now, String name, CountdownConfig item) {
        LocalDateTime itemDate = item.getDate();
        Duration diff = Duration.between(now, itemDate);
        return Countdown.builder()
                .name(name)
                .description(item.getDescription())
                .daysUntil(minZero(diff.toDaysPart()))
                .hoursUntil(minZero(diff.toHoursPart()))
                .minutesUntil(minZero(diff.toMinutesPart()))
                .secondsUntil(minZero(diff.toSecondsPart()))
                .build();
    }

    private static long minZero(long value) {
        return Math.max(value, 0);
    }
}
