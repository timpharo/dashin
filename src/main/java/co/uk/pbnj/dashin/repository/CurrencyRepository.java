package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.CurrencyConfig;
import co.uk.pbnj.dashin.CurrencyConverter;
import co.uk.pbnj.dashin.dto.CurrencyLatest;
import co.uk.pbnj.dashin.repository.entity.CurrencyLatestResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class CurrencyRepository implements CurrencyConverter {

    private final WebClient webClient;
    private final String authToken;
    private final String latestPath;

    public CurrencyRepository(CurrencyConfig config) {
        this.webClient = WebClient.create(config.getUrl());
        this.authToken = config.getAuthToken();
        this.latestPath = config.getLatestPath();
    }

    public Optional<CurrencyLatest> getLatestExchangeRate(String baseCurrency, String targetCurrency){
        Mono<CurrencyLatestResponse> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(latestPath)
                        .queryParam("apikey", authToken)
                        .queryParam("base_currency", baseCurrency)
                        .queryParam("currencies", targetCurrency)
                        .build())
                .retrieve()
                .bodyToMono(CurrencyLatestResponse.class);

        CurrencyLatestResponse response = responseMono.block();
        return Optional
                .ofNullable(response)
                .map(CurrencyLatestResponse::toCurrencyLatest);
    }
}
