package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.ActivateIntegrationProfile;
import co.uk.pbnj.dashin.dto.CountdownConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT,
        properties = {
                "countdown.countdownConfig.item1.description=Item 1",
                "countdown.countdownConfig.item1.date=2024-11-11T01:02:03",
                "countdown.countdownConfig.item2.description=Item 2",
                "countdown.countdownConfig.item2.date=2025-12-12T10:11:12",
        })
@ActivateIntegrationProfile
class CountdownAppConfigIT {

    @Autowired
    public CountdownAppConfig subject;

    @Test
    void parsesConfigCorrectly() {
        assertThat(subject.getCountdownConfig()).containsExactly(
                Map.entry("item1", CountdownConfig.builder()
                        .description("Item 1")
                        .date(LocalDateTime.of(2024, 11, 11, 1, 2, 3))
                        .build()),
                Map.entry("item2", CountdownConfig.builder()
                        .description("Item 2")
                        .date(LocalDateTime.of(2025, 12, 12, 10, 11, 12))
                        .build())
        );
    }

}