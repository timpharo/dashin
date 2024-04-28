package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.dto.CountdownConfig;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "countdown")
@Builder
@Getter
public class CountdownAppConfig {
    private final Map<String, CountdownConfig> countdownConfig;

    public CountdownAppConfig(Map<String, CountdownConfig> countdownConfig) {
        this.countdownConfig = countdownConfig;
    }
}

