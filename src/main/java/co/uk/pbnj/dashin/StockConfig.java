package co.uk.pbnj.dashin;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.time.LocalDateTime;

@RecordBuilder
public record StockConfig(
        String url,
        String authToken,
        String prevClosePath,
        String equityTickerId,
        String equityTickerCurrency,
        String equityTargetCurrency,
        long equityTickerCacheSeconds,
        int vestingAmount,
        LocalDateTime vestingDate) {
}
