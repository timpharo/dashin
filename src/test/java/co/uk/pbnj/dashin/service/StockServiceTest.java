package co.uk.pbnj.dashin.service;


import co.uk.pbnj.dashin.StockConfig;
import co.uk.pbnj.dashin.StockConfigBuilder;
import co.uk.pbnj.dashin.dto.*;
import co.uk.pbnj.dashin.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    private static final String EQUITY_TICKER_ID = "BKNG";
    private static final String EQUITY_TICKER_CURRENCY = "USD";
    private static final String EQUITY_TARGET_CURRENCY = "GBP";
    private static final double OPEN_AMOUNT = 1.1111;
    private static final double CLOSE_AMOUNT = 3.3333;
    private static final double TARGET_CURRENCY_EXCHANGE_RATE = 2.0;
    private static final double DEFAULT_EXCHANGE_RATE = 1.0;
    private static final int VESTING_AMOUNT = 2;
    private static final int FIXED_DAYS_TILL_VEST = 2;
    private static final LocalDateTime VEST_DATE = LocalDateTime.of(2024, 1, 7, 1, 2);
    public static final int CACHE_TIME = 60;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private CurrencyService currencyService;

    private StockService subject;

    @BeforeEach
    void beforeEach() {
        StockConfig stockConfig = StockConfigBuilder.builder()
                .equityTickerId(EQUITY_TICKER_ID)
                .equityTickerCurrency(EQUITY_TICKER_CURRENCY)
                .equityTargetCurrency(EQUITY_TARGET_CURRENCY)
                .vestingAmount(VESTING_AMOUNT)
                .vestingDate(VEST_DATE)
                .equityTickerCacheSeconds(CACHE_TIME)
                .build();

        Clock fixedClock = Clock.fixed(VEST_DATE.minusDays(FIXED_DAYS_TILL_VEST).toInstant(UTC), ZoneId.of("UTC"));

        subject = new StockService(stockRepository, currencyService, stockConfig, fixedClock);
    }

    @Test
    void returnStockEquityCalculations() {
        PrevCloseResult prevCloseResult = PrevCloseResultBuilder.builder()
                .open(OPEN_AMOUNT)
                .close(CLOSE_AMOUNT)
                .build();
        PrevCloseStock prevCloseStock = PrevCloseStockBuilder.builder()
                .results(List.of(prevCloseResult))
                .build();
        Optional<CurrencyLatest> currencyLatestOpt = Optional.of(
                CurrencyLatestBuilder.builder()
                        .currencyExchanges(Map.of(EQUITY_TARGET_CURRENCY, TARGET_CURRENCY_EXCHANGE_RATE))
                        .build()
        );
        given(stockRepository.getPreviousClose(EQUITY_TICKER_ID)).willReturn(Optional.of(prevCloseStock));
        given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(currencyLatestOpt);

        StockEquityCalculation stockEquityCalculation = subject.getStockEquityCalculation();

        assertThat(stockEquityCalculation.ticker()).isEqualTo(EQUITY_TICKER_ID);
        assertThat(stockEquityCalculation.valueOpenDay()).isEqualTo(OPEN_AMOUNT * VESTING_AMOUNT);
        assertThat(stockEquityCalculation.valueCloseDay()).isEqualTo(CLOSE_AMOUNT * VESTING_AMOUNT);
        assertThat(stockEquityCalculation.exchangeRate()).isEqualTo(TARGET_CURRENCY_EXCHANGE_RATE);
        assertThat(stockEquityCalculation.originalCurrency()).isEqualTo(EQUITY_TICKER_CURRENCY);
        assertThat(stockEquityCalculation.targetCurrency()).isEqualTo(EQUITY_TARGET_CURRENCY);
        assertThat(stockEquityCalculation.daysTillVest()).isEqualTo(FIXED_DAYS_TILL_VEST);
    }

    @Test
    void multipleCallsReturnsCachedStockEquityCalculations() {
        PrevCloseResult prevCloseResult = PrevCloseResultBuilder.builder()
                .open(OPEN_AMOUNT)
                .close(CLOSE_AMOUNT)
                .build();
        PrevCloseStock prevCloseStock = PrevCloseStockBuilder.builder()
                .results(List.of(prevCloseResult))
                .build();
        Optional<CurrencyLatest> currencyLatestOpt = Optional.of(
                CurrencyLatestBuilder.builder()
                        .currencyExchanges(Map.of(EQUITY_TARGET_CURRENCY, TARGET_CURRENCY_EXCHANGE_RATE))
                        .build()
        );
        given(stockRepository.getPreviousClose(EQUITY_TICKER_ID)).willReturn(Optional.of(prevCloseStock));
        given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(currencyLatestOpt);
        StockEquityCalculation firstResult = subject.getStockEquityCalculation();

        StockEquityCalculation secondResult = subject.getStockEquityCalculation();

        verify(stockRepository, times(1)).getPreviousClose(EQUITY_TICKER_ID);
        assertThat(firstResult).isEqualTo(secondResult);
    }

    @Test
    void returnNullWhenStockNotReturned() {
        given(stockRepository.getPreviousClose(EQUITY_TICKER_ID)).willReturn(Optional.empty());

        StockEquityCalculation stockEquityCalculation = subject.getStockEquityCalculation();

        assertThat(stockEquityCalculation).isNull();
    }

    @Test
    void defaultExchangeRateTo1WhenCurrencyExchangeNotReturned() {
        PrevCloseResult prevCloseResult = PrevCloseResultBuilder.builder()
                .open(OPEN_AMOUNT)
                .close(CLOSE_AMOUNT)
                .build();
        PrevCloseStock prevCloseStock = PrevCloseStockBuilder.builder()
                .results(List.of(prevCloseResult))
                .build();
        given(stockRepository.getPreviousClose(EQUITY_TICKER_ID)).willReturn(Optional.of(prevCloseStock));
        given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(Optional.empty());

        StockEquityCalculation stockEquityCalculation = subject.getStockEquityCalculation();

        assertThat(stockEquityCalculation.ticker()).isEqualTo(EQUITY_TICKER_ID);
        assertThat(stockEquityCalculation.valueOpenDay()).isEqualTo(OPEN_AMOUNT * VESTING_AMOUNT);
        assertThat(stockEquityCalculation.valueCloseDay()).isEqualTo(CLOSE_AMOUNT * VESTING_AMOUNT);
        assertThat(stockEquityCalculation.exchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
        assertThat(stockEquityCalculation.originalCurrency()).isEqualTo(EQUITY_TICKER_CURRENCY);
        assertThat(stockEquityCalculation.targetCurrency()).isEqualTo(EQUITY_TARGET_CURRENCY);
        assertThat(stockEquityCalculation.daysTillVest()).isEqualTo(FIXED_DAYS_TILL_VEST);
    }

}