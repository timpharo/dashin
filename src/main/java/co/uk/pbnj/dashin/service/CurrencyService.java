package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.CurrencyConverter;
import co.uk.pbnj.dashin.dto.CurrencyLatest;
import co.uk.pbnj.dashin.repository.CurrencyRepository;
import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyService implements CurrencyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRepository currencyRepository;
    private final Cache<String, CurrencyLatest> cache;

    public CurrencyService(CurrencyRepository currencyRepository, Cache<String, CurrencyLatest> cache) {
        this.currencyRepository = currencyRepository;
        this.cache = cache;
    }

    @Override
    public Optional<CurrencyLatest> getLatestExchangeRate(String baseCurrency, String targetCurrency) {
        String cacheKey = baseCurrency + targetCurrency;
        Optional<CurrencyLatest> result = Optional.ofNullable(cache.getIfPresent(cacheKey));

        if(result.isEmpty()) {
            LOGGER.info("Cache miss for key {}, getting from repository", cacheKey);
            result = currencyRepository.getLatestExchangeRate(baseCurrency, targetCurrency);
            result.ifPresent(currencyLatest -> cache.put(cacheKey, currencyLatest));
        }

        return result;
    }
}
