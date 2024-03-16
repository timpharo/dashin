package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.CurrencyConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrencyAppConfig {

    @Value("${currency.url}")
    private String url;

    @Value("${currency.authToken}")
    private String authToken;

    @Value("${currency.paths.latest}")
    private String latestPath;

    @Value("${currency.cacheSeconds}")
    private long cacheSeconds;

    @Bean
    CurrencyConfig appConfig() {
        return new CurrencyConfig(
                url,
                authToken,
                latestPath,
                cacheSeconds
        );
    }
}
