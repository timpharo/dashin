package co.uk.pbnj.dashin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public final class StockConfig {
    private String url;
    private String authToken;
    private String prevClosePath;
    private String equityTickerId;
    private String equityTickerCurrency;
    private String equityTargetCurrency;
    private long equityTickerCacheSeconds;
    private int vestingAmount;
    private LocalDateTime vestingDate;
}
