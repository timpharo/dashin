package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.dto.CountdownConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "countdown.countdownConfig.item1.description=Item 1",
                "countdown.countdownConfig.item1.date=2024-11-11",
                "countdown.countdownConfig.item2.description=Item 2",
                "countdown.countdownConfig.item2.date=2025-12-12",
        })
@ActiveProfiles("integration")
class CountdownAppConfigIT {

    @Autowired
    public CountdownAppConfig subject;

    @Test
    void parsesConfigCorrectly() {
        assertThat(subject.getCountdownConfig()).containsExactly(
                Map.entry("item1", CountdownConfig.builder()
                        .description("Item 1")
                        .date(LocalDate.of(2024, 11, 11))
                        .build()),
                Map.entry("item2", CountdownConfig.builder()
                        .description("Item 2")
                        .date(LocalDate.of(2025, 12, 12))
                        .build())
        );
    }

}