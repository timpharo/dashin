package co.uk.pbnj.dashin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public final class StockEquityCalculationWithHistory {
    private String ticker;
    private double valueOpenDay;
    private double valueCloseDay;
    private double exchangeRate;
    private String originalCurrency;
    private String targetCurrency;
    private long daysTillVest;
    private List<StockEquityCalculationHistoric> history;

    public Double getTargetValueOpenDay() {
        return valueOpenDay * exchangeRate;
    }

    public Double getTargetValueCloseDay() {
        return valueCloseDay * exchangeRate;
    }

    public static StockEquityCalculationWithHistory from(
            StockEquityCalculation calculation,
            List<StockEquityCalculationHistoric> historic) {

        return StockEquityCalculationWithHistory.builder()
                .ticker(calculation.getTicker())
                .valueOpenDay(calculation.getValueOpenDay())
                .valueCloseDay(calculation.getValueCloseDay())
                .exchangeRate(calculation.getExchangeRate())
                .originalCurrency(calculation.getOriginalCurrency())
                .targetCurrency(calculation.getTargetCurrency())
                .daysTillVest(calculation.getDaysTillVest())
                .history(historic)
                .build();
    }
}
