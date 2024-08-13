package co.uk.pbnj.dashin.controller.v1;

import co.uk.pbnj.dashin.ActivateIntegrationProfile;
import co.uk.pbnj.dashin.dto.StockEquityCalculation;
import co.uk.pbnj.dashin.dto.StockEquityCalculationHistoric;
import co.uk.pbnj.dashin.dto.StockEquityCalculationWithHistory;
import co.uk.pbnj.dashin.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActivateIntegrationProfile
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
        StockEquityCalculationHistoric historic1 = StockEquityCalculationHistoric.builder()
                .date(LocalDate.of(2024, 8, 13))
                .valueOpenDay(22.22)
                .valueCloseDay(33.33)
                .exchangeRate(4.4)
                .build();

        given(stockService.getStockEquityCalculation()).willReturn(calculation);
        given(stockService.getStockEquityCalculationHistoric()).willReturn(List.of(historic1));

        client.get()
                .uri("/v1/stock-equity")
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> {
                            spec.expectBody(StockEquityCalculationWithHistory.class)
                                    .value(result -> assertThat(result.getTicker()).isEqualTo("ticker"))
                                    .value(result -> assertThat(result.getValueOpenDay()).isEqualTo(1.1))
                                    .value(result -> assertThat(result.getValueCloseDay()).isEqualTo(2.2))
                                    .value(result -> assertThat(result.getExchangeRate()).isEqualTo(3.3))
                                    .value(result -> assertThat(result.getOriginalCurrency()).isEqualTo("USD"))
                                    .value(result -> assertThat(result.getTargetCurrency()).isEqualTo("GBP"))
                                    .value(result -> assertThat(result.getDaysTillVest()).isEqualTo(10))
                                    .value(result -> assertThat(result.getHistory()).hasSize(1))
                                    .value(result -> {
                                        StockEquityCalculationHistoric firstHistoricItem = result.getHistory().get(0);
                                        assertThat(firstHistoricItem.getDate()).isEqualTo(LocalDate.of(2024, 8, 13));
                                        assertThat(firstHistoricItem.getExchangeRate()).isEqualTo(4.4);
                                        assertThat(firstHistoricItem.getValueOpenDay()).isEqualTo(22.22);
                                        assertThat(firstHistoricItem.getValueCloseDay()).isEqualTo(33.33);
                                    });
                        }
                        //TODO assert all other values

                );
    }

}