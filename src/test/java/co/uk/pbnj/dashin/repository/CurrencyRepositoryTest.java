package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.CurrencyConfig;
import co.uk.pbnj.dashin.dto.CurrencyLatest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyRepositoryTest {
    private static final String AUTH_TOKEN = "AUTH_TOKEN1";
    private static final String BASE_CURRENCY = "USD";
    private static final String TARGET_CURRENCY = "GBP";
    private static final String LATEST_PATH = "/latest";
    private static MockWebServer mockBackEnd;
    private CurrencyRepository subject;

    @BeforeEach
    void beforeEach() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        CurrencyConfig config = CurrencyConfig.builder()
                .url(mockBackEnd.url("/").toString())
                .authToken(AUTH_TOKEN)
                .latestPath(LATEST_PATH)
                .build();
        subject = new CurrencyRepository(config);
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
                            "data": {
                              "GBP": 0.55
                            }
                          }
                        """)
                .addHeader("Content-Type", "application/json"));
        Optional<CurrencyLatest> result = subject.getLatestExchangeRate(BASE_CURRENCY, TARGET_CURRENCY);

        assertThat(result).contains(
                new CurrencyLatest(Map.of(TARGET_CURRENCY, 0.55))
        );
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        HttpUrl requestUrl = recordedRequest.getRequestUrl();
        assertThat(requestUrl).isNotNull();
        assertThat(requestUrl.encodedPath()).isEqualTo(LATEST_PATH);
        assertThat(requestUrl.querySize()).isEqualTo(3);
        assertThat(requestUrl.queryParameter("apikey")).isEqualTo(AUTH_TOKEN);
        assertThat(requestUrl.queryParameter("base_currency")).isEqualTo(BASE_CURRENCY);
        assertThat(requestUrl.queryParameter("currencies")).isEqualTo(TARGET_CURRENCY);
    }

}