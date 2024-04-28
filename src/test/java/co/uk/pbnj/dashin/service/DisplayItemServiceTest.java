package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.config.TodoDisplayConfig;
import co.uk.pbnj.dashin.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static co.uk.pbnj.dashin.dto.TodoListType.*;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DisplayItemServiceTest {
    private static final String ALL_HOURS = "00:00-23:59";
    private static final String ALL_DAYS = "Monday-Sunday";
    private static final String SUNDAY = "Sunday";
    private static final String MONDAY = "Monday";
    private static final String TUESDAY = "Tuesday";
    private static final String WEDNESDAY = "Wednesday";
    private static final String THURSDAY = "Thursday";
    private static final String FRIDAY = "Friday";
    private static final String SATURDAY = "Saturday";
    private static final int SUNDAY_DAY_OF_MONTH = 1;
    private static final int MONDAY_DAY_OF_MONTH = 2;
    private static final int TUESDAY_DAY_OF_MONTH = 3;
    private static final int WEDNESDAY_DAY_OF_MONTH = 4;
    private static final int THURSDAY_DAY_OF_MONTH = 5;
    private static final int FRIDAY_DAY_OF_MONTH = 6;
    private static final int SATURDAY_DAY_OF_MONTH = 7;
    private static final String TODO_TYPE = "Todo";
    private static final String STOCK_TYPE = "StockEquityCalculation";
    private static final String COUNTDOWN_TYPE = "Countdown";

    @Mock
    private TodoDisplayConfig todoDisplayConfig;

    @Mock
    private CountdownService countdownService;

    @Test
    void displaysAllItemsWhenNoDisplayConfig(){
        Clock clockSunday = Clock.fixed(LocalDateTime.of(2023, 1, 1, 0, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        given(todoDisplayConfig.getDisplayConfig(any())).willReturn(null);
        given(countdownService.getCountdownItems()).willReturn(Set.of("COUNTDOWN_1"));
        DisplayItemService subject = new DisplayItemService(clockSunday, todoDisplayConfig, countdownService);

        List<DisplayItem> results = subject.getDisplayItems();

        assertThat(results).containsExactlyInAnyOrder(
                displayItem("SHOPPING", TODO_TYPE, todoList(SHOPPING)),
                displayItem("PERSONAL", TODO_TYPE, todoList(PERSONAL)),
                displayItem("WORK", TODO_TYPE, todoList(WORK)),
                displayItem("LEARN", TODO_TYPE, todoList(LEARN)),
                displayItem("INBOX", TODO_TYPE, todoList(INBOX)),
                displayItem("STOCK EQUITY", STOCK_TYPE, "/v1/stock-equity"),
                displayItem("COUNTDOWN_1", COUNTDOWN_TYPE, countdown("COUNTDOWN_1"))
        );
    }

    @ParameterizedTest
    @MethodSource("allItemsDisplayedArguments")
    void displaysAllItemsWhenAllItemsConfiguredToDisplay(int dayOfMonth, String days, String hours){
        Clock clockSunday = Clock.fixed(LocalDateTime.of(2023, 1, dayOfMonth, 0, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC);
        given(todoDisplayConfig.getDisplayConfig(any())).willReturn(new DisplayConfig(days, hours));
        given(countdownService.getCountdownItems()).willReturn(Set.of("COUNTDOWN_1"));
        DisplayItemService subject = new DisplayItemService(clockSunday, todoDisplayConfig, countdownService);

        List<DisplayItem> results = subject.getDisplayItems();

        assertThat(results).containsExactlyInAnyOrder(
                displayItem("SHOPPING", TODO_TYPE, todoList(SHOPPING)),
                displayItem("PERSONAL", TODO_TYPE, todoList(PERSONAL)),
                displayItem("WORK", TODO_TYPE, todoList(WORK)),
                displayItem("LEARN", TODO_TYPE, todoList(LEARN)),
                displayItem("INBOX", TODO_TYPE, todoList(INBOX)),
                displayItem("STOCK EQUITY", STOCK_TYPE, "/v1/stock-equity"),
                displayItem("COUNTDOWN_1", COUNTDOWN_TYPE, countdown("COUNTDOWN_1"))
        );
    }

    static Stream<Arguments> allItemsDisplayedArguments(){
        return Stream.of(
                Arguments.of(SUNDAY_DAY_OF_MONTH, SUNDAY, ALL_HOURS),
                Arguments.of(MONDAY_DAY_OF_MONTH, MONDAY, ALL_HOURS),
                Arguments.of(TUESDAY_DAY_OF_MONTH, TUESDAY, ALL_HOURS),
                Arguments.of(WEDNESDAY_DAY_OF_MONTH, WEDNESDAY, ALL_HOURS),
                Arguments.of(THURSDAY_DAY_OF_MONTH, THURSDAY, ALL_HOURS),
                Arguments.of(FRIDAY_DAY_OF_MONTH, FRIDAY, ALL_HOURS),
                Arguments.of(SATURDAY_DAY_OF_MONTH, SATURDAY, ALL_HOURS),

                Arguments.of(SUNDAY_DAY_OF_MONTH, ALL_DAYS, ALL_HOURS),
                Arguments.of(MONDAY_DAY_OF_MONTH, ALL_DAYS, ALL_HOURS),
                Arguments.of(TUESDAY_DAY_OF_MONTH, ALL_DAYS, ALL_HOURS),
                Arguments.of(WEDNESDAY_DAY_OF_MONTH, ALL_DAYS, ALL_HOURS),
                Arguments.of(THURSDAY_DAY_OF_MONTH, ALL_DAYS, ALL_HOURS),
                Arguments.of(FRIDAY_DAY_OF_MONTH, ALL_DAYS, ALL_HOURS),
                Arguments.of(SATURDAY_DAY_OF_MONTH, ALL_DAYS, ALL_HOURS)
        );
    }

    private static DisplayItem displayItem(String name, String type, String location) {
        return DisplayItem.builder()
                .name(name)
                .type(type)
                .location(location)
                .build();
    }

    private static String todoList(TodoListType type) {
        return format("/v1/todo/%s", type.name());
    }

    private static String countdown(String name) {
        return format("/v1/countdown/%s", name);
    }

}