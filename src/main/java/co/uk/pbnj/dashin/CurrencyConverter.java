package co.uk.pbnj.dashin;

import co.uk.pbnj.dashin.dto.CurrencyLatest;

import java.util.Optional;

public interface CurrencyConverter {
    Optional<CurrencyLatest> getLatestExchangeRate(String baseCurrency, String targetCurrency);
}
