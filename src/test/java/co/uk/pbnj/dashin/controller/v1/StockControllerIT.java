package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.dto.StockEquityCalculation;
import co.uk.pbnj.dashin.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class StockControllerIT {

    @MockBean
    public StockService stockService;

    @Autowired
    public StockController controller;

    private WebTestClient client;

    @BeforeEach
    public void setup() {
        client = WebTestClient.bindToController(controller).build();
    }

    @Test
    void getStockEquityCalculation() {
        StockEquityCalculation calculation = StockEquityCalculation.builder()
                .ticker("ticker")
                .valueOpenDay(1.1)
                .valueCloseDay(2.2)
                .exchangeRate(3.3)
                .originalCurrency("USD")
                .targetCurrency("GBP")
                .daysTillVest(10)
                .build();
        given(stockService.getStockEquityCalculation()).willReturn(calculation);

        client.get()
                .uri("/v1/stock-equity")
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> spec.expectBody(StockEquityCalculation.class)
                                .isEqualTo(calculation)
                );
    }

}