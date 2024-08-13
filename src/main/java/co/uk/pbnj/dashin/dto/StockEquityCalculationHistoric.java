package co.uk.pbnj.dashin.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public final class StockEquityCalculationHistoric {
    private LocalDate date;
    private double valueOpenDay;
    private double valueCloseDay;
    private double exchangeRate;

    public Double getTargetValueOpenDay() {
        return valueOpenDay * exchangeRate;
    }

    public Double getTargetValueCloseDay() {
        return valueCloseDay * exchangeRate;
    }
}
