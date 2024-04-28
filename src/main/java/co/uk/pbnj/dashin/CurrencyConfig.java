package co.uk.pbnj.dashin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public final class CurrencyConfig {
        private String url;
        private String authToken;
        private String latestPath;
        private long cacheSeconds;
}
