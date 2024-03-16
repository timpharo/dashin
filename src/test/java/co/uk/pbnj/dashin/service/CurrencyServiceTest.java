package co.uk.pbnj.dashin.service;

import co.uk.pbnj.dashin.dto.CurrencyLatest;
import co.uk.pbnj.dashin.dto.CurrencyLatestBuilder;
import co.uk.pbnj.dashin.repository.CurrencyRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    public static final String BASE_CURRENCY = "USD";
    public static final String TARGET_CURRENCY = "GBP";

    @Mock
    private CurrencyRepository currencyRepository;

    private CurrencyService subject;

    @BeforeEach
    void beforeEach() {
        Cache<String, CurrencyLatest> cache = CacheBuilder.newBuilder().build();
        subject = new CurrencyService(currencyRepository, cache);
    }

    @Test
    void callsRepositoryToRetrieveLatestExchangeRate() {
        Optional<CurrencyLatest> currencyLatest = Optional.of(CurrencyLatestBuilder.builder().build());
        given(currencyRepository.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY)).willReturn(currencyLatest);

        Optional<CurrencyLatest> result = subject.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);

        assertThat(result).isEqualTo(currencyLatest);
    }

    @Test
    void returnsCachedResultWhenRetrieveLatestExchangeRateCalled() {
        Optional<CurrencyLatest> currencyLatest = Optional.of(CurrencyLatestBuilder.builder().build());
        given(currencyRepository.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY)).willReturn(currencyLatest);
        Optional<CurrencyLatest> previousResult = subject.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);

        Optional<CurrencyLatest> result = subject.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);

        verify(currencyRepository, times(1)).getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);
        assertThat(previousResult).isEqualTo(currencyLatest);
        assertThat(result).isEqualTo(currencyLatest);
    }

    @Test
    void doesNotCachedResultWhenRetrieveLatestExchangeRateReturnsIsEmpty() {
        Optional<CurrencyLatest> currencyLatest = Optional.of(CurrencyLatestBuilder.builder().build());
        given(currencyRepository.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY))
                .willReturn(Optional.empty())
                .willReturn(currencyLatest);
        Optional<CurrencyLatest> previousResult = subject.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);

        Optional<CurrencyLatest> result = subject.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);

        verify(currencyRepository, times(2)).getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);
        assertThat(previousResult).isEmpty();
        assertThat(result).isEqualTo(currencyLatest);
    }

}