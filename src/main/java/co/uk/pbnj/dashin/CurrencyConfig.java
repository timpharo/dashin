package co.uk.pbnj.dashin;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record CurrencyConfig(
        String url,
        String authToken,
        String latestPath,
        long cacheSeconds
        ) {
}
