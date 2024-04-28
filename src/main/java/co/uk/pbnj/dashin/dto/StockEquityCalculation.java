package co.uk.pbnj.dashin.dto;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record StockEquityCalculation(
        String ticker,
        double valueOpenDay,
        double valueCloseDay,
        double exchangeRate,
        String originalCurrency,
        String targetCurrency,
        long daysTillVest) {

    public Double targetValueOpenDay(){
        return valueOpenDay * exchangeRate;
    }

    public Double targetValueCloseDay(){
        return valueCloseDay * exchangeRate;
    }
}
