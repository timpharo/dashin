package co.uk.pbnj.dashin.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockEquityCalculationTest {
    private static final String TICKER = "ticker1";
    private static final double OPEN_DAY_VALUE = 1.0;
    private static final double CLOSE_DAY_VALUE = 2.0;
    private static final double EXCHANGE_RATE = 5.0;
    private static final int DAYS_TILL_VEST = 25;
    private static final String ORIG_CURRENCY = "USD";
    private static final String TARGET_CURRENCY = "GBP";

    @Test
    void hasCorrectConfiguration(){
        StockEquityCalculation subject = new StockEquityCalculation(
                TICKER, OPEN_DAY_VALUE, CLOSE_DAY_VALUE,
                EXCHANGE_RATE, ORIG_CURRENCY, TARGET_CURRENCY, DAYS_TILL_VEST
        );

        assertThat(subject.ticker()).isEqualTo(TICKER);
        assertThat(subject.targetValueOpenDay()).isEqualTo(OPEN_DAY_VALUE * EXCHANGE_RATE);
        assertThat(subject.targetValueCloseDay()).isEqualTo(CLOSE_DAY_VALUE * EXCHANGE_RATE);
        assertThat(subject.exchangeRate()).isEqualTo(EXCHANGE_RATE);
        assertThat(subject.originalCurrency()).isEqualTo(ORIG_CURRENCY);
        assertThat(subject.targetCurrency()).isEqualTo(TARGET_CURRENCY);
    }

}