package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.StockConfig;
import co.uk.pbnj.dashin.StockConfigBuilder;
import co.uk.pbnj.dashin.dto.PrevCloseResult;
import co.uk.pbnj.dashin.dto.PrevCloseStock;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StockRepositoryTest {
    private static final String AUTH_TOKEN = "AUTH_TOKEN1";
    private static final String PREV_CLOSE_PATH = "/prevClose/%s";
    private static final String TICKER_ID = "BKNG";

    private static final String PREV_CLOSE_PATH_WITH_TICKER = PREV_CLOSE_PATH.formatted(TICKER_ID);

    private static MockWebServer mockBackEnd;

    private StockRepository subject;

    @BeforeEach
    void beforeEach() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        String baseUrl = "http://localhost:%s".formatted(mockBackEnd.getPort());
        StockConfig stockConfig = StockConfigBuilder.builder()
                .url(baseUrl)
                .authToken(AUTH_TOKEN)
                .prevClosePath(PREV_CLOSE_PATH)
                .build();
        subject = new StockRepository(stockConfig);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void correctlyGetsPreviousCloseData() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setBody("""
                        {
                             "results": [
                                 {
                                     "o": 3419.76,
                                     "c": 3408.14
                                 }
                             ]
                         }
                        """)
                .addHeader("Content-Type", "application/json"));

        Optional<PrevCloseStock> result = subject.getPreviousClose(TICKER_ID);

        assertThat(result).contains(
                new PrevCloseStock(List.of(new PrevCloseResult(3419.76, 3408.14)))
        );
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        HttpUrl requestUrl = recordedRequest.getRequestUrl();
        assertThat(requestUrl).isNotNull();
        assertThat(requestUrl.encodedPath()).isEqualTo(PREV_CLOSE_PATH_WITH_TICKER);
        assertThat(requestUrl.querySize()).isEqualTo(2);
        assertThat(requestUrl.queryParameter("apiKey")).isEqualTo(AUTH_TOKEN);
        assertThat(requestUrl.queryParameter("adjusted")).isEqualTo("true");
    }
}