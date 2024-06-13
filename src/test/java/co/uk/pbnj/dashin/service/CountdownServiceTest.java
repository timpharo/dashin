package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.config.CountdownAppConfig;
import co.uk.pbnj.dashin.dto.Countdown;
import co.uk.pbnj.dashin.dto.CountdownConfig;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

class CountdownServiceTest {
    private static final String ITEM_1_KEY = "item1";
    private static final String ITEM_2_KEY = "item2";
    private static final String ITEM_3_KEY = "item3";
    private static final LocalDateTime BASE_DATE_TIME = LocalDateTime.of(2024, 1, 1, 1, 1, 1);
    private static final Clock CLOCK = Clock.fixed(BASE_DATE_TIME.toInstant(UTC), UTC);

    @Test
    void returnCorrectRepresentationsWhenNoneConfigured() {
        CountdownAppConfig config = new CountdownAppConfig(Map.of());
        CountdownService subject = new CountdownService(CLOCK, config);

        Optional<Countdown> result = subject.getCountdownRepresentation(ITEM_1_KEY);

        assertThat(result).isEmpty();
    }

    @Test
    void returnCorrectRepresentation() {
        CountdownConfig item1 = CountdownConfig.builder()
                .description("Item 1")
                .date(BASE_DATE_TIME.plusDays(1).plusHours(2).plusMinutes(3).plusSeconds(4))
                .build();
        CountdownConfig item2 = CountdownConfig.builder()
                .description("Item 2")
                .date(BASE_DATE_TIME.plusDays(5).plusHours(6).plusMinutes(7).plusSeconds(8))
                .build();
        CountdownAppConfig config = CountdownAppConfig.builder()
                .countdownConfig(Map.of(ITEM_1_KEY, item1, ITEM_2_KEY, item2))
                .build();

        CountdownService subject = new CountdownService(CLOCK, config);

        Optional<Countdown> result = subject.getCountdownRepresentation(ITEM_1_KEY);

        assertThat(result).contains(
                Countdown.builder()
                        .name(ITEM_1_KEY)
                        .description(item1.getDescription())
                        .daysUntil(1).hoursUntil(2).minutesUntil(3).secondsUntil(4)
                        .build());
    }

    @Test
    void returnCorrectRepresentationsInPast() {
        CountdownConfig item1 = CountdownConfig.builder()
                .description("Item 1")
                .date(BASE_DATE_TIME.minusDays(1).minusHours(2).minusMinutes(3).minusSeconds(4))
                .build();
        CountdownConfig item2 = CountdownConfig.builder()
                .description("Item 2")
                .date(BASE_DATE_TIME.minusDays(5).minusHours(6).minusMinutes(7).minusSeconds(8))
                .build();
        CountdownAppConfig config = CountdownAppConfig.builder()
                .countdownConfig(Map.of(ITEM_1_KEY, item1, ITEM_2_KEY, item2))
                .build();

        CountdownService subject = new CountdownService(CLOCK, config);

        Optional<Countdown> result = subject.getCountdownRepresentation(ITEM_2_KEY);

        assertThat(result).contains(
                Countdown.builder()
                        .name(ITEM_2_KEY)
                        .description(item2.getDescription())
                        .daysUntil(0).hoursUntil(0).minutesUntil(0).secondsUntil(0)
                        .build()
        );
    }

    @Test
    void returnCorrectListOfItemsByItemChronologyAscending() {
        CountdownConfig item1 = CountdownConfig.builder()
                .description("Item 1")
                .date(BASE_DATE_TIME.plusDays(2).plusHours(2).plusMinutes(2).plusSeconds(2))
                .build();
        CountdownConfig item2 = CountdownConfig.builder()
                .description("Item 2")
                .date(BASE_DATE_TIME.plusDays(2).plusHours(2).plusMinutes(2).plusSeconds(3))
                .build();
        CountdownConfig item3 = CountdownConfig.builder()
                .description("Item 3")
                .date(BASE_DATE_TIME.plusDays(1).plusHours(1).plusMinutes(1).plusSeconds(1))
                .build();
        CountdownAppConfig config = CountdownAppConfig.builder()
                .countdownConfig(Map.of(
                        ITEM_1_KEY, item1,
                        ITEM_2_KEY, item2,
                        ITEM_3_KEY, item3
                )).build();

        CountdownService subject = new CountdownService(CLOCK, config);

        List<String> results = subject.getCountdownItems();

        assertThat(results).containsExactly(ITEM_3_KEY, ITEM_1_KEY, ITEM_2_KEY);
    }

}