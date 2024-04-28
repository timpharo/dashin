package co.uk.pbnj.dashin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public final class StockEquityCalculation {
    private String ticker;
    private double valueOpenDay;
    private double valueCloseDay;
    private double exchangeRate;
    private String originalCurrency;
    private String targetCurrency;
    private long daysTillVest;

    public Double getTargetValueOpenDay() {
        return valueOpenDay * exchangeRate;
    }

    public Double getTargetValueCloseDay() {
        return valueCloseDay * exchangeRate;
    }
}
