package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.CurrencyConfig;
import co.uk.pbnj.dashin.dto.CurrencyLatest;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Duration;

@Configuration
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public Cache<String, CurrencyLatest> currencyExchangeRateCache(CurrencyConfig currencyConfig) {
        return CacheBuilder.newBuilder()
                .initialCapacity(1)
                .expireAfterAccess(Duration.ofSeconds(currencyConfig.cacheSeconds()))
                .build();
    }
}
