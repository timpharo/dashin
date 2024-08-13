package co.uk.pbnj.dashin.repository;

import co.uk.pbnj.dashin.StockConfig;
import co.uk.pbnj.dashin.dto.DailyOpenCloseResult;
import co.uk.pbnj.dashin.dto.PrevCloseStock;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class StockRepositoryTest {
    private static final String AUTH_TOKEN = "AUTH_TOKEN1";
    private static final String PREV_CLOSE_PATH = "/v2/prevClose/%s";
    private static final String DAILY_OPEN_CLOSE_PATH = "/v1/open-close/%s/%s";
    private static final String TICKER_ID = "BKNG";
    public static final LocalDate DAILY_OPEN_CLOSE_DATE = LocalDate.of(2024, 8, 12);

    private static final String PREV_CLOSE_PATH_WITH_TICKER = PREV_CLOSE_PATH.formatted(TICKER_ID);
    private static final String DAILY_OPEN_CLOSE_PATH_WITH_DETAILS = DAILY_OPEN_CLOSE_PATH.formatted(
            TICKER_ID,
            DAILY_OPEN_CLOSE_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    private static MockWebServer mockBackEnd;

    private StockRepository subject;

    @BeforeEach
    void beforeEach() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        StockConfig stockConfig = StockConfig.builder()
                .url(mockBackEnd.url("/").toString())
                .authToken(AUTH_TOKEN)
                .prevClosePath(PREV_CLOSE_PATH)
                .dailyOpenClosePath(DAILY_OPEN_CLOSE_PATH)
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
                                     "h": 4.44,
                                     "o": 2.22,
                                     "c": 3.33,
                                     "l": 1.11
                                 }
                             ]
                         }
                        """)
                .addHeader("Content-Type", "application/json"));

        Optional<PrevCloseStock> result = subject.getPreviousClose(TICKER_ID);

        assertThat(result).contains(
                new PrevCloseStock(List.of(new DailyOpenCloseResult(2.22, 3.33, 4.44, 1.11)))
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

    @Test
    void correctlyGetsDailyOpenClose() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                .setBody("""
                        {
                            "low": 1.11,
                            "open": 2.22,
                            "close": 3.33,
                            "high": 4.44
                        }
                        """)
                .addHeader("Content-Type", "application/json"));

        Optional<DailyOpenCloseResult> result = subject.getDailyOpenClose(TICKER_ID, DAILY_OPEN_CLOSE_DATE);

        assertThat(result).contains(
                new DailyOpenCloseResult(2.22, 3.33, 4.44, 1.11)
        );
        assertExpectedGetDailyOpenCloseRequest();
    }

    @Test
    void correctlyHandles404ForDailyOpenClose() throws Exception {
        mockBackEnd.enqueue(new MockResponse()
                        .setResponseCode(404)
                    .addHeader("Content-Type", "application/json"));

        Optional<DailyOpenCloseResult> result = subject.getDailyOpenClose(TICKER_ID, DAILY_OPEN_CLOSE_DATE);

        assertThat(result).isEmpty();
        assertExpectedGetDailyOpenCloseRequest();
    }

    private static void assertExpectedGetDailyOpenCloseRequest() throws InterruptedException {
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        HttpUrl requestUrl = recordedRequest.getRequestUrl();
        assertThat(requestUrl).isNotNull();
        assertThat(requestUrl.encodedPath()).isEqualTo(DAILY_OPEN_CLOSE_PATH_WITH_DETAILS);
        assertThat(requestUrl.querySize()).isEqualTo(2);
        assertThat(requestUrl.queryParameter("apiKey")).isEqualTo(AUTH_TOKEN);
        assertThat(requestUrl.queryParameter("adjusted")).isEqualTo("true");
    }
}