package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.StockConfig;
import co.uk.pbnj.dashin.dto.PrevCloseStock;
import co.uk.pbnj.dashin.repository.entity.PrevCloseStockResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class StockRepository {
    private final String authToken;
    private final String prevClosePath;
    private final WebClient webClient;

    public StockRepository(StockConfig stockConfig) {
        this.authToken = stockConfig.authToken();
        this.prevClosePath = stockConfig.prevClosePath();
        this.webClient = WebClient.create(stockConfig.url());
    }

    public Optional<PrevCloseStock> getPreviousClose(String tickerId) {
        Mono<PrevCloseStockResponse> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(prevClosePath.formatted(tickerId))
                        .queryParam("adjusted", "true")
                        .queryParam("apiKey", authToken)
                        .build())
                .retrieve()
                .bodyToMono(PrevCloseStockResponse.class);

        PrevCloseStockResponse response = responseMono.block();
        return Optional
                .ofNullable(response)
                .map(PrevCloseStockResponse::toPrevCloseStock);
    }
}
