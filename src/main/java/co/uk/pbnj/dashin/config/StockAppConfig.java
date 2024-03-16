package co.uk.pbnj.dashin.config;

import co.uk.pbnj.dashin.StockConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class StockAppConfig {

    @Value("${stock.url}")
    private String url;

    @Value("${stock.authToken}")
    private String authToken;

    @Value("${stock.paths.prevClose}")
    private String prevClosePath;

    @Value("${stock.equity.tickerId}")
    private String equityTickerId;

    @Value("${stock.equity.tickerCurrency}")
    private String equityTickerCurrency;

    @Value("${stock.equity.targetCurrency}")
    private String equityTargetCurrency;

    @Value("${stock.equity.vestingAmount}")
    private int equityVestingAmount;

    @Value("${stock.equity.vestingDate}")
    private LocalDateTime equityVestingDate;

    @Value("${stock.equity.cacheSeconds}")
    private long equityTickerCacheSeconds;

    @Bean
    StockConfig stockConfig() {
        return new StockConfig(
                url,
                authToken,
                prevClosePath,
                equityTickerId,
                equityTickerCurrency,
                equityTargetCurrency,
                equityTickerCacheSeconds,
                equityVestingAmount,
                equityVestingDate
        );
    }
}
