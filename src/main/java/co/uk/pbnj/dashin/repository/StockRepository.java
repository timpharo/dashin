package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.StockConfig;
import co.uk.pbnj.dashin.dto.DailyOpenCloseResult;
import co.uk.pbnj.dashin.dto.PrevCloseStock;
import co.uk.pbnj.dashin.repository.entity.DailyOpenCloseStockResponse;
import co.uk.pbnj.dashin.repository.entity.PrevCloseStockResponse;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Repository
public class StockRepository {
    private final String authToken;
    private final String prevClosePath;
    private final String dailyOpenClosePath;
    private final WebClient webClient;

    public StockRepository(StockConfig stockConfig) {
        this.authToken = stockConfig.getAuthToken();
        this.prevClosePath = stockConfig.getPrevClosePath();
        this.dailyOpenClosePath = stockConfig.getDailyOpenClosePath();
        this.webClient = WebClient.create(stockConfig.getUrl());
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

    public Optional<DailyOpenCloseResult> getDailyOpenClose(String tickerId, LocalDate date) {
        String dateParam = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Mono<DailyOpenCloseStockResponse> responseMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(dailyOpenClosePath.formatted(tickerId, dateParam))
                        .queryParam("adjusted", "true")
                        .queryParam("apiKey", authToken)
                        .build())
                .retrieve()
                .bodyToMono(DailyOpenCloseStockResponse.class);

        DailyOpenCloseStockResponse response = responseMono
                .onErrorResume(WebClientResponseException.NotFound.class, (e) -> Mono.empty())
                .block();
        return Optional
                .ofNullable(response)
                .map(DailyOpenCloseStockResponse::toDailyOpenCloseResult);
    }
}
