package co.uk.pbnj.dashin.controller.v1;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StockControllerTest {

    @Test
    void returnsCorrectStockPath(){
        String result = StockController.getV1StockEquityPath();

        assertThat(result).isEqualTo("/v1/stock-equity");
    }

}