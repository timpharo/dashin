package co.uk.pbnj.dashin.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.time.DayOfWeek.*;
import static java.time.LocalDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;

class DisplayConfigTest {

    private static final LocalDateTime SUN_SOD = parse("2023-01-01T00:00");
    private static final LocalDateTime MON_SOD = SUN_SOD.plusDays(1);
    private static final LocalDateTime TUE_SOD = SUN_SOD.plusDays(2);
    private static final LocalDateTime WED_SOD = SUN_SOD.plusDays(3);
    private static final LocalDateTime THU_SOD = SUN_SOD.plusDays(4);
    private static final LocalDateTime FRI_SOD = SUN_SOD.plusDays(5);
    private static final LocalDateTime SAT_SOD = SUN_SOD.plusDays(6);
    private static final String MON_TO_FRI = "Monday - Friday";
    private static final String ALL_TIMES = "00:00 - 23:59";
    private static final int MIDDAY = 12;

    @ParameterizedTest
    @MethodSource(value = {
            "daysRange",
            "days",
            "timesBorderTests",
            "daysAndTimesBorderTests"
    })
    void shouldReturnTrue(LocalDateTime date, String configDays, String configTimes, boolean expectedResult){
        DisplayConfig subject = new DisplayConfig(configDays, configTimes);

        boolean result = subject.shouldDisplay(date);

        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> daysRange(){
        return Stream.of(
                Arguments.of(SUN_SOD.withHour(MIDDAY), MON_TO_FRI, ALL_TIMES, false),
                Arguments.of(MON_SOD.withHour(MIDDAY), MON_TO_FRI, ALL_TIMES, true),
                Arguments.of(TUE_SOD.withHour(MIDDAY), MON_TO_FRI, ALL_TIMES, true),
                Arguments.of(WED_SOD.withHour(MIDDAY), MON_TO_FRI, ALL_TIMES, true),
                Arguments.of(THU_SOD.withHour(MIDDAY), MON_TO_FRI, ALL_TIMES, true),
                Arguments.of(FRI_SOD.withHour(MIDDAY), MON_TO_FRI, ALL_TIMES, true),
                Arguments.of(SAT_SOD.withHour(MIDDAY), MON_TO_FRI, ALL_TIMES, false)
        );
    }

    private static Stream<Arguments> days(){
        return Stream.of(
                Arguments.of(SUN_SOD.withHour(MIDDAY), SUNDAY.toString(), ALL_TIMES, true),
                Arguments.of(MON_SOD.withHour(MIDDAY), MONDAY.toString(), ALL_TIMES, true),
                Arguments.of(TUE_SOD.withHour(MIDDAY), TUESDAY.toString(), ALL_TIMES, true),
                Arguments.of(WED_SOD.withHour(MIDDAY), WEDNESDAY.toString(), ALL_TIMES, true),
                Arguments.of(THU_SOD.withHour(MIDDAY), THURSDAY.toString(), ALL_TIMES, true),
                Arguments.of(FRI_SOD.withHour(MIDDAY), FRIDAY.toString(), ALL_TIMES, true),
                Arguments.of(SAT_SOD.withHour(MIDDAY), SATURDAY.toString(), ALL_TIMES, true),

                Arguments.of(SUN_SOD.withHour(MIDDAY), MONDAY.toString(), ALL_TIMES, false),
                Arguments.of(MON_SOD.withHour(MIDDAY), TUESDAY.toString(), ALL_TIMES, false),
                Arguments.of(TUE_SOD.withHour(MIDDAY), WEDNESDAY.toString(), ALL_TIMES, false),
                Arguments.of(WED_SOD.withHour(MIDDAY), THURSDAY.toString(), ALL_TIMES, false),
                Arguments.of(THU_SOD.withHour(MIDDAY), FRIDAY.toString(), ALL_TIMES, false),
                Arguments.of(FRI_SOD.withHour(MIDDAY), SATURDAY.toString(), ALL_TIMES, false),
                Arguments.of(SAT_SOD.withHour(MIDDAY), SUNDAY.toString(), ALL_TIMES, false)
        );
    }

    private static Stream<Arguments> timesBorderTests(){
        return Stream.of(
                Arguments.of(SUN_SOD.withHour(MIDDAY), SUNDAY.toString(), "00:00 - 11:59", false),
                Arguments.of(SUN_SOD.withHour(MIDDAY), SUNDAY.toString(), "00:00 - 12:00", true),
                Arguments.of(SUN_SOD.withHour(MIDDAY), SUNDAY.toString(), "00:00 - 12:01", true),

                Arguments.of(SUN_SOD.withHour(MIDDAY).minusMinutes(1), SUNDAY.toString(), "11:58 - 23:59", true),
                Arguments.of(SUN_SOD.withHour(MIDDAY).minusMinutes(1), SUNDAY.toString(), "11:59 - 23:59", true),
                Arguments.of(SUN_SOD.withHour(MIDDAY).minusMinutes(1), SUNDAY.toString(), "12:00 - 23:59", false)
        );
    }

    private static Stream<Arguments> daysAndTimesBorderTests(){
        return Stream.of(
                Arguments.of(parse("2022-12-31T23:59"), "Sunday", ALL_TIMES, false),
                Arguments.of(parse("2023-01-01T00:00"), "Sunday", ALL_TIMES, true),
                Arguments.of(parse("2023-01-02T00:00"), "Sunday", ALL_TIMES, false)
        );
    }

}