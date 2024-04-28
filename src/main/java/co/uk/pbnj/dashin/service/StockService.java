package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.StockConfig;
import co.uk.pbnj.dashin.dto.*;
import co.uk.pbnj.dashin.repository.StockRepository;
import com.google.common.base.Suppliers;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class StockService {
    public static final double DEFAULT_EXCHANGE_RATE = 1.0;
    private final StockRepository stockRepository;
    private final CurrencyService currencyService;
    private final String equityTickerId;
    private final int vestingAmount;
    private final LocalDateTime vestingDate;
    private final String equityTickerCurrency;
    private final String equityTargetCurrency;
    private final Clock clock;

    private final Supplier<StockEquityCalculation> stockEquityCalculationSupplier;

    public StockService(StockRepository stockRepository, CurrencyService currencyService, StockConfig stockConfig, Clock clock) {
        this.stockRepository = stockRepository;
        this.currencyService = currencyService;
        this.equityTickerId = stockConfig.getEquityTickerId();
        this.vestingAmount = stockConfig.getVestingAmount();
        this.vestingDate = stockConfig.getVestingDate();
        this.equityTickerCurrency = stockConfig.getEquityTickerCurrency();
        this.equityTargetCurrency = stockConfig.getEquityTargetCurrency();
        this.clock = clock;

        this.stockEquityCalculationSupplier = Suppliers.memoizeWithExpiration(
                this::calculateStockEquity,
                stockConfig.getEquityTickerCacheSeconds(), SECONDS);
    }

    public StockEquityCalculation getStockEquityCalculation() {
        return stockEquityCalculationSupplier.get();
    }

    private StockEquityCalculation calculateStockEquity() {
        Optional<PrevCloseStock> previousCloseOpt = stockRepository.getPreviousClose(equityTickerId);

        if(previousCloseOpt.isPresent()){
            Optional<CurrencyLatest> latestExchangeRate = currencyService.getLatestExchangeRate(equityTickerCurrency, equityTargetCurrency);

            PrevCloseStock prevCloseStock = previousCloseOpt.get();
            PrevCloseResult prevCloseResult = prevCloseStock.getResults().get(0);

            double open = (prevCloseResult.getOpen() * vestingAmount);
            double close = (prevCloseResult.getClose() * vestingAmount);

            double exchangeRate = latestExchangeRate.map(it ->
                    it.getCurrencyExchanges().get(equityTargetCurrency))
                                            .orElse(DEFAULT_EXCHANGE_RATE);

            return new StockEquityCalculation(
                    equityTickerId,
                    open,
                    close,
                    exchangeRate,
                    equityTickerCurrency,
                    equityTargetCurrency,
                    (Duration.between(LocalDateTime.now(clock), vestingDate).toDays())
            );

        }

        return null;
    }
}
