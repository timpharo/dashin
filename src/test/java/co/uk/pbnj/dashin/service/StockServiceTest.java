package co.uk.pbnj.dashin.service;


import co.uk.pbnj.dashin.StockConfig;
import co.uk.pbnj.dashin.dto.*;
import co.uk.pbnj.dashin.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    private static final double HIGH_AMOUNT = 4.4444;
    private static final double LOW_AMOUNT = 0.1111;
    private static final double TARGET_CURRENCY_EXCHANGE_RATE = 2.0;
    private static final double DEFAULT_EXCHANGE_RATE = 1.0;
    private static final int VESTING_AMOUNT = 2;
    private static final int FIXED_DAYS_TILL_VEST = 2;
    private static final LocalDateTime VEST_DATE = LocalDateTime.of(2024, 1, 7, 1, 2);
    private static final LocalDate DATE_NOW = LocalDate.of(2024, 1, 5);
    private static final int CACHE_TIME = 60;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private CurrencyService currencyService;

    private StockService subject;

    @BeforeEach
    void beforeEach() {
        StockConfig stockConfig = StockConfig.builder()
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

    @Nested
    class EquityCalculation {

        @Test
        void returnStockEquityCalculations() {
            DailyOpenCloseResult prevCloseResult = DailyOpenCloseResult.builder()
                    .open(OPEN_AMOUNT)
                    .close(CLOSE_AMOUNT)
                    .build();
            PrevCloseStock prevCloseStock = PrevCloseStock.builder()
                    .results(List.of(prevCloseResult))
                    .build();
            Optional<CurrencyLatest> currencyLatestOpt = Optional.of(
                    CurrencyLatest.builder()
                            .currencyExchanges(Map.of(EQUITY_TARGET_CURRENCY, TARGET_CURRENCY_EXCHANGE_RATE))
                            .build()
            );
            given(stockRepository.getPreviousClose(EQUITY_TICKER_ID)).willReturn(Optional.of(prevCloseStock));
            given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(currencyLatestOpt);

            StockEquityCalculation stockEquityCalculation = subject.getStockEquityCalculation();

            assertThat(stockEquityCalculation.getTicker()).isEqualTo(EQUITY_TICKER_ID);
            assertThat(stockEquityCalculation.getValueOpenDay()).isEqualTo(OPEN_AMOUNT * VESTING_AMOUNT);
            assertThat(stockEquityCalculation.getValueCloseDay()).isEqualTo(CLOSE_AMOUNT * VESTING_AMOUNT);
            assertThat(stockEquityCalculation.getTargetValueOpenDay()).isEqualTo((OPEN_AMOUNT * VESTING_AMOUNT) * TARGET_CURRENCY_EXCHANGE_RATE);
            assertThat(stockEquityCalculation.getTargetValueCloseDay()).isEqualTo((CLOSE_AMOUNT * VESTING_AMOUNT) * TARGET_CURRENCY_EXCHANGE_RATE);

            assertThat(stockEquityCalculation.getExchangeRate()).isEqualTo(TARGET_CURRENCY_EXCHANGE_RATE);
            assertThat(stockEquityCalculation.getOriginalCurrency()).isEqualTo(EQUITY_TICKER_CURRENCY);
            assertThat(stockEquityCalculation.getTargetCurrency()).isEqualTo(EQUITY_TARGET_CURRENCY);
            assertThat(stockEquityCalculation.getDaysTillVest()).isEqualTo(FIXED_DAYS_TILL_VEST);
        }

        @Test
        void multipleCallsReturnsCachedStockEquityCalculations() {
            DailyOpenCloseResult prevCloseResult = DailyOpenCloseResult.builder()
                    .open(OPEN_AMOUNT)
                    .close(CLOSE_AMOUNT)
                    .build();
            PrevCloseStock prevCloseStock = PrevCloseStock.builder()
                    .results(List.of(prevCloseResult))
                    .build();
            Optional<CurrencyLatest> currencyLatestOpt = Optional.of(
                    CurrencyLatest.builder()
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
            DailyOpenCloseResult prevCloseResult = DailyOpenCloseResult.builder()
                    .open(OPEN_AMOUNT)
                    .close(CLOSE_AMOUNT)
                    .build();
            PrevCloseStock prevCloseStock = PrevCloseStock.builder()
                    .results(List.of(prevCloseResult))
                    .build();
            given(stockRepository.getPreviousClose(EQUITY_TICKER_ID)).willReturn(Optional.of(prevCloseStock));
            given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(Optional.empty());

            StockEquityCalculation stockEquityCalculation = subject.getStockEquityCalculation();

            assertThat(stockEquityCalculation.getTicker()).isEqualTo(EQUITY_TICKER_ID);
            assertThat(stockEquityCalculation.getValueOpenDay()).isEqualTo(OPEN_AMOUNT * VESTING_AMOUNT);
            assertThat(stockEquityCalculation.getValueCloseDay()).isEqualTo(CLOSE_AMOUNT * VESTING_AMOUNT);
            assertThat(stockEquityCalculation.getTargetValueOpenDay()).isEqualTo((OPEN_AMOUNT * VESTING_AMOUNT) * DEFAULT_EXCHANGE_RATE);
            assertThat(stockEquityCalculation.getTargetValueCloseDay()).isEqualTo((CLOSE_AMOUNT * VESTING_AMOUNT) * DEFAULT_EXCHANGE_RATE);

            assertThat(stockEquityCalculation.getExchangeRate()).isEqualTo(DEFAULT_EXCHANGE_RATE);
            assertThat(stockEquityCalculation.getOriginalCurrency()).isEqualTo(EQUITY_TICKER_CURRENCY);
            assertThat(stockEquityCalculation.getTargetCurrency()).isEqualTo(EQUITY_TARGET_CURRENCY);
            assertThat(stockEquityCalculation.getDaysTillVest()).isEqualTo(FIXED_DAYS_TILL_VEST);
        }
    }

    @Nested
    class EquityCalculationHistoric {
        private static final LocalDate DATE_NOW_MINUS_1_M = DATE_NOW.minusMonths(1);
        private static final LocalDate DATE_NOW_MINUS_6_M = DATE_NOW.minusMonths(6);
        private static final LocalDate DATE_NOW_MINUS_12_M = DATE_NOW.minusMonths(12);

        @Test
        void returnStockEquityCalculations() {
            DailyOpenCloseResult openCloseResult = DailyOpenCloseResult.builder()
                    .open(OPEN_AMOUNT)
                    .close(CLOSE_AMOUNT)
                    .high(HIGH_AMOUNT)
                    .low(LOW_AMOUNT)
                    .build();
            Optional<CurrencyLatest> currencyLatestOpt = Optional.of(
                    CurrencyLatest.builder()
                            .currencyExchanges(Map.of(EQUITY_TARGET_CURRENCY, TARGET_CURRENCY_EXCHANGE_RATE))
                            .build()
            );
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_1_M)).willReturn(Optional.of(openCloseResult));
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_6_M)).willReturn(Optional.of(openCloseResult));
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_12_M)).willReturn(Optional.of(openCloseResult));
            given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(currencyLatestOpt);

            List<StockEquityCalculationHistoric> stockEquityCalculation = subject.getStockEquityCalculationHistoric();

            assertThat(stockEquityCalculation).containsExactly(
                    expectedHistoricWith(DATE_NOW_MINUS_1_M, TARGET_CURRENCY_EXCHANGE_RATE),
                    expectedHistoricWith(DATE_NOW_MINUS_6_M, TARGET_CURRENCY_EXCHANGE_RATE),
                    expectedHistoricWith(DATE_NOW_MINUS_12_M, TARGET_CURRENCY_EXCHANGE_RATE)
            );
        }

        @Test
        void multipleCallsReturnsCachedStockEquityCalculations() {
            DailyOpenCloseResult openCloseResult = DailyOpenCloseResult.builder()
                    .open(OPEN_AMOUNT)
                    .close(CLOSE_AMOUNT)
                    .high(HIGH_AMOUNT)
                    .low(LOW_AMOUNT)
                    .build();
            Optional<CurrencyLatest> currencyLatestOpt = Optional.of(
                    CurrencyLatest.builder()
                            .currencyExchanges(Map.of(EQUITY_TARGET_CURRENCY, TARGET_CURRENCY_EXCHANGE_RATE))
                            .build()
            );
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_1_M)).willReturn(Optional.of(openCloseResult));
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_6_M)).willReturn(Optional.of(openCloseResult));
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_12_M)).willReturn(Optional.of(openCloseResult));
            given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(currencyLatestOpt);
            List<StockEquityCalculationHistoric> firstResult = subject.getStockEquityCalculationHistoric();

            List<StockEquityCalculationHistoric> secondResult = subject.getStockEquityCalculationHistoric();

            verify(stockRepository, times(1)).getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_1_M);
            assertThat(firstResult).isEqualTo(secondResult);
        }

        @Test
        void returnEmptyListWhenStockNotReturned() {
            given(stockRepository.getDailyOpenClose(eq(EQUITY_TICKER_ID), any())).willReturn(Optional.empty());

            List<StockEquityCalculationHistoric> stockEquityCalculation = subject.getStockEquityCalculationHistoric();

            assertThat(stockEquityCalculation).isEmpty();
        }

        @Test
        void usedDefaultExchangeRateWhenCurrencyExchangeNotReturned() {
            DailyOpenCloseResult openCloseResult = DailyOpenCloseResult.builder()
                    .open(OPEN_AMOUNT)
                    .close(CLOSE_AMOUNT)
                    .high(HIGH_AMOUNT)
                    .low(LOW_AMOUNT)
                    .build();
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_1_M)).willReturn(Optional.of(openCloseResult));
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_6_M)).willReturn(Optional.of(openCloseResult));
            given(stockRepository.getDailyOpenClose(EQUITY_TICKER_ID, DATE_NOW_MINUS_12_M)).willReturn(Optional.of(openCloseResult));
            given(currencyService.getLatestExchangeRate(EQUITY_TICKER_CURRENCY, EQUITY_TARGET_CURRENCY)).willReturn(Optional.empty());

            List<StockEquityCalculationHistoric> stockEquityCalculation = subject.getStockEquityCalculationHistoric();

            assertThat(stockEquityCalculation).containsExactly(
                    expectedHistoricWith(DATE_NOW_MINUS_1_M, DEFAULT_EXCHANGE_RATE),
                    expectedHistoricWith(DATE_NOW_MINUS_6_M, DEFAULT_EXCHANGE_RATE),
                    expectedHistoricWith(DATE_NOW_MINUS_12_M, DEFAULT_EXCHANGE_RATE)
            );
        }

        private StockEquityCalculationHistoric expectedHistoricWith(LocalDate date, double exchangeRate) {
            return StockEquityCalculationHistoric.builder()
                    .date(date)
                    .valueOpenDay(OPEN_AMOUNT * VESTING_AMOUNT)
                    .valueCloseDay(CLOSE_AMOUNT * VESTING_AMOUNT)
                    .exchangeRate(exchangeRate)
                    .build();
        }
    }



}